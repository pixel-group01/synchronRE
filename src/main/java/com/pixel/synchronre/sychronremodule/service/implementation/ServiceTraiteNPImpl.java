package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TraiteNPMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTraiteNP;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@Service @RequiredArgsConstructor
public class ServiceTraiteNPImpl implements IServiceTraiteNP
{
    private final TraiteNPRepository traiteNPRepo;
    private final TraiteNPMapper traiteNPMapper;
    private final ILogService logService;
    private final ObjectCopier<TraiteNonProportionnel> traiteNPCopier;
    private final IServiceCalculsComptablesTraite traiteComptaService;
    private final CedanteTraiteRepository ctRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final IJwtService jwtService;
    private final IServiceMouvement mvtService;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;

    @Override @Transactional
    public TraiteNPResp create(CreateTraiteNPReq dto)
    {
        boolean isCourtier = jwtService.UserIsCourtier();
        TraiteNonProportionnel traiteNP = traiteNPMapper.mapToTraiteNP(dto);
        Statut statut = isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode);
        traiteNP.setStatut(statut);
        traiteNP = traiteNPRepo.save(traiteNP);
        logService.logg(TraitesActions.CREATE_TNP, null, traiteNP, "TraiteNonProportionnel");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.CREATE_TNP, traiteNP.getTraiteNpId(), traiteNP.getStatut().getStaCode(), null));
        TraiteNPResp traiteNPResp = traiteNPRepo.findTraiteById(traiteNP.getTraiteNpId());
        return traiteNPResp;
    }

    @Override
    public Page<TraiteNPResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? Collections.singletonList("SAI-CRT") : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, fncId, userId, cedId, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override @Transactional
    public TraiteNPResp update(UpdateTraiteNPReq dto)
    {
        TraiteNonProportionnel traiteNP = traiteNPRepo.findById(dto.getTraiteNpId()).orElseThrow(()->new AppException("Traité introuvable"));
        TraiteNonProportionnel oldTraiteNP = traiteNPCopier.copy(traiteNP);
        BeanUtils.copyProperties(dto, traiteNP);
        traiteNP.setTraiEcerciceRattachement(EnumUtils.getEnum(EXERCICE_RATTACHEMENT.class, dto.getTraiEcerciceRattachement()));
        traiteNP.setTraiPeriodicite(EnumUtils.getEnum(PERIODICITE.class, dto.getTraiPeriodicite()));
        traiteNP.setNature(new Nature(dto.getNatCode()));
        traiteNP.setTraiDevise(new Devise(dto.getDevCode()));
        traiteNP.setTraiCompteDevise(new Devise(dto.getTraiCompteDevCode()));
        traiteNP.setCourtierPlaceur(new Cessionnaire(dto.getCourtierPlaceurId()));
        traiteNP.setTraiTauxCourtier(dto.getTraiTauxCourtier());
        traiteNP.setTraiTauxCourtierPlaceur(dto.getTraiTauxCourtierPlaceur());
        traiteNP = traiteNPRepo.save(traiteNP);
        if((dto.getTraiTauxCourtier() == null && traiteNP.getTraiTauxCourtier() != null) || dto.getTraiTauxCourtier().compareTo(traiteNP.getTraiTauxCourtier()) != 0)
        {
            eventPublisher.publishEvent(new SimpleEvent<TraiteNonProportionnel>(this, "Modifier du taux courtier sur un traité", traiteNP));
        }
        if((dto.getTraiTauxCourtierPlaceur() == null && traiteNP.getTraiTauxCourtierPlaceur() != null) || dto.getTraiTauxCourtierPlaceur().compareTo(traiteNP.getTraiTauxCourtierPlaceur()) != 0)
        {
            eventPublisher.publishEvent(new SimpleEvent<TraiteNonProportionnel>(this, "Modifier du taux courtier placeur sur un traité", traiteNP));
        }
        logService.logg("Modification d'un traité non proportionnel", oldTraiteNP, traiteNP, "TraiteNonProportionnel");
        TraiteNPResp traiteNPResp = traiteNPMapper.mapToTraiteNPResp(traiteNP);
        return traiteNPResp;
    }

    @Override
    public UpdateTraiteNPReq edit(Long traiId) {
        return traiteNPRepo.getEditDtoById(traiId);
    }

    @Override
    public TraiteNPResp getTraiteDetails(Long traiId)
    {
        TraiteNPResp details = traiteNPRepo.findTraiteById(traiId);
        if(details == null) return null;
        details.setTraiTauxDejaPlace(traiteComptaService.calculateTauxDejaPlace(traiId));
        details.setTraiTauxRestantAPlacer(traiteComptaService.calculateTauxRestantAPlacer(traiId));
        PmdGlobalResp pmdGlobalResp = ctRepo.getPmdGlobal(traiId);
        if(pmdGlobalResp == null) return details;
        details.setTraiPmd(pmdGlobalResp.getTraiPmd());
        details.setTraiPmdCourtier(pmdGlobalResp.getTraiPmdCourtier());
        details.setTraiPmdCourtierPlaceur(pmdGlobalResp.getTraiPmdCourtierPlaceur());
        details.setTraiPmdNette(pmdGlobalResp.getTraiPmdNette());
        return details;
    }

    @Override
    public Page<TraiteNPResp> searchSaisieSouscripteur(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabSaisieSouscripteur : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, 2024L, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> enCoursDeValidation(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabEncoursValidattion : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, 2024L, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> enCoursDeReglement(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabEnReglement : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, 2024L, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> solde(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabSolde : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, 2024L, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> archive(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabArchive : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, 2024L, pageable);
        return traiteNPResps;
    }

    @Override
    public void transmettreTraiteAuValidateur(Long traiteNpId, int returnPageSize) {
            mvtService.createMvtTraite(new MvtReq(TraitesActions.TRANSMETTRE_AU_VALIDATEUR, traiteNpId, EN_ATTENTE_DE_VALIDATION.staCode, null));
            userRepo.getUserByTypeFunction("TYF_VAL").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDeValidationAuValidateur(u.getEmail(), u.getFirstName()));
    }

    @Override
    public void retournerAuSouscripteur(MvtReq dto) {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.RETOURNER_AU_SOUSCRIPTEUR, dto.getObjectId(), RETOURNER_VALIDATEUR.staCode, motif));
        Optional<TraiteNonProportionnel> traiteNpId = traiteNPRepo.findById(dto.getObjectId());
        //userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerEmailRetourSinistreAuSouscripteur(u.getEmail(), u.getFirstName(), traiteNpId, dto.getMvtObservation()));
    }

    @Override
    public void retournerAuValidateur(MvtReq dto) {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.RETOURNER_AU_VALIDATEUR, dto.getObjectId(), RETOURNER_COMPTABLE.staCode, motif));
        Optional<TraiteNonProportionnel> traiteNpId = traiteNPRepo.findById(dto.getObjectId());
        //userRepo.getUserByTypeFunctionAndCedId("TYF_VAL", cedId).forEach(u->emailSenderService.envoyerEmailRetourSinistreAuValidateur(u.getEmail(), u.getFirstName(), sinCode, dto.getMvtObservation()));
    }

    @Override
    public void valider(Long traiteNpId) {
        mvtService.createMvtTraite(new MvtReq(TraitesActions.VALIDER_TRAITE, traiteNpId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
        //userRepo.getUserByTypeFunction("TYF_COMPTA").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDePaiementAuComptable(u.getEmail(), u.getFirstName()));
        //envoyerNoteCessionSinistreEtNoteDebit(sinId);
    }


}