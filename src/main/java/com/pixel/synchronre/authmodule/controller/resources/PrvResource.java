package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.services.spec.IPrivilegeService;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.CreatePrivilegeDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/privileges")
public class PrvResource
{
    private final IPrivilegeService prvService;
    private final PrvRepo prvRepo;

    @PostMapping(path = "/create")
    public ReadPrvDTO createPrv(@RequestBody @Valid CreatePrivilegeDTO dto) throws UnknownHostException, IllegalAccessException {
        return prvService.createPrivilege(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadPrvDTO> searchPrv(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException, IllegalAccessException {
        return prvService.searchPrivileges(key, PageRequest.of(num, size));
    }

    @GetMapping(path = "/existsByName/{name}")
    public boolean existsByName(@PathVariable String name) throws UnknownHostException, IllegalAccessException {
        return prvRepo.existsByName(name);
    }

    @GetMapping(path = "/existsByName/{name}/{prvId}")
    public boolean existsByName(@PathVariable String name, @PathVariable Long prvId) throws UnknownHostException, IllegalAccessException {
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
}
