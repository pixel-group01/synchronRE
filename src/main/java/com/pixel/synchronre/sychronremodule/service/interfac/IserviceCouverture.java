package com.pixel.synchronre.sychronremodule.service.interfac;



import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.CreateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.UpdateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IserviceCouverture {

    CouvertureDetailsResp createCouverture(CreateCouvertureReq dto) throws UnknownHostException;
    CouvertureDetailsResp updateCouverture(UpdateCouvertureReq dto) throws UnknownHostException;

    List<CouvertureListResp> getCouerturesParents();

    Page<CouvertureListResp> searchCouverture(String key, Pageable pageable);
    List<CouvertureListResp> getCouerturesFilles(Long couParentId);
}
