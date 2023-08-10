package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvToFunctionAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvToRoleAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppRole;
import com.pixel.synchronre.authmodule.model.entities.PrvToRoleAss;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class RoleLoader implements Loader
{
    private final PrvToRoleAssRepo ptrRepo;
    private final PrvRepo prvRepo;
    private final RoleRepo roleRepo;

    @Override
    public void load()
    {
        AppRole roleOpeSai = roleRepo.save(new AppRole(null, "ROL-OPE-SAI", "Opérateur de saisie global"));
        AppRole roleOpeSaiFac = roleRepo.save(new AppRole(null, "ROL-OPE-SAI-FAC", "Opérateur de saisie fac"));
        AppRole roleOpeSaiTrai = roleRepo.save(new AppRole(null, "ROL-OPE-SAI-TRAI", "Opérateur de saisie traité"));
        AppRole roleOpeSaiSin = roleRepo.save(new AppRole(null, "ROL-OPE-SAI-SIN", "Opérateur de saisie sinistre"));
        AppRole roleVal = roleRepo.save(new AppRole(null, "ROL-VAL", "Validateur global"));
        AppRole roleSous = roleRepo.save(new AppRole(null, "ROL-SOUS", "Souscripteur global"));
        AppRole roleValFac = roleRepo.save(new AppRole(null, "ROL-VAL-FAC", "Validateur fac"));
        AppRole roleValTrai = roleRepo.save(new AppRole(null, "ROL-VAL-TRAI", "Validateur traité"));
        AppRole roleValSin = roleRepo.save(new AppRole(null, "ROL-VAL-SIN", "Validateur sinistre"));
        AppRole roleObs = roleRepo.save(new AppRole(null, "ROL-OBS", "Observateur global"));
        AppRole roleObsFac = roleRepo.save(new AppRole(null, "ROL-OBS-FAC", "Observateur fac"));
        AppRole roleObsTrai = roleRepo.save(new AppRole(null, "ROL-OBS-TRAI", "Observateur traité"));
        AppRole roleObsSin = roleRepo.save(new AppRole(null, "ROL-OBS-SIN", "Observateur sinistre"));
        AppRole roleObsCompta = roleRepo.save(new AppRole(null, "ROL-OBS-COMPTA", "Observateur comptable"));
        AppRole roleObsTech = roleRepo.save(new AppRole(null, "ROL-OBS-TECH", "Observateur technique"));
        AppRole roleObsFonc = roleRepo.save(new AppRole(null, "ROL-OBS-FONC", "Observateur fonctionnel"));
        AppRole roleAdm = roleRepo.save(new AppRole(null, "ROL-ADM", "Administrateur global"));
        AppRole roleAdmTech = roleRepo.save(new AppRole(null, "ROL-ADM-TECH", "Administrateur technique"));
        AppRole roleAdmFonc = roleRepo.save(new AppRole(null, "ROL-ADM-FONC", "Administrateur fonctionnel"));
        AppRole roleCompta = roleRepo.save(new AppRole(null, "ROL-COMPTA", "Comptable"));
        AppRole roleComptaFac = roleRepo.save(new AppRole(null, "ROL-COMPTA-FAC", "Comptable fac"));
        AppRole roleComptaTrai = roleRepo.save(new AppRole(null, "ROL-COMPTA-TRAI", "Comptable traité"));
        AppRole roleComptaSin = roleRepo.save(new AppRole(null, "ROL-COMPTA-SIN", "Comptable sinistre"));
        AppRole roleDev = roleRepo.save(new AppRole(null, "ROL-DEV", "Developpeur"));

        prvRepo.findAll().forEach(prv->ptrRepo.save(new PrvToRoleAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), prv, roleDev)));
    }
}
