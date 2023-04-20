package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceReglement {
    ReglementDetailsResp createReglement(TypeReglement typeReg, CreateReglementReq dto) throws UnknownHostException;
    ReglementDetailsResp updateReglement(UpdateReglementReq dto) throws UnknownHostException;
    Page<ReglementListResp> searchReglement(String key, TypeReglement typeReg, Pageable pageable);
}