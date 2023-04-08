package com.pixel.synchronre.sychronremodule.service.interfac;



import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.CreateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.UpdateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceCouverture {

    CouvertureDetailsResp createCouverture(CreateCouvertureReq dto) throws UnknownHostException;
    CouvertureDetailsResp updateCouverture(UpdateCouvertureReq dto) throws UnknownHostException;
    Page<CouvertureListResp> searchCouverture(String key, Pageable pageable);
}
