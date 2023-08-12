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
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@Service
@RequiredArgsConstructor
public class ServiceRepartitionImpl implements IserviceRepartition
{
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
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
    private final DecimalFormat decimalFormat;

    private final ParamCessionLegaleRepository pclRepoo;

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
        resp.setTaux(capital.multiply(CENT).divide(capitalInit, 20, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 20, RoundingMode.HALF_UP));
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
        
        BigDecimal capital = capitalInit.multiply(taux).divide(CENT, 20, RoundingMode.HALF_UP);
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le taux de repartition ne doit pas exéder " + CENT.multiply(restARepartir).divide(capitalInit, 2, RoundingMode.HALF_UP) + "%");

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(taux);
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 20, RoundingMode.HALF_UP));
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

        BigDecimal capital = tauxBesoin.multiply(restARepartir).divide(CENT, 20, RoundingMode.HALF_UP);
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(CENT).divide(capitalInit, 20, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 20, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));

        this.calculatePrimeAndCms(tauxCmsRea, tauxCmsCourtage, aff, resp);
        return resp;
    }

    @Override
    public CalculationRepartitionRespDto saveRep(CalculationRepartitionRespDto dto)
    {
        List<UpdateCesLegReq> pclPfs = dto.getParamCesLegsPremierFranc();
        SimpleRepDto conservationDto = new SimpleRepDto(dto.getConservationCapital(), dto.getConservationTaux(), dto.getConservationRepId(), dto.getConservationPrime(), dto.getAffId());
        SimpleRepDto facobDto = new SimpleRepDto(dto.getFacobCapital(), dto.getFacobTaux(), dto.getFacobRepId(), dto.getFacobPrime(), dto.getAffId());
        SimpleRepDto xlDto = new SimpleRepDto(dto.getXlCapital(), dto.getXlTaux(), dto.getConservationRepId(), dto.getXlPrime(), dto.getAffId());
        List<UpdateCesLegReq> pclSimples = dto.getParamCesLegs();

        pclPfs = this.savePclReps(pclPfs);
        conservationDto = this.saveTraite(conservationDto, "REP_CONSERVATION");
        facobDto = this.saveTraite(facobDto, "REP_FACOB");
        facobDto = this.saveTraite(xlDto, "REP_XL");
        pclSimples = this.savePclReps(pclSimples);

        dto.setParamCesLegsPremierFranc(pclPfs);
        dto.setConservationRepId(conservationDto.getRepId());
        dto.setFacobRepId(facobDto.getRepId());
        dto.setXlRepId(xlDto.getRepId());
        dto.setParamCesLegs(pclSimples);
        return dto;
    }
    private List<UpdateCesLegReq> savePclReps(List<UpdateCesLegReq> pclPfs)
    {
        pclPfs = pclPfs.stream().map(this::savePclRep).collect(Collectors.toList());
        return pclPfs;
    }

    private SimpleRepDto saveTraite(SimpleRepDto dto, String typeTraite)
    {
        if(dto.getRepId() == null && (dto.getRepCapital() == null || dto.getRepCapital().compareTo(ZERO) == 0))
            return dto;
        Long repId = dto.getRepId();
        List<Repartition> traiteReps = repRepo.findByAffaireAndTypeRep(dto.getAffId(), typeTraite);

        if(traiteReps.size() > 1) throw new AppException("Plusieurs traités du même type sur la même affaire");

        Repartition traiteRep = traiteReps.isEmpty() ? null : traiteReps.get(0);

        String action="";
        Repartition oldRep = null;
        if(traiteRep == null)
        {
            Type repCesLegType = typeRepo.findByUniqueCode(typeTraite).orElseThrow(()->new AppException("Type introuvable : " + typeTraite));

            traiteRep = new Repartition();
            traiteRep.setAffaire(new Affaire(dto.getAffId()));
            traiteRep.setType(repCesLegType);
            action = switch (typeTraite)
                    {
                        case "REP_CONSERVATION"-> RepartitionActions.CREATE_CONSERVATION_REPARTITION;
                        case "REP_FACOB"->RepartitionActions.CREATE_FACOB_REPARTITION;
                        case "REP_XL"->RepartitionActions.CREATE_XL_REPARTITION;
                        default -> "";
                    };
        }
        else
        {
            //traiteRep = repRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Repartition introuvable"));
            oldRep = repCopier.copy(traiteRep);
            action = switch (typeTraite)
                    {
                        case "REP_CONSERVATION"-> RepartitionActions.UPDATE_CONSERVATION_REPARTITION;
                        case "REP_FACOB"->RepartitionActions.UPDATE_FACOB_REPARTITION;
                        case "REP_XL"->RepartitionActions.UPDATE_XL_REPARTITION;
                        default -> "";
                    };
        }
        traiteRep.setRepStatut(true);
        traiteRep.setRepCapital(dto.getRepCapital());
        traiteRep.setRepTaux(dto.getRepTaux());
        traiteRep.setRepCapitalLettre(ConvertMontant.numberToLetter(dto.getRepCapital()));

        traiteRep.setRepPrime(dto.getRepPrime());
        traiteRep = repRepo.save(traiteRep);
        dto.setRepId(dto.getRepId());
        try {
            logService.logg(action, oldRep, traiteRep, SynchronReTables.REPARTITION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return dto;
    }

    private UpdateCesLegReq savePclRep(UpdateCesLegReq pclDto)
    {
        Long pclRepId = pclDto.getRepId();
        Repartition pclRep = repRepo.findByAffIdAndPclId(pclDto.getAffId(), pclDto.getParamCesLegalId());
        //Repartition pclRep;
        String action="";
        Repartition oldRep = null;
        if(pclRep == null)
        {
            if(pclDto.isAccepte())
            {
                pclRep = new Repartition();
                pclRep.setAffaire(new Affaire(pclDto.getAffId()));
                pclRep.setParamCessionLegale(new ParamCessionLegale(pclDto.getParamCesLegalId()));
                Type repCesLegType = typeRepo.findByUniqueCode("REP_CES_LEG").orElseThrow(()->new AppException("Type introuvable : REP_CES_LEG"));
                pclRep.setType(repCesLegType);
                action = RepartitionActions.CREATE_CES_LEG_REPARTITION;
            }
            else
            {
                return pclDto;
            }
        }
        else
        {
            //pclRep = repRepo.findById(pclDto.getRepId()).orElseThrow(()->new AppException("Repartition introuvable"));
            oldRep = repCopier.copy(pclRep);
            action = RepartitionActions.UPDATE_CES_LEG_REPARTITION;
        }
        pclRep.setRepStatut(pclDto.isAccepte());
        pclRep.setRepCapital(pclDto.getRepCapital());
        pclRep.setRepTaux(pclDto.getRepTaux());
        pclRep.setRepCapitalLettre(ConvertMontant.numberToLetter(pclDto.getRepCapital()));

        pclRep.setRepPrime(pclDto.getPrime());
        pclRep = repRepo.save(pclRep);
        pclDto.setRepId(pclRep.getRepId());
        try {
            logService.logg(action, oldRep, pclRep, SynchronReTables.REPARTITION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return pclDto;
    }


    @Override
    public CalculationRepartitionRespDto calculateRepByAffId(Long affId, boolean modeUpdate)
    {
        if(affId == null) throw new AppException("Veuillez fournir l'ID de l'affaire");
        Affaire aff = affRepo.findById(affId).orElse(null);
        CalculationRepartitionReqDto dto = new CalculationRepartitionReqDto();
        BigDecimal partCedante = aff.getPartCedante() != null ? aff.getPartCedante() : aff.getFacSmpLci();
        dto.setAffId(affId);
        dto.setPartCedante(partCedante);
        if(modeUpdate)
        {
            List<Repartition> conservationsReps = repRepo.findByAffaireAndTypeRep(affId, "REP_CONSERVATION");
            List<Repartition> facobReps = repRepo.findByAffaireAndTypeRep(affId, "REP_FACOB");
            List<Repartition> xlReps = repRepo.findByAffaireAndTypeRep(affId, "REP_XL");

            if(conservationsReps.size() > 1) throw new AppException("Plusieurs traités de type conservation sur la même affaire");
            if(facobReps.size() > 1) throw new AppException("Plusieurs traités de type facob sur la même affaire");
            if(xlReps.size() > 1) throw new AppException("Plusieurs traités xl sur la même affaire");

            Repartition conservationsRep = conservationsReps.isEmpty() ? null : conservationsReps.get(0);
            Repartition facobRep = facobReps.isEmpty() ? null : facobReps.get(0);
            Repartition xlRep = xlReps.isEmpty() ? null : xlReps.get(0);

            dto.setConservationRepId(conservationsRep == null ? null : conservationsRep.getRepId());
            dto.setConservationCapital(conservationsRep == null ? null : conservationsRep.getRepCapital());

            dto.setFacobRepId(facobRep == null ? null : facobRep.getRepId());
            dto.setFacobCapital(facobRep == null ? null : facobRep.getRepCapital());

            dto.setXlRepId(xlRep == null ? null : xlRep.getRepId());
            dto.setXlCapital(xlRep == null ? null : xlRep.getRepCapital());
            List<Long> effectivePclIdsOnAffaire =  pclRepo.findPclIdsOnAffaire(affId);
            dto.setPclIds(effectivePclIdsOnAffaire);
        }
        if(!modeUpdate)
        {
            List<Long> possiblePclIds = pclRepo.findPossiblePclByAffId(affId).stream().map(ParamCessionLegaleListResp::getParamCesLegId).collect(Collectors.toList());
            dto.setPclIds(possiblePclIds);
        }
        return calculateRepByDto(dto);
    }

    @Override
    public CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionRespDto dto)
    {
        CalculationRepartitionReqDto reqDto = repMapper.mapToCalculationRepartitionReqDto(dto);
        return this.calculateRepByDto(reqDto);
    }

    @Override
    public CalculationRepartitionRespDto calculateRepByDto(CalculationRepartitionReqDto dto)
    {
        if(dto == null) throw new AppException("Veuillez fournir les données d'entrée");
        if(dto.getAffId() == null) throw new AppException("Veuillez fournir l'ID de l'affaire");
        Affaire aff = affRepo.findById(dto.getAffId()).orElse(null);
        String devise = aff.getDevise().getDevCode();
        if(aff == null) return null;
        BigDecimal mtPartCedante = dto.getPartCedante() == null ? ZERO : dto.getPartCedante();

        BigDecimal smplCi = aff.getFacSmpLci() == null ? ZERO : aff.getFacSmpLci();
        if(smplCi.compareTo(ZERO) == 0) throw new AppException("La LCI de l'affaire est nulle");
        if(mtPartCedante.compareTo(smplCi)>0) throw new AppException("Le montant de la part cédante ne peut excéder celui de la LCI (" + decimalFormat.format(smplCi) + devise +")");
        BigDecimal tauxPartCedante = mtPartCedante.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal prime100 = aff.getFacPrime() == null ? ZERO : aff.getFacPrime();
        BigDecimal primePartCedante = tauxPartCedante.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);
        BigDecimal conservationCapital = dto.getConservationCapital() == null ? ZERO : dto.getConservationCapital();
        BigDecimal facobCapital = dto.getFacobCapital() == null ? ZERO : dto.getFacobCapital();
        BigDecimal xlCapital = dto.getXlCapital() == null ? ZERO : dto.getXlCapital();

        //String paysCode = affRepo.getPaysCodebyAffId(dto.getAffId());
        List<Long> possiblePclIds = pclRepo.findPossiblePclByAffId(dto.getAffId()).stream().map(ParamCessionLegaleListResp::getParamCesLegId).collect(Collectors.toList());
        List<Long> acceptedPclIds = dto.getPclIds() == null ? new ArrayList<>() : dto.getPclIds();
        List<Long> pclPflIds = possiblePclIds.stream().filter(pclId->pclRepo.pclIsPf(pclId)).collect(Collectors.toList());
        List<UpdateCesLegReq> paramCesLegsPremierFranc =
                pclPflIds.stream().map(pclId->
        {
            ParamCessionLegale pcl = pclRepo.findById(pclId).orElseThrow(()->new AppException("Paramètre de cession légale introuvable"));
            BigDecimal pclOriginalTaux = pcl.getParamCesLegTaux();
            BigDecimal pclCapital = pclOriginalTaux.multiply(mtPartCedante).divide(CENT, 100, RoundingMode.HALF_UP);
            BigDecimal pclNewtaux =  pclOriginalTaux.multiply(tauxPartCedante).divide(CENT, 100, RoundingMode.HALF_UP);
            BigDecimal pclPrime =  pclNewtaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);


            UpdateCesLegReq updateCesLegReq = new UpdateCesLegReq();
            Repartition rep = repRepo.findByAffIdAndPclId(dto.getAffId(), pclId);
            Long repId = rep == null ? null : rep.getRepId();


            updateCesLegReq.setAffId(dto.getAffId());
            updateCesLegReq.setParamCesLegLibelle(pcl.getParamCesLegLibelle() + " "+ pclOriginalTaux.doubleValue() + "%");
            updateCesLegReq.setRepCapital(pclCapital.setScale(0,RoundingMode.HALF_UP));
            updateCesLegReq.setRepId(repId);
            updateCesLegReq.setRepTaux(pclNewtaux.setScale(2,RoundingMode.HALF_UP));
            updateCesLegReq.setPrime(pclPrime.setScale(0,RoundingMode.HALF_UP));
            updateCesLegReq.setParamCesLegalId(pcl.getParamCesLegId());
            updateCesLegReq.setAccepte(acceptedPclIds.contains(pclId));
            return updateCesLegReq;
        }).collect(Collectors.toList());

        BigDecimal mtClPf = paramCesLegsPremierFranc.stream().filter(UpdateCesLegReq::isAccepte).map(UpdateCesLegReq::getRepCapital).reduce(ZERO, BigDecimal::add);
        BigDecimal capitauxNetCl = mtPartCedante.subtract(mtClPf);

        BigDecimal conservationTaux = conservationCapital==null ? null : conservationCapital.multiply(CENT).divide(mtPartCedante, 100, RoundingMode.HALF_UP);
        BigDecimal facobTaux = facobCapital == null ? null : facobCapital.multiply(CENT).divide(mtPartCedante, 100, RoundingMode.HALF_UP);
        BigDecimal xlTaux =  xlCapital == null ? null : xlCapital.multiply(CENT).divide(mtPartCedante, 100, RoundingMode.HALF_UP);

        BigDecimal conservationPrime = conservationTaux == null ? null : conservationTaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);
        BigDecimal facobPrime = facobTaux == null ? null : facobTaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);
        BigDecimal xlPrime = xlCapital == null ? null : xlTaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);


        Repartition repConservation = dto.getConservationRepId() == null ? null : repRepo.findById(dto.getConservationRepId()).orElse(null);
        Repartition repFacob = dto.getFacobRepId() == null ? null : repRepo.findById(dto.getFacobRepId()).orElse(null);
        Repartition repXl = dto.getXlRepId() == null ? null : repRepo.findById(dto.getXlRepId()).orElse(null);

        //Repartition repConservation = repConservations.isEmpty() ? null : repConservations.get(0);
        //Repartition repFacob = repFacobs.isEmpty() ? null :  repFacobs.get(0);
        //Repartition repXl = repXls.isEmpty() ? null :  repXls.get(0);

        BigDecimal capitalTraites = conservationCapital.add(facobCapital).add(xlCapital);
        if(capitalTraites.compareTo(capitauxNetCl)>0) throw new AppException("La somme des montants de traité ne peut exéder les capitaux nets de cessions légales");
        BigDecimal bruteBesoinFac = capitauxNetCl.subtract(capitalTraites);
        BigDecimal bruteBesoinFacTaux = bruteBesoinFac.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal bruteBesoinFacPrime = bruteBesoinFacTaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);

        List<Long> pclSimpleIds = possiblePclIds.stream().filter(pclId->pclRepo.pclIsSimple(pclId)).collect(Collectors.toList());

        List<UpdateCesLegReq> paramCesLegs =
                pclSimpleIds.stream().map(pclId->
                {
                    ParamCessionLegale pcl = pclRepo.findById(pclId).orElseThrow(()->new AppException("Paramètre de cession légale introuvable"));
                    BigDecimal pclOriginalTaux = pcl.getParamCesLegTaux();

                    BigDecimal pclNewtaux =  pclOriginalTaux.multiply(bruteBesoinFacTaux).divide(CENT, 100, RoundingMode.HALF_UP);
                    BigDecimal pclCapital = pclNewtaux.multiply(smplCi).divide(CENT, 100, RoundingMode.HALF_UP);
                    BigDecimal pclPrime = pclNewtaux.multiply(prime100).divide(CENT, 100, RoundingMode.HALF_UP);


                    UpdateCesLegReq updateCesLegReq = new UpdateCesLegReq();
                    Repartition rep = repRepo.findByAffIdAndPclId(dto.getAffId(), pclId);
                    Long repId = rep == null ? null : rep.getRepId();


                    updateCesLegReq.setAffId(dto.getAffId());
                    updateCesLegReq.setParamCesLegLibelle(pcl.getParamCesLegLibelle()  + " "+  pclOriginalTaux.doubleValue() + "%");
                    updateCesLegReq.setRepCapital(pclCapital.setScale(0, RoundingMode.HALF_UP));
                    updateCesLegReq.setRepId(repId);
                    updateCesLegReq.setRepTaux(pclNewtaux.setScale(2, RoundingMode.HALF_UP));
                    updateCesLegReq.setPrime(pclPrime.setScale(0, RoundingMode.HALF_UP));
                    updateCesLegReq.setParamCesLegalId(pcl.getParamCesLegId());
                    updateCesLegReq.setAccepte(acceptedPclIds.contains(pclId));
                    return updateCesLegReq;
                }).collect(Collectors.toList());

        BigDecimal mtClSimple = paramCesLegs.stream().filter(UpdateCesLegReq::isAccepte).map(UpdateCesLegReq::getRepCapital).reduce(ZERO, BigDecimal::add);
        if(mtClSimple.compareTo(bruteBesoinFac)>0) throw new AppException("Veuillez réajuster les montants de traité ou retirer certaines cessions légales");

        BigDecimal tauxClSimple = paramCesLegs.stream().filter(UpdateCesLegReq::isAccepte).map(UpdateCesLegReq::getRepTaux).reduce(ZERO, BigDecimal::add);
        BigDecimal primeClSimple = paramCesLegs.stream().filter(UpdateCesLegReq::isAccepte).map(UpdateCesLegReq::getPrime).reduce(ZERO, BigDecimal::add);

        BigDecimal besoinFacNetCL = bruteBesoinFac.subtract(mtClSimple);
        BigDecimal besoinFacNetCLTaux = bruteBesoinFacTaux.subtract(tauxClSimple);
        BigDecimal besoinFacNetCLPrime = bruteBesoinFacPrime.subtract(primeClSimple);

        BigDecimal mtPlacements = repRepo.calculateMtTotalPlacementbyAffaire(dto.getAffId());
        mtPlacements = mtPlacements == null ? ZERO : mtPlacements;
        if(mtPlacements.compareTo(besoinFacNetCL)>0) throw new AppException("Le besoin  fac net de cessions légales (" + decimalFormat.format(besoinFacNetCL) + devise +") doit être supérieur au montant des capitaux déjà placé (" + decimalFormat.format(mtPlacements)+ " " + devise +")");
        BigDecimal besoinFac = besoinFacNetCL.subtract(mtPlacements);

        CalculationRepartitionRespDto resp = new CalculationRepartitionRespDto();
        resp.setAffId(dto.getAffId());
        resp.setMtPartCedante(mtPartCedante.setScale(0, RoundingMode.HALF_UP));
        resp.setTauxPartCedante(tauxPartCedante.setScale(2, RoundingMode.HALF_UP));
        resp.setPrimePartCedante(primePartCedante.setScale(2, RoundingMode.HALF_UP));
        resp.setParamCesLegsPremierFranc(paramCesLegsPremierFranc);
        resp.setCapitauxNetCL(capitauxNetCl.setScale(2, RoundingMode.HALF_UP));

        resp.setConservationCapital(conservationCapital.setScale(0, RoundingMode.HALF_UP));
        resp.setConservationTaux(conservationTaux.setScale(2, RoundingMode.HALF_UP));
        resp.setConservationPrime(conservationPrime.setScale(0, RoundingMode.HALF_UP));
        resp.setConservationRepId(repConservation == null ? null : repConservation.getRepId());

        resp.setFacobCapital(facobCapital.setScale(0, RoundingMode.HALF_UP));
        resp.setFacobTaux(facobTaux.setScale(2, RoundingMode.HALF_UP));
        resp.setFacobPrime(facobPrime.setScale(0, RoundingMode.HALF_UP));
        resp.setFacobRepId(repFacob == null ? null : repFacob.getRepId());

        resp.setXlCapital(xlCapital.setScale(0, RoundingMode.HALF_UP));
        resp.setXlTaux(xlTaux.setScale(2, RoundingMode.HALF_UP));
        resp.setXlPrime(xlPrime.setScale(0, RoundingMode.HALF_UP));
        resp.setXlRepId(repXl == null ? null : repXl.getRepId());

        resp.setBruteBesoinFac(bruteBesoinFac.setScale(0, RoundingMode.HALF_UP));
        resp.setBruteBesoinFacTaux(bruteBesoinFacTaux.setScale(2, RoundingMode.HALF_UP));
        resp.setBruteBesoinFacPrime(bruteBesoinFacPrime.setScale(0, RoundingMode.HALF_UP));

        resp.setParamCesLegs(paramCesLegs);

        resp.setBesoinFacNetCL(besoinFacNetCL.setScale(0, RoundingMode.HALF_UP));
        resp.setBesoinFacNetCLTaux(besoinFacNetCLTaux.setScale(2, RoundingMode.HALF_UP));
        resp.setBesoinFacNetCLPrime(besoinFacNetCLPrime.setScale(0, RoundingMode.HALF_UP));
        resp.setBesoinFac(besoinFac.setScale(0, RoundingMode.HALF_UP));
        /*

         */

        return resp;
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

        List<UpdateCesLegReq> noneAcceptedPclReps = pclRepo.findPossiblePclByAffId(affId).stream()
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
        BigDecimal repCapital = capitalInit == null ? ZERO : capitalInit.multiply(pcl.getParamCesLegTaux()).divide(new BigDecimal(100), 20,RoundingMode.HALF_UP);
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
