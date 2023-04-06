package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.approle.CreateRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.ReadRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto) throws UnknownHostException;
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);
}
