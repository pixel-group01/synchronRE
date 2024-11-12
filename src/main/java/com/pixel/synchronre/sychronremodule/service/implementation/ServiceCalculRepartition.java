package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.PlacementDocUploader;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.UN;

@Service
@RequiredArgsConstructor
public class ServiceCalculRepartition implements IserviceCalculRepartition
{
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
    private final IServiceCalculsComptables comptaService;
    private final RepartitionMapper repMapper;
    private final ObjectCopier<Repartition> repCopier;
    private final ObjectCopier<Affaire>  affCopier;
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
    private final IServiceInterlocuteur intService;

    private final ParamCessionLegaleRepository pclRepoo;

    @Override @Transactional
    public CalculationRepartitionRespDto saveRep(CalculationRepartitionRespDto dto) throws UnknownHostException {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()-> new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        BigDecimal loadedPartCedante = affaire.getPartCedante();
        BigDecimal dtoPartCedante = dto.getMtPartCedante();
        if(dtoPartCedante == null) throw  new AppException("Veuillez saisir le montant de la part cédante");
        if( (loadedPartCedante != null && loadedPartCedante.compareTo(dtoPartCedante) != 0) || loadedPartCedante == null)
        {
            affaire.setPartCedante(dtoPartCedante);
            logService.logg(AffaireActions.CHANGE_PART_CEDANTE, oldAffaire, affaire, SynchronReTables.AFFAIRE);
        }
        List<UpdateCesLegReq> pclPfs = dto.getParamCesLegsPremierFranc();
        SimpleRepDto conservationDto = new SimpleRepDto(dto.getConservationCapital(), dto.getConservationTaux(), dto.getConservationRepId(), dto.getConservationPrime(), dto.getAffId());
        SimpleRepDto facobDto = new SimpleRepDto(dto.getFacobCapital(), dto.getFacobTaux(), dto.getFacobRepId(), dto.getFacobPrime(), dto.getAffId());
        SimpleRepDto xlDto = new SimpleRepDto(dto.getXlCapital(), dto.getXlTaux(), dto.getXlRepId(), dto.getXlPrime(), dto.getAffId());
        List<UpdateCesLegReq> pclSimples = dto.getParamCesLegs();

        Type repResCourtType = typeRepo.findByUniqueCode("REP_RES_COURT").orElseThrow(()->new AppException("Type introuvable : REP_RES_COURT"));
        if(pclSimples != null && !pclSimples.isEmpty()) //On retire de la liste la réserve courtier si elle s'y trouve
        {
            pclSimples = pclSimples.stream().filter(pcl->!pcl.getParamCesLegLibelle().toUpperCase().equals(repResCourtType.getName().toUpperCase())).toList();
        }

        pclPfs = this.savePclReps(pclPfs);
        conservationDto = this.saveTraite(conservationDto, "REP_CONSERVATION");
        facobDto = this.saveTraite(facobDto, "REP_FACOB");
        facobDto = this.saveTraite(xlDto, "REP_XL");


        pclSimples = this.savePclReps(pclSimples);
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.ENREGISTRER_REPARTITION, dto.getAffId(), EN_COURS_DE_REPARTITION.staCode, null));

        dto.setParamCesLegsPremierFranc(pclPfs);
        dto.setConservationRepId(conservationDto.getRepId());
        dto.setFacobRepId(facobDto.getRepId());
        dto.setXlRepId(xlDto.getRepId());
        dto.setParamCesLegs(pclSimples);
        return dto;
    }

    @Override
    public CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital, BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Long repIdToExclude)
    {
        if(capital.compareTo(ZERO)<0) throw new AppException("Le capital doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId, repIdToExclude);
        if(capital.subtract(restARepartir).compareTo(UN)>0) throw new AppException("Le montant du capital ne doit pas exéder le besoin fac");
        restARepartir = restARepartir == null ? ZERO : restARepartir;
        BigDecimal smplCi = aff.getFacSmpLci() == null ? ZERO : aff.getFacSmpLci();
        if(restARepartir.compareTo(ZERO) < 0 || smplCi.compareTo(ZERO) <= 0) return new CalculRepartitionResp(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(CENT).divide(smplCi, 2, RoundingMode.HALF_UP));
        //resp.setTauxBesoinFac(capital.multiply(CENT).divide(restARepartir, 20, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));

        return calculatePrimeAndCms(tauxCmsRea, tauxCmsCourtage, aff, resp);
    }

    private CalculRepartitionResp calculatePrimeAndCms(BigDecimal tauxCmsRea, BigDecimal tauxCmsCourtage, Affaire aff, CalculRepartitionResp resp)
    {
        BigDecimal facPrime = aff.getFacPrime();
        if(facPrime == null || tauxCmsRea == null || tauxCmsCourtage == null) return resp;

        BigDecimal cmsRea = facPrime.multiply(tauxCmsRea).divide(CENT, 0, RoundingMode.HALF_UP);
        BigDecimal cmsCourtage = facPrime.multiply(tauxCmsCourtage).divide(CENT, 0, RoundingMode.HALF_UP);
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
    public BigDecimal calculateSommeCapitauxCessionsLegalesPremierFranc(Long affId)
    {
        BigDecimal mtCapitauxClPf = repRepo.calculateSommeCapitauxCessionsLegalesPF(affId);
        return mtCapitauxClPf == null ? ZERO : mtCapitauxClPf;
    }

    @Override
    public BigDecimal calculateCapitauxNetCL(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal partCedante = affaire.getPartCedante();
        BigDecimal smpLci = affaire.getFacSmpLci() == null ? ZERO : affaire.getFacSmpLci();
        partCedante = partCedante == null ? smpLci : partCedante;
        BigDecimal mtCapitauxClPf = this.calculateSommeCapitauxCessionsLegalesPremierFranc(affId);
        return  partCedante.compareTo(ZERO) == 0 ? ZERO : partCedante.subtract(mtCapitauxClPf);
    }

    @Override
    public BigDecimal calculateSommeCapitauxTraites(Long affId)
    {
        BigDecimal sommeCapitauxTraites = repRepo.calculateSommeCapitauxTraites(affId);
        return sommeCapitauxTraites == null ? ZERO : sommeCapitauxTraites;
    }

    @Override
    public BigDecimal calculateBesoinFacBrut(Long affId)
    {
        BigDecimal capitauxNetCl = this.calculateCapitauxNetCL(affId);
        return capitauxNetCl.subtract(this.calculateSommeCapitauxTraites(affId));
    }

    @Override
    public BigDecimal calculateTauxBesoinFacBrut(Long affId)
    {
        BigDecimal besoinFacBrut = this.calculateBesoinFacBrut(affId);
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smpLci = affaire.getFacSmpLci() == null ? ZERO : affaire.getFacSmpLci();
        if(smpLci == ZERO) throw new AppException("La LCI de l'affaire est nulle");
        return besoinFacBrut.divide(smpLci, 100, RoundingMode.HALF_UP).multiply(CENT);
    }

    @Override
    public BigDecimal calculateSommeCapitauxCLSimple(Long affId)
    {
        return repRepo.calculateSommeCapitauxCLSimple(affId);
    }

    @Override
    public BigDecimal calculateBesoinFacNetCL(Long affId)
    {
        BigDecimal besoinFacBrut = this.calculateBesoinFacBrut(affId);
        BigDecimal sommeCapitauxClSimple = this.calculateSommeCapitauxCLSimple(affId);
        return besoinFacBrut.subtract(sommeCapitauxClSimple);
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

    private List<UpdateCesLegReq> savePclReps(List<UpdateCesLegReq> pclPfs)
    {
        pclPfs = pclPfs.stream().map(this::savePclRep).collect(Collectors.toList());
        return pclPfs;
    }

    private SimpleRepDto saveTraite(SimpleRepDto dto, String typeTraite)
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smplCi = aff.getFacSmpLci();
        //BigDecimal mtPartCedante = aff.getPartCedante();
        boolean repCapitalIsNull = dto.getRepCapital() == null || dto.getRepCapital().compareTo(ZERO) == 0;
        dto.setRepCapital(repCapitalIsNull ? ZERO : dto.getRepCapital());
        BigDecimal repCapital = dto.getRepCapital();
        if(smplCi == null || smplCi.compareTo(ZERO) == 0) throw new AppException("impossible de faire un placement. La LCI de l'affaire est nulle");
        //mtPartCedante = mtPartCedante == null || mtPartCedante.compareTo(ZERO) == 0 ? smplCi : mtPartCedante;
        BigDecimal repTaux = repCapital.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal repPrime = aff.getFacPrime() == null  ? ZERO : aff.getFacPrime().multiply(repTaux);

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
        traiteRep.setRepCapital(repCapital);
        traiteRep.setRepTaux(repTaux);
        traiteRep.setRepCapitalLettre(ConvertMontant.numberToLetter(repCapital));

        traiteRep.setRepPrime(repPrime);
        traiteRep = repRepo.save(traiteRep);
        dto.setRepId(dto.getRepId());
        logService.logg(action, oldRep, traiteRep, SynchronReTables.REPARTITION);
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

        Affaire aff = affRepo.findById(pclDto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BigDecimal smplCi = aff.getFacSmpLci();
        if(smplCi == null || smplCi.compareTo(ZERO) == 0) throw new AppException("impossible de faire un placement. La LCI de l'affaire est nulle");
        BigDecimal mtPartCedante = aff.getPartCedante() == null || aff.getPartCedante().compareTo(ZERO) == 0 ? smplCi : aff.getPartCedante();

        BigDecimal tauxPartCedante = mtPartCedante.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal tauxBesoinFacBrut = this.calculateTauxBesoinFacBrut(aff.getAffId());

        ParamCessionLegale pcl = pclRepo.findById(pclDto.getParamCesLegalId()).orElseThrow(()->new AppException("Paramètre de cession légale introuvable"));
        BigDecimal pclOriginalTaux = pcl.getParamCesLegTaux();
        boolean isPclPF = !pclRepo.pclIsSimple(pclDto.getParamCesLegalId());
        BigDecimal pclNewtaux =  pclOriginalTaux.multiply(isPclPF ? tauxPartCedante : tauxBesoinFacBrut).divide(CENT, 100, RoundingMode.HALF_UP);
        BigDecimal pclCapital = pclNewtaux.multiply(smplCi).divide(CENT, 100, RoundingMode.HALF_UP);

        BigDecimal pclPrime =  aff.getFacPrime() == null || aff.getFacPrime().compareTo(ZERO) == 0 ? ZERO : pclNewtaux.multiply(aff.getFacPrime()).divide(CENT, 100, RoundingMode.HALF_UP);

        pclRep.setRepStatut(pclDto.isAccepte());
        pclRep.setRepCapital(pclCapital);
        pclRep.setRepTaux(pclNewtaux);
        pclRep.setRepCapitalLettre(ConvertMontant.numberToLetter(pclDto.getRepCapital()));

        pclRep.setRepPrime(pclPrime);
        pclRep = repRepo.save(pclRep);
        pclDto.setRepId(pclRep.getRepId());
        logService.logg(action, oldRep, pclRep, SynchronReTables.REPARTITION);
        return pclDto;
    }

    @Override
    public CalculationRepartitionRespDto calculateRepByAffId(Long affId)
    {
        boolean modeUpdate = repRepo.repartitionModeIsUpdate(affId);
        if(affId == null) throw new AppException("Veuillez fournir l'ID de l'affaire");
        Affaire aff = affRepo.findById(affId).orElse(null);
        CalculationRepartitionReqDto dto = new CalculationRepartitionReqDto();
        BigDecimal partCedante = aff.getPartCedante() != null ? aff.getPartCedante() : aff.getFacSmpLci();
        dto.setAffId(affId);
        dto.setPartCedante(partCedante);
        dto.setModeUpdate(modeUpdate);
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
            BigDecimal pclNewtaux =  pclOriginalTaux.multiply(tauxPartCedante).divide(CENT, 100, RoundingMode.HALF_UP);
            BigDecimal pclCapital = pclNewtaux.multiply(smplCi).divide(CENT, 100, RoundingMode.HALF_UP);
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

        BigDecimal conservationTaux = conservationCapital==null ? null : conservationCapital.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal facobTaux = facobCapital == null ? null : facobCapital.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);
        BigDecimal xlTaux =  xlCapital == null ? null : xlCapital.multiply(CENT).divide(smplCi, 100, RoundingMode.HALF_UP);

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

        Repartition reserveCourtier = repRepo.findReserveCourtierByAffId(dto.getAffId()); //On récupère la réserve courtier

        BigDecimal besoinFacNetCL = bruteBesoinFac.subtract(mtClSimple).subtract(reserveCourtier != null && reserveCourtier.getRepCapital()!= null ? reserveCourtier.getRepCapital() : ZERO);
        BigDecimal besoinFacNetCLTaux = bruteBesoinFacTaux.subtract(tauxClSimple).subtract(reserveCourtier != null && reserveCourtier.getRepTaux()!= null ? reserveCourtier.getRepTaux() : ZERO);
        BigDecimal besoinFacNetCLPrime = bruteBesoinFacPrime.subtract(primeClSimple).subtract(reserveCourtier != null && reserveCourtier.getRepPrime()!= null ? reserveCourtier.getRepPrime() : ZERO);

        BigDecimal mtPlacements = repRepo.calculateMtTotalPlacementbyAffaire(dto.getAffId());
        mtPlacements = mtPlacements == null ? ZERO : mtPlacements;
        if(mtPlacements.subtract(besoinFacNetCL).compareTo(UN)>0) throw new AppException("Le besoin  fac net de cessions légales (" + decimalFormat.format(besoinFacNetCL) + devise +") doit être supérieur au montant des capitaux déjà placés (" + decimalFormat.format(mtPlacements)+ " " + devise +")");
        BigDecimal besoinFac = besoinFacNetCL.subtract(mtPlacements);

        CalculationRepartitionRespDto resp = new CalculationRepartitionRespDto();
        resp.setModeUpdate(dto.isModeUpdate());
        resp.setAffId(dto.getAffId());
        resp.setMtPartCedante(mtPartCedante.setScale(0, RoundingMode.HALF_UP));
        resp.setTauxPartCedante(tauxPartCedante.setScale(2, RoundingMode.HALF_UP));
        resp.setPrimePartCedante(primePartCedante.setScale(0, RoundingMode.HALF_UP));
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

        if(reserveCourtier != null)
        {
            Type repResCourtType = typeRepo.findByUniqueCode("REP_RES_COURT").orElseThrow(()->new AppException("Type introuvable : REP_RES_COURT"));
            paramCesLegs.add(new UpdateCesLegReq(reserveCourtier.getRepCapital(), reserveCourtier.getRepTaux().setScale(2, RoundingMode.HALF_UP), reserveCourtier.getRepPrime() == null ? ZERO : reserveCourtier.getRepPrime().setScale(0, RoundingMode.HALF_UP), false, repResCourtType.getName()));
        }
        return resp;
    }
}
