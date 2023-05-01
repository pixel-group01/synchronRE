package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.MvtMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBordereau;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
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
    private final IServiceCalculsComptables comptaService;
    private final RepartitionMapper repMapper;
    private final ObjectCopier<Repartition> repCopier;
    private final ILogService logService;
    private final IServiceMouvement mvtService;
    private BigDecimal ZERO = BigDecimal.ZERO;
    private BigDecimal CENT = new BigDecimal(100);
    private final ParamCessionLegaleRepository pclRepo;
    private final MvtMapper mvtMapper;
    private final MouvementRepository mvtRepo;
    private final PlacementDocUploader placementDocUploader;
    private final EmailSenderService mailSenderService;
    @Value("${synchronre.email}")
    private String synchronreEmail;

    @Override
    public RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToRepartition(dto);
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
            oldRep = new Repartition();
            rep = repRepo.findByAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId());
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
        }
        else
        {
            rep = repMapper.mapToCesLegRepartition(dto);
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
            rep = repRepo.findByAffaireAndTypeCed(dto.getAffId(), "REP_CED");
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
        }
        else
        {
            rep = repMapper.mapToPartCedRepartition(dto);
        }

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
    private final IserviceBordereau bordService;
    @Override @Transactional //Placemement
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException
    {
        boolean firstPlacement = !repRepo.affaireHasPlacement(dto.getAffId());
        boolean existsByAffaireAndTypeRepAndCesId = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
        Repartition rep;
        Repartition oldRep = null;
        if(existsByAffaireAndTypeRepAndCesId)
        {
            rep = repRepo.findByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
        }
        else
        {
            rep = repMapper.mapToPlaRepartition(dto);
            rep.setRepStaCode(new Statut(StatutEnum.SAISIE_CRT.staCode));
            rep = repRepo.save(rep);
            mvtService.createMvtPlacement(new MvtReq(rep.getRepId(), StatutEnum.SAISIE_CRT.staCode, null));
            bordService.createBordereau(rep.getRepId());
        }
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
        logService.logg(RepartitionActions.UPDATE_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        repRepo.save(rep);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable)
    {
        return repRepo.searchRepartition(StringUtils.stripAccentsToUpperCase(key), affId, repType, staCodes,pageable);
    }

    @Override
    public CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage)
    {
        if(capital.compareTo(ZERO)<0) throw new AppException("Le capital doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le montant du capital ne doit pas exéder le besoin fac");
        restARepartir = restARepartir == null ? ZERO : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? ZERO : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(ZERO) <= 0 || capitalInit.compareTo(ZERO) <= 0) return new CalculRepartitionResp(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(CENT).divide(capitalInit, 2, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 2, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));

        return calculatePrimeAndCms(tauxCmsRea, tauxCmsCourtage, aff, resp);
    }

    private CalculRepartitionResp calculatePrimeAndCms(BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Affaire aff, CalculRepartitionResp resp)
    {
        BigDecimal facPrime = aff.getFacPrime();
        if(facPrime == null || tauxCmsRea == null || tauxCmsCourtage == null) return resp;

        BigDecimal cmsRea = facPrime.multiply(tauxCmsRea).divide(CENT, 2, RoundingMode.HALF_UP);
        BigDecimal cmsCourtage = facPrime.multiply(tauxCmsCourtage).divide(CENT, 2, RoundingMode.HALF_UP);
        BigDecimal primeNetteCes = facPrime.subtract(cmsRea);
        BigDecimal cmsCedante = cmsRea.subtract(cmsCourtage);
        resp.setCmsRea(cmsRea);
        resp.setPrimeNetteCessionnaire(primeNetteCes);
        resp.setCmsCourtage(cmsCourtage);
        resp.setCmsCedante(cmsCedante);

        return resp;
    }

    @Override
    public CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage)
    {
        if(taux.compareTo(ZERO)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? ZERO : restARepartir;

        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? ZERO : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(ZERO) <= 0 || capitalInit.compareTo(ZERO) <= 0) return new CalculRepartitionResp(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);
        
        BigDecimal capital = capitalInit.multiply(taux.divide(CENT, 2, RoundingMode.HALF_UP));
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le taux de repartition ne doit pas exéder " + CENT.multiply(restARepartir).divide(capitalInit, 2, RoundingMode.HALF_UP) + "%");

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(taux);
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 2, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));

        return calculatePrimeAndCms(tauxCmsRea, tauxCmsCourtage, aff, resp);
    }


    @Override
    public CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage)
    {
        if(tauxBesoin.compareTo(ZERO)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(tauxBesoin.compareTo(CENT)>0) throw new AppException("Le taux de repartition ne doit pas exéder 100% du besoin fac");

        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? ZERO : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? ZERO : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(ZERO) <= 0 || capitalInit.compareTo(ZERO) <= 0) return new CalculRepartitionResp(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);

        BigDecimal capital = tauxBesoin.divide(CENT, 2, RoundingMode.HALF_UP).multiply(restARepartir);
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(CENT).divide(capitalInit, 2, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 2, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));

        this.calculatePrimeAndCms(tauxCmsRea, tauxCmsCourtage, aff, resp);
        return resp;
    }

    @Override
    public void deletePlacement(Long repId) throws UnknownHostException
    {
        boolean plaExists = repRepo.placementExists(repId);
        if(plaExists)
        {
            Repartition placement = repRepo.findById(repId).orElse(null);
            if(placement != null)
            {
                Repartition oldPlacement = repCopier.copy(placement);
                repRepo.deleteById(repId);
                logService.logg(SynchronReActions.DELETE_PLACEMENT, oldPlacement, new Repartition(),SynchronReTables.REPARTITION);
            }
        }
    }

    @Override
    public List<ParamCessionLegaleListResp> getCesLegParam(Long affId)
    {
        return pclRepo.findByAffId(affId).stream()
            .peek(pcl->pcl.setParamCesLegCapital(this.calculateRepByTaux(affId, pcl.getParamCesLegTaux(), null, null).getCapital()))
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
            }
        });
    }

    @Override @Transactional
    public void transmettreNoteDeCession(Long plaId) throws IllegalAccessException, UnknownHostException {
        Affaire affaire = repRepo.getAffairedByRepId(plaId).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de transmettre la note de cession de ce placement car l'affaire est non réalisée");

        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(MAIL.staCode));
        String interlocEmail = repRepo.getInterlocuteurEmail(plaId);
        Cessionnaire cessionnaire = repRepo.getCessionnaireByRepId(plaId).orElseThrow(()->new AppException("Cessionnaire introuvable"));

        mailSenderService.sendNoteCessionEmail(synchronreEmail, cessionnaire.getCesEmail(), cessionnaire.getCesInterlocuteur(),affaire.getAffCode(), plaId, "Note de cession");
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
    public void modifierPlacement(UpdatePlaRepartitionReq dto) throws UnknownHostException {
        Long affId = repRepo.getAffIdByRepId(dto.getPlaId());
        if(affId == null) throw new AppException("Affaire introuvable");
        String affStatutCrea = affRepo.getAffStatutCreation(affId);
        if(affStatutCrea != null && affStatutCrea.equals("NON_REALISEE"))  throw new AppException("Impossible de modifier ce placement car l'affaire est non réalisée");
        Repartition placement = repRepo.findPlacementById(dto.getPlaId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition oldPlacement = repCopier.copy(placement);
        placement.setRepCapital(dto.getRepCapital());
        placement.setRepTaux(dto.getRepTaux());
        placement.setRepSousCommission(dto.getRepSousCommission());
        placement.setRepTauxComCourt(dto.getRepTauxComCourt());
        placement.setRepTauxComCed(dto.getRepSousCommission().subtract(dto.getRepTauxComCourt()));
        placement.setRepStaCode(new Statut(EN_ATTENTE_DE_VALIDATION.staCode));
        logService.logg(RepartitionActions.UPDATE_PLA_REPARTITION, oldPlacement, placement, SynchronReTables.REPARTITION);
        if(dto.getAvisModificationCession() != null)
            placementDocUploader.uploadDocument(new UploadDocReq(dto.getPlaId(), "AVI_MOD_CES", null ,"Avis de modification de cession",dto.getAvisModificationCession()));
        mvtService.createMvtPlacement(new MvtReq(dto.getPlaId(), EN_ATTENTE_DE_VALIDATION.staCode, null));
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


}
