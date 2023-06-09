package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class AdminLoader implements Loader
{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final RoleToFunctionAssRepo rtfRepo;
    private final PasswordEncoder pe;
    private final PrvRepo prvRepo;
    private final FunctionRepo fncRepo;
    private final PrvToFunctionAssRepo ptfRepo;

    @Override
    public void load()
    {
       //Developpeur
        AppUser userDev = userRepo.save(new AppUser(null, "Développeur", "Synchrone-Re", null, 4l,
                pe.encode("KD@fgfysh458@"), "pixelgroup09@gmail.com", "0505050505",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));
        AppFunction fncDev = fncRepo.save(new AppFunction(null, null, 4l, "Développeur synchrone-Re", userDev, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userDev.setCurrentFunctionId(fncDev.getId());
        userRepo.save(userDev);
        AppRole roleDev = roleRepo.findByRoleCode("ROL-DEV");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleDev, fncDev));


        //AMINI
        AppUser useradmin1 = userRepo.save(new AppUser(null, "N’GUESSAN", "Yao Yavo Basile", null, 4l,
                pe.encode("KD@f8z73t@"), "Basile.nguessan@groupensia.com", "0505893546",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));
        AppFunction functionAdmin1 = fncRepo.save(new AppFunction(null, null, 4l, "Administrateur SyncrhoneRe", useradmin1, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        useradmin1.setCurrentFunctionId(functionAdmin1.getId());
        userRepo.save(useradmin1);
        AppRole roleAdmin = roleRepo.findByRoleCode("ROL-ADM");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(1), roleAdmin, functionAdmin1));


        //user NSIA COTE D'IVOIRE
        AppUser userci = userRepo.save(new AppUser(null, "userci", "userci", 1L, 4l,
                pe.encode("1234"), "userci@gmail.com", "userci-tel",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));

        //Operateur de saisie FAC CI
        AppRole roleSaisieFacNsiaCi = roleRepo.findByRoleCode("ROL-OPE-SAI-FAC");
        AppFunction fncSaiFacCI = fncRepo.save(new AppFunction(null, 1L, 4l, "Opérateur de saisie FAC NSIA-CI", userci, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(userci);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaCi, fncSaiFacCI));
        //


        //User NSIA BENIN
        AppUser userbn = userRepo.save(new AppUser(null, "userbn", "userbn", 2l, 4l,
                pe.encode("1234"), "userbn@gmail.com", "userbn-tel",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));

        //Operateur de saisie FAC BENIN
        AppRole roleSaisieFacNsiaBn = roleRepo.findByRoleCode("ROL-OPE-SAI-FAC");
        AppFunction fncSaiFacBN = fncRepo.save(new AppFunction(null, 2L, 4l, "Opérateur de saisie FAC NSIA-BENIN", userbn, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(userbn);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaBn, fncSaiFacBN));

        //USER NSIA TOGO
        AppUser usertg = userRepo.save(new AppUser(null, "usertg", "usertg", 3l, 4l,
                pe.encode("1234"), "usertg@gmail.com", "usertg-tel",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        //Operateur de saisie FAC TOGO
        AppRole roleSaisieFacNsiaTg = roleRepo.findByRoleCode("ROL-OPE-SAI-FAC");
        AppFunction fncSaiFactg = fncRepo.save(new AppFunction(null, 3L, 4l, "Opérateur de saisie FAC NSIA-TOGO", usertg, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(usertg);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaTg, fncSaiFactg));

        //SOUSCRIPTEUR
        AppUser userSouscripteur = userRepo.save(new AppUser(null, "souscripteur", "souscripteur", null, 4l,
                pe.encode("1234"), "souscripteur@gmail.com", "123456783",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        //Souscripteur Nelson RE
        AppRole roleSouscripteur = roleRepo.findByRoleCode("ROL-OPE-SAI-FAC");
        AppFunction fncSouscripteur = fncRepo.save(new AppFunction(null, null, 4l, "Souscripteur Nelson RE", userSouscripteur, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(userSouscripteur);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSouscripteur, fncSouscripteur));

        //Validateur NelsonRE
        AppUser userValidateur = userRepo.save(new AppUser(null, "Validateur", "Validateur", null, 4l,
                pe.encode("1234"), "validateur@gmail.com", "1234567",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        AppRole roleValidateur = roleRepo.findByRoleCode("ROL-VAL-FAC");
        AppFunction fncValidateur = fncRepo.save(new AppFunction(null, null, 4l, "Validateur Nelson RE", userValidateur, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(userValidateur);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleValidateur, fncValidateur));

        //Comptable NelsonRE
        AppUser userComptable = userRepo.save(new AppUser(null, "Comptable", "Comptable", null, 4l,
                pe.encode("1234"), "comptable@gmail.com", "12345588",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        AppRole roleComptable = roleRepo.findByRoleCode("ROL-COMPTA-FAC");
        AppFunction fncComptable = fncRepo.save(new AppFunction(null, null, 4l, "Comptable Nelson RE", userComptable, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userRepo.save(userComptable);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleComptable, fncComptable));


    }
}
