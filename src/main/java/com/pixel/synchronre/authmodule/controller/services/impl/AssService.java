package com.pixel.synchronre.authmodule.controller.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssService //implements IAssService
{/*
    private final AssignationMapper assMapper;
    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final ILogService logger;
    private final PrvRepo prvRepo;
    private final FunctionRepo functionRepo;
    private final RoleToFunctionAssRepo rtfRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final IJwtService jwtService;


    @Override @Transactional
    public AppFunction createPrincipalAss(CreateFunctionDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss = assMapper.mapToPrincipalAss(dto);
        if(!functionRepo.userHasAnyPrincipalAss(dto.getUserId())) principalAss.getAss().setAssStatus(1);
        principalAss = functionRepo.save(principalAss);
        PrincipalAssHisto histo = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.PRINCIPAL_ASSIGNATION_CREATED, eai);
        principalAssHistoRepo.save(histo);

        Long principalAssId = principalAss.getAssId();
        dto.getRoleIds().forEach(id->
        {
            RoleAss roleAss = new RoleAss();
            roleAss.setAss(new Ass(1, dto.getStartsAt(), dto.getEndsAt()));
            roleAss.setPrincipalAss(new PrincipalAss(principalAssId));
            roleAss.setRole(new AppRole(id));
            roleAss = rtfRepo.save(roleAss);

            RoleAssHisto roleAssHisto = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(roleAssHisto);
        });
        PrvAssSpliterDTO prvAssSpliterDTO = assMapper.mapToPrvAssSpliterDTO(dto.getPrvIds(), dto.getRoleIds(), principalAssId, dto.getStartsAt(), dto.getEndsAt(), false);
        this.treatPrvsAssignation(prvAssSpliterDTO, principalAssId, dto.getStartsAt(), dto.getEndsAt(), eai);
        return principalAss;
    }



    @Override @Transactional
    public void setPrincipalAssAsDefault(Long principalAssId)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(principalAssId).orElse(null);
        if(principalAss == null) return;
        functionRepo.findActiveByUser(principalAss.getUser().getUserId()).forEach(prAss->
        {
            if(!prAss.getAssId().equals(principalAssId))
            {
                prAss.setAss(new Ass(2, prAss.getAss().getStartsAt(), prAss.getAss().getEndsAt()));
                PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(prAss, AssignationEventTypes.ASSIGNATION_SET_AS_NONE_DEFAULT, eai);
                principalAssHistoRepo.save(principalAssHisto);
            }

        });
        if(principalAss.getAss().getAssStatus() == 1) return;
        principalAss.setAss(new Ass(1, principalAss.getAss().getStartsAt(), principalAss.getAss().getEndsAt()));
        PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.ASSIGNATION_SET_AS_DEFAULT, eai);
        scm.refreshSecurityContext();
        principalAssHistoRepo.save(principalAssHisto);

        AppUser user = principalAss.getUser();
        principalAss.getUser().setDefaultAssId(principalAssId);
        user = userRepo.save(user);
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.CHANGE_DEFAULT_ASSIGNATION_ID, eai);
        userHistoRepo.save(userHisto);
    }

    @Override @Transactional
    public void revokePrincipalAss(Long principalAssId)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(principalAssId).orElse(null);
        if(principalAss == null) return;
        if(principalAss.getAss().getAssStatus() == 3) return;
        principalAss.getAss().setAssStatus(3);
        PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.ASSIGNATION_DEACTIVATED, eai);
        principalAssHistoRepo.save(principalAssHisto);
    }

    @Override @Transactional
    public void restorePrincipalAss(Long principalAssId)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(principalAssId).orElse(null);
        if(principalAss == null) return;
        if(principalAss.getAss().getAssStatus() == 1 || principalAss.getAss().getAssStatus() == 2) return;
        principalAss.getAss().setAssStatus(2);
        PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, eai);
        principalAssHistoRepo.save(principalAssHisto);
    }

    @Override @Transactional
    public PrincipalAss setPrincipalAssAuthorities(SetAuthoritiesToPrincipalAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(dto.getPrincipalAssId()).orElse(null);
        if(principalAss == null) return principalAss;
        Long principalAssId = principalAss.getAssId();
        Set<Long> roleIds = dto.getRoleIds(); Set<Long> prvIds = dto.getPrvIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleIds, principalAssId, startsAt, endsAt, true);
        PrvAssSpliterDTO prvAssSpliterDTO = assMapper.mapToPrvAssSpliterDTO(prvIds, roleIds, principalAssId, startsAt, endsAt, true);
        treatRolesAssignation(roleAssSpliterDTO, principalAssId, startsAt, endsAt, eai);

        this.treatPrvsAssignation(prvAssSpliterDTO, principalAssId, dto.getStartsAt(), dto.getEndsAt(), eai);
        return principalAss;
    }

    @Override @Transactional
    public void updatePrincipalAss(UpdatePrincipalAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(dto.getAssId()).orElse(null);
        if(principalAss == null) return;

        Long principalAssId = principalAss.getAssId();
        Set<Long> roleIds = dto.getRoleIds();
        Set<Long> prvIds = dto.getPrvIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();

        principalAss.setIntitule(dto.getIntitule());
        principalAss.getAss().setStartsAt(startsAt);
        principalAss.getAss().setEndsAt(endsAt);

        PrincipalAssHisto histo = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.ASSIGNATION_UPDATED, eai);
        principalAssHistoRepo.save(histo);

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleIds, principalAssId, startsAt, endsAt, true);

        PrvAssSpliterDTO prvAssSpliterDTO = assMapper.mapToPrvAssSpliterDTO(prvIds, roleIds, principalAssId, startsAt, endsAt, true);

        treatRolesAssignation(roleAssSpliterDTO, principalAssId, startsAt, endsAt, eai);

        this.treatPrvsAssignation(prvAssSpliterDTO, principalAssId, dto.getStartsAt(), dto.getEndsAt(), eai);
    }


    @Override @Transactional
    public void addRolesToPrincipalAss(RolesAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = functionRepo.findById(dto.getPrincipalAssId()).orElse(null);
        if(principalAss == null) return;

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(dto.getRoleIds(), principalAss.getAssId(), dto.getStartsAt(), dto.getEndsAt());
        this.treatRolesAssignation(roleAssSpliterDTO, principalAss.getAssId(), dto.getStartsAt(), dto.getEndsAt(), eai);
    }

    @Override @Transactional
    public void removeRolesToPrincipalAss(RolesAssDTO dto)
    {
        dto.getRoleIds().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(dto.getPrincipalAssId(), id);
            if(roleAss != null) roleAss.setAss(new Ass(2, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            else
            {
                roleAss = new RoleAss();
                roleAss.setAss(new Ass(2, dto.getStartsAt(), dto.getEndsAt()));
                roleAss.setPrincipalAss(new PrincipalAss(dto.getPrincipalAssId()));
            }

            roleAss = rtfRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_REVOKED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
            roleAssHistoRepo.save(histo);
        });
    }

    @Override @Transactional
    public void addPrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        assignPrvToPrincipalAss(dto, 1);
    }

    @Override @Transactional
    public void removePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        dto.getPrvIds().forEach(prvId->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(dto.getPrincipalAssId(), prvId);
            int newAssStatus = prvRepo.anyPrincipalAssRoleHasPrivilegeId(dto.getPrincipalAssId(), prvId) ? 3 : 2;
            if(prvAss != null)
            {
                if(prvAss.getAss().getAssStatus()==3) return;
                prvAss.setAss(new Ass(newAssStatus, dto.getStartsAt(), dto.getEndsAt()));
            }
            else
            {
                prvAss = new PrvAss();
                prvAss.setAss(new Ass(newAssStatus, dto.getStartsAt(), dto.getEndsAt()));
                prvAss.setPrincipalAss(new PrincipalAss(dto.getPrincipalAssId()));
            }
            AssignationEventTypes eventType = newAssStatus == 3 ? AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS :
                    newAssStatus == 2 ? AssignationEventTypes.PRIVILEGE_REMOVED_TO_PRINCIPAL_ASS : AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS;
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, eventType, scm.getEventActorIdFromSCM());
            prvAssHistoRepo.save(histo);
        });
    }

    @Override @Transactional
    public void revokePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        assignPrvToPrincipalAss(dto, 3);
    }

    @Override @Transactional
    public void restorePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(dto.getPrincipalAssId(), id);
            if(prvAss == null) return;
            if(prvAss.getAss().getAssStatus() != 3) return;
            prvAss.setAss(new Ass(2, prvAss.getAss().getStartsAt(), prvAss.getAss().getEndsAt()));

            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_RESTORED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
            prvAssHistoRepo.save(histo);
        });
    }

    @Override @Transactional
    public void setRolePrivileges(PrvsToRoleDTO dto)
    {
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
            prvToRoleAss.setAss(new Ass(2, prvToRoleAss.getAss().getStartsAt(), prvToRoleAss.getAss().getEndsAt()));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_REVOKED_TO_ROLE, scm.getEventActorIdFromSCM());
            logger.save(histo);
        });

        prvIdsToBeAdded.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            if(prvToRoleAss == null)
            {
                prvToRoleAss = new PrvToRoleAss(null, new Ass(1, startsAt, endsAt), new AppPrivilege(id), new AppRole(roleId));
                prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
                PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
                logger.save(histo);
            }
            else if(prvToRoleAss.getAss().getAssStatus() != 1)
            {
                    prvToRoleAss.getAss().setAssStatus(1);
                    PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
                    logger.save(histo);
            }

        });

        prvIdsToChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            prvToRoleAss.setAss(new Ass(prvToRoleAss.getAss().getAssStatus(), startsAt, endsAt));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.VALIDITY_PERIOD_CHANGED, scm.getEventActorIdFromSCM());
            logger.save(histo);
        });

        prvIdsToActivateAndChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            prvToRoleAss.setAss(new Ass(1, startsAt, endsAt));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, scm.getEventActorIdFromSCM());
            logger.save(histo);
        });
    }

    @Override @Transactional
    public void addPrivilegesToRole(PrvsToRoleDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(dto.getRoleId(), id);
            if(prvToRoleAss != null)
            {
                if(1 == prvToRoleAss.getAss().getAssStatus()) return;
            }
            else
            {
                prvToRoleAss = new PrvToRoleAss();
                prvToRoleAss.setRole(new AppRole(dto.getRoleId()));
                prvToRoleAss.setPrivilege(new AppPrivilege(id));
            }
            prvToRoleAss.setAss(new Ass(1, dto.getStartsAt(), dto.getEndsAt()));

            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_ROLE, scm.getEventActorIdFromSCM());
            logger.save(histo);
        });
    }

    @Override @Transactional
    public void removePrivilegesToRole(PrvsToRoleDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(dto.getRoleId(), id);
            if(prvToRoleAss != null)
            {
                if(2 == prvToRoleAss.getAss().getAssStatus() || 3 == prvToRoleAss.getAss().getAssStatus()) return;
            }
            else
            {
                prvToRoleAss = new PrvToRoleAss();
                prvToRoleAss.setRole(new AppRole(dto.getRoleId()));
            }
            prvToRoleAss.setAss(new Ass(2, dto.getStartsAt(), dto.getEndsAt()));

            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_ROLE, scm.getEventActorIdFromSCM());
            logger.save(histo);
        });
    }

    private void treatRolesAssignation(RoleAssSpliterDTO roleAssSpliterDTO, Long principalAssId, LocalDate startsAt, LocalDate endsAt, EventActorIdentifier eai)
    {
        roleAssSpliterDTO.getRoleIdsToBeRemoved().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(2, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            roleAss = rtfRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_REVOKED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToBeAddedAsNew().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(principalAssId, id);
            if(roleAss == null)
            {
                roleAss = new RoleAss();
                roleAss.setAss(new Ass(1, startsAt, endsAt));
                roleAss.setRole(new AppRole(id));
                roleAss.setPrincipalAss(new PrincipalAss(principalAssId));
            }
            else
            {
                roleAss.setAss(new Ass(1, startsAt, endsAt));
            }
            roleAss = rtfRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToChangeTheDates().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(roleAss.getAss().getAssStatus(), startsAt, endsAt));
            roleAss = rtfRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.VALIDITY_PERIOD_CHANGED, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToActivate().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.getAss().setAssStatus(1);
            //roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToActivateAndChangeTheDates().forEach(id->
        {
            RoleAss roleAss = rtfRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(1, startsAt, endsAt));
            //roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, eai);
            roleAssHistoRepo.save(histo);
        });
    }

    private void treatPrvsAssignation(PrvAssSpliterDTO prvAssSpliterDTO, Long principalAssId, LocalDate startsAt, LocalDate endsAt, EventActorIdentifier eai)
    {

        prvAssSpliterDTO.getPrvIdsToBeRemoved().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(principalAssId, id);
            if(prvAss == null)
            {
                prvAss = new PrvAss();
                prvAss.setAss(new Ass(3, startsAt, endsAt));
                prvAss.setPrincipalAss(new PrincipalAss(principalAssId));
                prvAss.setPrv(new AppPrivilege(id));
            }
            else
            {
                prvAss.getAss().setAssStatus(3);
            }
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto prvAssHisto = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS, eai);
            prvAssHistoRepo.save(prvAssHisto);
        });

        prvAssSpliterDTO.getPrvIdsToBeAddedAsNew().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(principalAssId, id);
            if(prvAss == null)
            {
                prvAss = new PrvAss();
                prvAss.setAss(new Ass(1, startsAt, endsAt));
                prvAss.setPrincipalAss(new PrincipalAss(principalAssId));
                prvAss.setPrv(new AppPrivilege(id));
                prvAss = prvRepo.save(prvAss);
                PrvAssHisto prvAssHisto = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
                prvAssHistoRepo.save(prvAssHisto);
            }
            else
            {
                prvAss.setAss(new Ass(1, startsAt, endsAt));
                prvAss = prvRepo.save(prvAss);
                PrvAssHisto prvAssHisto = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_RESTORED_TO_PRINCIPAL_ASS, eai);
                prvAssHistoRepo.save(prvAssHisto);
            }
        });


        //=========================

        prvAssSpliterDTO.getPrvIdsToChangeTheDates().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(principalAssId, id);
            prvAss.setAss(new Ass(prvAss.getAss().getAssStatus(), startsAt, endsAt));
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.VALIDITY_PERIOD_CHANGED, eai);
            prvAssHistoRepo.save(histo);
        });

        prvAssSpliterDTO.getPrvIdsToActivate().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(principalAssId, id);
            prvAss.getAss().setAssStatus(1);
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, eai);
            prvAssHistoRepo.save(histo);
        });

        prvAssSpliterDTO.getPrvIdsToActivateAndChangeTheDates().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(principalAssId, id);
            prvAss.setAss(new Ass(1, startsAt, endsAt));
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, eai);
            prvAssHistoRepo.save(histo);
        });
    }

    private void assignPrvToPrincipalAss(PrvsAssDTO dto, int status)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvAss prvAss = prvRepo.findByPrincipalAssAndPrv(dto.getPrincipalAssId(), id);
            if(prvAss != null)
            {
                if(status == prvAss.getAss().getAssStatus()) return;
                prvAss.setAss(new Ass(status, prvAss.getAss().getStartsAt(), prvAss.getAss().getEndsAt()));
            }
            else
            {
                prvAss = new PrvAss();
                prvAss.setAss(new Ass(status, dto.getStartsAt(), dto.getEndsAt()));
                prvAss.setPrincipalAss(new PrincipalAss(dto.getPrincipalAssId()));
            }
            AssignationEventTypes eventType = status == 1 ? AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS :
                    status == 2 ? AssignationEventTypes.PRIVILEGE_REMOVED_TO_PRINCIPAL_ASS : AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS;
            prvAss = prvRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, eventType, scm.getEventActorIdFromSCM());
            prvAssHistoRepo.save(histo);
        });
    }*/
}
