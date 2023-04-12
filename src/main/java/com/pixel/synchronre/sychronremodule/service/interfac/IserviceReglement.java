package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceReglement {
    PaiementDetailsResp createPaiement(CreatePaiementReq dto) throws UnknownHostException;
    PaiementDetailsResp updatePaiement(UpdatePaiementReq dto) throws UnknownHostException;
    Page<ReglementListResp> searchReglement(String key, Pageable pageable);
}
