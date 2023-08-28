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

        List<AppPrivilege> prvsAdmin = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-ADM", "GET-STAT-PARAM","GET-STAT-PARAM", "GET-USER-DET",
                "CRT-USER-FNC", "GET-USER-LST", "UPD-USER", "BLQ-USER", "UBLQ-USER", "SEND-ACT-MAIL", "CRT-FNC", "UPD-FNC", "SET-FNC-DFLT",
                "RVK-FNC", "RSTR-FNC", "GET-ACT-FNC-LST", "GET-ALL-FNC-LST", "CRT-PRV", "GET-PRV-LST", "CRT-ROL", "GET-ROL-LST",
                "UPD-ROL", "CRT-BANK", "UPD-BANK", "GET-BANK-LST", "CRT-BRAN", "UPD-BRAN", "GET-BRAN-LST", "CRT-CED", "UPD-CED",
                "GET-CED-LST", "CRT-CES", "UPD-CES", "GET-CES-LEG-PARAM-LST", "CRT-CES-LEG-PARAM", "UPD-CES-LEG-PARAM",
                "GET-CES-LST", "CRT-COUV", "UPD-COUV", "GET-COUV-LST", "CRT-DEV", "UPD-DEV", "GET-DEV-LST", "CRT-EXE", "UPD-EXE",
                "GET-EXE-LST", "CRT-PAY", "UPD-PAY", "GET-PAY-LST","GET-CES-LEG-PARAM-LST"));
        prvsAdmin.forEach(prv->this.addPrvToRole(prv, roleAdmin));

        AppRole roleAdminTech = roleRepo.findByRoleCode("ROL-ADM-TECH");
        List<AppPrivilege> prvsAdminTech = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-ADM", "GET-STAT-PARAM", "GET-USER-DET", "CRT-USER-FNC", "GET-USER-LST",
                "UPD-USER", "BLQ-USER", "UBLQ-USER", "SEND-ACT-MAIL", "CRT-FNC", "UPD-FNC", "SET-FNC-DFLT", "RVK-FNC", "RSTR-FNC", "GET-ACT-FNC-LST", "GET-ALL-FNC-LST",
                "CRT-PRV", "GET-PRV-LST", "CRT-ROL", "GET-ROL-LST", "UPD-ROL"));
        prvsAdminTech.forEach(prv->this.addPrvToRole(prv, roleAdminTech));

        AppRole roleAdminFonc = roleRepo.findByRoleCode("ROL-ADM-FONC");
        List<AppPrivilege> prvAdminFonc = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-ADM", "GET-STAT-PARAM", "CRT-BANK", "UPD-BANK", "GET-BANK-LST",
                "CRT-BRAN", "UPD-BRAN", "GET-BRAN-LST", "CRT-CED", "UPD-CED", "GET-CED-LST", "CRT-CES", "UPD-CES", "GET-CES-LEG-PARAM-LST", "CRT-CES-LEG-PARAM",
                "UPD-CES-LEG-PARAM", "GET-CES-LST", "CRT-COUV", "UPD-COUV", "GET-COUV-LST", "CRT-DEV", "UPD-DEV", "GET-DEV-LST", "CRT-EXE", "UPD-EXE", "GET-EXE-LST",
                "CRT-PAY", "UPD-PAY", "GET-PAY-LST","GET-CES-LEG-PARAM-LST"));
        prvAdminFonc.forEach(prv->this.addPrvToRole(prv, roleAdminFonc));

        //ROLES GLOBALS
        AppRole roleOpeSaisie = roleRepo.findByRoleCode("ROL-OPE-SAI");
        List<AppPrivilege> prvOpeSaisie = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-LST","CRT-FAC","UPD-FAC","GET-FAC-DET",
                "ARCH-FAC","MARK-FAC-REA","MARK-FAC-NON-REA","ADD-DOC-FAC","CRT-REP-FAC","TRANS-FAC-SOUS",
                "GET-FAC-C-SAI","GET-FAC-C-PLA","GET-FAC-C-REG","GET-FAC-ARCH","GET-FAC-HIST","GET-ETA-COMPT-FAC","GET-FAC-MOUV","GET-AFF-PCL","UPD-REP","CRT-CED-LEG-REP",
                "GET-FAC-HISTO","GET-FAC-MSG-SOUS","GET-STAT-SIN","GET-SIN-LST","GET-SIN-FAC-LST","GET-SIN-TRAI-LST","GET-STAT-SIN","CRT-SIN",
                "UPD-SIN","DEL-SIN","TRANS-SIN","GET-SIN-LST","GET-SIN-FAC-LST","GET-SIN-TRAI-LST","GET-SIN-SAI-LST","GET-SIN-TRANS-LST",
                "GET-SIN-AVAL-LST","GET-SIN-SOLD-LST","GET-SIN-SUIV-LST","GET-SIN-ARCH-LST","GET-SIN-REG-LST","GET-SIN-HISTO","GET-SIN-DET","GET-SIN-MSG-SOUS",
                "TRANS-SIN"));
        prvOpeSaisie.forEach(prv->this.addPrvToRole(prv, roleOpeSaisie));


        //ROLES GLOBALS
        AppRole roleSous = roleRepo.findByRoleCode("ROL-SOUS");
        List<AppPrivilege> prvOpeSous = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-LST","CRT-FAC","UPD-FAC","GET-FAC-DET",
                "ARCH-FAC","MARK-FAC-REA","MARK-FAC-NON-REA","ADD-DOC-FAC","CRT-REP-FAC","TRANS-FAC-SOUS",
                "GET-FAC-C-SAI","GET-FAC-C-PLA","GET-FAC-C-REG","GET-FAC-ARCH","GET-FAC-HIST","GET-ETA-COMPT-FAC","GET-FAC-MOUV","GET-FAC-HISTO","CRT-PLA-FAC","RET-FAC-CED",
                "TRANS-FAC-SOUS","TRANS-FAC-VAL","GET-FAC-MSG-VAL","GET-FAC-REFU-MSG","SEND-NOT-DEB-FAC","SEND-NOT-CRED-FAC","SEND-NOT-CES-FAC",
                "GET-CES-AFF-LST","GET-PLA-RET-MSG","ACPT-PLA","ANL-PLA","REFU-PLA","RET-PLA","VAL-PLA","GET-PLA-C-SAI","GET-PLA-A-VAL","GET-PLA-VAL","CRT-CED-LEG-REP",
                "CRT-PLA","DLT-PLA","GET-AFF-PCL","UPD-PLA","TRANS-PLA","UPD-REP",
                "GET-STAT-SIN","GET-SIN-LST","GET-SIN-FAC-LST",
                "GET-SIN-TRAI-LST","GET-STAT-SIN","GET-SIN-LST","GET-SIN-FAC-LST","GET-SIN-TRAI-LST","GET-SIN-TRANS-LST","GET-SIN-AVAL-LST","GET-SIN-SOLD-LST",
                "GET-SIN-SUIV-LST","GET-SIN-ARCH-LST","GET-SIN-REG-LST","GET-SIN-HISTO","GET-SIN-DET","TRANS-SIN-VAL","GET-SIN-MSG-VAL","RET-SIN-CED", "EDIT-NOT-DEB-FAC"));
        prvOpeSous.forEach(prv->this.addPrvToRole(prv, roleSous));

        AppRole roleValidateur = roleRepo.findByRoleCode("ROL-VAL");
        List<AppPrivilege> prvValidateur = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-LST","GET-FAC-ARCH","GET-FAC-HIST","VAL-FAC",
                "VAL-PLA","GET-FAC-C-REG","GET-FAC-DET","EDIT-NOT-DEB-FAC","GET-ETA-COMPT-FAC","RET-FAC-SOUS","GET-FAC-HISTO","GET-FAC-MSG-COMPTA","GET-FAC-MOUV","RET-PLA",
                "GET-PLA-A-VAL","GET-PLA-VAL",
                "GET-STAT-SIN","GET-SIN-LST","GET-SIN-FAC-LST","GET-SIN-TRAI-LST","GET-STAT-SIN","GET-SIN-AVAL-LST","GET-SIN-SOLD-LST",
                "GET-SIN-SUIV-LST","GET-SIN-ARCH-LST","VAL-SIN","TRANS-SIN-COMPTA","RET-SIN-SOUS","GET-SIN-HISTO","GET-SIN-DET","GET-SIN-REG-LST",
                "GET-SIN-MSG-COMPTA","TRANS-FAC-COMPTA"));
        prvValidateur.forEach(prv->this.addPrvToRole(prv, roleValidateur));

        AppRole roleComptable = roleRepo.findByRoleCode("ROL-COMPTA");
        List<AppPrivilege> prvComptable = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-C-REG","GET-FAC-DET","GET-ETA-COMPT-FAC",
                "CRT-REV-FAC","GET-REG-FAC-LST","GET-PAI-FAC-LST","GET-REV-FAC-LST","UPD-PAI-FAC","UPD-REV-FAC",
                        "GET-STAT-SIN","GET-SIN-SOLD-LST-COMPTA","GET-SIN-SUIV-LST-COMPTA",
                "GET-SIN-ARCH-LST-COMPTA","GET-REG-SIN-LST","CRT-PAI-SIN","CRT-REV-SIN","UPD-REV-SIN","GET-ETA-COMPT-SIN","TRANS-SIN","RET-SIN-VAL",
                "GET-SIN-MSG-COMPTA"));
        prvComptable.forEach(prv->this.addPrvToRole(prv, roleComptable));
        //FIN ROLES GLOBALS

        //Affaire FAC
        AppRole roleOpeSaisieFac = roleRepo.findByRoleCode("ROL-OPE-SAI-FAC");
        List<AppPrivilege> prvOpeSaisieFac = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-LST","GET-FAC-C-SAI","GET-FAC-C-PLA",
                "GET-FAC-C-REG","GET-FAC-ARCH","GET-FAC-HIST","CRT-FAC","UPD-FAC","GET-FAC-DET","SEND-FAC","CRT-PLA-FAC","ARCH-FAC",
                "MARK-FAC-REA","MARK-FAC-NON-REA","ADD-DOC-FAC","GET-FAC-MOUV","GET-FAC-REFU-MSG","GET-FAC-RET-MSG","GET-PLA-RET-MSG", "EDIT-NOT-DEB-FAC"));
        prvOpeSaisieFac.forEach(prv->this.addPrvToRole(prv, roleOpeSaisieFac));

        AppRole roleOpeSaisieSin = roleRepo.findByRoleCode("ROL-OPE-SAI-SIN");
        List<AppPrivilege> prvOpeSaisieSin = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-SIN","CRT-SIN","UPD-SIN","DEL-SIN","TRANS-SIN","GET-SIN-LST",
                "GET-SIN-FAC-LST","GET-SIN-TRAI-LST","GET-SIN-SAI-LST","GET-SIN-TRANS-LST","GET-SIN-AVAL-LST","GET-SIN-SOLD-LST","GET-SIN-SUIV-LST",
                "GET-SIN-ARCH-LST","GET-SIN-REG-LST","GET-SIN-HISTO","GET-SIN-DET","GET-SIN-MSG-SOUS","TRANS-SIN-VAL","GET-SIN-MSG-VAL","RET-SIN-CED",
                "GET-SIN-MSG-VAL","RET-SIN-SOUS","RET-SIN-SOUS"));
        prvOpeSaisieSin.forEach(prv->this.addPrvToRole(prv, roleOpeSaisieSin));

        AppRole roleOpeSaisieTrai = roleRepo.findByRoleCode("ROL-OPE-SAI-TRAI");
        List<AppPrivilege> prvOpeSaisieTrai = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-TRAI","GET- TRAI-LST","GET-TRAI-C-SAI"));
        prvOpeSaisieTrai.forEach(prv->this.addPrvToRole(prv, roleOpeSaisieTrai));

        AppRole roleValidateurFac = roleRepo.findByRoleCode("ROL-VAL-FAC");
        List<AppPrivilege> prvValidateurFac = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-LST","GET-FAC-C-REG","GET-FAC-ARCH",
                "GET-FAC-HIST","VAL-FAC","VAL-PLA","GET-SIN-REG-LST","GET-SIN-HISTO","GET-SIN-DET", "TRANS-FAC-COMPTA"));
        prvValidateurFac.forEach(prv->this.addPrvToRole(prv, roleValidateurFac));

        AppRole roleComptableFac = roleRepo.findByRoleCode("ROL-COMPTA-FAC");
        List<AppPrivilege> prvComptableFac = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-FAC","GET-FAC-C-REG","GET-FAC-ARCH","GET-FAC-HIST",
                "GET-FAC-DET","RET-FAC","GET-ETA-COMPT-FAC","CRT-REV-FAC","GET-REG-FAC-LST","GET-PAI-FAC-LST",
                "GET-REV-FAC-LST","EDIT-NOT-DEB-FAC"));
        prvComptableFac.forEach(prv->this.addPrvToRole(prv, roleComptableFac));

        //TRANS-FAC-COMPTA : Transmettre une affaire à la comptabilité
        //        //EDIT-NOT-DEB-FAC : imprimer note de débit fac


//        //Sinistre FAC
//        AppRole roleOpeSaisieSinistres = roleRepo.findByRoleCode("ROL-OPE-SAI-SIN");
//        List<AppPrivilege> prvOpeSaisieSinistres = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-SIN","GET-SIN-LST","GET-SIN-TRAI-LST","GET-SIN-FAC-LST"));
//        prvOpeSaisieSinistres.forEach(prv->this.addPrvToRole(prv, roleOpeSaisieSinistres));

        AppRole roleValidateurSin = roleRepo.findByRoleCode("ROL-VAL-SIN");
        List<AppPrivilege> prvValidateurSin = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-SIN","GET-SIN-AVAL-LST","GET-SIN-SOLD-LST","GET-SIN-SUIV-LST",
                "GET-SIN-ARCH-LST","TRANS-SIN-COMPTA","RET-SIN-SOUS","GET-SIN-HISTO","GET-SIN-DET","GET-SIN-REG-LST","GET-SIN-MSG-COMPTA"));
        prvValidateurSin.forEach(prv->this.addPrvToRole(prv, roleValidateurSin));

        AppRole roleComptableSin = roleRepo.findByRoleCode("ROL-COMPTA-SIN");
        List<AppPrivilege> prvComptableSin = prvRepo.findByPrvCodes(Arrays.asList("GET-STAT-SIN","GET-SIN-SOLD-LST-COMPTA","GET-SIN-SUIV-LST-COMPTA",
                "GET-SIN-ARCH-LST-COMPTA","GET-REG-SIN-LST","CRT-PAI-SIN","CRT-REV-SIN","UPD-REV-SIN","GET-ETA-COMPT-SIN","RET-SIN-VAL","GET-SIN-MSG-COMPTA"));
        prvComptableSin.forEach(prv->this.addPrvToRole(prv, roleComptableSin));








    }
}
