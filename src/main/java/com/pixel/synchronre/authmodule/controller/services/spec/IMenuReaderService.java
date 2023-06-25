package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.menu.CreateMenuDTO;
import com.pixel.synchronre.authmodule.model.dtos.menu.PrvToMenuDTO;
import com.pixel.synchronre.authmodule.model.entities.Menu;

import java.net.UnknownHostException;
import java.util.Set;

public interface IMenuReaderService
{
    boolean menuHasPrv(String menuCode, String prvCode);
    boolean prvCanSeeMenu(String prvCode, String menuCode);

    Set<String> getMenuPrvCodes(String menuCode);

    Set<Long> getMenuPrvIds(String menuCode);

    Set<Menu> getMenusByFncId(Long fncId);

    boolean fncCanSeeMenu(Long fncId, String menuCode);


}
