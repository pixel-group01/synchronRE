package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.RenewFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IserviceAffaire {
    FacultativeDetailsResp createFacultative(CreateFacultativeReq dto, HistoDetails hd);
    boolean deleteAffaire(Long affId);

    String generateAffCode(Long affId);

    FacultativeDetailsResp updateFacultative(UpdateFacultativeReq dto, HistoDetails hd);
    Page<FacultativeListResp> searchFacultative(String key, Pageable pageable);

    FacultativeDetailsResp renewAffaire(RenewFacultativeReq dto);

    EtatComptableAffaire getEtatComptable(Long affId);

    void setAsNonRealisee(Long affId);

    void setAsRealisee(Long affId);

    boolean senNoteDebitFac(Long affId) throws Exception;
    Base64FileDto printNoteCreditFac(Long affId, Long cesId) throws Exception;

    Page<FacultativeListResp> transmettreAffaireAuSouscripteur(Long affId, Pageable pageable);

    Page<FacultativeListResp> retournerAffaireALaCedante(MvtReq dto, Pageable pageable);
    Page<FacultativeListResp> validerAffaire(Long affId, Pageable pageable);

    Page<FacultativeListResp> searchAffaires(Long exeCode, String key, List<String> staCodes, Pageable pageable);

    boolean placementIsFinished(Long affId);


    Page<FacultativeListResp> supprimerAffaire(Long affId, Pageable pageable);



}