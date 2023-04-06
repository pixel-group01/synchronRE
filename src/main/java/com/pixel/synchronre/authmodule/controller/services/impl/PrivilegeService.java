package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.services.spec.IPrivilegeService;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvToFunctionAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.PrvToRoleAssRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.constants.AuthActions;
import com.pixel.synchronre.authmodule.model.constants.AuthTables;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.CreatePrivilegeDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.PrivilegeMapper;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.SelectedPrvDTO;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class PrivilegeService implements IPrivilegeService
{
    private final PrvRepo prvRepo;
    private final PrvToFunctionAssRepo prvAssRepo;
    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final PrivilegeMapper prvMapper;
    private final IJwtService jwtService;
    private final ILogService logger;

    @Override @Transactional
    public ReadPrvDTO createPrivilege(CreatePrivilegeDTO dto) throws UnknownHostException {
        AppPrivilege privilege = prvMapper.getAppPrivilege(dto);
        privilege = prvRepo.save(privilege);
        AppPrivilege oldPrv = new AppPrivilege();
        BeanUtils.copyProperties(privilege, oldPrv);
        logger.logg(AuthActions.CREATE_PRV, oldPrv, privilege, AuthTables.PRV_TABLE);
        return prvMapper.mapToReadPrivilegeDTO(privilege);
    }

    @Override
    public Page<ReadPrvDTO> searchPrivileges(String searchKey, Pageable pageable)
    {
        Page<AppPrivilege> privilegePage = prvRepo.searchPrivileges(StringUtils.stripAccentsToUpperCase(searchKey), pageable);
        List<ReadPrvDTO> readPrvDTOS = privilegePage.stream().map(prvMapper::mapToReadPrivilegeDTO).collect(Collectors.toList());
        return new PageImpl<>(readPrvDTOS, pageable, privilegePage.getTotalElements());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Set<Long> roleIds)
    {
        Set<Long> selectedPrvIds = prvToRoleAssRepo.findActivePrvIdsForRoles(roleIds);
        return prvRepo.findAll().stream().map(prv->
                new SelectedPrvDTO(prv.getPrivilegeId(),
                        prv.getPrivilegeCode(), prv.getPrivilegeName(),
                        selectedPrvIds.contains(prv.getPrivilegeId()),
                        selectedPrvIds.contains(prv.getPrivilegeId()))).collect(Collectors.toList());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Long fncId, Set<Long> oldRoleIds, Set<Long> roleIds, Set<Long> prvIds)
    {
        Set<Long> ownedPrvIds = prvRepo.getFunctionPrvIds(fncId);
        //List<AppPrivilege> allPrvs = prvRepo.findAll();
        Set<Long> selectedPrvIds = prvToRoleAssRepo.findActivePrvIdsForRoles(roleIds);

        //Set<Long> addedRoleIds = roleIds.stream().filter(rId-> !oldRoleIds.contains(rId)).collect(Collectors.toSet());
        Set<Long> retiredRoleIds = oldRoleIds.stream().filter(rId-> !roleIds.contains(rId)).collect(Collectors.toSet());
        Set<Long> prvIdsToBeRetired = retiredRoleIds == null ? new HashSet<>() : retiredRoleIds.isEmpty() ? new HashSet<>() : prvToRoleAssRepo.findActivePrvIdsForRoles(retiredRoleIds).stream().filter(prvId->!selectedPrvIds.contains(prvId)).collect(Collectors.toSet());
        prvIds = prvIds.stream().filter(prvId->!prvIdsToBeRetired.contains(prvId)).collect(Collectors.toSet());
        selectedPrvIds.addAll(prvIds);
        return prvRepo.findAll().stream().map(prv->new SelectedPrvDTO(prv.getPrivilegeId(), prv.getPrivilegeCode(), prv.getPrivilegeName(), selectedPrvIds.contains(prv.getPrivilegeId()), ownedPrvIds.contains(prv.getPrivilegeId()))).collect(Collectors.toList());
    }
}
