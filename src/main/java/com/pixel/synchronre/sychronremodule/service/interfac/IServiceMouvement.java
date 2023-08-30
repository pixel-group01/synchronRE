package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

public interface IServiceMouvement
{
    @Transactional
    void createMvtAffaire(MvtReq dto) throws UnknownHostException;

    @Transactional
    void createMvtPlacement(MvtReq dto) throws UnknownHostException;

    @Transactional
    void createMvtSinistre(MvtReq dto) throws UnknownHostException;
    Page<MouvementListResp> findMouvementById(Long affId, Long sinId , Pageable pageable);
}
