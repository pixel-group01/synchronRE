package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import com.pixel.synchronre.authmodule.model.dtos.appuser.AuthResponseDTO;
import com.pixel.synchronre.logmodule.model.entities.Log;
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
import java.util.*;
import java.util.function.Function;

@Service @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    private final FunctionRepo functionRepo;
    private final UserRepo userRepo;

    @Override
    public AuthResponseDTO generateJwt(UserDetails userDetails, String connectionId)
    {
        String userEmail = userDetails.getUsername();
        Long userId = userRepo.getUserIdByEmail(userEmail);
        Set<Long> visibilityIds = functionRepo.getCurrentFncVisibilityIds(userDetails.getUsername());
        Set<Long> functionIds = functionRepo.getCurrentFncVisibilityIds(userDetails.getUsername());
        Long functionId = functionIds == null || functionIds.size() != 1 ? null : new ArrayList<>(functionIds).get(0);
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("userId", userId);
        extraClaims.put("userEmail", userEmail);
        extraClaims.put("authorities", userDetails.getAuthorities());
        extraClaims.put("visibilityId", visibilityIds == null ? null : visibilityIds.size() != 1 ? null : new ArrayList<>(visibilityIds).get(0));
        extraClaims.put("functionId", functionId);
        extraClaims.put("connectionId", connectionId);

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
    public String getCurrentJwt()
    {
        HttpServletRequest request = HttpServletManager.getCurrentHttpRequest();
        if(request == null) return null;
        String token  = request.getHeader("Authorization").substring("Bearer ".length());
        return token;
    }

    @Override
    public Object getClaim(String claimName)
    {
        return this.extractAllClaims(this.getCurrentJwt()).get(claimName);
    }
}
