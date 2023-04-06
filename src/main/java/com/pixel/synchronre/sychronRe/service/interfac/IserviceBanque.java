package com.pixel.synchronre.sychronRe.service.interfac;


import com.pixel.synchronre.sychronRe.model.dto.projection.BanqueInfo;
import com.pixel.synchronre.sychronRe.model.dto.request.BanqueReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Banque;

import java.util.List;

public interface IserviceBanque {

    Banque saveBanque(BanqueReqDTO banqueReqDTO);
    List<BanqueInfo> getAllBanque();
    BanqueReqDTO updateBanque(Long banId, BanqueReqDTO banqueReqDTO);
    BanqueReqDTO delete(Long banId);
}
