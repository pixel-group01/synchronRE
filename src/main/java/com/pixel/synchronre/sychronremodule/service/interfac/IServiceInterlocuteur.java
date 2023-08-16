package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.CreateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceInterlocuteur
{
    InterlocuteurListResp createInterlocuteur(CreateInterlocuteurReq dto) throws UnknownHostException;
    InterlocuteurListResp updateInterlocuteur(UpdateInterlocuteurReq dto) throws UnknownHostException;
    Page<InterlocuteurListResp> searchInterlocuteur(String key, Long cesId, Pageable pageable);
    List<InterlocuteurListResp> getInterlocuteurByCessionnaire(Long cesId);
    List<InterlocuteurListResp> getInterlocuteurByPlacement(Long repId);
    void deleteInterlocuteur(Long intId) throws UnknownHostException;

}
