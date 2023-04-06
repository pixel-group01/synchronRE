package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.repositories.MenuRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuService;
import com.pixel.synchronre.authmodule.model.dtos.menu.CreateMenuDTO;
import jakarta.validation.Valid;
import com.pixel.synchronre.authmodule.model.dtos.menu.PrvToMenuDTO;
import com.pixel.synchronre.authmodule.model.entities.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/gest-asso")
public class MenuResource
{
    private final IMenuService menuService;
    private final MenuRepo menuRepo;

    @PostMapping(path = "/menu/create-menu")
    public Menu createMenu(@RequestBody @Valid CreateMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        return menuService.createMenu(dto);
    }

    @GetMapping(path = "/menu/search")
    public Page<Menu> searchMenu(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException, IllegalAccessException {
        return menuRepo.searchMenu2(key, PageRequest.of(num, size));
    }

    @GetMapping(path = "/menu/fnc-can-see-menu/{fncId}/{nemuCode}")
    public boolean FncCanSeeMenu(@PathVariable Long fncId, @PathVariable String nemuCode) throws UnknownHostException, IllegalAccessException {
        return menuService.fncCanSeeMenu(fncId, nemuCode);
    }

    @GetMapping(path = "/menu/prv-can-see-menu/{prvCode}/{nemuCode}")
    public boolean prvCanSeeMenu(@PathVariable String prvCode, @PathVariable String menuCode) throws UnknownHostException, IllegalAccessException {
        return menuService.prvCanSeeMenu(prvCode, menuCode);
    }

    @PostMapping(path = "/menu/add-prv-to-menu")
    public void addPrvToMenu(@RequestBody @Valid PrvToMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        menuService.addPrvToMenu(dto);
    }

    @PostMapping(path = "/menu/rmv-prv-to-menu")
    public void removePrvToMenu(@RequestBody @Valid PrvToMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        menuService.removePrvToMenu(dto);
    }
}