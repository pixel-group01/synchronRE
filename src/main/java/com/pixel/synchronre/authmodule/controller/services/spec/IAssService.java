package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.RolesToFncDTO;

public interface IAssService
{
    void addRolesToFunction(RolesToFncDTO dto);
    void removeRolesToFunction(RolesToFncDTO dto);

    void addPrivilegesToFunction(PrvsToFncDTO dto);
    void removePrivilegesToFunction(PrvsToFncDTO dto); //assStatus = 2
    void revokePrivilegesToFunction(PrvsToFncDTO dto); //assStatus = 3
    void restorePrivilegesToFunction(PrvsToFncDTO dto); //assStatus = 2

    void setRolePrivileges(PrvsToRoleDTO dto);
    void addPrivilegesToRole(PrvsToRoleDTO dto);
    void removePrivilegesToRole(PrvsToRoleDTO dto);
}