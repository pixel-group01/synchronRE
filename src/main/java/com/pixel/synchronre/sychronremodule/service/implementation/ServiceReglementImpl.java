package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateRegementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
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
    @Override
    public ReglementDetailsResp createReglement(CreateReglementReq dto) throws UnknownHostException {
        return null;
    }

    @Override
    public ReglementDetailsResp updateReglement(UpdateRegementReq dto) throws UnknownHostException {
        return null;
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, Pageable pageable) {
        return null;
    }
}
