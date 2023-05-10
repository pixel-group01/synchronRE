package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.model.entities.*;
import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SynchronReApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }

   //@Bean @Order(1)
    public CommandLineRunner start(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder pe, StatutRepository staRepo, PaysRepository paysRepo,
                                   BrancheRepository braRepo, CouvertureRepository couRepo, CedRepo cedRepo, TypeRepo typeRepo, TypeParamRepo typeParamRepo,
                                   FacultativeRepository facRepo, CessionnaireRepository cesRepo, AffaireRepository affRepo,DeviseRepository devRepo,
                                   PrvRepo prvRepo, FunctionRepo fncRepo, ParamCessionLegaleRepository pcslRepo, PrvToFunctionAssRepo ptfRepo,
                                   ExerciceRepository exeRepo
                                  )
    {
        return args->{

            Type t1 = new Type(1l, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
            Type t2 = new Type(2l, TypeGroup.TYPE_REP, "REP_CED", "Répartition de type part cédante", PersStatus.ACTIVE, null, null);
            Type t3 = new Type(3l, TypeGroup.TYPE_REP, "REP_PLA", "Répartition de type placement", PersStatus.ACTIVE, null, null);

            //Type t4 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
            //Type t5 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
            //Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
            Type t7 = new Type(4l, TypeGroup.TYPE_PRV, "PRV-AFF", "Privilège du module affaire", PersStatus.ACTIVE, null, null);
            Type t8 = new Type(5l, TypeGroup.TYPE_PRV, "PRV-ADM", "Privilège du module admin", PersStatus.ACTIVE, null, null);

            Type paiement = new Type(6l, TypeGroup.TYPE_REGLEMENT, "paiements", "Paiement reçu", PersStatus.ACTIVE, null, null);
            Type reversement = new Type(7l, TypeGroup.TYPE_REGLEMENT, "reversements", "Reversement", PersStatus.ACTIVE, null, null);
            Type reglement_sinistre = new Type(8l, TypeGroup.TYPE_REGLEMENT, "REG-SIN", "Reglement Sinistre", PersStatus.ACTIVE, null, null);


            Type fil= new Type(9l, TypeGroup.TYPE_CED, "FIL", "Filiale", PersStatus.ACTIVE, null, null);
            Type rea = new Type(10l, TypeGroup.TYPE_CED, "REA", "Réassureur", PersStatus.ACTIVE, null, null);
            Type facType= new Type(11l, TypeGroup.TYPE_AFFAIRE, "FAC", "Facultative", PersStatus.ACTIVE, null, null);
            Type trai = new Type(12l, TypeGroup.TYPE_AFFAIRE, "TRAITE", "Traite", PersStatus.ACTIVE, null, null);

            Type photo = new Type(13l, TypeGroup.DOCUMENT, "PHT", "Photo", PersStatus.ACTIVE, null, "user");

            Type docReglement = new Type(14l, TypeGroup.DOCUMENT, "DOC_REG", "Document de règlement", PersStatus.ACTIVE, null, "reglement");
            Type recuReglement = new Type(15l, TypeGroup.DOCUMENT, "RECU_REG", "Recu de règlement", PersStatus.ACTIVE, null, "reglement");
            Type chequeRegelemnt = new Type(16l, TypeGroup.DOCUMENT, "CHEQ", "Chèque de règlement", PersStatus.ACTIVE, null, "reglement");
            Type bordereauVirement = new Type(17l, TypeGroup.DOCUMENT, "BORD_VIR", "Bordereau de virement", PersStatus.ACTIVE, null, "reglement");
            Type ordreVirement = new Type(18l, TypeGroup.DOCUMENT, "ORDE_VIR", "Ordre de virement", PersStatus.ACTIVE, null, "reglement");

            Type avisModCession = new Type(25l, TypeGroup.DOCUMENT, "AVI_MOD_CES", "Avis de modification de cession", PersStatus.ACTIVE, null, "placement");
            Type noteCession = new Type(26l, TypeGroup.DOCUMENT, "NOT_CES", "Note de cession", PersStatus.ACTIVE, null, "placement");
            Type Virement = new Type(27l, TypeGroup.MODE_REGLEMENT, "VRG", "Virement bancaire", PersStatus.ACTIVE, null, null);
            Type Chèque = new Type(28l, TypeGroup.MODE_REGLEMENT, "CHE", "Chèque", PersStatus.ACTIVE, null, null);

            typeRepo.saveAll(Arrays.asList(t1,t2,t3,t7,t8,fil,rea, paiement, reversement, facType, trai, photo,
                    avisModCession, noteCession,Virement,Chèque, docReglement, recuReglement, chequeRegelemnt, bordereauVirement));
            ordreVirement = typeRepo.save(ordreVirement);
            typeParamRepo.save(new TypeParam(null, docReglement, recuReglement, PersStatus.ACTIVE));
            typeParamRepo.save(new TypeParam(null, docReglement, chequeRegelemnt, PersStatus.ACTIVE));
            typeParamRepo.save(new TypeParam(null, docReglement, bordereauVirement, PersStatus.ACTIVE));
            typeParamRepo.save(new TypeParam(null, docReglement, ordreVirement, PersStatus.ACTIVE));

            AppPrivilege agentSaisie = new AppPrivilege(1l, "SAISIE", "AGENT_DE_SAISIE", new Type(4l));
            AppPrivilege validateur = new AppPrivilege(2l, "VALIDATEUR", "VALIDATEUR", new Type(4l));
            AppPrivilege observateur = new AppPrivilege(3l, "OBSERVATEUR", "OBSERVATEUR", new Type(4l));
            AppPrivilege roleAdmin = new AppPrivilege(4l, "ADMIN", "ADMINISTRATEUR", new Type(4l));
            AppPrivilege comptable = new AppPrivilege(5l, "COMPTABLE", "COMPTABLE", new Type(4l));
            AppPrivilege souscripteur = new AppPrivilege(6l, "SOUSCRIPTEUR", "SOUSCRIPTEUR", new Type(4l));

            prvRepo.saveAll(Arrays.asList(agentSaisie, validateur, observateur, roleAdmin, comptable, souscripteur));

            AppUser useradmin = new AppUser(1l, "admin", "admin", null, 4l,
                    pe.encode("1234"), "admin@gmail.com", "1234",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userci = new AppUser(2l, "userci", "userci", 1l, 4l,
                    pe.encode("1234"), "userci@gmail.com", "userci-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userbn = new AppUser(3l, "userbn", "userbn", 2l, 4l,
                    pe.encode("1234"), "userbn@gmail.com", "userbn-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser usertg = new AppUser(4l, "usertg", "usertg", 3l, 4l,
                    pe.encode("1234"), "usertg@gmail.com", "usertg-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userSaisienre = new AppUser(5l, "Koffi", "Alain", null, 4l,
                    pe.encode("1234"), "agentsaisienre@gmail.com", "123456789",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userValidateur = new AppUser(6l, "Seka", "Jean-Georesse", null, 4l,
                    pe.encode("1234"), "agentvalidateur@gmail.com", "123456780",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userObservateur = new AppUser(7l, "Ouattara", "Ali", null, 4l,
                    pe.encode("1234"), "agentobservateur@gmail.com", "123456781",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userComptable = new AppUser(8l, "Asseke", "Elisé", null, 4l,
                    pe.encode("1234"), "comptable@gmail.com", "123456782",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userSouscripteur = new AppUser(9l, "Gossé", "Gossé Maxim", null, 4l,
                    pe.encode("1234"), "souscripteur@gmail.com", "123456783",
                    true, true, null, LocalDateTime.now(),
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

            Statut s1 = new Statut("SAI", "Saisie", "Affaire saisie", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s2 = new Statut("TRA", "Transmis", "Affaire transmise", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s3 = new Statut("RET", "Retournée", "Affaire retournée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s4 = new Statut("VAL", "Validée", "Affaire validée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s5 = new Statut("ARC", "Archivée", "Affaire archivée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s6 = new Statut("SUP", "Supprimée", "Affaire supprimée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());

            Statut s7 = new Statut("ACT", "Actif", "Actif", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());
            Statut s8 = new Statut("SUPP", "Supprimée", "Supprimé", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());


            Statut s9 = new Statut("APLA", "Attente de placement", "Affaire en attente de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut S10 = new Statut("CPLA", "En cours de placement", "Affaire en cours de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut S11 = new Statut("CREP", "En cours de repartition", "Affaire en cours de repartition", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());

            Statut s12 = new Statut("AVAL", "En attente de validation", "Placement en attente de validation", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut S13 = new Statut("VAL", "Validé", "Placement validé", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut S14 = new Statut("ACONF", "En attente de confirmation", "Placement en attente de confirmation", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut s15 = new Statut("REFUSE", "Refusé", "Placement refusé", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut S16 = new Statut("ANNULE", "Annulé", "Pladement annulé", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut S17 = new Statut("MOD", "Modifié", "Placement modifié", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut staMail = new Statut("MAIL", "Mail", "Mail envoyé", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut s18 = new Statut("ACCEPTE", "Accepté", "Placement accepté", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());


            Statut s19 = new Statut("SAI-CRT", "Saisie courtier", "Affaire saisie par le courtier", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s20 = new Statut("APAI", "En attente de paiement", "Placement en attente de paiement", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut s21 = new Statut("CPAI", "En cours de paiement", "Placement en cours de paiement", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
            Statut s22 = new Statut("SOLD", "Règlement soldé", "Règlement soldé", TypeStatut.REGLEMENT, LocalDateTime.now(), LocalDateTime.now());
            staRepo.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8,s9,S10,S11, s12,S13,S14, s15,S16,S17, s18, s19, s20, s21, s22, staMail ));

            Devise dev0 = new Devise ("CFA",  "Franc CFA", "F CFA", "XOF", s7);
            Devise dev1 = new Devise ("AED",  "Dirham EMIRAT ", "Dirham EMIRAT ", null,  s7);
            Devise dev2 = new Devise ("AOA",  "Angolan kwanza", "Angolan kwanza", null,  s7);
            Devise dev3 = new Devise ("BDT",  "Taka Bangladesh", "Taka Bangladesh", null,  s7);
            Devise dev4 = new Devise ("BHD",  "Dinar Bahrein", "Dinar Bahrein", null,  s7);
            Devise dev5 = new Devise ("BIF",  "Franc Burundais", "Franc Burundais", null,  s7);
            Devise dev6 = new Devise ("BTN",  "Bhoutan-Ngultru", "Bhoutan-Ngultru", null,  s7);
            Devise dev7 = new Devise ("BWP",  "Pula Botswana", "Pula Botswana", null, s7);
            Devise dev8 = new Devise ("CHF",  "Francs suisse", "Francs suisse", null,  s7);
            Devise dev9 = new Devise ("CVE",  "Escudo", "Escudo", null,  s7);
            Devise dev10 = new Devise ("DJF",  "Franc Djibouti", "Franc Djibouti", null,  s7);
            Devise dev11 = new Devise ("DZD",  "Dinar Algérien", "Dinar Algérien", null,  s7);
            Devise dev12 = new Devise ("EGP",  "Livre Egypte", "Livre Egypte", null,  s7);
            Devise dev13 = new Devise ("ETB",  "Birr Ethiopie", "Birr Ethiopie", null,  s7);
            Devise dev14 = new Devise ("EUR",  "Euro", "Euro", null, s7);
            Devise dev15 = new Devise ("GBP",  "Livre sterling", "Livre sterling", null, s7);
            Devise dev16 = new Devise ("GHS",  "Cedis Ghanéen n", "Cedis Ghanéen n", null,  s7);
            Devise dev17 = new Devise ("GMD",  "Dalasi Gambie", "Dalasi Gambie", null, s7);
            Devise dev18 = new Devise ("GNF",  "Franc Guinée", "Franc Guinée", null,  s7);
            Devise dev19 = new Devise ("IDR",  "Rupiah Indonés.", "Rupiah Indonés.", null,  s7);
            Devise dev20 = new Devise ("INR",  "Roupie indienne", "Roupie indienne", null,  s7);
            Devise dev21 = new Devise ("IQD",  "Dinar Irakien", "Dinar Irakien", null,  s7);
            Devise dev22 = new Devise ("IRR",  "Rial Iranien", "Rial Iranien", null,  s7);
            Devise dev23 = new Devise ("JOD",  "Dinar Jordanien", "Dinar Jordanien", null,  s7);
            Devise dev24 = new Devise ("JPY",  "YEN", "YEN", null,  s7);
            Devise dev25 = new Devise ("KES",  "Shilling Kenya", "Shilling Kenya", null,  s7);
            Devise dev26 = new Devise ("KPW",  "Won -Corée Nord", "Won -Corée Nord", null,  s7);
            Devise dev27 = new Devise ("KRW",  "Corée du Sud ", "Corée du Sud ", null,  s7);
            Devise dev28 = new Devise ("KWD",  "Dinar Koweïtien", "Dinar Koweïtien", null, s7);
            Devise dev29 = new Devise ("LBP",  "Livre Libanaise", "Livre Libanaise", null,  s7);
            Devise dev30 = new Devise ("LRD",  "Dollar libérien", "Dollar libérien", null,  s7);
            Devise dev31 = new Devise ("LSL",  "Lesotho-Loti", "Lesotho-Loti", null, s7);
            Devise dev32 = new Devise ("LYD",  "Dinar Lybien", "Dinar Lybien", null,  s7);
            Devise dev33 = new Devise ("MAD",  "Dirham Marocain", "Dirham Marocain", null,  s7);
            Devise dev34 = new Devise ("MGA",  "Ariayry Madaga.", "Ariayry Madaga.", null,  s7);
            Devise dev35 = new Devise ("MGF",  "Franc magache", "Franc magache", null,  s7);
            Devise dev36 = new Devise ("MRO",  "Ougiya Maurita.", "Ougiya Maurita.", null,  s7);
            Devise dev37 = new Devise ("MRU",  "MRU", "MRU", null,  s7);
            Devise dev38 = new Devise ("MUR",  "Roupie Maurice", "Roupie Maurice", null,  s7);
            Devise dev39 = new Devise ("MVR",  "Roupie maldive", "Roupie maldive", null, s7);
            Devise dev40 = new Devise ("MWK",  "Kwacha Malawi", "Kwacha Malawi", null,  s7);
            Devise dev41 = new Devise ("MYR",  "Ringgit Malais.", "Ringgit Malais.", null,  s7);
            Devise dev42 = new Devise ("MZM",  "Franc Mozambiq.", "Franc Mozambiq.", null,  s7);
            Devise dev43 = new Devise ("MZN",  "Metical Mozamb.", "Metical Mozamb.", null,  s7);
            Devise dev44 = new Devise ("NAD",  null, "0", null,  s7);
            Devise dev45 = new Devise ("NGN",  "Naira", "Naira", null,  s7);
            Devise dev46 = new Devise ("NPR",  "Népal Roupie", "Népal Roupie", null,  s7);
            Devise dev47 = new Devise ("OMR",  "Rial d''OMAN", "Rial d''OMAN", null,  s7);
            Devise dev48 = new Devise ("PGK",  "PAPOUASIE KINA", "PAPOUASIE KINA", null,  s7);
            Devise dev49 = new Devise ("PHP",  "Peso philippin", "Peso philippin", null,  s7);
            Devise dev50 = new Devise ("PKR",  "Pakistan-Roupie", "Pakistan-Roupie", null,  s7);
            Devise dev51 = new Devise ("QAR",  "Riyal du Qatar", "Riyal du Qatar", null,  s7);
            Devise dev52 = new Devise ("SZL ",  "Swaziland Lilan", "Swaziland Lilan", null,  s7);
            Devise dev53 = new Devise ("RWF",  "Franc Rwandais", "Franc Rwandais", null, s7);
            devRepo.saveAll(Arrays.asList(dev0,dev1,dev2,dev3,dev4,dev5,dev6,dev7,dev8,dev9,dev10,dev11,dev12,dev13,dev14,dev15,dev16,dev17,dev18,dev19,dev20,dev21,dev22,dev23,dev24,dev25,dev26,dev27,dev28,dev29,dev30,dev31,dev32,dev33,dev34,dev35,dev36,dev37,dev38,dev39,dev40,dev41,dev42,dev43,dev44,dev45,dev46,dev47,dev48,dev49,dev50,dev51,dev52,dev53));

            Branche b1 = new Branche(1L, "VIE", "VIE", LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Branche b2 = new Branche(2L, "NVI", "Non Vie", LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            braRepo.saveAll(Arrays.asList(b1, b2));

            Couverture c1 = new Couverture(1L, "Multirisques professionnelle", "Multirisques professionnelle", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Couverture c2 = new Couverture(2L, "Globale Dommage", "Globale Dommage", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Couverture c3 = new Couverture(3L, "Décès groupe", "Décès groupe", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            couRepo.saveAll(Arrays.asList(c1, c2, c3));

            Pays ci = new Pays("CIV", "+225","Côte d'Ivoire",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays bn = new Pays("BNN", "+226","Benin",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays tg = new Pays("TGO", "+224","Togo",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays sen = new Pays("SEN","+227", "Sénegal",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays GAB = new Pays("GAB","+227", "Gabon",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays gui = new Pays("GUI","+227", "Guinée",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays BFO = new Pays("BFO","+225", "Burkina Faso",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays MAL = new Pays("MAL","+225", "Mali",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays CMR = new Pays("CMR","+225", "Cameroun",  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            paysRepo.saveAll(Arrays.asList(ci, bn, tg, sen,GAB, gui, BFO, MAL,CMR));

            ParamCessionLegale franc1 = new ParamCessionLegale(null,"Cession légale au 1er franc SEN RE",new BigDecimal(0),new BigDecimal(6.5),new Pays("SEN"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc2 = new ParamCessionLegale(null,"Cession légale Fac SEN RE",new BigDecimal(0),new BigDecimal(10),new Pays("SEN"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc3 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("SEN"),new Statut("ACT"),3L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc4 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("SEN"),new Statut("ACT"),4L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc1, franc2,franc3,franc4));

            ParamCessionLegale franc5 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("CIV"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc6 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("CIV"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc5,franc6));

            ParamCessionLegale franc7 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("BNN"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc8 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("BNN"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc7, franc8));

            ParamCessionLegale franc9 = new ParamCessionLegale(null,"Cession légale au 1er franc SGC RE",new BigDecimal(0),new BigDecimal(15),new Pays("GAB"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc10 = new ParamCessionLegale(null,"Cession légale Fac SGC RE",new BigDecimal(0),new BigDecimal(5),new Pays("GAB"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc11 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("GAB"),new Statut("ACT"),3L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc12 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("GAB"),new Statut("ACT"),4L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc9, franc10,franc11,franc12));

            ParamCessionLegale franc13 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("CMR"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc14 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("CMR"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc13, franc14));

            ParamCessionLegale franc15 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("GUI"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc16 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("GUI"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc15, franc16));

            ParamCessionLegale franc17 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("BFO"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc18 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("BFO"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc17, franc18));

            ParamCessionLegale franc19 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("TGO"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc20 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("TGO"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc19, franc20));

            ParamCessionLegale franc21 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("MAL"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
            ParamCessionLegale franc22 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("MAL"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
            pcslRepo.saveAll(Arrays.asList(franc21, franc22));

            BigDecimal FIVE = new BigDecimal(5);
            Cessionnaire ces1 = new Cessionnaire(1l, "AVENI-RE", "ARE", "are@gmail.com", "123546", "123456879", "are", "ABJ","ATSIN Ghislain Hermann", FIVE,LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces3 = new Cessionnaire(3l, "NCA-RE", "NCARE", "ncare@gmail.com", "ncare-tel", "ncare-cel", "ncare", "ABJ","COULIBALY Lenimama Ibrahima", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces2 = new Cessionnaire(2l, "GRAND-RE", "GRE", "gre@gmail.com", "gre-tel", "gre-cel", "gre", "ABJ","ATSIN Ghislain Hermann", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire nre = new Cessionnaire(4l, "NELSON-RE", "NRE", "nre@gmail.com", "nre-tel", "nre-cel", "nre", "ABJ","KOUSSI N'Guéssan Charlemargne", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces5 = new Cessionnaire(5l, "SCA INTER A RE SOLUTION RE", "SCARE", "sca@gmail.com", "12354685", "123456825", "are", "ABJ","ADOU Venance", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces6 = new Cessionnaire(6l, "CONTINENTAL-RE", "CRE", "cnre@gmail.com", "cnrare-tel", "cnare-cel", "ncnre", "ABJ","YOUIN Salif", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces7 = new Cessionnaire(7l, "SCG-RE", "SCG-RE", "sgre@gmail.com", "sgre-tel", "sgre-cel", "sgre", "ABJ","KONAN Laurent", FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);
            Cessionnaire ces8 = new Cessionnaire(8l, "WAICA-RE", "WRE", "wre@gmail.com", "wre-tel", "wre-cel", "wre", "ABJ", "ESSOH Fernand",FIVE, LocalDateTime.now(), LocalDateTime.now(), s7);

         cesRepo.saveAll(Arrays.asList(ces1, ces2, ces3, nre,ces5, ces6, ces7, ces8));


            //Cedante nre = new Cessionnaire(1l, "Nelson RE", "NRE", ,"05 05 05 05 01", "nre@gmail.com", "NRE", "NRE", "ABJ", null, ci,new AppUser(1l),null, rea, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            //nre = cedRepo.save(nre); nre.setCedParentId(nre.getCedId());nre = cedRepo.save(nre);

            Cedante nsiaci = new Cedante(1l, "NSIA CI", "NSIA-CI", "05 05 05 05 01", "nsiaci@gmail.com", "NSIA CI", "NSIA FAX", "CI","YOUIN Salif" ,nre, ci,new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante nsiabn = new Cedante(2l, "NSIA BN", "NSIA-BN", "05 05 05 05 02", "nsiabn@gmail.com", "NSIA BN", "NSIA FAX", "BN","Coulibaly Lenimama", nre, bn, new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante nsiatg = new Cedante(3l, "NSIA TG", "NSIA-TG", "05 05 05 05 03", "nsiaci@gmail.com", "NSIA TG", "NSIA FAX", "TG","Atsin Ghislain Herman", nre, tg, new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            nsiaci = cedRepo.save(nsiaci);
            nsiabn = cedRepo.save(nsiabn);
            nsiatg = cedRepo.save(nsiatg);


//            Facultative fac =new Facultative(1L,"FAC-00231","","",LocalDate.of(2023, 04, 01),LocalDate.of(2023, 04, 01),new BigDecimal(1000000000),"");
//            facRepo.save(fac);

            //Exercice
            Exercice exe0 = new Exercice(2020L,"Gestion 2020",false,s7,LocalDateTime.now(), LocalDateTime.now());
            Exercice exe1 = new Exercice(2021L,"Gestion 2021",false,s7,LocalDateTime.now(), LocalDateTime.now());
            Exercice exe2 = new Exercice(2022L,"Gestion 2022",false,s7,LocalDateTime.now(), LocalDateTime.now());
            Exercice exe3 = new Exercice(2023L,"Gestion 2023",true,s7,LocalDateTime.now(), LocalDateTime.now());
            exeRepo.saveAll(Arrays.asList(exe0,exe1,exe2,exe3));


         Affaire affnsiaci = new Affaire(new BigDecimal(30000000),"AFF-001", "SNDI", "Affaire NSIA-CI (DEV)", LocalDate.now(), LocalDate.of(2024, 12, 05),exe0);
            affnsiaci.setStatut(new Statut("SAI"));
            affnsiaci.setCedante(nsiaci);
         Facultative facnsiaci = new Facultative(affnsiaci, "AFF-001", new BigDecimal(30000000), new BigDecimal(30000000));


         facRepo.save(facnsiaci);

            Affaire affnsiabn = new Affaire(new BigDecimal(20000000),"AFF-002", "DGMP", "Affaire NSIA-BN (Marchés Publics)", LocalDate.now(), LocalDate.of(2025, 10, 05),exe2);
            affnsiabn.setStatut(new Statut("SAI"));
            affnsiabn.setCedante(nsiabn);
            Facultative facaffnsiabn = new Facultative(affnsiabn, "AFF-002", new BigDecimal(20000000), new BigDecimal(20000000));
            facRepo.save(facaffnsiabn);

            Affaire affnsiatg = new Affaire(new BigDecimal(40000000),"AFF-003", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2028, 11, 02),exe3);
            affnsiatg.setStatut(new Statut("SAI"));
            affnsiatg.setCedante(nsiatg);
            Facultative facaffnsiatg = new Facultative(affnsiatg, "AFF-003", new BigDecimal(40000000), new BigDecimal(40000000));
            facRepo.save(facaffnsiatg);

            Affaire aff1 = new Affaire(new BigDecimal(50000000),"AFF-004", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2023, 10, 05),new Exercice(2023L));
            aff1.setStatut(new Statut("SAI"));
            Facultative fac = new Facultative(aff1, "AFF-004", new BigDecimal(50000000), new BigDecimal(50000000));
            facRepo.save(fac);
        };
    }
}
