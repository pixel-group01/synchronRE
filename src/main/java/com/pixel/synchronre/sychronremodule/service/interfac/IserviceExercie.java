package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.CreateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.UpdateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IserviceExercie {
    ExerciceDetailsResp createExercice(CreateExerciceReq dto) throws UnknownHostException;

    @Transactional
    ExerciceDetailsResp activateExercice(Long exeCode) throws UnknownHostException;

    ExerciceDetailsResp updateExercice(UpdateExerciceReq dto) throws UnknownHostException;
    List<ExerciceListResp> searchExercice(String key);

    Exercice getExerciceCourant();

    List<ExerciceDetailsResp> getExerciceCourantAndPlus1();
}
