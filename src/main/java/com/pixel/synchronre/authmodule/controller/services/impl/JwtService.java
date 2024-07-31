package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuMutatorService;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuReaderService;
import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import com.pixel.synchronre.authmodule.model.dtos.appuser.AuthResponseDTO;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.model.dtos.response.JwtInfos;
import com.pixel.synchronre.logmodule.model.entities.Log;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import com.pixel.synchronre.sharedmodule.utilities.HttpServletManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    private final FunctionRepo functionRepo;
    private final UserRepo userRepo;
    private final CedRepo cedRepo;
    private final CessionnaireRepository cesRepo;
    private final IMenuReaderService menuService;

    @Override
    public AuthResponseDTO generateJwt(UserDetails userDetails, String connectionId)
    {
        String userEmail = userDetails.getUsername();
        AppUser user = userRepo.findByEmail(userEmail).orElseThrow(()->new AppException("Utilisateur introuvable"));
        Set<Long> visibilityIds = functionRepo.getCurrentFncVisibilityIds(userDetails.getUsername());
        Set<Long> functionIds = functionRepo.getCurrentFncIds(user.getUserId());
        Long functionId = functionIds == null || functionIds.size() != 1 ? null : new ArrayList<>(functionIds).get(0);
        Map<String, Object> extraClaims = new HashMap<>(); //functionId = 1l;


        AppFunction function = functionId == null ? null : functionRepo.findById(functionId).orElse(null);
        Long cedId = function == null ? null : function.getVisibilityId();
        Cedante ced = cedId == null ? null : cedRepo.findById(cedId).orElse(null);

        Set<String> menus = menuService.getMenusByFncId(functionId);
        Set<String> authorities = userDetails.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.toSet());
        Set<String> authAndMenus = new HashSet<String>(authorities);
        authAndMenus.addAll(menus);

        Long cesId = function == null ? null : function.getCesId(); user.getCesId();
        Cessionnaire ces = cesId == null ? null : cesRepo.findById(cesId).orElse(null);
        Long userId = user.getUserId();
        extraClaims.put("userId", userId);
        extraClaims.put("email", userEmail);
        extraClaims.put("nom", user.getFirstName());
        extraClaims.put("tel", user.getTel());
        extraClaims.put("prenom", user.getLastName());
        extraClaims.put("authorities", authAndMenus);
        extraClaims.put("menus", menus);
        extraClaims.put("visibilityId", visibilityIds == null ? null : visibilityIds.size() != 1 ? null : new ArrayList<>(visibilityIds).get(0));
        extraClaims.put("functionId", functionId);
        extraClaims.put("connectionId", connectionId);

        extraClaims.put("functionName", function == null ? null : function.getName());
        extraClaims.put("tyfId", function == null || function.getTypeFunction() == null ? null : function.getTypeFunction().getTypeId());
        extraClaims.put("tyfCode", function == null || function.getTypeFunction() == null ? null : function.getTypeFunction().getUniqueCode());
        extraClaims.put("tyfLibelle", function == null || function.getTypeFunction() == null ? null : function.getTypeFunction().getName());

        extraClaims.put("cedId", cedId);
        extraClaims.put("cedName", ced == null ? null : ced.getCedNomFiliale());
        extraClaims.put("cedSigle", ced == null ? null : ced.getCedSigleFiliale());
        extraClaims.put("isCourtier", cedId==null);

        extraClaims.put("cesId", cesId);
        extraClaims.put("cesNom", ces == null ? null : ces.getCesNom());
        extraClaims.put("cesSigle", ces == null ? null : ces.getCesSigle());

        extraClaims.put("functionStartingDate", function == null || function.getStartsAt() == null ? null : Date.from(function.getStartsAt().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        extraClaims.put("functionEndingDate", function == null || function.getEndsAt() == null ? null : Date.from(function.getEndsAt().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        boolean hasCurrentFonction = functionId != null && functionRepo.functionIsCurrentForUser(functionId, userId);
        boolean doesNotHaveAnyFunction = !functionRepo.userHasAnyAppFunction(userId);
        //boolean hasActiveFunctionsButNotCurrent = ;
        //boolean currentFunctionHasExpired = ;

        return generateJwt(userEmail, extraClaims);
    }

    @Override
    public AuthResponseDTO generateJwt(String username, Map<String, Object> extraClaims)
    {
        String accessToken = Jwts.builder().setClaims(extraClaims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_TOKEN_DURATION ))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256).compact();

        String refreshToken = Jwts.builder().setSubject(username)
                .claim("connectionId", extraClaims.get("connectionId"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_TOKEN_DURATION ))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256).compact();
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    public String extractUsername()
    {
        return this.extractUsername(this.getCurrentJwt());
    }

    @Override
    public String extractConnectionId()
    {
        Claims claims= this.extractAllClaims(this.getCurrentJwt());
        return claims.get("connectionId", String.class);
    }

    @Override
    public String extractTyfCode() {
        Claims claims= this.extractAllClaims(this.getCurrentJwt());
        return claims.get("tyfCode", String.class);
    }
    @Override
    public String extractTyfLibelle() {
        Claims claims= this.extractAllClaims(this.getCurrentJwt());
        return claims.get("tyfLibelle", String.class);
    }
    @Override
    public String extractTyfId() {
        Claims claims= this.extractAllClaims(this.getCurrentJwt());
        return claims.get("tyfId", String.class);
    }

    @Override
    public String extractUsername(String jwt)
    {
        return this.extractClaim(jwt, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        return claimResolver.apply(this.extractAllClaims(jwt));
    }

    private Key getSigningKey()
    {
        byte[] keyBytes = Base64.getDecoder().decode(SecurityConstants.SEC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwt)
    {
        return Jwts.parserBuilder().setSigningKey(this.getSigningKey()).build().parseClaimsJws(jwt).getBody();
    }

    @Override
    public Log getUserInfosFromJwt(String token)
    {
        Log log = new Log();
        Claims claims= this.extractAllClaims(token);
        Long  functionId = claims.get("functionId", Long.class);
        log.setUserEmail(this.extractUsername(token));
        log.setUserId(claims.get("userId", Long.class));
        log.setFunction(functionId == null ? null : functionRepo.findById(functionId).orElse(null));
        log.setConnectionId(claims.get("connectionId", String.class));
        return log;
    }

    @Override
    public Log getUserInfosFromJwt()
    {
        return this.getUserInfosFromJwt(this.getCurrentJwt());
    }

    @Override
    public JwtInfos getJwtInfos() {
        return this.getJwtInfos(this.getCurrentJwt());
    }

    @Override
    public JwtInfos getJwtInfos(String jwt)
    {
        JwtInfos jwtInfos = new JwtInfos();
        Claims claims= this.extractAllClaims(jwt);
        Long  functionId = claims.get("functionId", Long.class);
        AppFunction function = functionId == null ? null : functionRepo.findById(functionId).orElse(null);
        Long cedId = function == null ? null : function.getVisibilityId();
        Cedante ced = cedId == null ? null : cedRepo.findById(cedId).orElse(null);

        jwtInfos.setFncId(functionId);
        jwtInfos.setFncName(function == null ? "" : function.getName());
        jwtInfos.setCedId(cedId);
        jwtInfos.setCedName(ced == null ? "" : ced.getCedNomFiliale());
        jwtInfos.setCedSigle(ced == null ? "" : ced.getCedSigleFiliale());
        jwtInfos.setUserEmail(this.extractUsername(jwt));
        jwtInfos.setUserId(claims.get("userId", Long.class));
        jwtInfos.setNomPrenom(claims.get("nom", String.class) + " " + claims.get("prenom", String.class));
        jwtInfos.setAuthorities(claims.get("authorities", List.class));
        jwtInfos.setConnectionId(claims.get("connectionId", String.class));
        jwtInfos.setTokenStartingDate(this.extractClaim(jwt,Claims::getIssuedAt));
        jwtInfos.setTokenEndingDate(this.extractClaim(jwt,Claims::getExpiration));
        jwtInfos.setFncStartingDate(function == null ? null : function.getStartsAt());
        jwtInfos.setFncEndingDate(function == null ? null : function.getEndsAt());


        return jwtInfos;
    }

    @Override
    public String getCurrentJwt()
    {
        HttpServletRequest request = HttpServletManager.getCurrentHttpRequest();
        if(request == null) return null;
        return request.getHeader("Authorization") == null ? null : request.getHeader("Authorization").substring("Bearer ".length());
    }

    @Override
    public Object getClaim(String claimName)
    {
        return this.extractAllClaims(this.getCurrentJwt()).get(claimName);
    }

    @Override
    public Long getConnectedUserId() {
        Object userId = this.getClaim("userId");
        return userId == null ? null : Long.valueOf(String.valueOf(userId));
    }

    @Override
    public Long getConnectedUserFunctionId() {
        Object functionId = this.getClaim("functionId");
        return functionId == null ? null : Long.valueOf(String.valueOf(functionId));
    }

    @Override
    public Long getConnectedUserCedId() {
        Object visibilityId = this.getClaim("visibilityId");
        return visibilityId == null ? null : Long.valueOf(String.valueOf(visibilityId));
    }

    @Override
    public Long getConnectedUserCesId()
    {
        Long userId = this.getConnectedUserId();
        return userRepo.getUserCesId(userId);
    }

    @Override
    public boolean UserIsCourtier()
    {
        return this.getConnectedUserCedId() == null;
    }

    @Override
    public boolean hasAnyAuthority(String... auth)
    {
        if(auth == null) return false;
        Set<String> authList = Arrays.stream(auth).collect(Collectors.toSet());
        Set<String> userAuthorities = this.getAuthorities();
        boolean hasAnyAuthority =userAuthorities.stream().anyMatch(authList::contains);
        return hasAnyAuthority;
    }

    @Override
    public Set<String> getAuthorities()
    {
        Object authorities = this.getClaim("authorities");
        return authorities == null ? new HashSet<>() : new HashSet<>((List<String>) authorities);
    }

    class TokenStatus
    {
        private int status;
        private String description;
        private String message;
    }
}