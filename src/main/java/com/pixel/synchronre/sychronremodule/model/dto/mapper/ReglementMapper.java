package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglementMapper {
    @Mapping(target = "typeReglement", expression = "java(com.pixel.synchronre.sychronremodule.model.enums.TypeReglement.valueOf(\"PAIEMENT_RECU\"))")
    Reglement mapToPaiement(CreatePaiementReq dto);
    PaiementDetailsResp mapToPaiementDetailsResp(Reglement res);
}
