package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CessionnaireMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCessionnaire;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;

@Service @RequiredArgsConstructor
public class serviceCessionnaireImpl implements IserviceCessionnaire
{
    private final CessionnaireRepository cesRepo;
    private final CessionnaireMapper cesMapper;
    private final ObjectCopier<Cessionnaire> cesCopier;
    private final ILogService logService;
    private final TypeRepo typeRepo;
    @Override @Transactional
    public CessionnaireDetailsResp createCessionnaire(CreateCessionnaireReq dto) throws UnknownHostException {
        Cessionnaire ces = cesMapper.mapToCessionnaire(dto);
        Type type = typeRepo.findByUniqueCode("CES").orElseThrow(()->new AppException("Type introuvable : CES"));
        ces.setType(type);
        ces = cesRepo.save(ces);
        logService.logg(SynchronReActions.CREATE_CESSIONNAIRE, null, ces, SynchronReTables.CESSIONNAIRE);
        return cesMapper.mapToCessionnaireDetailsResp(ces);
    }

    @Override @Transactional
    public CessionnaireDetailsResp updateCessionnaire(UpdateCessionnaireReq dto) throws UnknownHostException {
        Cessionnaire ces = cesRepo.findById(dto.getCesId()).orElseThrow(()->new AppException("Cessionnaire introuvable"));
        Cessionnaire oldCes = cesCopier.copy(ces);
        ces.setCesNom(dto.getCesNom());
        ces.setCesEmail(dto.getCesEmail());
        ces.setCesTelephone(dto.getCesTelephone());
        ces.setCesSigle(dto.getCesSigle());
        ces.setCesAdressePostale(dto.getCesAdressePostale());
        ces.setCesSituationGeo(dto.getCesSituationGeo());
        ces = cesRepo.save(ces);
        logService.logg(SynchronReActions.UPDATE_CESSIONNAIRE, oldCes, ces, SynchronReTables.CESSIONNAIRE);
        return cesMapper.mapToCessionnaireDetailsResp(ces);
    }

    @Override
    public Page<CessionnaireListResp> searchCessionnaire(String key, Pageable pageable)
    {
        return cesRepo.searchCessionnaires(StringUtils.stripAccentsToUpperCase(key), pageable);
    }

    @Override
    public Cessionnaire getCourtier() {
        List<Cessionnaire> cessionnaires = cesRepo.getCourtiers();
        if(cessionnaires == null || cessionnaires.isEmpty()) throw new AppException("Aucun coutier détecté dans le système");
        return cessionnaires.get(0);
    }
}
