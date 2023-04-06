package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.dtos.appprivilege.CreatePrivilegeDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.SelectedPrvDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

public interface IPrivilegeService
{
    ReadPrvDTO createPrivilege(CreatePrivilegeDTO dto) throws UnknownHostException;
    Page<ReadPrvDTO> searchPrivileges(String searchKey, Pageable pageable);
    List<SelectedPrvDTO> getSelectedPrvs(Set<Long> roleIds);
    List<SelectedPrvDTO> getSelectedPrvs(Long prAssId, Set<Long> oldRoleIds, Set<Long> roleIds, Set<Long> prvIds);
}
