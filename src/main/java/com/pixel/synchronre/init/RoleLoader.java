package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppRole;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class RoleLoader implements Loader
{
    private final TypeRepo typeRepo;
    private final RoleRepo roleRepo;

    @Override
    public void load()
    {
        AppRole roleOpeSai = roleRepo.save(new AppRole(null, "ROL-OPE-SAI", "Opérateur de saisie global"));

    }
}
