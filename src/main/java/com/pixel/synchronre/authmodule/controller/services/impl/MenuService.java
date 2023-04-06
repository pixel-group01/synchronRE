package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.MenuRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuService;
import com.pixel.synchronre.authmodule.model.constants.AuthActions;
import com.pixel.synchronre.authmodule.model.constants.AuthTables;
import com.pixel.synchronre.authmodule.model.dtos.menu.CreateMenuDTO;
import com.pixel.synchronre.authmodule.model.dtos.menu.MenuMapper;
import com.pixel.synchronre.authmodule.model.dtos.menu.PrvToMenuDTO;
import com.pixel.synchronre.authmodule.model.entities.Menu;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MenuService implements IMenuService
{
    private final MenuRepo menuRepo;
    private final PrvRepo prvRepo;
    private final MenuMapper menuMapper;
    private final ILogService logger;

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

    @Override @Transactional
    public Menu createMenu(CreateMenuDTO dto) throws UnknownHostException {
        Menu menu = menuMapper.mapToMenu(dto);
        String codeChain = Arrays.stream(dto.getPrvsCodes())
                .filter(code->!menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.joining(Menu.chainSeparator));
        menu.setPrvsCodesChain(codeChain);
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.CREATE_MENU, null, menu, AuthTables.MENU_TABLE);
        return menu;
    }

    @Override @Transactional
    public void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException
    {
        Menu menu = menuRepo.findByMenuCode(dto.getMenuCode());
        Menu oldMenu = new Menu();
        BeanUtils.copyProperties(menu, oldMenu);

        String codeChain = Arrays.stream(dto.getPrvCodes())
                .filter(code->!menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.joining(Menu.chainSeparator));
        if(codeChain == null || codeChain.trim().equals("")) return;
        menu.setPrvsCodesChain(menu.getPrvsCodesChain() + Menu.chainSeparator + codeChain);
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.ADD_PRV_TO_MENU, oldMenu, menu, "menu");
    }

    @Override
    public void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException
    {
        Menu menu = menuRepo.findByMenuCode(dto.getMenuCode());
        String codeChain = menu.getPrvsCodesChain();

        if(codeChain == null || codeChain.trim().equals("")) return;
        Menu oldMenu = new Menu();
        BeanUtils.copyProperties(menu, oldMenu);

        Set<String> prvCodesToRemove = Arrays.stream(dto.getPrvCodes())
                .filter(code->menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.toSet());
        if(codeChain == null || codeChain.trim().equals("")) return;

        menu.setPrvsCodesChain(Arrays.stream(codeChain.split(Menu.chainSeparator)).filter(code->!prvCodesToRemove.contains(code)).collect(Collectors.joining(Menu.chainSeparator)));
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.RMV_PRV_TO_MENU, oldMenu, menu, "menu");
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
}
