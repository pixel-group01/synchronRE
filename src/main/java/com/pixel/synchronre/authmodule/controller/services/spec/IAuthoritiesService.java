package com.pixel.synchronre.authmodule.controller.services.spec;

import java.util.Set;

public interface IAuthoritiesService
{
    public Set<String> getAuthorities(Long userId);
}
