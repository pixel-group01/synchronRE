package com.pixel.synchronre.authmodule.config;

import com.pixel.synchronre.authmodule.filters.JwtAuthenticationFilter;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.DefaultSignatureValidatorFactory;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import io.jsonwebtoken.security.Keys;
import com.pixel.synchronre.authmodule.model.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

@Configuration @EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final JwtAuthenticationFilter authenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors(cors -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList("http://localhost:4200/","http://localhost:8000/","http://localhost/",
                                "http://164.160.41.153/","http://10.100.100.106/", "http://141.94.206.196:8000"
                        ));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        return config;
                    };
                    cors.configurationSource(source);
                })
                .authorizeHttpRequests()
                .requestMatchers("/*/open/**", "/reports/**","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and().authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public JwtSignatureValidator jwtSignatureValidator()
    {
        byte[] keyBytes = Base64.getDecoder().decode(SecurityConstants.SEC_KEY);

        return  new DefaultJwtSignatureValidator(new DefaultSignatureValidatorFactory(),
                SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(keyBytes),
                s -> Base64.getDecoder().decode(s));
    }
}
