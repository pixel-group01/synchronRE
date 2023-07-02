package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.MenuRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvToFunctionAssRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuReaderService;
import com.pixel.synchronre.authmodule.model.entities.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MenuReaderService implements IMenuReaderService
{
    private final MenuRepo menuRepo;
    private final PrvRepo prvRepo;
    private final PrvToFunctionAssRepo ptfRepo;

    @Override
    public boolean menuHasPrv(String menuCode, String prvCode)
    {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean prvCanSeeMenu(String prvCode, String menuCode) {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean fncCanSeeMenu(Long fncId, String menuCode) {
        Set<Long> fncPrvIds = prvRepo.getFunctionPrvIds(fncId);
        Set<Long> menuPrvIds = this.getMenuPrvIds(menuCode);
        if(fncPrvIds == null || menuPrvIds == null) return false;
        fncPrvIds.retainAll(menuPrvIds);
        return !fncPrvIds.isEmpty();
    }

    @Override
    public Set<String> getMenuPrvCodes(String menuCode) {
        String prvCodeChain = menuRepo.getPrvsCodesByMenuCode(menuCode);
        return  prvCodeChain == null ? new HashSet<>() : new HashSet<>(Arrays.asList(prvCodeChain.split(",")));
    }

    @Override
    public Set<Long> getMenuPrvIds(String menuCode) {
        return  menuRepo.getMenuPrvIdsByMenuCodes(this.getMenuPrvCodes(menuCode));
    }

    @Override
    public Set<String> getMenusByFncId(Long fncId)
    {
        Set<Long> prvIds = ptfRepo.getFncPrvIds(fncId);
        Set<String> prvCodes = prvRepo.getPrvCodesByPrvIds(prvIds);
        Set<String> menus = menuRepo.findAll().stream()
                .filter(m->m.getPrvsCodes().stream().anyMatch(menuCode->prvCodes.contains(menuCode)))
                .map(m->"MENU_" + m.getMenuCode())
                .collect(Collectors.toSet());
        return menus;
    }
}
