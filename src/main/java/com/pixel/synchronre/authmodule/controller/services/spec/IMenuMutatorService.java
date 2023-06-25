package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.menu.CreateMenuDTO;
import com.pixel.synchronre.authmodule.model.dtos.menu.PrvToMenuDTO;
import com.pixel.synchronre.authmodule.model.entities.Menu;

import java.net.UnknownHostException;
import java.util.Set;

public interface IMenuMutatorService
{

    Menu createMenu(CreateMenuDTO dto) throws UnknownHostException;
    void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
    void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
}
