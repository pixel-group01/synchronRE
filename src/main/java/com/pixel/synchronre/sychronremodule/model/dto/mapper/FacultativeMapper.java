package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class FacultativeMapper
{
    @Autowired protected IServiceCalculsComptables comptaService;
    @Autowired protected IJwtService jwtService;
    @Autowired protected RepartitionRepository repRepo;
    @Autowired protected CessionnaireRepository cesRepo;
    @Autowired protected TypeRepo typeRepo;
    @Autowired protected IServiceCalculsComptables comptaAffaireService;
    @Autowired protected AffaireRepository affRepo;

    @Mapping(target = "cedante", expression = "java(dto.getCedId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(dto.getCedId()))")
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"SAI\"))")
    @Mapping(target = "couverture", expression = "java(dto.getCouvertureId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Couverture(dto.getCouvertureId()))")
    @Mapping(target = "devise", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Devise(dto.getDevCode()))")
    @Mapping(target = "affType", expression = "java(typeRepo.findByUniqueCode(\"FAC\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "affUserCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "affFonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "exercice", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Exercice(dto.getExeCode()))")
    public abstract Affaire mapToAffaire(CreateFacultativeReq dto);

    public Facultative mapToFacultative(CreateFacultativeReq dto)
    {
        Long connectedUserId = jwtService.getConnectedUserId();
        Long connectedFncId = jwtService.getConnectedUserFunctionId();
        Affaire affaire = new Affaire();
        BeanUtils.copyProperties(dto, affaire);
        affaire.setFacNumeroPolice(dto.getFacNumeroPolice());
        affaire.setFacPrime(dto.getFacPrime());
        affaire.setFacSmpLci(dto.getFacSmpLci());
        affaire.setAffStatutCreation(dto.getAffStatutCreation());
        affaire.setCedante(dto.getCedId() == null ? null : new Cedante(dto.getCedId()));
        affaire.setCouverture(dto.getCouvertureId() == null ? null : new Couverture(dto.getCouvertureId()));
        affaire.setAffUserCreator(connectedUserId == null ? null : new AppUser(connectedUserId));
        affaire.setAffFonCreator(connectedFncId == null ? null : new AppFunction(connectedFncId));
        affaire.setDevise(dto.getDevCode() == null ? null : new Devise(dto.getDevCode()));
        return new Facultative(affaire, dto.getFacNumeroPolice(), dto.getFacSmpLci(), dto.getFacPrime());
    }

    @Mapping(target = "cedenteId", expression = "java(fac.getCedante() == null ? null : fac.getCedante().getCedId())")
    @Mapping(target = "statutCode", expression = "java(fac.getStatut() == null ? null : fac.getStatut().getStaCode())")
    @Mapping(target = "devCode", expression = "java(fac.getDevise() == null ? null : fac.getDevise().getDevCode())")
    @Mapping(target = "couvertureId", expression = "java(fac.getCouverture() == null ? null : fac.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(fac.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(fac.getAffId()))")
    public abstract FacultativeDetailsResp mapToFacultativeDetailsResp(Facultative fac);

    @Mapping(target = "cedenteId", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedId())")
    @Mapping(target = "cedNomFiliale", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedNomFiliale())")
    @Mapping(target = "cedSigleFiliale", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedSigleFiliale())")
    @Mapping(target = "statutCode", expression = "java(aff.getStatut() == null ? null : aff.getStatut().getStaCode())")
    @Mapping(target = "couvertureId", expression = "java(aff.getCouverture() == null ? null : aff.getCouverture().getCouId())")
    @Mapping(target = "devCode", expression = "java(aff.getDevise() == null ? null : aff.getDevise().getDevCode())")
    @Mapping(target = "couLibelle", expression = "java(aff.getCouverture() == null ? null : aff.getCouverture().getCouLibelle())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(aff.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(aff.getAffId()))")

    @Mapping(target = "branId", source = "couverture.branche.branId")
    @Mapping(target = "branLibelle", source = "couverture.branche.branLibelle")
    @Mapping(target = "couId", source = "couverture.couId")
    @Mapping(target = "exeCode", source = "exercice.exeCode")
    @Mapping(target = "placementTermine", expression = "java(this.placementIsFinished(aff.getAffId()))")
    public abstract FacultativeDetailsResp mapToFacultativeDetailsResp(Affaire aff);

    @Mapping(target = "mtTotalCmsCedante", expression = "java(comptaService.calculateMtTotaleCmsCed(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "mtTotalCmsCourtage", expression = "java(comptaService.calculateMtTotalCmsCourtage(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")

    @Mapping(target = "mtTotalPrimeNetteCes", expression = "java(comptaService.calculateMtTotalAReverseAuxCes(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "primeNetteCmsCedante", expression = "java(comptaService.calculatePrimeNetteCommissionCed(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "dejaRegle", expression = "java(comptaService.calculateDejaRegle(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "resteARegler", expression = "java(comptaService.calculateRestARegler(aff.getAffId()).setScale(5, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "tauxDeReglement", expression = "java(comptaService.calculateTauxDeReglement(aff.getAffId()).setScale(2, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "dejaReverse", expression = "java(comptaService.calculateDejaReverse(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "mtAttenteReversement", expression = "java(comptaService.calculateMtEnAttenteDeAReversement(aff.getAffId()).setScale(0, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "resteAReverser", expression = "java(comptaService.calculateRestAReverser(aff.getAffId()).setScale(5, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "tauxDeReversement", expression = "java(comptaService.calculateTauxDeReversement(aff.getAffId()).setScale(2, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "detailsEtatComptables", expression = "java(this.getDetailsEtatComptables(aff.getAffId()))")

    @Mapping(target = "cedenteId", expression = "java(aff.getCedante() == null ? null : aff.getCedante().getCedId())")
    @Mapping(target = "statutCode", expression = "java(aff.getStatut() == null ? null : aff.getStatut().getStaCode())")
    @Mapping(target = "devCode", expression = "java(aff.getDevise() == null ? null : aff.getDevise().getDevCode())")
    @Mapping(target = "couvertureId", expression = "java(aff.getCouverture() == null ? null : aff.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(comptaService.calculateRestARepartir(aff.getAffId()).setScale(5, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "capitalDejaReparti", expression = "java(comptaService.calculateDejaRepartir(aff.getAffId()).setScale(5, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "mtTotalPrimeBruteCes", expression = "java(repRepo.calculateMtPrimeBruteByAffaire(aff.getAffId()))")
    @Mapping(target = "mtTotalPrimeCessionnaireNetteComCed", expression = "java(comptaService.calculateMtTotalPrimeCessionnaireNetteComCed(aff.getAffId()))")
    public abstract EtatComptableAffaire mapToEtatComptableAffaire(Affaire aff);

    protected boolean placementIsFinished(Long affId)
    {
        BigDecimal besFac = this.comptaAffaireService.calculateRestARepartir(affId);
        besFac = besFac == null ? BigDecimal.ZERO : besFac;
        return besFac.compareTo(BigDecimal.ZERO) == 0;
    }

    protected List<EtatComptableAffaire.DetailsEtatComptable> getDetailsEtatComptables(Long affId)
    {
        return repRepo.getPlaIdsByAffId(affId).stream()
                .map(plaId->this.getDetailsEtatComptable(plaId))
                .collect(Collectors.toList());
    }

    public EtatComptableAffaire.DetailsEtatComptable getDetailsEtatComptable(Long plaId)
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        Long cesId = repRepo.getCesIdByRepId(plaId);
        Long affId = affRepo.getAffIdByRepId(plaId);

        //Affaire aff = affRepo.
        Cessionnaire ces = cesRepo.findById(cesId).orElseThrow(()->new AppException("Cessionnaire introuvable"));
        EtatComptableAffaire.DetailsEtatComptable details = new EtatComptableAffaire().new DetailsEtatComptable();
        details.setCesId(cesId); details.setCesNom(ces.getCesNom()); details.setCesSigle(ces.getCesSigle());
        details.setMtSousCms(repRepo.calculateMtSousCmsByCes(plaId));
        details.setMtCmsCedante(comptaService.calculateMtCmsCedByCes(plaId));
        details.setMtCmsCourtage(comptaService.calculateMtCmsCourtageByCes(plaId));
        details.setTauxSousCms(repRepo.getTauxSousCommission(plaId));
        details.setTauxCmsCedante(repRepo.getTauxCmsCedante(plaId));
        details.setTauxCmsCourtage(repRepo.getTauxCmsCourtage(plaId));
        details.setMtPrimeBruteCes(repRepo.calculateMtPrimeBruteByCes(plaId));
        details.setMtPrimeNetteCes(comptaService.calculateMtPrimeNetteByCes(plaId));
        details.setMtCapital(repRepo.getRepCapitalByRepId(plaId));
        //TODO dejaReverse resteAReverser tauxDeReversement
        details.setDejaReverse(comptaService.calculateDejaReverseByCes(plaId));
        details.setResteAReverser(comptaService.calculateRestAReverserbyCes(plaId));

        return details;
    }
}
