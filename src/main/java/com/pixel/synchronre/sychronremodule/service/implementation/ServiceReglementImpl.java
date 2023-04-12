package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service @AllArgsConstructor
public class ServiceReglementImpl implements IserviceReglement {
    private final ReglementRepository regRepo;
    private final ReglementMapper reglementMapper;
    private final ObjectCopier<Cedante> paiCopier;
    private final ILogService logService;

    @Override
    public PaiementDetailsResp createPaiement(CreatePaiementReq dto) throws UnknownHostException {
        Reglement paiement = reglementMapper.mapToPaiement(dto);
        paiement = regRepo.save(paiement);
        logService.logg(SynchronReActions.CREATE_PAIEMENT, null, paiement, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToPaiementDetailsResp(paiement);
    }

    @Override
    public PaiementDetailsResp updatePaiement(UpdatePaiementReq dto) throws UnknownHostException {
        return null;
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, Pageable pageable) {
        return null;
    }
}
