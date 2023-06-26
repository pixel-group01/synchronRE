package com.pixel.synchronre.sychronremodule.service.interfac;


import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.CreateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.UpdateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceDevise {

    DeviseDetailsResp createDevise(CreateDeviseReq dto) throws UnknownHostException;
    DeviseDetailsResp updateDevise(UpdateDeviseReq dto) throws UnknownHostException;
    List<DeviseListResp> searchDevise(String key);
}
