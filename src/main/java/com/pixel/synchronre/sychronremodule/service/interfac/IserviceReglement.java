package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.typemodule.model.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

public interface IserviceReglement
{
    ReglementDetailsResp createReglementAffaire(String typeReg, CreateReglementReq dto) throws UnknownHostException;

    ReglementDetailsResp createPaiementAffaire(CreateReglementReq dto) throws UnknownHostException;
    @Transactional
    ReglementDetailsResp createReversementAffaire(CreateReglementReq dto) throws UnknownHostException;


    ReglementDetailsResp createReglementSinistre(String typeReg, CreateReglementReq dto) throws UnknownHostException;


    @Transactional
    ReglementDetailsResp createPaiementSinistre(CreateReglementReq dto) throws UnknownHostException;

    @Transactional
    ReglementDetailsResp createReversementSinistre(CreateReglementReq dto) throws UnknownHostException;

    ReglementDetailsResp updateReglement(UpdateReglementReq dto) throws UnknownHostException;
    Page<ReglementListResp> searchReglement(String key, Long affId, Long sinId, String typRegUniqueCode, Pageable pageable);
}
