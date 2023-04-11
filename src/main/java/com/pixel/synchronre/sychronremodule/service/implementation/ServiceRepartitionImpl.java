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
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
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

import java.net.UnknownHostException;

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

    @Override
    public RepartitionDetailsResp createRepartition(CreateRepartitionReq dto) throws UnknownHostException {
        Repartition rep = repMapper.mapToRepartition(dto);
        rep = repRepo.save(rep);
        logService.logg(RepartitionActions.CREATE_REPARTITION, null, rep, RepartitionTables.REPARTITION);
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
    public CalculRepartitionResp calculateRepByCapital(Long affId, Float capital)
    {
        Affaire aff = affRepo.findById(affId).orElse(null);
        Float restARepartir = affService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? 0 : restARepartir;
        if(aff == null) return null;
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(100 * capital/aff.getAffCapitalInitial());
        resp.setTauxBesoinFac(100 * capital / restARepartir);
        resp.setBesoinFacRestant(capital - restARepartir);
        return resp;
    }

    @Override
    public CalculRepartitionResp calculateRepByTaux(Long affId, Float taux)
    {
        Affaire aff = affRepo.findById(affId).orElse(null);
        Float restARepartir = affService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? 0 : restARepartir;
        if(aff == null) return null;
        Float capital = taux * aff.getAffCapitalInitial();
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(taux);
        resp.setTauxBesoinFac(100 * capital / restARepartir);
        resp.setBesoinFacRestant(capital - restARepartir);
        return resp;
    }

    @Override
    public CalculRepartitionResp calculateRepByTauxBesoinFac(Long affId, Float tauxBesoin) {
        Affaire aff = affRepo.findById(affId).orElse(null);
        Float restARepartir = affService.calculateRestARepartir(affId);
        restARepartir = restARepartir == null ? 0 : restARepartir;

        Float capital = tauxBesoin * restARepartir;

        if(aff == null) return null;
        CalculRepartitionResp resp = new CalculRepartitionResp();
        resp.setCapital(capital);
        resp.setTaux(100 * capital/aff.getAffCapitalInitial());
        resp.setTauxBesoinFac(100 * capital / restARepartir);
        resp.setBesoinFacRestant(capital - restARepartir);
        return resp;
    }
}
