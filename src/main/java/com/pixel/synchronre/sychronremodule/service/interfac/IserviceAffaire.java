package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.sychronremodule.model.constants.AffStatutGroup;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

public interface IserviceAffaire {
    EtatComptableAffaire getEtatComptable(Long affId);

    void setAsNonRealisee(Long affId) throws UnknownHostException;

    void setAsRealisee(Long affId) throws UnknownHostException;

    boolean senNoteDebitFac(Long affId) throws Exception;
    Base64FileDto printNoteCreditFac(Long affId, Long cesId) throws Exception;

    Page<FacultativeListResp> transmettreAffaireAuSouscripteur(Long affId, Pageable pageable) throws UnknownHostException;

    Page<FacultativeListResp> retournerAffaireALaCedante(MvtReq dto, Pageable pageable) throws UnknownHostException;
    Page<FacultativeListResp> validerAffaire(Long affId, Pageable pageable) throws UnknownHostException;

    Page<FacultativeListResp> searchAffaires(Long exeCode, String key, List<String> staCodes, Pageable pageable);

    boolean placementIsFinished(Long affId);


}