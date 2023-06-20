package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sychronremodule.model.dao.ExerciceRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
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
public class ExerciceLoader implements Loader
{
    private final ExerciceRepository exeRepo;
    @Override
    public void load()
    {
        //Exercice
        Exercice exe0 = new Exercice(2020L,"Gestion 2020",false,new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Exercice exe1 = new Exercice(2021L,"Gestion 2021",false,new Statut("ACT"),LocalDateTime.now(), LocalDateTime.now());
        Exercice exe2 = new Exercice(2022L,"Gestion 2022",false,new Statut("ACT"),LocalDateTime.now(), LocalDateTime.now());
        Exercice exe3 = new Exercice(2023L,"Gestion 2023",true,new Statut("ACT"),LocalDateTime.now(), LocalDateTime.now());
        exeRepo.saveAll(Arrays.asList(exe0,exe1,exe2,exe3));
    }
}
