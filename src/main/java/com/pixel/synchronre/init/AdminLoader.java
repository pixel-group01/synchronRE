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
        /*
        AppUser useradmin = new AppUser(1l, "admin", "admin", null, 4l,
                pe.encode("1234"), "admin@gmail.com", "1234",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userci = new AppUser(2l, "userci", "userci", 1l, 4l,
                pe.encode("1234"), "userci@gmail.com", "userci-tel",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userbn = new AppUser(3l, "userbn", "userbn", 2l, 4l,
                pe.encode("1234"), "userbn@gmail.com", "userbn-tel",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser usertg = new AppUser(4l, "usertg", "usertg", 3l, 4l,
                pe.encode("1234"), "usertg@gmail.com", "usertg-tel",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userSaisienre = new AppUser(5l, "Koffi", "Alain", null, 4l,
                pe.encode("1234"), "agentsaisienre@gmail.com", "123456789",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userValidateur = new AppUser(6l, "Seka", "Jean-Georesse", null, 4l,
                pe.encode("1234"), "agentvalidateur@gmail.com", "123456780",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userObservateur = new AppUser(7l, "Ouattara", "Ali", null, 4l,
                pe.encode("1234"), "agentobservateur@gmail.com", "123456781",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userComptable = new AppUser(8l, "Asseke", "Elisé", null, 4l,
                pe.encode("1234"), "comptable@gmail.com", "123456782",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());

        AppUser userSouscripteur = new AppUser(9l, "Gossé", "Gossé Maxim", null, 4l,
                pe.encode("1234"), "souscripteur@gmail.com", "123456783",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now());*/


        //userRepo.saveAll(Arrays.asList(useradmin, userci, userbn, usertg, userSaisienre, userValidateur, userObservateur, userComptable, userSouscripteur, useradmin1));

        /*AppFunction fncUserci = new AppFunction(1l, 1l, 4l, "Acteur de saisie NSIA-CI", userci, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncUserbn = new AppFunction(2l, 2l, 4l, "Acteur de saisie NSIA-BN", userbn, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncUsertg = new AppFunction(3l, 3l, 4l, "Acteur de saisie NSIA-TG", usertg, 1, LocalDate.now(), LocalDate.now().plusYears(1));

        AppFunction functionAdmin = new AppFunction(4l, null, 4l, "Administrateur SyncrhoneRe", useradmin, 1, LocalDate.now(), LocalDate.now().plusYears(1));

        AppFunction fncAgentSaisie = new AppFunction(5l, null, 4l, "Acteur de saisie NelsonRE", userSaisienre, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncObservateur = new AppFunction(6l, null, 4l, "Observateur SynchronRE", userObservateur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncValidateur = new AppFunction(7l, null, 4l, "Acteur de Validation", userValidateur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncComptable = new AppFunction(8l, null, 4l, "Comptable NelsonRE", userComptable, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncSouscripteur = new AppFunction(9l, null, 5l, "Souscripteur NelsonRE", userSouscripteur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        fncRepo.saveAll(Arrays.asList(fncUserci, fncUserbn, fncUsertg,functionAdmin,fncAgentSaisie, fncObservateur, fncValidateur, fncComptable, fncSouscripteur, functionAdmin1));*/

        //AppFunction function = new AppFunction(1l, null, 4l, "Administrateur SyncrhoneRe", admin, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        //AppFunction function = new AppFunction(1l, null, 4l, "Administrateur SyncrhoneRe", admin, 1, LocalDate.now(), LocalDate.now().plusYears(1));


        AppUser userDev = userRepo.save(new AppUser(null, "Développeur", "Synchrone-Re", null, 4l,
                pe.encode("KD@fgfysh458@"), "pixelgroup09@gmail.com", "0505050505",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));

        AppFunction fncDev = fncRepo.save(new AppFunction(null, null, 4l, "Développeur synchrone-Re", userDev, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userDev.setCurrentFunctionId(fncDev.getId());
        userRepo.save(userDev);
        AppRole roleDev = roleRepo.findByRoleCode("ROL-DEV");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleDev, fncDev));

        AppUser useradmin1 = userRepo.save(new AppUser(null, "N’GUESSAN", "Yao Yavo Basile", null, 4l,
                pe.encode("KD@f8z73t@"), "Basile.nguessan@groupensia.com", "0505893546",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));
        AppFunction functionAdmin1 = fncRepo.save(new AppFunction(null, null, 4l, "Administrateur SyncrhoneRe", useradmin1, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        useradmin1.setCurrentFunctionId(functionAdmin1.getId());
        userRepo.save(useradmin1);
        AppRole roleAdmin = roleRepo.findByRoleCode("ROL-ADM");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(1), roleAdmin, functionAdmin1));
    }
}
