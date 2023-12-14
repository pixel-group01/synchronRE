package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IserviceCessionnaire
{
    CessionnaireDetailsResp createCessionnaire(CreateCessionnaireReq dto) throws UnknownHostException;
    CessionnaireDetailsResp updateCessionnaire(UpdateCessionnaireReq dto) throws UnknownHostException;
    Page<CessionnaireListResp> searchCessionnaire(String key, Pageable pageable);

    Cessionnaire getCourtier();

    List<CessionnaireListResp> getCessionnairesByAffaire(Long affId);
    List<CessionnaireListResp> getCessionnairesBySinistre(Long sinId);
}
