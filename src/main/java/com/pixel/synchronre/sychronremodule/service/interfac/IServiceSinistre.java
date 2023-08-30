package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceSinistre
{

    SinistreDetailsResp createSinistre(CreateSinistreReq dto) throws UnknownHostException;
    SinistreDetailsResp updateSinistre(UpdateSinistreReq dto) throws UnknownHostException;

    void envoyerNoteCessionSinistreEtNoteDebit(Long sinId) throws UnknownHostException, Exception;

    Page<SinistreDetailsResp> searchSinistre(String key, List<String> staCodes, Pageable pageable);

    Page<SinistreDetailsResp> searchSinFacArch(String key, Pageable pageable);

    Page<SinistreDetailsResp> searchSinFacSaisiByCedante(String key, Pageable pageable);
    Page<SinistreDetailsResp> searchSinFacTransmiByCedante(String key, Pageable pageable);

    Page<SinistreDetailsResp> searchSinFacAttenteValidation(String key, Pageable pageable);

    Page<SinistreDetailsResp> searchSinFacEnReglement(String key, Pageable pageable);

    Page<SinistreDetailsResp> searchSinFacSolde(String key, Pageable pageable);

    EtatComptableSinistreResp getEtatComptable(Long sinId);

    Page<SinistreDetailsResp> transmettreSinistreAuSouscripteur(Long sinId, int returnPageSize) throws UnknownHostException;

    Page<SinistreDetailsResp> transmettreSinistreAuValidateur(Long sinId, int returnPageSize) throws UnknownHostException;

    Page<SinistreDetailsResp> retournerALaCedante(MvtReq dto, int returnPageSize) throws UnknownHostException;

    Page<SinistreDetailsResp> retournerAuSouscripteur(MvtReq dto, int returnPageSize) throws UnknownHostException;

    Page<SinistreDetailsResp> retournerAuValidateur(MvtReq dto, int returnPageSize) throws UnknownHostException;

    Page<SinistreDetailsResp> valider(Long sinId, int returnPageSize) throws Exception;

    Page<SinistreDetailsResp> searchSinFacSuivi(String key, Pageable pageable);

    void envoyerNoteCessionSinistre(Long sinId, Long cesId) throws Exception;

    void envoyerNoteDebitSinistre(Long sinId) throws Exception;
}
