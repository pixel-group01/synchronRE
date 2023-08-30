package com.pixel.synchronre.authmodule.controller.services.impl;


import com.pixel.synchronre.authmodule.controller.repositories.*;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.UpdateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appuser.AuthResponseDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.*;
import com.pixel.synchronre.authmodule.model.entities.*;
import com.pixel.synchronre.authmodule.controller.services.spec.IFunctionService;
import com.pixel.synchronre.authmodule.model.constants.AuthActions;
import com.pixel.synchronre.authmodule.model.constants.AuthTables;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.FncMapper;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.ReadFncDTO;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FunctionService implements IFunctionService{

    @Lazy @Autowired private UserDetailsService uds;

    private final FunctionRepo functionRepo;
    private final RoleToFunctionAssRepo rtfRepo;
    private final PrvToFunctionAssRepo ptfRepo;
    private final UserRepo userRepo;
    private final FncMapper fncMapper;
    private final AssMapper assMapper;
    private final ILogService logger;
    private final ObjectCopier<AppFunction> functionCopier;
    private final ObjectCopier<AppUser> userCopier;
    private final ObjectCopier<RoleToFncAss> rtfCopier;
    private final ObjectCopier<PrvToFunctionAss> ptfCopier;
    private final IJwtService jwtService;

    @Override
    public Long getActiveCurrentFunctionId(Long userId)
    {
        Set<Long> ids = functionRepo.getCurrentFncIds(userId);
        return ids == null ? null : ids.size() != 1 ? null : new ArrayList<>(ids).get(0);
    }

    @Override
    public String getActiveCurrentFunctionName(Long userId) {
        Set<String> fncNames = functionRepo.getCurrentFncNames(userId);
        return fncNames == null ? null : fncNames.size() != 1 ? null : new ArrayList<>(fncNames).get(0);
    }

    @Override @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ReadFncDTO createFnc(CreateFncDTO dto) throws UnknownHostException
    {
        AppFunction function = fncMapper.mapToFunction(dto);
        boolean userHasFunction = functionRepo.userHasAnyAppFunction(dto.getUserId());
        function = functionRepo.save(function);
        Long fncId = function.getId();
        if(!userHasFunction)
        {
            function.setFncStatus(1);
            AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
            AppUser oldUser = userCopier.copy(user);
            user.setCurrentFunctionId(fncId);
            user = userRepo.save(user);
            logger.logg(AuthActions.SET_USER_DEFAULT_FNC_ID, oldUser, user, AuthTables.USER_TABLE);
        }
        logger.logg(AuthActions.CREATE_FNC, null, function, AuthTables.FUNCTION);
        Set<Long> roleIds = dto.getRoleIds() == null ? new HashSet<>() : dto.getRoleIds();
        Set<Long> prvIds = dto.getPrvIds() == null ? new HashSet<>() : dto.getPrvIds();

        roleIds.forEach(id->
        {
            RoleToFncAss roleToFunctionAss = new RoleToFncAss();
            roleToFunctionAss.setAssStatus(1);
            roleToFunctionAss.setStartsAt(dto.getStartsAt());
            roleToFunctionAss.setEndsAt(dto.getEndsAt());
            roleToFunctionAss.setRole(new AppRole(id));
            roleToFunctionAss.setFunction(new AppFunction(fncId));
            roleToFunctionAss = rtfRepo.save(roleToFunctionAss);
            try {
                logger.logg(AuthActions.ADD_ROLE_TO_FNC, null, roleToFunctionAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        PrvAssSpliterDTO prvAssSpliterDTO = assMapper.mapToPrvAssSpliterDTO(prvIds, roleIds, fncId, dto.getStartsAt(), dto.getEndsAt(), false);
        prvAssSpliterDTO.getPrvIdsToBeAddedAsNew().forEach(id->
        {
            PrvToFunctionAss prvToFunctionAss = new PrvToFunctionAss();
            prvToFunctionAss.setAssStatus(1);
            prvToFunctionAss.setStartsAt(dto.getStartsAt());
            prvToFunctionAss.setEndsAt(dto.getEndsAt());
            prvToFunctionAss.setPrivilege(new AppPrivilege(id));
            prvToFunctionAss.setFunction(new AppFunction(fncId));
            ptfRepo.save(prvToFunctionAss);
            try {
                logger.logg(AuthActions.ADD_PRV_TO_FNC, null, prvToFunctionAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public AuthResponseDTO setFunctionAsDefault(Long fncId) throws UnknownHostException
    {
        AppFunction function  = functionRepo.findById(fncId).orElseThrow(()->new AppException("Fonction inconnue"));

        functionRepo.findActiveByUser(function.getUser().getUserId()).forEach(fnc->
        {
            if(!fnc.getId().equals(fncId) && fnc.getFncStatus() == 1)
            {
                AppFunction oldFnc = functionCopier.copy(fnc);
                fnc.setFncStatus(2);
                fnc = functionRepo.save(fnc);
                try {
                    logger.logg(AuthActions.SET_FNC_AS_NONE_DEFAULT, oldFnc, fnc, AuthTables.FUNCTION);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        if(function.getFncStatus() == 1) return null;
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(1);
        logger.logg(AuthActions.SET_FNC_AS_NONE_DEFAULT, oldFnc, function, AuthTables.FUNCTION);


        if(function == null || function.getUser() == null ||function.getUser().getUserId() == null) throw new AppException("Utilisateur introuvable");
        AppUser user = userRepo.findById(function.getUser().getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
        AppUser oldUser = userCopier.copy(user);
        user.setCurrentFunctionId(fncId);
        user = userRepo.save(user);
        logger.logg(AuthActions.SET_USER_DEFAULT_FNC_ID, oldUser, user, AuthTables.USER_TABLE);
        UserDetails userDetails = uds.loadUserByUsername(user.getEmail());
        return jwtService.generateJwt(userDetails, UUID.randomUUID().toString());
    }

    @Override @Transactional
    public void revokeFunction(Long fncId) throws UnknownHostException {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 3) return;
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(3); functionRepo.save(function);
        logger.logg(AuthActions.REVOKE_FNC, oldFnc, function, AuthTables.FUNCTION);
    }

    @Override @Transactional
    public void restoreFunction(Long fncId) throws UnknownHostException
    {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 1 || function.getFncStatus() == 2) return;
        AppFunction oldFnc = functionCopier.copy(function);
        function.setFncStatus(2); functionRepo.save(function);
        logger.logg(AuthActions.REVOKE_FNC, oldFnc, function, AuthTables.FUNCTION);
    }

    @Override @Transactional
    public ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto)
    {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        if(function == null) return null;
        Long fncId = function.getId();
        Set<Long> roleIds = dto.getRoleIds(); Set<Long> prvIds = dto.getPrvIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleIds, fncId, startsAt, endsAt, true);
        PrvAssSpliterDTO prvAssSpliterDTO = assMapper.mapToPrvAssSpliterDTO(prvIds, roleIds, fncId, startsAt, endsAt, true);
        treatRolesAssignation(roleAssSpliterDTO, fncId, startsAt, endsAt);

        this.treatPrvsAssignation(prvAssSpliterDTO, fncId, dto.getStartsAt(), dto.getEndsAt());

        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public ReadFncDTO updateFunction(UpdateFncDTO dto)
    {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        if(function == null) return null;
        function.setName(dto.getName());
        function.setStartsAt(dto.getStartsAt());
        function.setVisibilityId(dto.getVisibilityId());
        function.setEndsAt(dto.getEndsAt());
        ReadFncDTO readFncDTO = this.setFunctionAuthorities(new SetAuthoritiesToFunctionDTO(dto.getFncId(),dto.getStartsAt(), dto.getEndsAt(), dto.getRoleIds(), dto.getPrvIds()));
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getActiveCurrentFunction(Long userId)
    {
        Long currentFncId = this.getActiveCurrentFunctionId(userId);
        if(currentFncId == null) return null;
        AppFunction function = functionRepo.findById(currentFncId).orElseThrow(()->new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        //List<ReadPrvDTO> prvDtos = ptfRepo.getFncPrivileges(currentFncId).stream().map(prvMapper::mapToReadPrivilegeDTO).collect(Collectors.toList());
        //List<ReadRoleDTO> roleDtos = rtfRepo.getFncRoles(currentFncId).stream().map(roleMapper::mapToReadRoleDTO).collect(Collectors.toList());
        //readFncDTO.setPrivileges(prvDtos);
        //readFncDTO.setRoles(roleDtos);
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getFunctioninfos(Long foncId)
    {
        AppFunction function = functionRepo.findById(foncId).orElseThrow(()-> new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        return readFncDTO;
    }

    private void treatRolesAssignation(RoleAssSpliterDTO roleAssSpliterDTO, Long fncId, LocalDate startsAt, LocalDate endsAt)
    {
        roleAssSpliterDTO.getRoleIdsToBeRemoved().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(2);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.REMOVE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleIdsToBeAddedAsNew().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            if(rtfAss == null)
            {
                rtfAss = new RoleToFncAss();
                rtfAss.setAssStatus(1); rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
                rtfAss.setRole(new AppRole(id));
                rtfAss.setFunction(new AppFunction(fncId));
                rtfRepo.save(rtfAss);
                try {
                    logger.logg(AuthActions.ADD_ROLE_TO_FNC, null, rtfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
                rtfAss.setAssStatus(1);
                rtfRepo.save(rtfAss);
                try {
                    logger.logg(AuthActions.RESTORE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        roleAssSpliterDTO.getRoleIdsToChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.CHANGE_ROLE_TO_FNC_VALIDITY_PERIOD, oldRtfAss, rtfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleIdsToActivate().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.RESTORE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleIdsToActivateAndChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, oldRtfAss, rtfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    private void treatPrvsAssignation(PrvAssSpliterDTO prvAssSpliterDTO, Long fncId, LocalDate startsAt, LocalDate endsAt)
    {

        prvAssSpliterDTO.getPrvIdsToBeRemoved().forEach(id->
        {
            PrvToFunctionAss ptfAss = ptfRepo.findByFncAndPrv(fncId, id);
            if(ptfAss == null)
            {
                ptfAss = new PrvToFunctionAss();
                ptfAss.setAssStatus(3); ptfAss.setStartsAt(startsAt); ptfAss.setEndsAt(endsAt);
                ptfAss.setFunction(new AppFunction(fncId));
                ptfAss.setPrivilege(new AppPrivilege(id));
                try {
                    logger.logg(AuthActions.REMOVE_PRV_TO_FNC, null, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                PrvToFunctionAss oldPtfAss = ptfCopier.copy(ptfAss);
                ptfAss.setAssStatus(3); ptfAss = ptfRepo.save(ptfAss);
                try {
                    logger.logg(AuthActions.REMOVE_PRV_TO_FNC, oldPtfAss, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        prvAssSpliterDTO.getPrvIdsToBeAddedAsNew().forEach(id->
        {
            PrvToFunctionAss  ptfAss = ptfRepo.findByFncAndPrv(fncId, id);
            if(ptfAss == null)
            {
                ptfAss = new PrvToFunctionAss();
                ptfAss.setAssStatus(1);
                ptfAss.setFunction(new AppFunction(fncId));
                ptfAss.setPrivilege(new AppPrivilege(id));
                ptfAss = ptfRepo.save(ptfAss);
                try {
                    logger.logg(AuthActions.ADD_PRV_TO_FNC, null, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                PrvToFunctionAss oldPtfAss = ptfCopier.copy(ptfAss);
                ptfAss.setAssStatus(1);
                ptfAss = ptfRepo.save(ptfAss);
                try {
                    logger.logg(AuthActions.ADD_PRV_TO_FNC, oldPtfAss, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });


        //=========================

        prvAssSpliterDTO.getPrvIdsToChangeTheDates().forEach(id->
        {
            PrvToFunctionAss ptfAss = ptfRepo.findByFncAndPrv(fncId, id);
            PrvToFunctionAss oldPtfAss = ptfCopier.copy(ptfAss);
            ptfAss.setStartsAt(startsAt);ptfAss.setEndsAt(endsAt);
            ptfAss = ptfRepo.save(ptfAss);
            try {
                logger.logg(AuthActions.CHANGE_PRV_TO_FNC_VALIDITY_PERIOD, oldPtfAss, ptfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        prvAssSpliterDTO.getPrvIdsToActivate().forEach(id->
        {
            PrvToFunctionAss ptfAss = ptfRepo.findByFncAndPrv(fncId, id);
            PrvToFunctionAss oldPtfAss = ptfCopier.copy(ptfAss);
            ptfAss.setAssStatus(1);
            ptfAss = ptfRepo.save(ptfAss);
            try {
            logger.logg(AuthActions.RESTORE_PRV_TO_FNC, oldPtfAss, ptfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        prvAssSpliterDTO.getPrvIdsToActivateAndChangeTheDates().forEach(id->
        {
            PrvToFunctionAss ptfAss = ptfRepo.findByFncAndPrv(fncId, id);
            PrvToFunctionAss oldPtfAss = ptfCopier.copy(ptfAss);
            ptfAss.setAssStatus(1);ptfAss.setStartsAt(startsAt);ptfAss.setEndsAt(endsAt);
            ptfAss = ptfRepo.save(ptfAss);
            try {
                logger.logg(AuthActions.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, oldPtfAss, ptfAss, AuthTables.ASS);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    private void assignPrvToFnc(PrvsToFncDTO dto, int status)
    {
        String action = status == 2 ? AuthActions.REMOVE_PRV_TO_FNC : status == 3 ? AuthActions.REVOKE_PRV_TO_FNC : AuthActions.ADD_PRV_TO_FNC;
        dto.getPrvIds().forEach(id->
        {
            PrvToFunctionAss ptfAss = ptfRepo.findByFncAndPrv(dto.getFncId(), id);
            if(ptfAss != null)
            {

                if(status == ptfAss.getAssStatus()) return;
                PrvToFunctionAss oldptfAss = ptfCopier.copy(ptfAss);
                ptfAss.setAssStatus(status);
                ptfRepo.save(ptfAss);
                try {
                    logger.logg(AuthActions.RESTORE_PRV_TO_FNC, oldptfAss, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                ptfAss = new PrvToFunctionAss();
                ptfAss.setPrivilege(new AppPrivilege(id));ptfAss.setFunction(new AppFunction(dto.getFncId()));
                ptfAss.setAssStatus(status); ptfAss.setStartsAt(dto.getStartsAt()); ptfAss.setEndsAt(dto.getEndsAt());
                ptfRepo.save(ptfAss);
                try {
                    logger.logg(action, null, ptfAss, AuthTables.ASS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
