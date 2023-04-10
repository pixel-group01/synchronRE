package com.pixel.synchronre.sychronremodule.service.interfac;


import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IservicePays {

    PaysDetailsResp createPays(CreatePaysReq dto) throws UnknownHostException;
    PaysDetailsResp updatePays(UpdatePaysReq dto) throws UnknownHostException;
    Page<PaysListResp> searchPays(String key, Pageable pageable);
}
