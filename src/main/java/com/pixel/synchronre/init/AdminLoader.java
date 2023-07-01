package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.authmodule.model.entities.PrvToFunctionAss;
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
    private final TypeRepo typeRepo;
    private final PasswordEncoder pe;
    private final PrvRepo prvRepo;
    private final FunctionRepo fncRepo;
    private final PrvToFunctionAssRepo ptfRepo;

    @Override
    public void load()
    {
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
                LocalDateTime.now());

        userRepo.saveAll(Arrays.asList(useradmin, userci, userbn, usertg, userSaisienre, userValidateur, userObservateur, userComptable, userSouscripteur));

        AppFunction fncUserci = new AppFunction(1l, 1l, 4l, "Acteur de saisie NSIA-CI", userci, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncUserbn = new AppFunction(2l, 2l, 4l, "Acteur de saisie NSIA-BN", userbn, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncUsertg = new AppFunction(3l, 3l, 4l, "Acteur de saisie NSIA-TG", usertg, 1, LocalDate.now(), LocalDate.now().plusYears(1));

        AppFunction functionAdmin = new AppFunction(4l, null, 4l, "Administrateur SyncrhoneRe", useradmin, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncAgentSaisie = new AppFunction(5l, null, 4l, "Acteur de saisie NelsonRE", userSaisienre, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncObservateur = new AppFunction(6l, null, 4l, "Observateur SynchronRE", userObservateur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncValidateur = new AppFunction(7l, null, 4l, "Acteur de Validation", userValidateur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncComptable = new AppFunction(8l, null, 4l, "Comptable NelsonRE", userComptable, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        AppFunction fncSouscripteur = new AppFunction(9l, null, 5l, "Souscripteur NelsonRE", userSouscripteur, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        fncRepo.saveAll(Arrays.asList(fncUserci, fncUserbn, fncUsertg,functionAdmin, fncAgentSaisie, fncObservateur, fncValidateur, fncComptable, fncSouscripteur));

        AppPrivilege agentSaisie = prvRepo.save(new AppPrivilege(null, "SAISIE", "AGENT_DE_SAISIE", new Type(4l)));
        AppPrivilege validateur = prvRepo.save(new AppPrivilege(null, "VALIDATEUR", "VALIDATEUR", new Type(4l)));
        AppPrivilege observateur = prvRepo.save(new AppPrivilege(null, "OBSERVATEUR", "OBSERVATEUR", new Type(4l)));
        AppPrivilege roleAdmin = prvRepo.save(new AppPrivilege(null, "ADMIN", "ADMINISTRATEUR", new Type(4l)));
        AppPrivilege comptable = prvRepo.save(new AppPrivilege(null, "COMPTABLE", "COMPTABLE", new Type(4l)));
        AppPrivilege souscripteur = prvRepo.save(new AppPrivilege(null, "SOUSCRIPTEUR", "SOUSCRIPTEUR", new Type(4l)));

        PrvToFunctionAss saisiAssci = new PrvToFunctionAss(agentSaisie, fncUserci); saisiAssci.setAssStatus(1); saisiAssci.setStartsAt(LocalDate.now()); saisiAssci.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss saisiAssbn = new PrvToFunctionAss(agentSaisie, fncUserbn); saisiAssbn.setAssStatus(1); saisiAssbn.setStartsAt(LocalDate.now()); saisiAssbn.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss saisiAsstg = new PrvToFunctionAss(agentSaisie, fncUsertg); saisiAsstg.setAssStatus(1); saisiAsstg.setStartsAt(LocalDate.now()); saisiAsstg.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss adminAss = new PrvToFunctionAss(roleAdmin, functionAdmin); adminAss.setAssStatus(1); adminAss.setStartsAt(LocalDate.now()); adminAss.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss saisiAss = new PrvToFunctionAss(agentSaisie, fncAgentSaisie); saisiAss.setAssStatus(1); saisiAss.setStartsAt(LocalDate.now()); saisiAss.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss obsAss = new PrvToFunctionAss(observateur, fncObservateur); obsAss.setAssStatus(1); obsAss.setStartsAt(LocalDate.now()); obsAss.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss validAss = new PrvToFunctionAss(validateur, fncValidateur); validAss.setAssStatus(1); validAss.setStartsAt(LocalDate.now()); validAss.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss comptableAss = new PrvToFunctionAss(comptable, fncComptable); comptableAss.setAssStatus(1); comptableAss.setStartsAt(LocalDate.now()); comptableAss.setEndsAt(LocalDate.now().plusYears(1));
        PrvToFunctionAss sousCripteurAss = new PrvToFunctionAss(souscripteur, fncSouscripteur); sousCripteurAss.setAssStatus(1); sousCripteurAss.setStartsAt(LocalDate.now()); sousCripteurAss.setEndsAt(LocalDate.now().plusYears(1));

        ptfRepo.saveAll(Arrays.asList(saisiAssci, saisiAssbn, saisiAsstg, adminAss, saisiAss, obsAss, validAss, comptableAss, sousCripteurAss));

        //AppFunction function = new AppFunction(1l, null, 4l, "Administrateur SyncrhoneRe", admin, 1, LocalDate.now(), LocalDate.now().plusYears(1));
        //AppFunction function = new AppFunction(1l, null, 4l, "Administrateur SyncrhoneRe", admin, 1, LocalDate.now(), LocalDate.now().plusYears(1));


        userci.setCurrentFunctionId(1l); userbn.setCurrentFunctionId(2l); usertg.setCurrentFunctionId(3l);
        useradmin.setCurrentFunctionId(4l); userSaisienre.setCurrentFunctionId(5l); userObservateur.setCurrentFunctionId(6l);
        userValidateur.setCurrentFunctionId(7l); userComptable.setCurrentFunctionId(8l); userSouscripteur.setCurrentFunctionId(9l);

        userRepo.saveAll(Arrays.asList(userci, userbn, usertg, useradmin, userSaisienre, userObservateur, userValidateur, userComptable, userSouscripteur));


    }
}
