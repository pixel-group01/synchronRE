package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
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
    private final TypeRepo typeRepo;
    private final BanqueRepository banqueRepository;

    @Override
    public void load()
    {
        //Cessionnaires
        BigDecimal FIVE = new BigDecimal(5);
        Cessionnaire ces1 = cesRepo.save(new Cessionnaire(null, "AVENI-RE", "ARE", "are@gmail.com", "123546", "123456879", "are", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces3 = cesRepo.save(new Cessionnaire(null, "NCA-RE", "NCARE", "ncare@gmail.com", "ncare-tel", "ncare-cel", "ncare", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces2 = cesRepo.save(new Cessionnaire(null, "GRAND-RE", "GRE", "gre@gmail.com", "gre-tel", "gre-cel", "gre", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        //Cessionnaire nre = cesRepo.save(new Cessionnaire(null, "NELSON-RE", "NRE", "nre@gmail.com", "nre-tel", "nre-cel", "nre", "ABJ","KOUSSI N'Guéssan Charlemargne", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces5 = cesRepo.save(new Cessionnaire(null, "SCA INTER A RE SOLUTION RE", "SCARE", "sca@gmail.com", "12354685", "123456825", "are", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces6 = cesRepo.save(new Cessionnaire(null, "CONTINENTAL-RE", "CRE", "cnre@gmail.com", "cnrare-tel", "cnare-cel", "ncnre", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces7 = cesRepo.save(new Cessionnaire(null, "SCG-RE", "SCG-RE", "sgre@gmail.com", "sgre-tel", "sgre-cel", "sgre", "ABJ", FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire ces8 = cesRepo.save(new Cessionnaire(null, "WAICA-RE", "WRE", "wre@gmail.com", "wre-tel", "wre-cel", "wre", "ABJ",FIVE, typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cessionnaire courtierPlaceur = cesRepo.save(new Cessionnaire(null, "GUY CAPENTER", "GUY CAPENTER", "wre@gmail.com", "wre-tel", "wre-cel", "wre", "ABJ",FIVE, typeRepo.findByUniqueCode("COURT_PLA").orElseThrow(()->new AppException("Type de document inconnu")), LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        //cesRepo.saveAll(Arrays.asList(ces1, ces2, ces3,ces5, ces6, ces7, ces8));
        //cesRepo.save(nre);

        Banque testBanque1=banqueRepository.save(new Banque("B00213","IB 00331","GST5-SSS","AAA","AAA",LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));

        //Cedantes
        Cedante nsiaci = cedRepo.save(new Cedante(null, "NSIA CI", "NSIA-CI", "05 05 05 05 01", "nsiaci@gmail.com", "NSIA CI", "NSIA FAX", "CI","YOUIN Salif", new Banque("B00213"), new Pays("CIV"),new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        Cedante nsiasen = cedRepo.save(new Cedante(null, "NSIA Assurances Guinée Bissau", "NSIA-SEN", "05 05 05 05 02", "nsiasen@gmail.com", "NSIA SEN", "NSIA SEN FAX", "SEN","YOUIN N'diaye", new Banque("B00213"), new Pays("SEN"),new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        //        Cedante nsiabn = cedRepo.save(new Cedante(null, "NSIA BN", "NSIA-BN", "05 05 05 05 02", "nsiabn@gmail.com", "NSIA BN", "NSIA FAX", "BN","Coulibaly Lenimama",new Banque("B00213"),new Pays("BNN"), new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
//        Cedante nsiatg = cedRepo.save(new Cedante(null, "NSIA TG", "NSIA-TG", "05 05 05 05 03", "nsiaci@gmail.com", "NSIA TG", "NSIA FAX", "TG","Atsin Ghislain Herman",new Banque("B00213"), new Pays("TGO"), new AppUser(1l), null,  LocalDateTime.now(), LocalDateTime.now(), new Statut("ACT")));
        //cedRepo.saveAll(Arrays.asList(nsiaci, nsiabn, nsiatg));
        cedRepo.saveAll(Arrays.asList(nsiaci));
    }
}