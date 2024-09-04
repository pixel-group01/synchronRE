package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CessionnaireMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCessionnaire;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class serviceCessionnaireImpl implements IserviceCessionnaire
{
    private final CessionnaireRepository cesRepo;
    private final CessionnaireMapper cesMapper;
    private final ObjectCopier<Cessionnaire> cesCopier;
    private final ILogService logService;
    private final TypeRepo typeRepo;
    private final IServiceCalculsComptables comptaService;
    private final RepartitionRepository repRepo;
    private final RepartitionTraiteRepo repTraiRepo;
    private final IServiceCalculsComptablesSinistre sinComptaService;
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

    @Override
    public List<CessionnaireListResp> getCessionnairesByAffaire(Long affId) {
        return cesRepo.findByAffId(affId).stream()
                .map(ces->repRepo.getPlacementIdByAffIdAndCesId(affId, ces.getCesId()).orElse(null))
                .filter(Objects::nonNull)
                .filter(plaId->comptaService.calculateRestAReverserbyCes(plaId).setScale(4, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) != 0)
                .map(plaId->repRepo.getCessionnaireByRepId(plaId).orElse(null))
                .filter(Objects::nonNull)
                .map(ces->cesMapper.mapToCessionnaireListResp(ces))
                .collect(Collectors.toList());
    }

    @Override
    public List<CessionnaireListResp> getCessionnairesBySinistre(Long sinId)
    {
        List<CessionnaireListResp> cessionnaires = cesRepo.findBySinId(sinId);
        if(cessionnaires == null) return new ArrayList<>();

        return cessionnaires.stream()
                .filter(ces->sinComptaService.calculateResteAPayerBySinAndCes(sinId, ces.getCesId()).setScale(4, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) != 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<CessionnaireListResp> getCessionnairesByTraiteNp(Long traiteNpId)
    {
        List<CessionnaireListResp> cessionaires = cesRepo.findCessionnairesNotOnTraite(traiteNpId);
        return cessionaires;
    }

    @Override
    public List<CessionnaireListResp> getCourtierPlaceurs() {
        return cesRepo.getCourtierPlaceurs();
    }
}
