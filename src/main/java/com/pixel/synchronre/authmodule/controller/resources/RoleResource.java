package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.services.spec.IRoleService;
import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.model.dtos.approle.CreateRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.ReadRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RestController @RequestMapping("/roles")
public class RoleResource
{
    private final IRoleService roleService;
    private final RoleRepo roleRepo;

    @PostMapping(path = "/create")
    public ReadRoleDTO createRole(@RequestBody @Valid CreateRoleDTO dto) throws UnknownHostException, IllegalAccessException {
        return roleService.createRole(dto);
    }

    @PutMapping(path = "/update")
    public ReadRoleDTO setPrivileges(@RequestBody @Valid PrvsToRoleDTO dto) throws UnknownHostException, IllegalAccessException {
        return roleService.updateRole(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadRoleDTO> searchrole(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException, IllegalAccessException {
        return roleService.searchRoles(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/existsByName/{name}")
    public boolean existsByName(@PathVariable String name) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name);
    }

    @GetMapping(path = "/existsByName/{name}/{roleId}")
    public boolean existsByName(@PathVariable String name, @PathVariable Long roleId) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name, roleId);
    }

    @GetMapping(path = "/existsByCode/{code}")
    public boolean existsByCode(@PathVariable String code) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByCode(code);
    }

    @GetMapping(path = "/existsByCode/{code}/{roleId}")
    public boolean existsByCode(@PathVariable String code, @PathVariable Long roleId) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByCode(code, roleId);
    }
}
