package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePlaRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;
import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.UN;

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
    @Value("${spring.mail.username}")
    private String synchronreEmail;
    private final IServiceInterlocuteur intService;
    private final IserviceCalculRepartition calculRepartitionService;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final CessionnaireRepository cesRepo;
    private final IserviceBordereau bordService;
    private final TypeRepo typeRepo;
    private final IServiceCalculsComptablesSinistre sinComptaService;
    private final IServiceCalculsComptables comptaService;
    private final DetailsBordereauRepository dbRepo;


    @Override @Transactional //Placemement
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto)
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smplCi = aff.getFacSmpLci();

        boolean placementExistsByAffaireAndCesId = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
        boolean modeUpdate = dto.getRepId() != null || placementExistsByAffaireAndCesId;

        if(smplCi == null || smplCi.compareTo(ZERO) == 0) throw new AppException("impossible de faire un placement. La LCI de l'affaire est nulle");
        if(!STATUT_CREATION.REALISEE.name().equals(aff.getAffStatutCreation())) throw new AppException("Impossible de faire un placement sur une affaire en intance ou non réalisée");
        BigDecimal resteAPlacer = comptaService.calculateRestARepartir(dto.getAffId());
        resteAPlacer = resteAPlacer == null ? BigDecimal.ZERO : resteAPlacer;

        if(modeUpdate)
        {
            BigDecimal capitalToUpdate = dto.getRepId() != null ? repRepo.getRepCapitalByRepId(dto.getRepId()) :
                    repRepo.getRepCapitalByAffIdAndCesId(dto.getAffId(), dto.getCesId());
            capitalToUpdate = capitalToUpdate == null ? BigDecimal.ZERO : capitalToUpdate;
            resteAPlacer = resteAPlacer.add(capitalToUpdate);
        }
        BigDecimal futureResteARepartir = resteAPlacer.subtract(dto.getRepCapital());

        if (futureResteARepartir.abs().compareTo(UN) <=0)
        {
            dto.setRepCapital(resteAPlacer);
        }
        BigDecimal repTaux = dto.getRepCapital() == null || dto.getRepCapital().compareTo(ZERO) == 0 ? ZERO : dto.getRepCapital().multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal repPrime = aff.getFacPrime() == null ? ZERO : aff.getFacPrime().multiply(repTaux).divide(CENT, 100, RoundingMode.HALF_UP);
        boolean firstPlacement = !repRepo.affaireHasPlacement(dto.getAffId());

        Repartition rep;
        Repartition oldRep = null;


        if(modeUpdate)
        {
            if(dto.getRepId() == null) throw new AppException("l'ID du placement n'a pas été fourni");

            rep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
            oldRep = repCopier.copy(rep);
            rep.setCessionnaire(new Cessionnaire(dto.getCesId()));
            rep.setAffaire(new Affaire(dto.getAffId()));

            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTauxComCourt(dto.getRepTauxComCourt());
            rep.setRepSousCommission(dto.getRepSousCommission());
            BigDecimal comCed = dto.getRepSousCommission() == null ? ZERO :
                    dto.getRepTauxComCourt() == null ? dto.getRepSousCommission() :
                    dto.getRepSousCommission().subtract(dto.getRepTauxComCourt());
            rep.setRepTauxComCed(comCed);
            rep.setInterlocuteurPrincipal(new Interlocuteur(dto.getInterlocuteurPrincipalId()));
            String stringIntIds = dto.getAutreInterlocuteurIds() == null ? "" : dto.getAutreInterlocuteurIds() .stream().map(String::valueOf).collect(Collectors.joining(","));
            rep.setAutreInterlocuteurs(stringIntIds);
            rep.setRepTaux(repTaux);
            rep.setRepPrime(repPrime);
            rep = repRepo.save(rep);
            bordService.updateDetailsBordereaux(rep);
        }
        else // Create
        {
            rep = repMapper.mapToPlaRepartition(dto);
            rep.setRepStaCode(new Statut(StatutEnum.SAISIE_CRT.staCode));
            rep = repRepo.save(rep);
            mvtService.createMvtPlacement(new MvtReq(RepartitionActions.CREATE_PLA_REPARTITION, rep.getRepId(), StatutEnum.SAISIE_CRT.staCode, null));
           // bordService.createNoteDebit(aff.getAffId());
            bordService.createNoteCession(rep.getRepId());
        }
        rep.setRepTaux(repTaux);
        rep.setRepPrime(repPrime);
        logService.logg(modeUpdate ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        if(firstPlacement)
        {
            mvtService.createMvtAffaire(new MvtReq(modeUpdate ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, dto.getAffId(), EN_COURS_DE_PLACEMENT.staCode, null));
        }
        Cessionnaire ces = cesRepo.findById(dto.getCesId()).orElseThrow(()->new AppException("Cessionnaire introuvable "));
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable" ));
        rep.setCessionnaire(ces);
        rep.setAffaire(affaire);
        rep = repRepo.save(rep);
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
    public void deletePlacement(Long repId)
    {
        boolean plaExists = repRepo.placementExists(repId);
        if(plaExists)
        {
            Repartition placement = repRepo.findById(repId).orElse(null);
            Repartition oldPlacement = repCopier.copy(placement);
            placement.setRepStatut(false);
            repRepo.save(placement);
            DetailBordereau detailBordereau = dbRepo.findByPlaId(repId);
            if(detailBordereau != null)
            {
                detailBordereau.setDebStatut(false);
                dbRepo.save(detailBordereau);
            }
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
    public void transmettrePlacementPourValidation(Long plaId)
    {
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
    public void transmettrePlacementPourValidation(List<Long> plaIds)
    {
        if(plaIds != null && plaIds.size() > 0)
        {
            plaIds.forEach(id->this.transmettreNoteDeCession(id));
        }
    }

    @Override @Transactional
    public void retournerPlacement(Long plaId, String motif)
    {
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
    public void validerPlacement(Long plaId)
    {
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
    public void transmettreNoteDeCession(List<Long> plaIds)
    {
        plaIds.forEach(plaId-> {this.transmettreNoteDeCession(plaId);});
    }

    @Override @Transactional
    public void transmettreNoteDeCession(Long plaId)
    {
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
    public void refuserPlacement(Long plaId, String motif)
    {
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
    public void annulerPlacement(Long plaId)
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(ANNULE.staCode));
        placement.setRepStatut(false);
        mvtService.createMvtPlacement(new MvtReq(RepartitionActions.ANNULER_PLACEMENT, plaId, ANNULE.staCode, null));
        logService.saveLog(RepartitionActions.ANNULER_PLACEMENT);
    }

    @Override @Transactional
    public void accepterPlacement(Long plaId)
    {
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

    @Override @Transactional
    public void validerPlacement(List<Long> plaIds)
    {
        if(plaIds == null) return;
        plaIds.forEach(plaId-> this.validerPlacement(plaId));
    }

    @Override @Transactional
    public void annulerRepartition(Long repId)
    {
        Repartition repartition = repRepo.findById(repId).orElseThrow(()->new AppException("Repartition introuvable"));
        Repartition oldRepartition = repCopier.copy(repartition);
        repartition.setRepStatut(false);
        logService.logg(RepartitionActions.ANNULER_REPARTITION, oldRepartition, repartition, SynchronReTables.REPARTITION);
    }
    @Override @Transactional
    public void doRepartitionSinistre(Affaire aff, Long sinId, CessionnaireListResp ces)
    {
        Long cesId = ces.getCesId();
        BigDecimal repCaptital = sinComptaService.calculateMtAPayerBySinAndCes(sinId, cesId);
        Repartition sinRep = new Repartition();

        sinRep.setSinistre(new Sinistre(sinId));
        sinRep.setRepStatut(true);
        sinRep.setRepCapital(repCaptital);
        sinRep.setRepCapitalLettre(ConvertMontant.numberToLetter(repCaptital == null ? BigDecimal.ZERO : repCaptital.setScale(0, RoundingMode.HALF_UP)));
        sinRep.setType(typeRepo.findByUniqueCode("REP_SIN").orElseThrow(()->new AppException("Type de document inconnu")));
        //sinRep.setInterlocuteurPrincipal(ces.getCesInterlocuteur());

        Repartition placement = repRepo.findByAffaireAndTypeRepAndCesId(aff.getAffId(), "REP_PLA", cesId);
        if(placement == null) throw new AppException("Placement introuvable");
        sinRep.setRepTaux(repRepo.getTauRep(placement.getRepId()));
        sinRep.setCessionnaire(new Cessionnaire(cesId));
        repRepo.save(sinRep);
    }
    @Override @Transactional
    public void saveReserveCourtier(Long affId, BigDecimal facSmp, BigDecimal prime100, BigDecimal reserveCourtier)
    {
        Type type = typeRepo.findByUniqueCode("REP_RES_COURT").orElseThrow(()->new AppException("Type introuvable : REP_RES_COURT"));
        if(facSmp == null) throw new AppException("La SMPLCI de l'affaire ne peut être nulle.");
        Repartition repReserveCourtier = repRepo.findReserveCourtierByAffId(affId);
        BigDecimal oldReserveCourtier = repReserveCourtier == null || repReserveCourtier.getRepCapital() == null ? ZERO : repReserveCourtier.getRepCapital();

        BigDecimal besoinFac = comptaService.calculateRestARepartir(affId);
        besoinFac = besoinFac == null ? ZERO : besoinFac;
        if(reserveCourtier != null && reserveCourtier.subtract(besoinFac.add(oldReserveCourtier)).compareTo(UN)>0) throw new AppException("La réserve courtier ne peut être supérieure au besoin fac (" + besoinFac + ")");
        if(reserveCourtier != null && reserveCourtier.subtract(besoinFac.add(oldReserveCourtier)).abs().compareTo(UN)<=0) reserveCourtier = besoinFac;
        reserveCourtier = reserveCourtier == null ? BigDecimal.ZERO : reserveCourtier;

        BigDecimal tauxReserve = reserveCourtier.multiply(CENT).divide(facSmp, 20, RoundingMode.HALF_UP);
        BigDecimal primeResCourt = tauxReserve.multiply(prime100).divide(CENT, 20, RoundingMode.HALF_UP);
        if(repReserveCourtier != null)
        {
            repReserveCourtier.setRepCapital(reserveCourtier);
            repReserveCourtier.setRepTaux(tauxReserve);
            repReserveCourtier.setRepPrime(primeResCourt);
            repReserveCourtier.setRepCapitalLettre(ConvertMontant.numberToLetter(reserveCourtier.setScale(0, RoundingMode.HALF_UP)));
            repRepo.save(repReserveCourtier);
            return;
        }
        repReserveCourtier = new Repartition();
        repReserveCourtier.setAffaire(new Affaire(affId));
        repReserveCourtier.setRepCapital(reserveCourtier);
        repReserveCourtier.setRepTaux(tauxReserve);
        repReserveCourtier.setRepPrime(primeResCourt);
        repReserveCourtier.setRepStatut(true);
        repReserveCourtier.setRepStaCode(new Statut("ACT"));
        repReserveCourtier.setType(type);
        repReserveCourtier.setRepCapitalLettre(ConvertMontant.numberToLetter(reserveCourtier.setScale(0, RoundingMode.HALF_UP)));
        repRepo.save(repReserveCourtier);
    }
}
