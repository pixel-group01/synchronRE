package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.StatutMapper;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceStatut;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class StatutServiceImpl implements IServiceStatut {
    private final StatutRepository statRepo;
    private final StatutMapper statutMapper;
    private final ObjectCopier<Statut> staCopier;
    private final ILogService logService;

    @Override @Transactional
    public StatutDetailsResp createStatut(CreateStatutReq dto) throws UnknownHostException {
        Statut statut=statutMapper.mapToStatut(dto);
        statut=statRepo.save(statut);
        logService.logg(SynchronReActions.CREATE_STATUT, null, statut, SynchronReTables.STATUT);
        return statutMapper.mapToStatutDetailsResp(statut);
    }

    @Override @Transactional
    public StatutDetailsResp updateStatut(UpdateStatutReq dto) throws UnknownHostException {
        Statut statut = statRepo.findById(dto.getStaCode()).orElseThrow(()->new AppException("Statut introuvable"));
        Statut oldStat = staCopier.copy(statut);
        statut.setStaCode(dto.getStaCode());
        statut.setStaLibelle(dto.getStaLibelle());
        statut.setStaType(TypeStatut.valueOf(dto.getStaType()));
        statut.setStaLibelleLong(dto.getStaLibelleLong());
        statut=statRepo.save(statut);
        logService.logg(SynchronReActions.UPDATE_STATUT, oldStat, statut, SynchronReTables.STATUT);
        return statutMapper.mapToStatutDetailsResp(statut);
    }

    @Override
    public Page<StatutListResp> searchStatut(String key, Pageable pageable) {
        return statRepo.searchStatut(StringUtils.stripAccentsToUpperCase(key), pageable);
    }
}
