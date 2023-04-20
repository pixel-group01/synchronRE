package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

    //@Bean
    public CommandLineRunner start(UserRepo userRepo, PasswordEncoder pe, StatutRepository staRepo, PaysRepository paysRepo,
                                   BrancheRepository braRepo, CouvertureRepository couRepo, CedRepo cedRepo, TypeRepo typeRepo,
                                   FacultativeRepository facRepo, CessionnaireRepository cesRepo, AffaireRepository affRepo, PrvRepo prvRepo)
    {
        return args->{
           AppUser user = new AppUser(1l, "admin", "admin", null, 4l,
                    pe.encode("1234"), "admin@gmail.com", "1234",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userci = new AppUser(2l, "userci", "userci", 1l, 4l,
                    pe.encode("userci"), "userci@gmail.com", "userci-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser userbn = new AppUser(3l, "userbn", "userbn", 2l, 4l,
                    pe.encode("userbn"), "userbn@gmail.com", "userbn-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());

            AppUser usertg = new AppUser(4l, "usertg", "usertg", 3l, 4l,
                    pe.encode("usertg"), "usertg@gmail.com", "usertg-tel",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());
            userRepo.saveAll(Arrays.asList(user, userci, userbn, usertg));

            Type t1 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t2 = new Type(null, TypeGroup.TYPE_REP, "REP_CED", "Répartition de type part cédante", PersStatus.ACTIVE, null);
            Type t3 = new Type(null, TypeGroup.TYPE_REP, "REP_PLA", "Répartition de type placement", PersStatus.ACTIVE, null);

            Type t4 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t5 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t7 = new Type(null, TypeGroup.TYPE_PRV, "PRV-AFF", "Privilège du module affaire", PersStatus.ACTIVE, null);
            Type t8 = new Type(null, TypeGroup.TYPE_PRV, "PRV-ADM", "Privilège du module admin", PersStatus.ACTIVE, null);
            //Type t8 = new Type(null, TypeGroup.TYPE_PRV, "PRV-ADM", "Privilège du module admin", PersStatus.ACTIVE, null);
            //Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);

            Type fil= new Type(null, TypeGroup.TYPE_CED, "FIL", "Filiale", PersStatus.ACTIVE, null);
            Type rea = new Type(null, TypeGroup.TYPE_CED, "REA", "Réassureur", PersStatus.ACTIVE, null);

            typeRepo.saveAll(Arrays.asList(t1,t2,t3,t7,t8,fil,rea));

            Statut s1 = new Statut("SAI", "Saisie", "Affaire saisie", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s2 = new Statut("TRA", "Transmis", "Affaire transmise", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s3 = new Statut("RET", "Retournée", "Affaire retournée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s4 = new Statut("VAL", "Validée", "Affaire validée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s5 = new Statut("ARC", "Archivée", "Affaire archivée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s6 = new Statut("SUP", "Supprimée", "Affaire supprimée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s9 = new Statut("APLA", "Attente de placement", "Affaire en attente de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut S10 = new Statut("CPLA", "En cour de placement", "Affaire en cour de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut S11 = new Statut("CREP", "En cour de repartition", "Affaire en cour de repartition", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());


            Statut s7 = new Statut("ACT", "Actif", "Actif", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());
            Statut s8 = new Statut("SUPP", "Supprimée", "Supprimé", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());

            staRepo.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8,s9,S10,S11));

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
            paysRepo.saveAll(Arrays.asList(ci, bn, tg, sen));

         Cessionnaire ces1 = new Cessionnaire(1l, "AVENI-RE", "ARE", "are@gmail.com", "123546", "123456879", "are", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces3 = new Cessionnaire(3l, "NCA-RE", "NCARE", "ncare@gmail.com", "ncare-tel", "ncare-cel", "ncare", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces2 = new Cessionnaire(2l, "GRAND-RE", "GRE", "gre@gmail.com", "gre-tel", "gre-cel", "gre", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire nre = new Cessionnaire(4l, "NELSON-RE", "NRE", "nre@gmail.com", "nre-tel", "nre-cel", "nre", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces5 = new Cessionnaire(5l, "SCA INTER A RE SOLUTION RE", "SCARE", "sca@gmail.com", "12354685", "123456825", "are", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces6 = new Cessionnaire(6l, "CONTINENTAL-RE", "CRE", "cnre@gmail.com", "cnrare-tel", "cnare-cel", "ncnre", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces7 = new Cessionnaire(7l, "SCG-RE", "SCG-RE", "sgre@gmail.com", "sgre-tel", "sgre-cel", "sgre", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);
         Cessionnaire ces8 = new Cessionnaire(8l, "WAICA-RE", "WRE", "wre@gmail.com", "wre-tel", "wre-cel", "wre", "ABJ", LocalDateTime.now(), LocalDateTime.now(), s7);

         cesRepo.saveAll(Arrays.asList(ces1, ces2, ces3, nre,ces5, ces6, ces7, ces8));

            //Cedante nre = new Cessionnaire(1l, "Nelson RE", "NRE", ,"05 05 05 05 01", "nre@gmail.com", "NRE", "NRE", "ABJ", null, ci,new AppUser(1l),null, rea, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            //nre = cedRepo.save(nre); nre.setCedParentId(nre.getCedId());nre = cedRepo.save(nre);

            Cedante nsiaci = new Cedante(2l, "NSIA CI", "NSIA CI", "05 05 05 05 01", "nsiaci@gmail.com", "NSIA CI", "NSIA FAX", "CI", nre, ci,new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante nsiabn = new Cedante(3l, "NSIA BN", "NSIA BN", "05 05 05 05 02", "nsiabn@gmail.com", "NSIA BN", "NSIA FAX", "BN", nre, bn, new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante nsiatg = new Cedante(4l, "NSIA TG", "NSIA TG", "05 05 05 05 03", "nsiaci@gmail.com", "NSIA TG", "NSIA FAX", "TG", nre, tg, new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            nsiaci = cedRepo.save(nsiaci);
            nsiabn = cedRepo.save(nsiabn);
            nsiatg = cedRepo.save(nsiatg);


//            Facultative fac =new Facultative(1L,"FAC-00231","","",LocalDate.of(2023, 04, 01),LocalDate.of(2023, 04, 01),new BigDecimal(1000000000),"");
//            facRepo.save(fac);

         Affaire affnsiaci = new Affaire(new BigDecimal(30000000),"AFF-001", "SNDI", "Affaire NSIA-CI (DEV)", LocalDate.now(), LocalDate.of(2024, 12, 05));
            affnsiaci.setStatut(new Statut("SAI"));
            affnsiaci.setCedante(nsiaci);
         Facultative facnsiaci = new Facultative(affnsiaci, "AFF-001", new BigDecimal(30000000), new BigDecimal(30000000));


         facRepo.save(facnsiaci);

            Affaire affnsiabn = new Affaire(new BigDecimal(20000000),"AFF-002", "DGMP", "Affaire NSIA-BN (Marchés Publics)", LocalDate.now(), LocalDate.of(2025, 10, 05));
            affnsiabn.setStatut(new Statut("SAI"));
            affnsiabn.setCedante(nsiabn);
            Facultative facaffnsiabn = new Facultative(affnsiabn, "AFF-002", new BigDecimal(20000000), new BigDecimal(20000000));
            facRepo.save(facaffnsiabn);

            Affaire affnsiatg = new Affaire(new BigDecimal(40000000),"AFF-003", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2028, 11, 02));
            affnsiatg.setStatut(new Statut("SAI"));
            affnsiatg.setCedante(nsiatg);
            Facultative facaffnsiatg = new Facultative(affnsiatg, "AFF-003", new BigDecimal(40000000), new BigDecimal(40000000));
            facRepo.save(facaffnsiatg);

            Affaire aff1 = new Affaire(new BigDecimal(50000000),"AFF-004", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2023, 10, 05));
            aff1.setStatut(new Statut("SAI"));
            Facultative fac = new Facultative(aff1, "AFF-004", new BigDecimal(50000000), new BigDecimal(50000000));
            facRepo.save(fac);


            AppPrivilege prv1 = new AppPrivilege(1l, "SAI-AFF", "Saisir une affaire", t7);
            AppPrivilege prv2 = new AppPrivilege(2l, "VAL-AFF", "Valider une affaire", t7);
            prvRepo.saveAll(Arrays.asList(prv1, prv2));
        };
    }
}
