package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceTraiteNP
{
    TraiteNPResp create(CreateTraiteNPReq dto);

    Page<TraiteNPResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    List<TraiteNPResp> list(Long cedId, List<String> staCodes, Long exeCode);


    TraiteNPResp update(UpdateTraiteNPReq dto);

    UpdateTraiteNPReq edit(Long traiId);

    TraiteNPResp getTraiteDetails(Long traiId);

    Page<TraiteNPResp> searchSaisieSouscripteur(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    Page<TraiteNPResp> enCoursDeValidation(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    Page<TraiteNPResp> enCoursDeReglement(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    Page<TraiteNPResp> solde(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    Page<TraiteNPResp> archive(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable);

    void transmettreTraiteAuValidateur(Long traiteNpId, int returnPageSize);

    void retournerAuSouscripteur(MvtReq dto);

    void retournerAuValidateur(MvtReq dto);

    void valider(Long traiteNpId);

}

