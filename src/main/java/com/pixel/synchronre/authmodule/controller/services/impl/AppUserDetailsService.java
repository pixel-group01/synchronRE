package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.services.spec.IAuthoritiesService;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService
{
    private final UserRepo userRepo;
    private final IAuthoritiesService authoritiesService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser user = userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Utilisateur inexistant"));
        return new AppUserDetails(user, authoritiesService);
    }
}
