package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Reconstitution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class ReconstitutionMapper
{
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "tranche", expression = "java(dto.getTrancheId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Tranche(dto.getTrancheId()))")
    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNpId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNpId() ))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    public abstract Reconstitution mapToReconstitution(ReconstitutionReq dto);

    @Mapping(target = "traiteNpId", source = "traiteNonProportionnel.traiteNpId")
    @Mapping(target = "traiReference", source = "traiteNonProportionnel.traiReference")
    @Mapping(target = "traiNumero", source = "traiteNonProportionnel.traiNumero")
    @Mapping(target = "staCode", source = "statut.staCode")
    @Mapping(target = "staLibelle", source = "statut.staLibelle")
    @Mapping(target = "trancheId", source = "tranche.trancheId")
    @Mapping(target = "trancheLibelle", source = "tranche.trancheLibelle")
    public abstract ReconstitutionResp mapToReconstitutionResp(Reconstitution dto);
}
