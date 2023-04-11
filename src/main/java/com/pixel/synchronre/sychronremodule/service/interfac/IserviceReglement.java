package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateRegementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceReglement {
    ReglementDetailsResp createReglement(CreateReglementReq dto) throws UnknownHostException;
    ReglementDetailsResp updateReglement(UpdateRegementReq dto) throws UnknownHostException;
    Page<ReglementListResp> searchReglement(String key, Pageable pageable);
}
