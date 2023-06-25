package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.approle.CreateRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.ReadRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto) throws UnknownHostException;
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);

    @Transactional
    void setRolePrivileges(PrvsToRoleDTO dto);
}
