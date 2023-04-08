package com.pixel.synchronre.sychronremodule.service.interfac;


import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.UpdateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceParamCessionLegale {

    ParamCessionLegaleDetailsResp createParamCessionLegale(CreateParamCessionLegaleReq dto) throws UnknownHostException;
    ParamCessionLegaleDetailsResp  updateParamCessionLegale(UpdateParamCessionLegaleReq dto) throws UnknownHostException;
    Page<ParamCessionLegaleListResp> searchParamCessionLegale(String key, Pageable pageable);
}
