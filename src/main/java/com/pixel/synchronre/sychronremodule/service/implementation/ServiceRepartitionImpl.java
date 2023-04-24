package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

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
    private BigDecimal zero = new BigDecimal(0);
    private BigDecimal cent = new BigDecimal(100);
    private final ParamCessionLegaleRepository pclRepo;

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
        boolean existByAffaireAndPcl = repRepo.existsByIdAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId());
        if(repRepo.existsByIdAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId()))
        {
            oldRep = new Repartition();
            rep = repRepo.findByIdAffIdAndPclId(dto.getAffId(), dto.getParamCesLegalId());
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
         mvtService.createMvtSuivant(new MvtSuivantReq(StatutEnum.EN_COURS_DE_REPARTITION.staCode, dto.getAffId()));
         return repartitionDetailsResp;
    }

    @Override //Placemement
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException
    {
        boolean firstPlacement = !repRepo.affaireHasPlacement(dto.getAffId());
        boolean existsByAffaireAndTypeRep = repRepo.existsByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
        Repartition rep;
        Repartition oldRep = null;
        if(existsByAffaireAndTypeRep)
        {
            rep = repRepo.findByAffaireAndTypeRepAndCesId(dto.getAffId(), "REP_PLA", dto.getCesId());
            oldRep = repCopier.copy(rep);
            rep.setRepCapital(dto.getRepCapital());
            rep.setRepTaux(dto.getRepTaux());
        }
        else
        {
            rep = repMapper.mapToPlaRepartition(dto);
        }
        rep = repRepo.save(rep);
        logService.logg(existsByAffaireAndTypeRep ? RepartitionActions.UPDATE_PLA_REPARTITION : RepartitionActions.CREATE_PLA_REPARTITION, oldRep, rep, SynchronReTables.REPARTITION);
        if(firstPlacement)
        {
            mvtService.createMvtSuivant(new MvtSuivantReq(StatutEnum.EN_COURS_DE_PLACEMENT.staCode, dto.getAffId()));
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
    public Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, Pageable pageable)
    {
        return repRepo.searchRepartition(StringUtils.stripAccentsToUpperCase(key), affId, repType, pageable);
    }

    @Override
    public CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital)
    {
        if(capital.compareTo(zero)<0) throw new AppException("Le capital doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le montant du capital ne doit pas exéder le besoin fac");
        restARepartir = restARepartir == null ? zero : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(cent).divide(capitalInit, 3, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir, 3, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));
        return resp;
    }


    @Override
    public CalculRepartitionResp calculateRepByTaux(Long affId, BigDecimal taux)
    {
        if(taux.compareTo(zero)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        
        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? zero : restARepartir;

        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);
        
        BigDecimal capital = capitalInit.multiply(taux.divide(cent, 3, RoundingMode.HALF_UP));
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le taux de repartition ne doit pas exéder " + cent.multiply(restARepartir).divide(capitalInit, 3, RoundingMode.HALF_UP) + "%");

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(taux);
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir, 3, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));
        return resp;
    }


    @Override
    public CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, BigDecimal tauxBesoin)
    {
        if(tauxBesoin.compareTo(zero)<0) throw new AppException("Le taux de repartition doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(tauxBesoin.compareTo(cent)>0) throw new AppException("Le taux de repartition ne doit pas exéder 100% du besoin fac");

        if(aff == null) return null;
        BigDecimal restARepartir = comptaService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? zero : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);

        BigDecimal capital = tauxBesoin.divide(cent, 3, RoundingMode.HALF_UP).multiply(restARepartir);
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(cent).divide(capitalInit, 3, RoundingMode.HALF_UP));
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir, 3, RoundingMode.HALF_UP));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));
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
            .peek(pcl->pcl.setParamCesLegCapital(this.calculateRepByTaux(affId, pcl.getParamCesLegTaux()).getCapital()))
            .collect(Collectors.toList());
    }
}
