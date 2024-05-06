package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class PaysLoader implements Loader
{
    private final PaysRepository paysRepo;
    @Override
    public void load()
    {
        Pays ci = paysRepo.save(new Pays("CIV", "+225","Côte d'Ivoire",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays bn = paysRepo.save(new Pays("BNN", "+229","Benin",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays tg = paysRepo.save(new Pays("TGO", "+228","Togo",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays sen = paysRepo.save(new Pays("SEN","+221", "Sénegal",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays GAB = paysRepo.save(new Pays("GAB","+241", "Gabon",new Devise("XAF"), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays gui = paysRepo.save(new Pays("GUI","+224", "Guinée",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays BFO = paysRepo.save(new Pays("BFO","+226", "Burkina Faso",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays MAL = paysRepo.save(new Pays("MAL","+223", "Mali",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays CMR = paysRepo.save(new Pays("CMR","+237", "Cameroun",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));

        //Nouveau
        Pays CAF = paysRepo.save(new Pays("CAF","+236", "Centrafrique",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
       // Pays COM = paysRepo.save(new Pays("COM","+269", "Comores",new Devise("KMF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays COG = paysRepo.save(new Pays("COG","+242", "Congo",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays GNB = paysRepo.save(new Pays("GNB","+245", "Guinée-Bissau",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays GNQ = paysRepo.save(new Pays("GNQ","+240", "Guinée équatoriale",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays NER = paysRepo.save(new Pays("NER","+227", "Niger",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays TCD = paysRepo.save(new Pays("TCD","+235", "Tchad",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays CPV = paysRepo.save(new Pays("CPV","+238", "Cap-Vert",new Devise("CVE"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays GMB = paysRepo.save(new Pays("GMB","+220", "Gambie",new Devise("GMD"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays GHA = paysRepo.save(new Pays("GHA","+233", "Ghana",new Devise("GHS"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays LBR = paysRepo.save(new Pays("LBR","+231", "Libéria",new Devise("LRD"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Pays NGA = paysRepo.save(new Pays("NGA","+234", "Nigeria",new Devise("NGN"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
       // Pays SLE = paysRepo.save(new Pays("SLE","+232", "Sierra Leone",new Devise("SLL"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
    }
}
