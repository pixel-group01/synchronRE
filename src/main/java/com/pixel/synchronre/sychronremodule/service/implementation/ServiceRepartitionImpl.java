package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
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

    @Override
    public RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToRepartition(dto);
        rep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_REPARTITION, null, rep, SynchronReTables.REPARTITION);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    private RepartitionDetailsResp createCesLegRepartition(CreateCesLegReq dto) throws UnknownHostException
    {
        Repartition rep;
        Repartition oldRep = null;
        boolean existByAffaireAndPcl = repRepo.existsByAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId());
        if(repRepo.existsByAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId()))
        {
            rep = repRepo.findByAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId());
            oldRep = repCopier.copy(rep);
            rep.setRepStatut(dto.isAccepte());
        }
        else
        {
            rep = repMapper.mapToCesLegRepartition(dto);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
            rep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));
        }

        rep = repRepo.save(rep);
        logService.logg(existByAffaireAndPcl ? RepartitionActions.UPDATE_CES_LEG_REPARTITION : RepartitionActions.CREATE_CES_LEG_REPARTITION, oldRep , rep, SynchronReTables.REPARTITION);
        rep.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public List<RepartitionDetailsResp> createCesLegRepartitions(List<CreateCesLegReq> dtos) throws UnknownHostException {
        return dtos.stream().map(dto-> {
            try {
                return this.createCesLegRepartition(dto);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return  null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public RepartitionDetailsResp createPartCedRepartition(CreatePartCedRepartitionReq dto) throws UnknownHostException {
        boolean existsByAffaireAndTypeRep = repRepo.existsByAffaireAndTypeRep(dto.getAffId(), "REP_CED");
        Repartition rep;
        Repartition oldRep = null;
        if(existsByAffaireAndTypeRep)
        {
            rep = repRepo.findByAffaireAndTypeRep(dto.getAffId(), "REP_CED").get(0);
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
        }
        else
        {
            rep = repMapper.mapToPartCedRepartition(dto);
        }
        rep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));
        rep = repRepo.save(rep);
        logService.logg(existsByAffaireAndTypeRep ? RepartitionActions.UPDATE_CED_REPARTITION : RepartitionActions.CREATE_CED_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override @Transactional //Répartition de type cession légale et part cédante (balayage de tout l'écran vrouuuu)
    public RepartitionDetailsResp createCedLegRepartition(CreateCedLegRepartitionReq dto) throws UnknownHostException
    {
         this.createCesLegRepartitions(dto.getCesLegDtos());
         CreatePartCedRepartitionReq partCedDto = new CreatePartCedRepartitionReq();
         BeanUtils.copyProperties(dto, partCedDto, "cesLegDtos");
         RepartitionDetailsResp repartitionDetailsResp = this.createPartCedRepartition(partCedDto);
         mvtService.createMvtAffaire(new MvtReq(dto.getAffId(), StatutEnum.EN_COURS_DE_REPARTITION.staCode, null));

         return repartitionDetailsResp;
    }

    @Override @Transactional
    public RepartitionDetailsResp updateCedLegRepartition(UpdateCedLegRepartitionReq dto) throws UnknownHostException {
        Repartition cedRep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("L'ID de la répartition de type part cédante est inconnu"));
        Repartition oldCedRep = repCopier.copy(cedRep);
        cedRep.setRepTaux(dto.getRepTaux());
        cedRep.setRepCapital(dto.getRepCapital());
        cedRep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));
        repRepo.save(cedRep);
        dto.getUpdateCesLegReqs().stream().peek(pclRepDto-> this.updatePclReps(pclRepDto));
        logService.logg(RepartitionActions.CREATE_CED_REPARTITION, oldCedRep, cedRep, SynchronReTables.REPARTITION);
        return repMapper.mapToRepartitionDetailsResp(cedRep);
    }

    private void updatePclReps(UpdateCesLegReq pclRepDto)
    {
        Repartition pclRep ;
        if(pclRepDto.getRepId() != null)
        {
            pclRep = repRepo.findById(pclRepDto.getRepId()).orElseThrow(()->new AppException("Repartition de type cession légale introuvable"));
            Repartition oldPclRep = repCopier.copy(pclRep);
            pclRep.setRepStatut(pclRepDto.isAccepte());
            //pclRep.setRepTaux(pclRepDto.getRepTaux());
            //pclRep.setRepCapital(pclRepDto.getRepCapital());
            //pclRep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(pclRepDto.getRepCapital().doubleValue()));
            repRepo.save(pclRep);
        }
        else
        {
            CreateCesLegReq createCesLegReq = new CreateCesLegReq(pclRepDto.getRepCapital(), pclRepDto.getRepTaux(), pclRepDto.getAffId(), pclRepDto.getParamCesLegalId(), pclRepDto.isAccepte());
            try {
                this.createCesLegRepartition(createCesLegReq);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                throw new AppException("Une erreur est survenue lors de l'enregistrement");
            }
        }
    }

    @Override @Transactional //Placemement
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smplCi = aff.getFacSmpLci();
        if(smplCi == null || smplCi.compareTo(ZERO) == 0) throw new AppException("impossible de faire un placement. La LCI de l'affaire est nulle");
        BigDecimal repTaux = dto.getRepCapital() == null || dto.getRepCapital().compareTo(ZERO) == 0 ? ZERO : dto.getRepCapital().multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal repPrime = aff.getFacPrime() == null ? ZERO : aff.getFacPrime().multiply(repTaux);
        boolean firstPlacement = !repRepo.affaireHasPlacement(dto.getAffId());
        boolean existsByAffaireAndTypeRepAndCesId = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
        Repartition rep;
        Repartition oldRep = null;
        if(existsByAffaireAndTypeRepAndCesId)
        {
            if(dto.getRepId() == null) throw new AppException("Veuillez fournir l'ID du placement");
            rep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
            //rep = repRepo.findByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setInterlocuteurPrincipal(new Interlocuteur(dto.getInterlocuteurPrincipalId()));
            String stringIntIds = dto.getAutreInterlocuteurIds() == null ? "" : dto.getAutreInterlocuteurIds() .stream().map(String::valueOf).collect(Collectors.joining(","));
            rep.setAutreInterlocuteurs(stringIntIds);
        }
        else
        {
            rep = repMapper.mapToPlaRepartition(dto);
            rep.setRepStaCode(new Statut(StatutEnum.SAISIE_CRT.staCode));
            rep = repRepo.save(rep);
            mvtService.createMvtPlacement(new MvtReq(rep.getRepId(), StatutEnum.SAISIE_CRT.staCode, null));
            bordService.createBordereau(rep.getRepId());
        }
        rep.setRepTaux(repTaux);
        rep.setRepPrime(repPrime);
        logService.logg(existsByAffaireAndTypeRepAndCesId ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        if(firstPlacement)
        {
            mvtService.createMvtAffaire(new MvtReq(dto.getAffId(), EN_COURS_DE_PLACEMENT.staCode, null));
        }
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override @Transactional
    public RepartitionDetailsResp updateRepartition(UpdateRepartitionReq dto) throws UnknownHostException
    {
        Repartition rep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Répartition introuvable"));
        Repartition oldRep = repCopier.copy(rep);
        BeanUtils.copyProperties(dto, rep);
        rep.setType(new Type(dto.getTypId()));
        rep.setCessionnaire(new Cessionnaire(dto.getCesId()));
        rep.setAffaire(new Affaire(dto.getAffId()));
        rep.setParamCessionLegale(new ParamCessionLegale(dto.getParamCesLegalId()));
        rep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));
        repRepo.save(rep);
        logService.logg(RepartitionActions.UPDATE_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);

        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable)
    {
        return repRepo.searchRepartition(StringUtils.stripAccentsToUpperCase(key), affId, repType, staCodes,pageable);
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
    public CreateCedLegRepartitionReq getCedLegRepartitionDTO(Long affId) {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        CreateCedLegRepartitionReq dto = repRepo.getCedLegRepartitionDTO(affId);
        List<ParamCessionLegaleListResp> pcls = this.pclRepo.findPossiblePclByAffId(affId);
        List<CreateCesLegReq> cesLegReqs = pcls.stream().map(pcl->
        {
            boolean accepte = repRepo.existsValidByAffIdAndPclId(affId,pcl.getParamCesLegId());
            CreateCesLegReq cesLegReq =  new CreateCesLegReq();
            cesLegReq.setRepCapital(accepte ? repRepo.findValidByAffIdAndPclId(affId, pcl.getParamCesLegId()).getRepCapital() : pcl.getParamCesLegTaux().multiply(affaire.getAffCapitalInitial()).divide(CENT, 20, RoundingMode.HALF_UP));
            cesLegReq.setRepTaux(pcl.getParamCesLegTaux());
            cesLegReq.setAffId(affId);
            cesLegReq.setParamCesLegalId(pcl.getParamCesLegId());
            cesLegReq.setAccepte(accepte);
            return cesLegReq;
        }).collect(Collectors.toList());
        dto.setCesLegDtos(cesLegReqs);
        return dto;
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
        Long affId = repRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de transmettre ce placement à la validation car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(EN_ATTENTE_DE_VALIDATION.staCode));
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(plaId, EN_ATTENTE_DE_VALIDATION.staCode, null));
        logService.saveLog(RepartitionActions.TRANSMETTRE_PLACEMENT_POUR_VALIDATION);
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
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(RETOURNE.staCode));
        mvtService.createMvtPlacement(new MvtReq(plaId, RETOURNE.staCode, motif));
        logService.saveLog(RepartitionActions.RETOURNER_PLACEMENT);
    }

    @Override @Transactional
    public void validerPlacement(Long plaId) throws UnknownHostException {
        Long affId = repRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de valider ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(VALIDE.staCode));
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(plaId, VALIDE.staCode, null));
        logService.saveLog(RepartitionActions.VALIDER_PLACEMENT);
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
        mvtService.createMvtPlacement(new MvtReq(plaId, MAIL.staCode, null));
        logService.saveLog(RepartitionActions.TRANSMETTRE_NOTE_CESSION);
    }

    @Override @Transactional
    public void refuserPlacement(Long plaId, String motif) throws UnknownHostException {
        Long affId = repRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de refuser ce placement car l'affaire est non réalisée");
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez saisir le motif de retour");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(REFUSE.staCode));
        placement.setRepStatut(false);
        repRepo.save(placement);
        mvtService.createMvtPlacement(new MvtReq(plaId, REFUSE.staCode, motif));
        logService.saveLog(RepartitionActions.REFUSER_PLACEMENT);
    }

    @Override @Transactional
    public void annulerPlacement(Long plaId) throws UnknownHostException {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(ANNULE.staCode));
        mvtService.createMvtPlacement(new MvtReq(plaId, ANNULE.staCode, null));
        logService.saveLog(RepartitionActions.ANNULER_PLACEMENT);
    }

    @Override @Transactional
    public Repartition modifierPlacement(UpdatePlaRepartitionReq dto) throws UnknownHostException {
        Long affId = repRepo.getAffIdByRepId(dto.getRepId());
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de modifier ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition oldPlacement = repCopier.copy(placement);
        placement.setRepCapital(dto.getRepCapital());
        placement.setRepTaux(dto.getRepTaux());
        placement.setRepSousCommission(dto.getRepSousCommission());
        placement.setRepTauxComCourt(dto.getRepTauxComCourt());
        placement.setRepTauxComCed(dto.getRepSousCommission().subtract(dto.getRepTauxComCourt()));
        placement.setRepStaCode(new Statut(EN_ATTENTE_DE_VALIDATION.staCode));
        logService.logg(RepartitionActions.UPDATE_PLA_REPARTITION, oldPlacement, placement, SynchronReTables.REPARTITION);
        if(dto.getAvisModificationCession() != null)
            placementDocUploader.uploadDocument(new UploadDocReq(dto.getRepId(), "AVI_MOD_CES", null , "Avis de modification de cession", "Avis de modification de cession",dto.getAvisModificationCession()));
        mvtService.createMvtPlacement(new MvtReq(dto.getRepId(), EN_ATTENTE_DE_VALIDATION.staCode, null));
        return placement;
    }

    @Override @Transactional
    public void accepterPlacement(Long plaId) throws UnknownHostException {
        Long affId = repRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible d'accepter ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(ACCEPTE.staCode));
        mvtService.createMvtPlacement(new MvtReq(plaId, ACCEPTE.staCode, null));
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