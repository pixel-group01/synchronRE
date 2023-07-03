package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service @RequiredArgsConstructor
public class AssLoader implements Loader
{
    private final RoleRepo roleRepo;
    private final PrvRepo prvRepo;
    private final PrvToRoleAssRepo ptrRepo;

    private void addPrvToRole( AppPrivilege prv, AppRole role)
    {
        ptrRepo.save(new PrvToRoleAss(null, 1, null, null, prv, role));
    }

    @Override
    public void load()
    {
        AppRole roleAdmin = roleRepo.findByRoleCode("ROL-ADM");

        List<AppPrivilege> prvsAdmin = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-TRAI", "GET-STAT-FAC", "GET-STAT-SIN", "GET-STAT-ADM", "GET-STAT-PARAM", "GET-USER-DET", "CRT-USER-FNC", "GET-USER-LST", "UPD-USER", "BLQ-USER", "UBLQ-USER", "SEND-ACT-MAIL", "CRT-FNC", "UPD-FNC", "SET-FNC-DFLT", "RVK-FNC", "RSTR-FNC", "GET-ACT-FNC-LST", "GET-ALL-FNC-LST", "CRT-PRV", "GET-PRV-LST", "CRT-ROL", "GET-ROL-LST", "UPD-ROL", "CRT-BANK", "UPD-BANK", "GET-BANK-LST", "CRT-BRAN", "UPD-BRAN", "GET-BRAN-LST", "CRT-CED", "UPD-CED", "GET-CED-LST", "CRT-CES", "UPD-CES", "GET-CES-LEG-PARAM-LST", "CRT-CES-LEG-PARAM", "UPD-CES-LEG-PARAM", "GET-CES-LST", "CRT-COUV", "UPD-COUV", "GET-COUV-LST", "CRT-DEV", "UPD-DEV", "GET-DEV-LST", "CRT-EXE", "UPD-EXE", "GET-EXE-LST", "CRT-PAY", "UPD-PAY", "GET-PAY-LST"));
        prvsAdmin.forEach(prv->this.addPrvToRole(prv, roleAdmin));

        AppRole roleAdminTech = roleRepo.findByRoleCode("ROL-ADM-TECH");
        List<AppPrivilege> prvsAdminTech = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-TRAI", "GET-STAT-FAC", "GET-STAT-SIN", "GET-STAT-ADM", "GET-STAT-PARAM", "GET-USER-DET", "CRT-USER-FNC", "GET-USER-LST", "UPD-USER", "BLQ-USER", "UBLQ-USER", "SEND-ACT-MAIL", "CRT-FNC", "UPD-FNC", "SET-FNC-DFLT", "RVK-FNC", "RSTR-FNC", "GET-ACT-FNC-LST", "GET-ALL-FNC-LST", "CRT-PRV", "GET-PRV-LST", "CRT-ROL", "GET-ROL-LST", "UPD-ROL"));
        prvsAdminTech.forEach(prv->this.addPrvToRole(prv, roleAdminTech));

        AppRole roleAdminFonc = roleRepo.findByRoleCode("ROL-ADM-FONC");
        List<AppPrivilege> prvAdminFonc = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-TRAI", "GET-STAT-FAC", "GET-STAT-SIN", "GET-STAT-ADM", "GET-STAT-PARAM", "CRT-BANK", "UPD-BANK", "GET-BANK-LST", "CRT-BRAN", "UPD-BRAN", "GET-BRAN-LST", "CRT-CED", "UPD-CED", "GET-CED-LST", "CRT-CES", "UPD-CES", "GET-CES-LEG-PARAM-LST", "CRT-CES-LEG-PARAM", "UPD-CES-LEG-PARAM", "GET-CES-LST", "CRT-COUV", "UPD-COUV", "GET-COUV-LST", "CRT-DEV", "UPD-DEV", "GET-DEV-LST", "CRT-EXE", "UPD-EXE", "GET-EXE-LST", "CRT-PAY", "UPD-PAY", "GET-PAY-LST"));
        prvAdminFonc.forEach(prv->this.addPrvToRole(prv, roleAdminFonc));


    }
}