package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.menu.CreateMenuDTO;
import com.pixel.synchronre.authmodule.model.dtos.menu.PrvToMenuDTO;
import com.pixel.synchronre.authmodule.model.entities.Menu;

import java.net.UnknownHostException;
import java.util.Set;

public interface IMenuService
{
    boolean menuHasPrv(String menuCode, String prvCode);
    boolean prvCanSeeMenu(String prvCode, String menuCode);

    Set<String> getMenuPrvCodes(String menuCode);

    Set<Long> getMenuPrvIds(String menuCode);

    boolean fncCanSeeMenu(Long fncId, String menuCode);

    Menu createMenu(CreateMenuDTO dto) throws UnknownHostException;
    void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
    void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
}
