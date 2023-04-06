package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.services.spec.IRoleService;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.model.dtos.approle.CreateRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.ReadRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/gest-asso")
public class RoleResource
{
    private final IRoleService roleService;
    private final RoleRepo roleRepo;

    @PostMapping(path = "/roles/create-role")
    public ReadRoleDTO createrole(@RequestBody @Valid CreateRoleDTO dto) throws UnknownHostException, IllegalAccessException {
        return roleService.createRole(dto);
    }

    @GetMapping(path = "/roles/search")
    public Page<ReadRoleDTO> searchrole(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException, IllegalAccessException {
        return roleService.searchRoles(key, PageRequest.of(num, size));
    }

    @GetMapping(path = "/roles/existsByName/{name}")
    public boolean existsByName(@PathVariable String name) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name);
    }

    @GetMapping(path = "/roles/existsByName/{name}/{roleId}")
    public boolean existsByName(@PathVariable String name, @PathVariable Long roleId) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name, roleId);
    }

    @GetMapping(path = "/roles/existsByCode/{code}")
    public boolean existsByCode(@PathVariable String code) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByCode(code);
    }

    @GetMapping(path = "/roles/existsByCode/{code}/{roleId}")
    public boolean existsByCode(@PathVariable String code, @PathVariable Long roleId) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByCode(code, roleId);
    }
}