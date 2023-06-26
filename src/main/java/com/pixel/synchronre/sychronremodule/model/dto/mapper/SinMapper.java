package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SinMapper
{//
    @Autowired protected IJwtService jwtService;
    @Autowired protected RepartitionRepository repRepo;
    @Autowired protected SinRepo sinRepo;
    @Autowired protected AffaireRepository affRepo;
    @Autowired protected CessionnaireRepository cesRepo;
    @Autowired protected IServiceCalculsComptablesSinistre sinComptaService;

    @Mapping(target = "userCreator", expression = "java(jwtService.getConnectedUserId() == null ? null : new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "functionCreator", expression = "java(jwtService.getConnectedUserFunctionId() == null ? null : new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    //@Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"SAI\"))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    public abstract Sinistre mapToSinistre(CreateSinistreReq dto);

    @Mapping(target = "affId", source = "affaire.affId")
    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "affCapitalInitial", source = "affaire.affCapitalInitial")
    public abstract SinistreDetailsResp mapToSinistreDetailsResp(Sinistre sinistre);

    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "affCapitalInitial", source = "affaire.affCapitalInitial")

    @Mapping(target = "dejaRegle", expression = "java(sinComptaService.calculateMtDejaPayeBySin(sin.getSinId()))")
    @Mapping(target = "resteARegler", expression = "java(sinComptaService.calculateMtAPayerBySin(sin.getSinId()))")
    @Mapping(target = "tauxDeReglement", expression = "java(sinComptaService.calculateTauxDePaiementSinistre(sin.getSinId()))")

    @Mapping(target = "mtDejaReverse", expression = "java(sinComptaService.calculateMtSinistreTotalDejaReverseBySin(sin.getSinId()))")
    @Mapping(target = "mtEnAttenteDeReversement", expression = "java(sinComptaService.calculateMtSinistreEnAttenteDeAReversement(sin.getSinId()))")

    @Mapping(target = "detailsEtatComptableSinistres", expression = "java(this.getDetailsEtatComptables(sin.getSinId()))")
    public abstract EtatComptableSinistreResp mapToEtatComptableSinistre(Sinistre sin);

    protected List<EtatComptableSinistreResp.DetailsEtatComptableSinistre> getDetailsEtatComptables(Long sinId)
    {
        return repRepo.getCesIdsBySinId(sinId).stream()
                .map(cesId->this.getDetailsEtatComptableSinistre(sinId, cesId))
                .collect(Collectors.toList());
    }

    private EtatComptableSinistreResp.DetailsEtatComptableSinistre getDetailsEtatComptableSinistre(Long sinId, Long cesId)
    {
        Cessionnaire ces = cesRepo.findById(cesId).orElseThrow(()->new AppException("Cessionnaire introuvable"));
        EtatComptableSinistreResp.DetailsEtatComptableSinistre details = new EtatComptableSinistreResp().new DetailsEtatComptableSinistre();

        BeanUtils.copyProperties(ces, details);

        details.setMtTotalARegler(sinComptaService.calculateMtAPayerBySinAndCes(sinId, cesId));
        details.setMtDejaRegle(sinComptaService.calculateMtDejaPayeBySinAndCes(sinId, cesId));
        details.setMtResteARegler(sinComptaService.calculateResteAPayerBySinAndCes(sinId, cesId));
        details.setTauxDeReglement(sinComptaService.calculateTauxDePaiementSinistreBySinAndCes(sinId, cesId));

        return details;
    }
}
