package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class CedCesAffLoader implements Loader
{
    private final CedRepo cedRepo;
    private final AffaireRepository facRepo;
    private final CessionnaireRepository cesRepo;

    @Override
    public void load()
    {
        //Cessionnaires
        BigDecimal FIVE = new BigDecimal(5);
        Cessionnaire ces1 = new Cessionnaire(1l, "AVENI-RE", "ARE", "are@gmail.com", "123546", "123456879", "are", "ABJ","ATSIN Ghislain Hermann", FIVE,LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces3 = new Cessionnaire(3l, "NCA-RE", "NCARE", "ncare@gmail.com", "ncare-tel", "ncare-cel", "ncare", "ABJ","COULIBALY Lenimama Ibrahima", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces2 = new Cessionnaire(2l, "GRAND-RE", "GRE", "gre@gmail.com", "gre-tel", "gre-cel", "gre", "ABJ","ATSIN Ghislain Hermann", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire nre = new Cessionnaire(4l, "NELSON-RE", "NRE", "nre@gmail.com", "nre-tel", "nre-cel", "nre", "ABJ","KOUSSI N'Guéssan Charlemargne", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces5 = new Cessionnaire(5l, "SCA INTER A RE SOLUTION RE", "SCARE", "sca@gmail.com", "12354685", "123456825", "are", "ABJ","ADOU Venance", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces6 = new Cessionnaire(6l, "CONTINENTAL-RE", "CRE", "cnre@gmail.com", "cnrare-tel", "cnare-cel", "ncnre", "ABJ","YOUIN Salif", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces7 = new Cessionnaire(7l, "SCG-RE", "SCG-RE", "sgre@gmail.com", "sgre-tel", "sgre-cel", "sgre", "ABJ","KONAN Laurent", FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cessionnaire ces8 = new Cessionnaire(8l, "WAICA-RE", "WRE", "wre@gmail.com", "wre-tel", "wre-cel", "wre", "ABJ", "ESSOH Fernand",FIVE, LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        cesRepo.saveAll(Arrays.asList(ces1, ces2, ces3, nre,ces5, ces6, ces7, ces8));


        //Cedantes
        Cedante nsiaci = new Cedante(1l, "NSIA CI", "NSIA-CI", "05 05 05 05 01", "nsiaci@gmail.com", "NSIA CI", "NSIA FAX", "CI","YOUIN Salif" ,nre, new Pays("CIV"),new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cedante nsiabn = new Cedante(2l, "NSIA BN", "NSIA-BN", "05 05 05 05 02", "nsiabn@gmail.com", "NSIA BN", "NSIA FAX", "BN","Coulibaly Lenimama", nre, new Pays("BNN"), new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        Cedante nsiatg = new Cedante(3l, "NSIA TG", "NSIA-TG", "05 05 05 05 03", "nsiaci@gmail.com", "NSIA TG", "NSIA FAX", "TG","Atsin Ghislain Herman", nre, new Pays("TGO"), new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT"));
        cedRepo.saveAll(Arrays.asList(nsiaci, nsiabn, nsiatg));

        //Affaires
        Affaire affnsiaci = new Affaire(new BigDecimal(30000000),"AFF-001", "SNDI", "Affaire NSIA-CI (DEV)", LocalDate.now(), LocalDate.of(2024, 12, 05),new Exercice(2020L));
        affnsiaci.setStatut(new Statut("SAI"));
        affnsiaci.setCedante(nsiaci);
        Facultative facnsiaci = new Facultative(affnsiaci, "AFF-001", new BigDecimal(30000000), new BigDecimal(30000000));
        facRepo.save(facnsiaci);

        Affaire affnsiabn = new Affaire(new BigDecimal(20000000),"AFF-002", "DGMP", "Affaire NSIA-BN (Marchés Publics)", LocalDate.now(), LocalDate.of(2025, 10, 05),new Exercice(2022L));
        affnsiabn.setStatut(new Statut("SAI"));
        affnsiabn.setCedante(nsiabn);
        Facultative facaffnsiabn = new Facultative(affnsiabn, "AFF-002", new BigDecimal(20000000), new BigDecimal(20000000));
        facRepo.save(facaffnsiabn);

        Affaire affnsiatg = new Affaire(new BigDecimal(40000000),"AFF-003", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2028, 11, 02),new Exercice(2023L));
        affnsiatg.setStatut(new Statut("SAI"));
        affnsiatg.setCedante(nsiatg);
        Facultative facaffnsiatg = new Facultative(affnsiatg, "AFF-003", new BigDecimal(40000000), new BigDecimal(40000000));
        facRepo.save(facaffnsiatg);

        Affaire aff1 = new Affaire(new BigDecimal(50000000),"AFF-004", "SNDI", "DEV", LocalDate.now(), LocalDate.of(2023, 10, 05),new Exercice(2023L));
        aff1.setStatut(new Statut("SAI"));
        Facultative fac = new Facultative(aff1, "AFF-004", new BigDecimal(50000000), new BigDecimal(50000000));
        facRepo.save(fac);
    }
}
