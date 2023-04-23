package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class FacultativeMapper
{
    @Autowired protected IServiceCalculsComptables comptaService;
    @Autowired protected IJwtService jwtService;
    @Autowired protected RepartitionRepository repRepo;
    @Autowired protected CessionnaireRepository cesRepo;


    public Facultative mapToFacultative(CreateFacultativeReq dto)
    {
        Long connectedUserId = jwtService.getConnectedUserId();
        Long connectedFncId = jwtService.getConnectedUserFunctionId();
        Affaire affaire = new Affaire();
        BeanUtils.copyProperties(dto, affaire);
        affaire.setCedante(dto.getCedenteId() == null ? null : new Cedante(dto.getCedenteId()));
        affaire.setCouverture(dto.getCouvertureId() == null ? null : new Couverture(dto.getCouvertureId()));
        affaire.setAffUserCreator(connectedUserId == null ? null : new AppUser(connectedUserId));
        affaire.setAffFonCreator(connectedFncId == null ? null : new AppFunction(connectedFncId));
        return new Facultative(affaire, dto.getFacNumeroPolice(), dto.getFacSmpLci(), dto.getFacPrime());
    }

    @Mapping(target = "cedenteId", expression = "java(fac.getCedante() == null ? null : fac.getCedante().getCedId())")
    @Mapping(target = "statutCode", expression = "java(fac.getStatut() == null ? null : fac.getStatut().getStaCode())")
    @Mapping(target = "couvertureId", expression = "java(fac.getCouverture() == null ? null : fac.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(fac.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(fac.getAffId()))")
    public abstract FacultativeDetailsResp mapToFacultativeDetailsResp(Facultative fac);

    @Mapping(target = "cedenteId", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedId())")
    @Mapping(target = "cedNomFiliale", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedNomFiliale())")
    @Mapping(target = "cedSigleFiliale", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedSigleFiliale())")
    @Mapping(target = "statutCode", expression = "java(aff.getStatut() == null ? null : aff.getStatut().getStaCode())")
    @Mapping(target = "couvertureId", expression = "java(aff.getCouverture() == null ? null : aff.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(aff.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(aff.getAffId()))")
    public abstract FacultativeDetailsResp mapToFacultativeDetailsResp(Affaire aff);

    @Mapping(target = "mtTotalCmsCedante", expression = "java(comptaService.calculateMtTotaleCmsCed(aff.getAffId()))")
    @Mapping(target = "mtTotalCmsReaOwner", expression = "java(comptaService.calculateMtTotalCmsReaOwner(aff.getAffId()))")
    @Mapping(target = "mtTotalPrimeNetteCes", expression = "java(comptaService.calculateMtTotalAReverseAuxCes(aff.getAffId()))")
    @Mapping(target = "dejaRegle", expression = "java(comptaService.calculateDejaRegle(aff.getAffId()))")
    @Mapping(target = "resteARegler", expression = "java(comptaService.calculateRestARegler(aff.getAffId()))")
    @Mapping(target = "tauxDeReglement", expression = "java(comptaService.calculateTauxDeReglement(aff.getAffId()))")
    @Mapping(target = "dejaReverse", expression = "java(comptaService.calculateDejaReverse(aff.getAffId()))")
    @Mapping(target = "resteAReverser", expression = "java(comptaService.calculateRestAReverser(aff.getAffId()))")
    @Mapping(target = "tauxDeReversement", expression = "java(comptaService.calculateTauxDeReversement(aff.getAffId()))")
    @Mapping(target = "detailsEtatComptables", expression = "java(this.getDetailsEtatComptables(aff.getAffId()))")

    @Mapping(target = "cedenteId", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedId())")
    @Mapping(target = "statutCode", expression = "java(aff.getStatut() == null ? null : aff.getStatut().getStaCode())")
    @Mapping(target = "couvertureId", expression = "java(aff.getCouverture() == null ? null : aff.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(aff.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(aff.getAffId()))")
    public abstract EtatComptableAffaire mapToEtatComptableAffaire(Affaire aff);

    protected List<EtatComptableAffaire.DetailsEtatComptable> getDetailsEtatComptables(Long affId)
    {
        return repRepo.getCesIdsByAffId(affId).stream()
                .map(cesId->this.getDetailsEtatComptable(affId, cesId))
                .collect(Collectors.toList());
    }

    protected EtatComptableAffaire.DetailsEtatComptable getDetailsEtatComptable(Long affId, Long cesId)
    {
        Cessionnaire ces = cesRepo.findById(cesId).orElseThrow(()->new AppException("Cessionnaire introuvable"));
        EtatComptableAffaire.DetailsEtatComptable details = new EtatComptableAffaire().new DetailsEtatComptable();
        details.setCesId(cesId); details.setCesNom(ces.getCesNom()); details.setCesSigle(ces.getCesSigle());
        details.setMtCmsCedante(comptaService.calculateMtCmsCedByCes(affId, cesId));
        details.setMtCmsReaOwner(comptaService.calculateMtCmsReaOwnerByCes(affId, cesId));
        details.setMtPrimeNetteCes(comptaService.calculateMtPrimeNetteByCes(affId, cesId));
        //TODO dejaReverse resteAReverser tauxDeReversement

        return details;
    }
}
