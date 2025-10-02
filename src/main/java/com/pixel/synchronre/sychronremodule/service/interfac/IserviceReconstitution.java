package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reconstitution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IserviceReconstitution {
    ReconstitutionResp save(ReconstitutionReq dto);

    ReconstitutionResp create(ReconstitutionReq dto);

    boolean delete(Long reconstitutionId);
    Page<ReconstitutionResp> search(Long traiteNPId, String key, Pageable pageable);

    ReconstitutionReq edit(Long reconstitutionId);
}
