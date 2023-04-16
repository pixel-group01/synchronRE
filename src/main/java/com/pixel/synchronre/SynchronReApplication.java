package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
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

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SynchronReApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(UserRepo userRepo, PasswordEncoder pe, StatutRepository staRepo, PaysRepository paysRepo,
                                   BrancheRepository braRepo, CouvertureRepository couRepo, CedRepo cedRepo, TypeRepo typeRepo,
                                   FacultativeRepository facRepo)
    {
        return args->{
           AppUser user = new AppUser(1l, "admin", "admin", null,
                    pe.encode("1234"), "admin@gmail.com", "1234",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());
            userRepo.save(user);

            Type t1 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t2 = new Type(null, TypeGroup.TYPE_REP, "REP_CED", "Répartition de type part cédante", PersStatus.ACTIVE, null);
            Type t3 = new Type(null, TypeGroup.TYPE_REP, "REP_PLA", "Répartition de type placement", PersStatus.ACTIVE, null);

            Type t4 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t5 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);
            Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null);

            Type fil= new Type(null, TypeGroup.TYPE_CED, "FIL", "Filiale", PersStatus.ACTIVE, null);
            Type rea = new Type(null, TypeGroup.TYPE_CED, "REA", "Réassureur", PersStatus.ACTIVE, null);

            typeRepo.saveAll(Arrays.asList(t1,t2,t3,fil,rea));

            Statut s1 = new Statut("SAI", "Saisie", "Affaire saisie", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s2 = new Statut("TRA", "Transmis", "Affaire transmise", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s3 = new Statut("RET", "Retournée", "Affaire retournée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s4 = new Statut("VAL", "Validée", "Affaire validée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s5 = new Statut("ARC", "Archivée", "Affaire archivée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
            Statut s6 = new Statut("SUP", "Supprimée", "Affaire supprimée", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());


            Statut s7 = new Statut("ACT", "Actif", "Actif", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());
            Statut s8 = new Statut("SUPP", "Supprimée", "Supprimé", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());

            staRepo.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8));

            Branche b1 = new Branche(1L, "VIE", "VIE", LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Branche b2 = new Branche(2L, "NVI", "Non Vie", LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            braRepo.saveAll(Arrays.asList(b1, b2));

            Couverture c1 = new Couverture(1L, "Multirisques professionnelle", "Multirisques professionnelle", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Couverture c2 = new Couverture(2L, "Globale Dommage", "Globale Dommage", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Couverture c3 = new Couverture(3L, "Décès groupe", "Décès groupe", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            couRepo.saveAll(Arrays.asList(c1, c2, c3));

            Pays ci = new Pays(1l, "CIV", "Côte d'Ivoire", "+225", new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays bn = new Pays(2l, "BNN", "Benin", "+226", new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            Pays tg = new Pays(3l, "TGO", "Togo", "+226", new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
            paysRepo.saveAll(Arrays.asList(ci, bn, tg));

            Cedante nre = new Cedante(1l, "Nelson RE", "NRE", "05 05 05 05 01", "nre@gmail.com", "NRE", "NRE", "ABJ", null, ci,new AppUser(1l),null, rea, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            nre = cedRepo.save(nre); nre.setCedParentId(nre.getCedId());nre = cedRepo.save(nre);

            Cedante ced1 = new Cedante(2l, "NSIA CI", "NSIA CI", "05 05 05 05 01", "nsiaci@gmail.com", "NSIA CI", "NSIA FAX", "CI", nre.getCedId(), ci,new AppUser(1l), null, fil, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante ced2 = new Cedante(3l, "NSIA BN", "NSIA BN", "05 05 05 05 02", "nsiabn@gmail.com", "NSIA BN", "NSIA FAX", "BN", nre.getCedId(), bn, new AppUser(1l), null, fil, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            Cedante ced3 = new Cedante(4l, "NSIA TG", "NSIA TG", "05 05 05 05 03", "nsiaci@gmail.com", "NSIA TG", "NSIA FAX", "TG", nre.getCedId(), tg, new AppUser(1l), null, fil, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
            cedRepo.saveAll(Arrays.asList(ced1, ced2, ced3));


//            Facultative fac =new Facultative(1L,"FAC-00231","","",LocalDate.of(2023, 04, 01),LocalDate.of(2023, 04, 01),new BigDecimal(1000000000),"");
//            facRepo.save(fac);


//            CreateFacultativeReq fac1 = new CreateFacultativeReq("DGMP", "Pasation de marchés", LocalDate.of(2023, 04, 01), LocalDate.of(24,04,01), "A00123", new BigDecimal(1000000000), new BigDecimal(1000000000), new BigDecimal(1000000000), 1L, 1L);
//            facService.createFacultative(fac1);

        };
    }
}
