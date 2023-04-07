package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;


public interface IserviceBanque {

    BanqueDetailsResp createBanque(CreateBanqueReq dto) throws UnknownHostException;
    BanqueDetailsResp updateBanque(UpdateBanqueReq dto) throws UnknownHostException;
    Page<BanqueListResp> searchBanque(String key, Pageable pageable);
}
