package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRepartitionImpl implements IserviceRepartition
{
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final IserviceAffaire affService;
    private final RepartitionMapper repMapper;
    private final ObjectCopier<Repartition> repCopier;
    private final ILogService logService;
    private BigDecimal zero = new BigDecimal(0);
    private BigDecimal cent = new BigDecimal(100);

    @Override
    public RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToRepartition(dto);
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_REPARTITION, null, rep, RepartitionTables.REPARTITION);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }


    private RepartitionDetailsResp createCesLegRepartition(CreateCesLegReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToCesLegRepartition(dto);
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_CES_LEG_REPARTITION, null, rep, RepartitionTables.REPARTITION);
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
        Repartition rep = repMapper.mapToPartCedRepartition(dto);
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_CED_REPARTITION, null, rep, RepartitionTables.REPARTITION);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public RepartitionDetailsResp createPlaRepartition(CreatePlaRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToPlaRepartition(dto);
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_PLA_REPARTITION, null, rep, RepartitionTables.REPARTITION);
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
        logService.logg(RepartitionActions.UPDATE_REPARTITION, oldRep, rep, RepartitionTables.REPARTITION);
        repRepo.save(rep);
        return repMapper.mapToRepartitionDetailsResp(rep);
    }

    @Override
    public Page<RepartitionListResp> searchRepartition(String key, Pageable pageable)
    {
        return repRepo.searchRepartition(StringUtils.stripAccentsToUpperCase(key), pageable);
    }

    @Override
    public CalculRepartitionResp calculateRepByCapital(Long affId, BigDecimal capital)
    {
        if(capital.compareTo(zero)<0) throw new AppException("Le capital doit être un nombre strictement positif");
        Affaire aff = affRepo.findById(affId).orElse(null);
        if(aff == null) return null;
        BigDecimal restARepartir = affService.calculateRestARepartir(affId);
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le montant du capital ne doit pas exéder le besoin fac");
        restARepartir = restARepartir == null ? zero : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(cent).divide(aff.getAffCapitalInitial()));
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir));
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
        BigDecimal restARepartir = affService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? zero : restARepartir;

        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);
        
        BigDecimal capital = capitalInit.multiply(taux.divide(cent));
        if(capital.compareTo(restARepartir)>0) throw new AppException("Le taux de repartition ne doit pas exéder " + cent.multiply(restARepartir).divide(capitalInit) + "%");

        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(taux);
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir));
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
        BigDecimal restARepartir = affService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? zero : restARepartir;
        BigDecimal capitalInit = aff.getAffCapitalInitial() == null ? zero : aff.getAffCapitalInitial();
        if(restARepartir.compareTo(zero) <= 0 || capitalInit.compareTo(zero) <= 0) return new CalculRepartitionResp(zero, zero,zero,zero, zero);

        BigDecimal capital = tauxBesoin.divide(cent).multiply(restARepartir);
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(capital.multiply(cent).divide(capitalInit));
        resp.setTauxBesoinFac(capital.multiply(cent).divide(restARepartir));
        resp.setBesoinFac(restARepartir);
        resp.setBesoinFacRestant(restARepartir.subtract(capital));
        return resp;
    }
}
