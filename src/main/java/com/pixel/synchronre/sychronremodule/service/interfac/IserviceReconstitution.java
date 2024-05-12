package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IserviceReconstitution {
    ReconstitutionResp save(ReconstitutionReq dto);
    boolean delete(Long reconstitutionId);
    Page<ReconstitutionResp> search(Long traiteNPId, String key, Pageable pageable);
}
