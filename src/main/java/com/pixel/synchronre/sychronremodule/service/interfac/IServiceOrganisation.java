package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO;
import com.pixel.synchronre.sychronremodule.model.dto.organisation.UpdatePaysOrgDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface IServiceOrganisation
{
    OrganisationDTO create(OrganisationDTO dto);

    OrganisationDTO update(UpdatePaysOrgDTO dto);

    List<OrganisationDTO> getListOrganisations();

    Page<OrganisationDTO> search(String key, Pageable pageable);
}
