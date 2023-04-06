package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IAuthoritiesService;
import com.pixel.synchronre.authmodule.controller.services.spec.IFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthoritiesService implements IAuthoritiesService
{
    private final IFunctionService functionService;
    private final RoleRepo roleRepo;
    private final PrvRepo prvRepo;
    @Override
    public Set<String> getAuthorities(Long userId)
    {
        Long currentFctId = functionService.getActiveCurrentFunctionId(userId);
        return currentFctId == null ? new HashSet<>() :  prvRepo.getFunctionPrvCodes(currentFctId);
    }
}
