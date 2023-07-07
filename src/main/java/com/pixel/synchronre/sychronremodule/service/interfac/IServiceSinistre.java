package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceSinistre
{

    SinistreDetailsResp createSinistre(CreateSinistreReq dto) throws UnknownHostException;
    SinistreDetailsResp updateSinistre(UpdateSinistreReq dto) throws UnknownHostException;
    Page<SinistreDetailsResp> searchSinistre(String key, List<String> staCodes, Pageable pageable);
    Page<SinistreDetailsResp> searchSinistreSaisiByCedante(String key, List<String> staCodes, Pageable pageable);

    EtatComptableSinistreResp getEtatComptable(Long sinId);

    void transmettreSinistreAuCourtier(Long sinId) throws UnknownHostException;
}
