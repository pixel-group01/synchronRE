package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.authmodule.model.entities.Menu;
import com.pixel.synchronre.authmodule.model.entities.PrvToFunctionAss;
import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class MenuLoader implements Loader
{
    private final MenuRepo menuRepo;

    @Override
    public void load()
    {
        Menu menuStat = menuRepo.save(new Menu(null, "STAT", "Statistique", "GET-STAT-TRAI::GET-STAT-FAC::GET-STAT-SIN::GET-STAT-ADM", PersStatus.ACTIVE));
        Menu menuProd = menuRepo.save(new Menu(null, "PROD", "Productions", "GET-FAC-LST::GET-FAC-C-SAI::GET-FAC-C-PLA::GET-FAC-ARCH::GET-FAC-HIST::GET-TRAI-LST::GET-TRAI-C-SAI::VAL-FAC", PersStatus.ACTIVE));
        Menu menuSin = menuRepo.save(new Menu(null, "SIN", "Sinistres", "GET-SIN-SAI-LST::GET-SIN-TRANS-LST::GET-SIN-AVAL-LST::GET-SIN-SOLD-LST::GET-SIN-SUIV-LST::GET-SIN-ARCH-LST::VAL-SIN::RET-SIN::UPD-SIN::CRT-SIN::GET-SIN-REG-LST::GET-SIN-HISTO::GET-SIN-DET::RET-SIN-CED::GET-SIN-MSG-VAL::TRANS-SIN-VAL", PersStatus.ACTIVE));
        Menu menuCompta = menuRepo.save(new Menu(null, "COMPTA", "Comptabilité", "GET-REG-FAC-LST::GET-REG-SIN-LST::GET-PAI-FAC-LST::GET-REV-FAC-LST::GET-REG-SIN-LST::GET-SIN-SUIV-LST-COMPTA::GET-SIN-ARCH-LST-COMPTA::GET-SIN-SOLD-LST-COMPTA::CRT-PAI-SIN::CRT-REV-SIN::UPD-REV-SIN::GET-ETA-COMPT-SIN", PersStatus.ACTIVE));
        Menu menuParam = menuRepo.save(new Menu(null, "PARAM", "Paramètre", "GET-BANK-LST::GET-BRAN-LST::GET-CED-LST::GET-CES-LST::GET-COUV-LST::GET-DEV-LST::GET-EXE-LST::GET-PAY-LST", PersStatus.ACTIVE));
        Menu menuAdmin = menuRepo.save(new Menu(null, "ADMIN", "Administration", "GET-USER-LST::GET-PRV-LST::GET-ROL-LST::GET-LOG-HISTO", PersStatus.ACTIVE));
    }
}
