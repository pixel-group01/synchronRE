package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.repositories.PrvToRoleAssRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IAssService;
import com.pixel.synchronre.authmodule.controller.services.spec.IRoleService;
import com.pixel.synchronre.authmodule.controller.repositories.RoleRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.constants.AuthActions;
import com.pixel.synchronre.authmodule.model.constants.AuthTables;
import com.pixel.synchronre.authmodule.model.dtos.approle.CreateRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.ReadRoleDTO;
import com.pixel.synchronre.authmodule.model.dtos.approle.RoleMapper;
import com.pixel.synchronre.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.AppRole;
import com.pixel.synchronre.authmodule.model.entities.PrvToRoleAss;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j @ResponseStatus(HttpStatus.OK)
public class RoleService implements IRoleService
{
    private final RoleRepo roleRepo;
    private final RoleMapper roleMapper;
    private final ILogService logger;

    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final ObjectCopier<PrvToRoleAss> ptrCopier;

    @Override @Transactional
    public ReadRoleDTO createRole(CreateRoleDTO dto) throws UnknownHostException
    {
        AppRole role = roleMapper.mapToRole(dto);
        role = roleRepo.save(role);
        logger.logg(AuthActions.CREATE_ROLE, null, role, AuthTables.ROLE_TABLE);
        this.setRolePrivileges(new PrvsToRoleDTO(role.getRoleId(), dto.getPrvIds(), null, null, true));
        ReadRoleDTO readRoleDTO = roleMapper.mapToReadRoleDTO(role);
        readRoleDTO.setPrivileges(prvToRoleAssRepo.findActivePrivilegesForRoles(Collections.singleton(role.getRoleId())));
        return readRoleDTO;
    }

    @Override
    public Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable)
    {
        Page<AppRole> rolePage = roleRepo.searchRoles(StringUtils.stripAccentsToUpperCase(searchKey), pageable);
        List<ReadRoleDTO> readRoleDTOS = rolePage.stream().map(roleMapper::mapToReadRoleDTO).collect(Collectors.toList());
        return new PageImpl<>(readRoleDTOS, pageable, rolePage.getTotalElements());
    }

    @Override @Transactional
    public void setRolePrivileges(PrvsToRoleDTO dto) {
        Long roleId = dto.getRoleId(); Set<Long> prvIds = dto.getPrvIds() == null ? new HashSet<>(Collections.singletonList(0L)) : dto.getPrvIds().size() == 0 ? new HashSet<>(Collections.singletonList(0L)) : dto.getPrvIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt =  dto.getEndsAt();
        Set<Long> prvIdsToBeRemoved = prvToRoleAssRepo.findPrvIdsForRoleNotIn(roleId, prvIds); //
        Set<Long> prvIdsToBeAdded = prvToRoleAssRepo.findPrvIdsNotBelongingToRoleIn(roleId, prvIds);
        //Set<Long> prvIdsToNotBeChanged = prvToRoleAssRepo.findActivePrvIdsForRoleIn_sameDates(roleId, prvIds, startsAt.toLocalDate(), endsAt.toLocalDate());
        Set<Long> prvIdsToChangeTheDates = prvToRoleAssRepo.findActivePrvIdsForRoleIn_otherDates(roleId, prvIds, startsAt, endsAt);
        Set<Long> prvIdsToActivateAndChangeTheDates = prvToRoleAssRepo.findNoneActivePrvIdsForRoleIn(roleId, prvIds);

        prvIdsToBeRemoved.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            PrvToRoleAss oldPrvToRoleAss = ptrCopier.copy(prvToRoleAss);
            prvToRoleAss.setAssStatus(2);
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            try {
                logger.logg(AuthActions.REMOVE_PRV_TO_ROL, oldPrvToRoleAss, prvToRoleAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        prvIdsToBeAdded.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            if(prvToRoleAss == null)
            {
                prvToRoleAss = new PrvToRoleAss(null, 1, startsAt, endsAt, new AppPrivilege(id), new AppRole(roleId));
                prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
                try {
                    logger.logg(AuthActions.ADD_PRV_TO_ROL,null, prvToRoleAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else if(prvToRoleAss.getAssStatus() != 1)
            {
                PrvToRoleAss oldPrvToRoleAss = ptrCopier.copy(prvToRoleAss);

                prvToRoleAss.setAssStatus(1);
                try {
                    logger.logg(AuthActions.RESTORE_PRV_TO_ROL, oldPrvToRoleAss,prvToRoleAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

        });

        prvIdsToChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            PrvToRoleAss oldPrvToRoleAss = ptrCopier.copy(prvToRoleAss);
            prvToRoleAss.setStartsAt( startsAt);
            prvToRoleAss.setEndsAt(endsAt);
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            try {
                logger.logg(AuthActions.CHANGE_PRV_TO_ROL_VALIDITY_PERIOD, oldPrvToRoleAss,prvToRoleAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        prvIdsToActivateAndChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            PrvToRoleAss oldPrvToRoleAss = ptrCopier.copy(prvToRoleAss);
            prvToRoleAss.setAssStatus(1);
            prvToRoleAss.setStartsAt(startsAt);
            prvToRoleAss.setEndsAt(endsAt);
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            try {
                logger.logg(AuthActions.PRV_TO_ROL_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, oldPrvToRoleAss,prvToRoleAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }
}
