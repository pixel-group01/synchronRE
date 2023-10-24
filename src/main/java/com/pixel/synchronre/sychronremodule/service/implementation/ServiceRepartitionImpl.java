package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@Service
@RequiredArgsConstructor
public class ServiceRepartitionImpl implements IserviceRepartition
{
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final RepartitionMapper repMapper;
    private final ObjectCopier<Repartition> repCopier;
    private final ILogService logService;
    private final IServiceMouvement mvtService;
    private final BigDecimal ZERO = BigDecimal.ZERO;
    private final BigDecimal CENT = new BigDecimal(100);
    private final ParamCessionLegaleRepository pclRepo;
    private final PlacementDocUploader placementDocUploader;
    private final EmailSenderService mailSenderService;
    private final IserviceBordereau bordService;
    @Value("${spring.mail.username}")
    private String synchronreEmail;
    private final IServiceInterlocuteur intService;
    private final IserviceCalculRepartition calculRepartitionService;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final CessionnaireRepository cesRepo;

    @Override @Transactional //Placemement
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smplCi = aff.getFacSmpLci();
        if(smplCi == null || smplCi.compareTo(ZERO) == 0) throw new AppException("impossible de faire un placement. La LCI de l'affaire est nulle");
        if(!STATUT_CREATION.REALISEE.name().equals(aff.getAffStatutCreation())) throw new AppException("Impossible de faire un placement sur une affaire en intance ou non réalisée");
        BigDecimal repTaux = dto.getRepCapital() == null || dto.getRepCapital().compareTo(ZERO) == 0 ? ZERO : dto.getRepCapital().multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal repPrime = aff.getFacPrime() == null ? ZERO : aff.getFacPrime().multiply(repTaux);
        boolean firstPlacement = !repRepo.affaireHasPlacement(dto.getAffId());
        boolean existsByAffaireAndTypeRepAndCesId = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
        Repartition rep;
        Repartition oldRep = null;
        boolean modeUpdate = dto.getRepId() != null || existsByAffaireAndTypeRepAndCesId;

        if(modeUpdate)
        {
            if(dto.getRepId() != null)
            {
                rep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
                oldRep = repCopier.copy(rep);
                rep.setCessionnaire(new Cessionnaire(dto.getCesId()));
                rep.setAffaire(new Affaire(dto.getAffId()));
            }
            else // if(existsByAffaireAndTypeRepAndCesId)
            {
                if(dto.getRepId() == null) throw new AppException("Veuillez fournir l'ID du placement");
                rep = repRepo.findByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
                oldRep = repCopier.copy(rep);
            }
            rep.setRepCapital(dto.getRepCapital());
            rep.setInterlocuteurPrincipal(new Interlocuteur(dto.getInterlocuteurPrincipalId()));
            String stringIntIds = dto.getAutreInterlocuteurIds() == null ? "" : dto.getAutreInterlocuteurIds() .stream().map(String::valueOf).collect(Collectors.joining(","));
            rep.setAutreInterlocuteurs(stringIntIds);
        }
        else // Create
        {
            rep = repMapper.mapToPlaRepartition(dto);
            rep.setRepStaCode(new Statut(StatutEnum.SAISIE_CRT.staCode));
            rep = repRepo.save(rep);
            mvtService.createMvtPlacement(new MvtReq(RepartitionActions.CREATE_PLA_REPARTITION, rep.getRepId(), StatutEnum.SAISIE_CRT.staCode, null));
            bordService.createBordereau(rep.getRepId());
        }
        rep.setRepTaux(repTaux);
        rep.setRepPrime(repPrime);
        logService.logg(modeUpdate ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        if(firstPlacement)
        {
            mvtService.createMvtAffaire(new MvtReq(modeUpdate ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, dto.getAffId(), EN_COURS_DE_PLACEMENT.staCode, null));
        }
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable)
    {
        Page<RepartitionListResp> reps = repRepo.searchRepartition(StringUtils.stripAccentsToUpperCase(key), affId, repType, staCodes,pageable);
        reps.forEach(r->r.setRepTaux(r.getRepTaux().setScale(2, RoundingMode.HALF_DOWN)));
        return reps;
    }

    @Override
    public void deletePlacement(Long repId) throws UnknownHostException
    {
        boolean plaExists = repRepo.placementExists(repId);
        if(plaExists)
        {
            Repartition placement = repRepo.findById(repId).orElse(null);
            Repartition oldPlacement = repCopier.copy(placement);
            placement.setRepStatut(false);
            repRepo.save(placement);
            logService.logg(SynchronReActions.DELETE_PLACEMENT, oldPlacement, new Repartition(),SynchronReTables.REPARTITION);
        }
    }


    @Override
    public List<ParamCessionLegaleListResp> getCesLegParam(Long affId)
    {
        return pclRepo.findPossiblePclByAffId(affId).stream()
            .peek(pcl->pcl.setParamCesLegCapital(calculRepartitionService.calculateRepByTaux(affId, pcl.getParamCesLegTaux(), null, null, null).getCapital()))
            .collect(Collectors.toList());
    }

    @Override @Transactional
    public void transmettrePlacementPourValidation(Long plaId) throws UnknownHostException {
        Affaire aff = affRepo.getAffaireByRepId(plaId);
        if(aff == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = aff.getAffStatutCreation();
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de transmettre ce placement à la validation car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(EN_ATTENTE_DE_VALIDATION.staCode));
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.TRANSMETTRE_PLACEMENT_POUR_VALIDATION, plaId, EN_ATTENTE_DE_VALIDATION.staCode, null));
        logService.saveLog(RepartitionActions.TRANSMETTRE_PLACEMENT_POUR_VALIDATION);
        String cesNom = cesRepo.getCesNomByPlaId(plaId);
        userRepo.getUserByTypeFunction("TYF_VAL").forEach(u->emailSenderService.envoyerMailPourPlacementEnAttenteDeValidation(u.getEmail(), u.getFirstName(), aff.getAffCode(), cesNom, placement.getRepCapital(), aff.getDevise() == null ? null : aff.getDevise().getDevCode()));

    }

    @Override @Transactional
    public void transmettrePlacementPourValidation(List<Long> plaIds) throws UnknownHostException {
        if(plaIds != null && plaIds.size() > 0)
        {
            plaIds.forEach(id-> {
                try {
                    this.transmettreNoteDeCession(id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override @Transactional
    public void retournerPlacement(Long plaId, String motif) throws UnknownHostException {
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez saisir le motif de retour");
        Affaire aff = affRepo.getAffaireByRepId(plaId); if(aff == null) throw new AppException("Affaire introuvable");
        String cesNom = cesRepo.getCesNomByPlaId(plaId);
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(RETOURNE.staCode));
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.RETOURNER_PLACEMENT, plaId, RETOURNE.staCode, motif));
        logService.saveLog(RepartitionActions.RETOURNER_PLACEMENT);

        userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerMailPourPlacementRetourne(u.getEmail(), u.getFirstName(), aff.getAffCode(), cesNom, placement.getRepCapital(), aff.getDevise() == null ? null : aff.getDevise().getDevCode(), motif));
    }

    @Override @Transactional
    public void validerPlacement(Long plaId) throws UnknownHostException {
        Long affId = affRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de valider ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(VALIDE.staCode));
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.VALIDER_PLACEMENT, plaId, VALIDE.staCode, null));
        logService.saveLog(RepartitionActions.VALIDER_PLACEMENT);
        Affaire aff = affRepo.getAffaireByRepId(plaId); if(aff == null) throw new AppException("Affaire introuvable");
        String cesNom = cesRepo.getCesNomByPlaId(plaId);
        userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerMailPourEnvoieDeNoteDeCession(u.getEmail(), u.getFirstName(), aff.getAffCode(), cesNom, placement.getRepCapital(), aff.getDevise() == null ? null : aff.getDevise().getDevCode()));
    }

    @Override @Transactional
    public void transmettreNoteDeCession(List<Long> plaIds) {
        plaIds.forEach(plaId-> {
            try {
                this.transmettreNoteDeCession(plaId);
            } catch (IllegalAccessException | UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override @Transactional
    public void transmettreNoteDeCession(Long plaId) throws Exception {
        Affaire affaire = repRepo.getAffairedByRepId(plaId).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de transmettre la note de cession de ce placement car l'affaire est non réalisée ou en instance");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        Cessionnaire cessionnaire = repRepo.getCessionnaireByRepId(plaId).orElseThrow(()->new AppException("Cessionnaire introuvable"));


        List<InterlocuteurListResp> interlocuteurs = intService.getInterlocuteurByPlacement(plaId);
        if(interlocuteurs == null || interlocuteurs.isEmpty())   throw new AppException("Aucun interlocuteur n'a été choisi pour ce placement");
        interlocuteurs.forEach(interlocuteur->
        {
            try {
                mailSenderService.sendNoteCessionFacEmail(synchronreEmail, interlocuteur.getIntEmail(), interlocuteur.getIntNom(),affaire.getAffCode(), plaId, "Note de cession");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        placement.setRepStaCode(new Statut(MAIL.staCode));
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.TRANSMETTRE_NOTE_CESSION, plaId, MAIL.staCode, null));
        logService.saveLog(RepartitionActions.TRANSMETTRE_NOTE_CESSION);
    }

    @Override @Transactional
    public void refuserPlacement(Long plaId, String motif) throws UnknownHostException {
        Long affId = affRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de refuser ce placement car l'affaire est non réalisée");
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez saisir le motif de retour");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(REFUSE.staCode));
        placement.setRepStatut(false);
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.REFUSER_PLACEMENT, plaId, REFUSE.staCode, motif));
        logService.saveLog(RepartitionActions.REFUSER_PLACEMENT);
    }

    @Override @Transactional
    public void annulerPlacement(Long plaId) throws UnknownHostException {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(ANNULE.staCode));
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.ANNULER_PLACEMENT, plaId, ANNULE.staCode, null));
        logService.saveLog(RepartitionActions.ANNULER_PLACEMENT);
    }

    @Override @Transactional
    public void accepterPlacement(Long plaId) throws UnknownHostException {
        Long affId = affRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible d'accepter ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(ACCEPTE.staCode));
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.ACCEPTER_PLACEMENT, plaId, ACCEPTE.staCode, null));
        repRepo.save(placement);
        logService.saveLog(RepartitionActions.ACCEPTER_PLACEMENT);
    }

    @Override
    public void validerPlacement(List<Long> plaIds)
    {
        if(plaIds == null) return;
        plaIds.forEach(plaId->
        {
            try {
                this.validerPlacement(plaId);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }
}
