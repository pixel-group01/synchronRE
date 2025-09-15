package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.appuser.AuthResponseDTO;
import com.pixel.synchronre.logmodule.model.dtos.response.JwtInfos;
import com.pixel.synchronre.logmodule.model.entities.Log;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface IJwtService
{
    AuthResponseDTO generateJwt(UserDetails userDetails, String connectionId);
    AuthResponseDTO generateJwt(String username, Map<String, Object> extraClaims);

    String extractTyfLibelle();

    String extractTyfId();

    String extractUsername(String jwt);
    String extractUsername();
    String extractConnectionId();
    String extractTyfCode();
    <T> T extractClaim(String jwt, Function<Claims, T> f);

    Log getUserInfosFromJwt(String token);

    Log getUserInfosFromJwt();

    JwtInfos getJwtInfos();
    JwtInfos getJwtInfos(String jwt);

    String getCurrentJwt();

    Object getClaim(String claimName);
    Long getConnectedUserId();
    Long getConnectedUserFunctionId();
    Long getConnectedUserCedId();
    Long getConnectedUserCesId();

    boolean userIsCourtier();

    boolean hasAnyAuthority(String ...s);
    Set<String> getAuthorities();
}
