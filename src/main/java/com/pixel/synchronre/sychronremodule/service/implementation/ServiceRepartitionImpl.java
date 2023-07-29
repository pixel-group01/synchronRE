package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontantEnLettres;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
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
import java.text.NumberFormat;
import java.util.ArrayList;
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
    private final BigDecimal ZERO = BigDecimal.ZERO;
    private final BigDecimal CENT = new BigDecimal(100);
    private final ParamCessionLegaleRepository pclRepo;
    private final PlacementDocUploader placementDocUploader;
    private final EmailSenderService mailSenderService;
    private final IserviceBordereau bordService;
    @Value("${spring.mail.username}")
    private String synchronreEmail;

    private final ParamCessionLegaleRepository pclRepoo;

    @Override
    public RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToRepartition(dto);
        rep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(dto.getRepCapital().doubleValue()));
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
            rep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(dto.getRepCapital().doubleValue()));
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
        rep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(dto.getRepCapital().doubleValue()));
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
        cedRep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(dto.getRepCapital().doubleValue()));
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
        rep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(dto.getRepCapital().doubleValue()));
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
    public CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude)
    {
        if(capital.compareTo(ZERO)<0) throw new AppException("Le capital doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId, repIdToExclude);
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
    public CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude)
    {
        if(taux.compareTo(ZERO)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId, repIdToExclude);

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
    public CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude)
    {
        if(tauxBesoin.compareTo(ZERO)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(tauxBesoin.compareTo(CENT)>0) throw new AppException("Le taux de repartition ne doit pas exéder 100% du besoin fac");

        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId, repIdToExclude);
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
    public CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionReqDto dto)
    {
        if(dto == null) throw new AppException("Veuillez fournir les données d'entrée");
        if(dto.getAffId() == null) throw new AppException("Veuillez fournir l'ID de l'affaire");
        Affaire aff = affRepo.findById(dto.getAffId()).orElse(null);
        if(aff == null) return null;
        BigDecimal sommeTauxPcl = pclRepo.getSommeTauxParamCessionLegal(dto.getPclIds());
        sommeTauxPcl = sommeTauxPcl == null ? ZERO : sommeTauxPcl;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? ZERO : aff.getAffCapitalInitial();
        if(capitalInit.longValue() == 0) throw new AppException("Le capital initial de l'affaire est nul");
        BigDecimal restARepartir = comptaService.calculateRestARepartir(aff.getAffId(), dto.getRepIdToBeModified());
        BigDecimal besoinFacRestant = restARepartir.subtract(capitalInit.multiply(sommeTauxPcl).divide(CENT));

        CalculationRepartitionRespDto resp = new CalculationRepartitionRespDto();
        resp.setBesoinFac(restARepartir);

        if(dto.getRepCapital() != null && dto.getRepTaux() != null)
        {
            resp.setRepCapital(dto.getRepCapital());
            resp.setRepTaux(dto.getRepTaux());
        }
        else if(dto.getRepCapital() != null)
        {
            if(dto.getRepCapital().compareTo(ZERO)<0) throw new AppException("Le capital de repartition doit être un nombre strictement positif");
            if(dto.getRepCapital().compareTo(besoinFacRestant)>0) throw new AppException("Le capitaal ne peut exéder le reste à repartir (" + NumberFormat.getInstance().format(besoinFacRestant.doubleValue()) +")");
            besoinFacRestant = besoinFacRestant.subtract(dto.getRepCapital());
            BigDecimal repTaux = dto.getRepCapital().divide(capitalInit, 2, RoundingMode.HALF_UP).multiply(CENT);
            resp.setRepCapital(dto.getRepCapital());
            resp.setRepTaux(repTaux);
        }
        else if(dto.getRepTaux() != null)
        {
            if(dto.getRepTaux().compareTo(ZERO)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
            BigDecimal repCapital = capitalInit.multiply(dto.getRepTaux()).divide(CENT, 10, RoundingMode.HALF_UP);
            if(repCapital.compareTo(besoinFacRestant)>0) throw new AppException("Le taux de repartition ne doit pas exéder " + CENT.multiply(restARepartir).divide(capitalInit, 2, RoundingMode.HALF_UP) + "%");
            besoinFacRestant = besoinFacRestant.subtract(repCapital);
            resp.setRepCapital(repCapital);
            resp.setRepTaux(dto.getRepTaux());
        }
        resp.setBesoinFacRestant(besoinFacRestant);
        resp.setRepId(repRepo.getRepIdByAffIdAndTypeRep(dto.getAffId(), "REP_CED"));
        resp.setAffId(dto.getAffId());

        List<Long> pclIds = dto.getPclIds() == null ? new ArrayList<>() : dto.getPclIds();
        List<UpdateCesLegReq> pclReps =  pclRepo.findByAffId(dto.getAffId()).stream()
                .map(pcl->new UpdateCesLegReq(pcl,repRepo.getRepIdByAffIdAndPclId(dto.getAffId(), pcl.getParamCesLegId()), pcl.getParamCesLegTaux().multiply(capitalInit).divide(CENT, 2, RoundingMode.HALF_UP), dto.getAffId(),pclIds.contains(pcl.getParamCesLegId())))
                .collect(Collectors.toList());
        resp.setParamCesLegs(pclReps);
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
    public CreateCedLegRepartitionReq getCedLegRepartitionDTO(Long affId) {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        CreateCedLegRepartitionReq dto = repRepo.getCedLegRepartitionDTO(affId);
        List<ParamCessionLegaleListResp> pcls = this.pclRepo.findByAffId(affId);
        List<CreateCesLegReq> cesLegReqs = pcls.stream().map(pcl->
        {
            boolean accepte = repRepo.existsValidByAffIdAndPclId(affId,pcl.getParamCesLegId());
            CreateCesLegReq cesLegReq =  new CreateCesLegReq();
            cesLegReq.setRepCapital(accepte ? repRepo.findValidByAffIdAndPclId(affId, pcl.getParamCesLegId()).getRepCapital() : pcl.getParamCesLegTaux().multiply(affaire.getAffCapitalInitial()));
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
        return pclRepo.findByAffId(affId).stream()
            .peek(pcl->pcl.setParamCesLegCapital(this.calculateRepByTaux(affId, pcl.getParamCesLegTaux(), null, null, null).getCapital()))
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
        mailSenderService.sendNoteCessionFacEmail(synchronreEmail, cessionnaire.getCesEmail(), cessionnaire.getCesInterlocuteur(),affaire.getAffCode(), plaId, "Note de cession");
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

    @Override
    public UpdateCedLegRepartitionReq getUpdateCedLegDTO(Long affId)
    {
        Affaire aff = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire inexistante"));
        List<Repartition> repCeds = repRepo.findByAffaireAndTypeRep(affId, "REP_CED");
        Repartition repCed = repCeds == null ||repCeds.isEmpty() ? null : repCeds.get(0);
        List<UpdateCesLegReq> pclReps =repRepo.findUpdateCesLegReqByAffaireAndTypeRep(affId);
        List<Long> acceptedPclIds = pclReps.stream().filter(r->r.isAccepte()).map(r->r.getParamCesLegalId()).collect(Collectors.toList());

        List<UpdateCesLegReq> noneAcceptedPclReps = pclRepo.findByAffId(affId).stream()
                .filter(pcl->!acceptedPclIds.contains(pcl.getParamCesLegId()))
                .map(pcl->this.mapToUpdateCesLegReq(pcl, aff, false)).collect(Collectors.toList());

        pclReps.addAll(noneAcceptedPclReps);
        UpdateCedLegRepartitionReq updateCedLegRepartitionReq =  repMapper.mapToUpdateCedLegRepartitionReq(aff, repCed, pclReps);
        updateCedLegRepartitionReq.setBesoinFac(comptaService.calculateRestARepartir(affId));
        return updateCedLegRepartitionReq;
    }

    private UpdateCesLegReq mapToUpdateCesLegReq(ParamCessionLegaleListResp pcl, Affaire aff, boolean accepted)
    {
        BigDecimal capitalInit = aff.getAffCapitalInitial();
        BigDecimal repCapital = capitalInit == null ? ZERO : capitalInit.multiply(pcl.getParamCesLegTaux()).divide(new BigDecimal(100), 2,RoundingMode.HALF_UP);
        UpdateCesLegReq rep = new UpdateCesLegReq();
        rep.setRepTaux(pcl.getParamCesLegTaux());
        rep.setRepCapital(repCapital);
        rep.setRepId(repRepo.getRepIdByAffIdAndPclId(aff.getAffId(), pcl.getParamCesLegId()));
        rep.setAffId(aff.getAffId());
        rep.setAccepte(accepted);
        rep.setParamCesLegalId(pcl.getParamCesLegId());
        rep.setParamCesLegLibelle(pcl.getParamCesLegLibelle());
        return rep;
    }
}