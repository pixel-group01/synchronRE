package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.RoleToFncAss;
import com.pixel.synchronre.authmodule.model.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RoleToFunctionAssRepo extends JpaRepository<RoleToFncAss, Long>
{
    @Query("select r from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleId = r.roleId and roleToFncAss.assStatus = 1 and coalesce(roleToFncAss.startsAt, current_date ) <= current_date and coalesce(roleToFncAss.endsAt, current_date) >= current_date)")
    Set<AppRole> getFncRoles(Long fncId);

    @Query("select (count(r.roleId)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleId = ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasRole(Long fncId, Long roleId);

    @Query("select (count(r.roleId)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleName = ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasRole(Long fncId, String roleName);

    @Query("select (count(r.roleId)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleId in ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasAnyRole(Long fncId, List<Long> roleIds);

    @Query("select (count(r.roleId)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleName in ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasAnyRoleName(Long fncId, List<String> roleNames);

    @Query("select roleToFncAss.role.roleId from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1  and roleToFncAss.role.roleId not in ?2")
    Set<Long> findRoleIdsForFncNotIn(Long principalAssId, Set<Long> roleIdsToBeSet);

    @Query("select roleToFncAss.role.roleId from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.function.fncStatus = 1")
    Set<Long> findActiveRoleIdsBelongingToFnc(Long principalAssId);

    @Query("select roleToFncAss.role.roleId from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1 and roleToFncAss.startsAt = ?2 and roleToFncAss.startsAt = ?3")
    Set<Long> findActiveRoleIdsBelongingToFnc_sameDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select roleToFncAss.role.roleId from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1 and (roleToFncAss.startsAt <> ?2 or roleToFncAss.endsAt <> ?3)")
    Set<Long> findActiveRoleIdsBelongingToFnc_otherDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.roleId from AppRole r where not exists( select r2 from RoleToFncAss r2 where r2.function.id = ?1 and r2.role.roleId = r.roleId)")
    Set<Long> findRoleIdsNotBelongingToFnc(Long fncId);

    @Query("select r.role.roleId from RoleToFncAss r where r.function.id = ?1 and r.assStatus <> 1")
    Set<Long> findNoneActiveRoleIdsBelongingToFnc(Long fncId);

    @Query("select r.role.roleId from RoleToFncAss r where r.function.id = ?1 and r.function.fncStatus <> 1 and r.function.startsAt = ?2 and r.function.endsAt = ?3")
    Set<Long> findNoneActiveRoleIdsBelongingToFnc_sameDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.role.roleId from RoleToFncAss r where r.function.id = ?1 and r.function.fncStatus <> 1 and (r.startsAt <> ?2 or r.endsAt <> ?3)")
    Set<Long> findNoneActiveRoleIdsBelongingToFnc_otherDates(Long principalAssId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and r.role.roleId = ?2")
    RoleToFncAss findByFncAndRole(Long fncId, Long roleId);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and r.assStatus = 1 and current_date between coalesce(r.startsAt, current_date) and coalesce(r.endsAt, current_date)")
    List<RoleToFncAss> findActiveByFnc(Long fncId);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and locate(?2, cast(function('strip_accents', r.role.roleName) as string))>0 and r.assStatus = 1 and current_date between coalesce(r.startsAt, current_date) and coalesce(r.endsAt, current_date)")
    Page<RoleToFncAss> searchActiveByFnc(Long fncId, String key, Pageable pageable);
}