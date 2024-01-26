package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.CreateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciceMapper {
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Exercice mapToExercice(CreateExerciceReq dto);

    ExerciceDetailsResp mapToExerciceDetailsResp(Exercice exo);
}
