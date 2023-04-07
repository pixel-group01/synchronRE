package com.pixel.synchronre.sychronremodule.service.interfac;


import com.pixel.synchronre.sychronremodule.model.dto.branche.request.CreateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.request.UpdateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IserviceBranche {

    BrancheDetailsResp createBranche(CreateBrancheReq dto) throws UnknownHostException;
    BrancheDetailsResp updateBranche(UpdateBrancheReq dto) throws UnknownHostException;
    Page<BrancheListResp> searchBranche(String key, Pageable pageable);
}
