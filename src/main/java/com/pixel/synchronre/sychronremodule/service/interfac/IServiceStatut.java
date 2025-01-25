package com.pixel.synchronre.sychronremodule.service.interfac;


import com.pixel.synchronre.statsmodule.model.dtos.VStatSituationFinParReaCed;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceStatut {
    StatutDetailsResp createStatut(CreateStatutReq dto) throws UnknownHostException;
    StatutDetailsResp updateStatut(UpdateStatutReq dto) throws UnknownHostException;
    Page<StatutListResp> searchStatut(String key, Pageable pageable);

}
