package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ReglementMapper {
    @Mapping(target = "affaire",  expression = "java(dto.getAffId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    Reglement mapToPaiement(CreatePaiementReq dto);

    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "affDateEffet", source = "affaire.affDateEffet")
    @Mapping(target = "affDateEcheance", source = "affaire.affDateEcheance")
    @Mapping(target = "cedNomFiliale", source = "affaire.cedante.cedNomFiliale")
    @Mapping(target = "cedSigleFiliale", source = "affaire.cedante.cedSigleFiliale")
    @Mapping(target = "userId", source = "appUser.userId")
    PaiementDetailsResp mapToPaiementDetailsResp(Reglement res);

}

