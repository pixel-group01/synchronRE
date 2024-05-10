package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class RepartitionMapper {
    @Autowired protected TypeRepo typeRepo;
    @Autowired protected ParamCessionLegaleRepository pclRepo;
    @Autowired protected IServiceCalculsComptables comptaService;
    @Autowired protected RepartitionRepository repRepo;
    public abstract Repartition mapToRepartition(CreateRepartitionReq dto);

    @Mapping(target = "affId", source = "affaire.affId")
    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "cesNom", source = "cessionnaire.cesNom")
    @Mapping(target = "cesSigle", source = "cessionnaire.cesSigle")
    @Mapping(target = "cesEmail", source = "cessionnaire.cesEmail")
    @Mapping(target = "cesTelephone", source = "cessionnaire.cesTelephone")
    @Mapping(target = "affBesoinFac", expression = "java(comptaService.calculateRestARepartir(res.getAffaire().getAffId()))")
    @Mapping(target = "affTauxBesoinFac", expression = "java(comptaService.calculateTauxRestARepartir(res.getAffaire().getAffId()))")
    @Mapping(target = "repStaCode", source = "repStaCode.staCode")
    public abstract RepartitionDetailsResp mapToRepartitionDetailsResp(Repartition res);

    @Mapping(target = "repStatut", source = "accepte")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_CES_LEG\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "paramCessionLegale", expression = "java(dto.getParamCesLegalId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale(dto.getParamCesLegalId()))")
    public abstract Repartition mapToCesLegRepartition(CreateCesLegReq dto);

    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_CED\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    public abstract Repartition mapToPartCedRepartition(CreatePartCedRepartitionReq dto);

    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_PLA\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    @Mapping(target = "repTauxComCed", expression = "java(dto.getRepSousCommission().subtract(dto.getRepTauxComCourt()))")
    @Mapping(target = "interlocuteurPrincipal", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur(dto.getInterlocuteurPrincipalId()))")
    @Mapping(target = "autreInterlocuteurs", expression = "java(this.mapIntIdstoString(dto.getAutreInterlocuteurIds()))")
    public abstract Repartition mapToPlaRepartition(CreatePlaRepartitionReq dto);

    /*@Mapping(target = "affId", source = "affaire.affId")
    @Mapping(target = "paramCesLegalId", source = "paramCessionLegale.paramCesLegId")
    @Mapping(target = "paramCesLegLibelle", source = "paramCessionLegale.paramCesLegLibelle")
    @Mapping(target = "repTauxBesoinFac", expression = "java(this.calculateTauxBesoinFac(rep.aff, rep.getRepTaux()))")
    @Mapping(target = "accepte", expression = "java(repRepo.repExistsByAffaireAndPcl(rep.getAffaire().getAffId(), rep.getRepId()))")
    public abstract UpdateCesLegReq mapToUpdateCesLegReq(Repartition rep);*/

    public abstract UpdateCedLegRepartitionReq mapToUpdateCedLegRepartitionReq(Affaire aff, Repartition repCed, List<UpdateCesLegReq> updateCesLegReqs);

    @Mapping(target = "partCedante", source = "mtPartCedante")
    @Mapping(target = "pclIds", expression = "java(this.getAcceptedPclIds(dto))")
    public abstract CalculationRepartitionReqDto mapToCalculationRepartitionReqDto(CalculationRepartitionRespDto dto);


    protected List<Long> getAcceptedPclIds(CalculationRepartitionRespDto dto)
    {
        return Stream.concat(dto.getParamCesLegsPremierFranc().stream(), dto.getParamCesLegs().stream())
                .filter(UpdateCesLegReq::isAccepte).map(UpdateCesLegReq::getParamCesLegalId).collect(Collectors.toList());
    }

    protected String mapIntIdstoString(List<Long> intIds)
    {
        String stringIntIds = intIds == null ? "" : intIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return stringIntIds;
    }

    @Mapping(target = "repCapital", source = "pmd")
    @Mapping(target = "repTaux", source = "tauxCesLeg")
    @Mapping(target = "repStaCode", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "paramCessionLegale", expression = "java((new com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale(cesLeg.getParamCesLegalId())))")
    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java(typeRepo.findByUniqueCode(\"REP_TNP\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type (REP_TNP) introuvable\")))")
    public abstract Repartition mapToRepartition(CedanteTraiteReq.CesLeg cesLeg);
}