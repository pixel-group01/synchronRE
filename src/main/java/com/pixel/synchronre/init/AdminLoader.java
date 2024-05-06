package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.*;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final CessionnaireRepository cesRepo;
    private final TypeRepo typeRepo;
//
    /*
        Type fonctionOperateurDeSaisieCedante = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_SAI_CED", "Opérateur de saisie cédante", PersStatus.ACTIVE, null, null));
        Type fonctionSouscripteur = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_SOUS", "Souscripteur", PersStatus.ACTIVE, null, null));

        Type fonctionValidateur = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_VAL", "Validateur", PersStatus.ACTIVE, null, null));
        Type fonctionComptable = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_COMPTA", "Comptable", PersStatus.ACTIVE, null, null));

        Type fonctionAdminFonc = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_ADM_FONC", "Administrateur fonctionnel", PersStatus.ACTIVE, null, null));
        Type fonctionAdminTech = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_ADM_TECH", "Administrateur technique", PersStatus.ACTIVE, null, null));

        Type fonctionDev = typeRepo.save(new Type(null, TypeGroup.TYPE_FUNCTION, "TYF_DEV", "Développeur", PersStatus.ACTIVE, null, "DEV"));

     */
    @Override
    public void load()
    {
        Type tyfsous = typeRepo.findByUniqueCode("TYF_SOUS").orElseThrow(()->new AppException("Type fonction introuvable : TYF_SOUS"));
        Type tyfVal = typeRepo.findByUniqueCode("TYF_VAL").orElseThrow(()->new AppException("Type fonction introuvable : TYF_VAL"));
        Type tyfCompta = typeRepo.findByUniqueCode("TYF_COMPTA").orElseThrow(()->new AppException("Type fonction introuvable : TYF_COMPTA"));
        Type tyfDev = typeRepo.findByUniqueCode("TYF_DEV").orElseThrow(()->new AppException("Type fonction introuvable : TYF_DEV"));
        Type tyfSaiCed = typeRepo.findByUniqueCode("TYF_SAI_CED").orElseThrow(()->new AppException("Type fonction introuvable : TYF_SAI_CED"));

        //Nelson RE
        BigDecimal FIVE = new BigDecimal(5);
        Cessionnaire nelsonRe=cesRepo.save(new Cessionnaire(null, "NELSON-RE", "NRE", "nre@gmail.com", "nre-tel", "nre-cel", "nre", "ABJ", FIVE, typeRepo.findByUniqueCode("COURT").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
       //Developpeur
        AppUser userDev = userRepo.save(new AppUser(null, "Développeur", "Synchrone-Re", null, nelsonRe.getCesId(),
                pe.encode("1234"), "pixelgroup09@gmail.com", "0505050505",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));
        AppFunction fncDev = fncRepo.save(new AppFunction(null, null, nelsonRe.getCesId(), "Développeur synchrone-Re", userDev, tyfDev, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userDev.setCurrentFunctionId(fncDev.getId());
        userRepo.save(userDev);
        AppRole roleDev = roleRepo.findByRoleCode("ROL-DEV");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleDev, fncDev));


//        //AMINI
//        AppUser useradmin1 = userRepo.save(new AppUser(null, "N’GUESSAN", "Yao Yavo Basile", null, 4L,
//                pe.encode("KD@f8z73t@"), "Basile.nguessan@groupensia.com", "0505893546",
//                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
//                LocalDateTime.now()));
//        AppFunction functionAdmin1 = fncRepo.save(new AppFunction(null, null, 4l, "Administrateur SyncrhoneRe", useradmin1, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
//        useradmin1.setCurrentFunctionId(functionAdmin1.getId());
//        userRepo.save(useradmin1);
//        AppRole roleAdmin = roleRepo.findByRoleCode("ROL-ADM");
//        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(1), roleAdmin, functionAdmin1));


//      //user NSIA COTE D'IVOIRE
        AppUser userci = userRepo.save(new AppUser(null, "userci", "userci", 1L, nelsonRe.getCesId(),
                pe.encode("1234"), "userci@gmail.com", "userci-tel",
                true, true, null, LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now()));
//
//        //Operateur de saisie FAC CI
        AppRole roleSaisieFacNsiaCi = roleRepo.findByRoleCode("ROL-OPE-SAI");
        AppFunction fncSaiFacCI = fncRepo.save(new AppFunction(null, 1L, nelsonRe.getCesId(), "Opérateur de saisie NSIA-CI", userci,tyfSaiCed, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userci.setCurrentFunctionId(fncSaiFacCI.getId());
        userRepo.save(userci);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaCi, fncSaiFacCI));

//
//
//        //User NSIA BENIN
//        AppUser userbn = userRepo.save(new AppUser(null, "userbn", "userbn", 2l, 4l,
//                pe.encode("1234"), "userbn@gmail.com", "userbn-tel",
//                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
//                LocalDateTime.now()));
//
//        //Operateur de saisie FAC BENIN
//        AppRole roleSaisieFacNsiaBn = roleRepo.findByRoleCode("ROL-OPE-SAI");
//        AppFunction fncSaiFacBN = fncRepo.save(new AppFunction(null, 2L, 4l, "Opérateur de saisie FAC NSIA-BENIN", userbn, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
//        userRepo.save(userbn);
//        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaBn, fncSaiFacBN));
//
//        //USER NSIA TOGO
//        AppUser usertg = userRepo.save(new AppUser(null, "usertg", "usertg", 3l, 4l,
//                pe.encode("1234"), "usertg@gmail.com", "usertg-tel",
//                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
//                LocalDateTime.now()));
//        //Operateur de saisie FAC TOGO
//        AppRole roleSaisieFacNsiaTg = roleRepo.findByRoleCode("ROL-OPE-SAI");
//        AppFunction fncSaiFactg = fncRepo.save(new AppFunction(null, 3L, 4l, "Opérateur de saisie FAC NSIA-TOGO", usertg, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
//        userRepo.save(usertg);
//        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSaisieFacNsiaTg, fncSaiFactg));
//
//        //SOUSCRIPTEUR
        AppUser userSouscripteur = userRepo.save(new AppUser(null, "souscripteur", "souscripteur", null, nelsonRe.getCesId(),
                pe.encode("1234"), "souscripteur@gmail.com", "123456783",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
//        //Souscripteur Nelson RE

        AppFunction fncSouscripteur = fncRepo.save(new AppFunction(null, null, nelsonRe.getCesId(), "Souscripteur Nelson RE", userSouscripteur,tyfsous, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userSouscripteur.setCurrentFunctionId(fncSouscripteur.getId());
        userRepo.save(userSouscripteur);
        AppRole roleSouscripteur = roleRepo.findByRoleCode("ROL-SOUS");
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSouscripteur, fncSouscripteur));

        //        AppRole roleSouscripteurSaisiSin = roleRepo.findByRoleCode("ROL-OPE-SAI-SIN");
//        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleSouscripteurSaisiSin, fncSouscripteur));

//        //Validateur NelsonRE
        AppUser userValidateur = userRepo.save(new AppUser(null, "Validateur", "Validateur", null, nelsonRe.getCesId(),
                pe.encode("1234"), "validateur@gmail.com", "1234567",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        AppRole roleValidateur = roleRepo.findByRoleCode("ROL-VAL");
        AppFunction fncValidateur = fncRepo.save(new AppFunction(null, null, nelsonRe.getCesId(), "Validateur Nelson RE", userValidateur, tyfVal, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userValidateur.setCurrentFunctionId(fncValidateur.getId());
        userRepo.save(userValidateur);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleValidateur, fncValidateur));

////
////        //Comptable NelsonRE
        AppUser userComptable = userRepo.save(new AppUser(null, "Comptable", "Comptable", null, nelsonRe.getCesId(),
                pe.encode("1234"), "comptable@gmail.com", "12345588",
                true, true, null, LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now()));
        AppRole roleComptable = roleRepo.findByRoleCode("ROL-COMPTA");
        AppFunction fncComptable = fncRepo.save(new AppFunction(null, null, nelsonRe.getCesId(), "Comptable Nelson RE", userComptable,tyfCompta, 1, LocalDate.now(), LocalDate.now().plusYears(1)));
        userComptable.setCurrentFunctionId(fncComptable.getId());
        userRepo.save(userComptable);
        rtfRepo.save(new RoleToFncAss(null, 1, LocalDate.now(), LocalDate.now().plusYears(20), roleComptable, fncComptable));
    }
}