package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.repositories.PrvToRoleAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IPrivilegeService;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.PrvByTypeDTO;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.CreatePrivilegeDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Set;

@RequiredArgsConstructor
@RestController @RequestMapping("/privileges") @ResponseStatus(HttpStatus.OK)
public class PrvResource
{
    private final IPrivilegeService prvService;
    private final PrvRepo prvRepo;
    private final PrvToRoleAssRepo ptrRepo;

    @PostMapping(path = "/create")
    public ReadPrvDTO createPrv(@RequestBody @Valid CreatePrivilegeDTO dto) throws UnknownHostException, IllegalAccessException {
        return prvService.createPrivilege(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadPrvDTO> searchPrv(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size){
        return prvService.searchPrivileges(key, PageRequest.of(num, size));
    }

    @GetMapping(path = "/grouped-by-type")
    public Set<PrvByTypeDTO> getAllPrivilegesGroupedByType(){
        return prvService.getAllPrivlegesGroupesByType();
    }

    @GetMapping(path = "/existsByName/{name}")
    public boolean existsByName(@PathVariable String name){
        return prvRepo.existsByName(name);
    }

    @GetMapping(path = "/existsByName/{name}/{prvId}")
    public boolean existsByName(@PathVariable String name, @PathVariable Long prvId){
        return prvRepo.existsByName(name, prvId);
    }

    @GetMapping(path = "/existsByCode/{code}")
    public boolean existsByCode(@PathVariable String code) throws UnknownHostException, IllegalAccessException {
        return prvRepo.existsByCode(code);
    }

    @GetMapping(path = "/existsByCode/{code}/{prvId}")
    public boolean existsByCode(@PathVariable String code, @PathVariable Long prvId) throws UnknownHostException, IllegalAccessException {
        return prvRepo.existsByCode(code, prvId);
    }

    @GetMapping(path = "/privilegeIds-for-roleIds")
    public Set<Long> getPrvIdsForRoleIds(@RequestParam Set<Long> roleIds) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.findActivePrvIdsForRoles(roleIds);
    }

    @GetMapping(path = "/privileges-for-roleIds")
    public Set<ReadPrvDTO> getPrvsForRoleIds(@RequestParam Set<Long> roleIds) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.findActivePrivilegesForRoles(roleIds);
    }

    @GetMapping(path = "/privilege-belong-to-any-role")
    public boolean prvBelongToAnyRole(@RequestParam Set<Long> roleIds, @RequestParam Long prvId) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.prvBelongToAnyRole(prvId, roleIds);
    }
}
