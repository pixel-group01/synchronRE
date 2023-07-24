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
        Pays ci = new Pays("CIV", "+225","Côte d'Ivoire",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays bn = new Pays("BNN", "+229","Benin",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays tg = new Pays("TGO", "+228","Togo",new Devise("XOF"),  new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays sen = new Pays("SEN","+221", "Sénegal",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays GAB = new Pays("GAB","+241", "Gabon",new Devise("XAF"), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays gui = new Pays("GUI","+224", "Guinée",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays BFO = new Pays("BFO","+226", "Burkina Faso",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays MAL = new Pays("MAL","+223", "Mali",new Devise("XOF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Pays CMR = new Pays("CMR","+237", "Cameroun",new Devise("XAF"),new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        paysRepo.saveAll(Arrays.asList(ci, bn, tg, sen,GAB, gui, BFO, MAL,CMR));
    }
}
