package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.PrvToRoleAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PrvToRoleAssRepo extends JpaRepository<PrvToRoleAss, Long>
{
    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    boolean existsByRoleAndPrivilege(Long roleId, Long privilegeId);

    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = ?3")
    boolean existsByRoleAndPrivilege(Long roleId, Long privilegeId, int assStatus);

    @Query("select p from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    PrvToRoleAss findByRoleAndPrivilege(Long roleId, Long prvId);

    @Query("select p from PrvToRoleAss p where p.role.roleId = ?1")
    List<PrvToRoleAss> findByRole(Long roleId);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.assStatus = 1  and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date")
    Set<Long> findActivePrvIdsForRole(Long roleId);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId in ?1 and p.assStatus = 1  and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)")
    Set<Long> findActivePrvIdsForRoles(Set<Long> roleIds);

    @Query("""
    select new com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO(
        p.privilege.privilegeId, p.privilege.privilegeCode, p.privilege.privilegeName, p.privilege.prvType.name )
    from PrvToRoleAss p where p.role.roleId in ?1 and p.assStatus = 1  and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)
    """)
    Set<ReadPrvDTO> findActivePrivilegesForRoles(Set<Long> roleIds);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.assStatus = 1 and current_date between coalesce(p.startsAt, current_date) and coalesce(p.endsAt, current_date) and p.privilege.privilegeId not in ?2")
    Set<Long> findPrvIdsForRoleNotIn(Long roleId, Set<Long> prvIdsToBeSet);

    //@Query("select p.privilege.privilegeId from PrvToRoleAss p where (p.role.roleId <> ?1 or (p.role.roleId = ?1 and (p.ass.assStatus <>1 or coalesce(p.ass.startsAt, current_date) > current_date or coalesce(p.ass.endsAt, current_date) < current_date ))) and p.privilege.privilegeId in ?2")
    //Set<Long> findPrvIdsNotBelongingToRoleIn(Long roleId, Set<Long> prvIdsToBeSet);

    @Query("select p.privilegeId from AppPrivilege p where p.privilegeId in ?2 and not exists (select ptr.assId from PrvToRoleAss ptr where ptr.role.roleId = ?1 and ptr.privilege.privilegeId = p.privilegeId and current_date between coalesce(ptr.startsAt, current_date) and coalesce(ptr.endsAt, current_date))")
    Set<Long> findPrvIdsNotBelongingToRoleIn(Long roleId, Set<Long> prvIds);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.assStatus = 1 and p.privilege.privilegeId in ?2 and coalesce(p.startsAt, ?3) = ?3 and coalesce(p.endsAt, ?4) = ?4 and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date")
    Set<Long> findActivePrvIdsForRoleIn_sameDates(Long roleId, Set<Long> prvIdsToBeSet, LocalDate startsAt, LocalDate endsAt);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = :roleId and p.assStatus = 1 and p.privilege.privilegeId in :prvIdsToBeSet and( p.startsAt <> :startsAt or p.endsAt <> :endsAt)")
    Set<Long> findActivePrvIdsForRoleIn_otherDates(@Param("roleId") Long roleId, @Param("prvIdsToBeSet")Set<Long> prvIdsToBeSet, @Param("startsAt")LocalDate startsAt, @Param("endsAt") LocalDate endsAt);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.assStatus <> 1 and p.privilege.privilegeId in ?2")
    Set<Long> findNoneActivePrvIdsForRoleIn(Long roleId, Set<Long> prvIdsToBeSet);



    @Query("select p from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    Set<AppPrivilege> getRolePrivileges(long roleId);

    @Query("select (count(a.assId) > 0) from PrvToRoleAss a where a.role.roleId = ?1 and a.privilege.privilegeId = ?2 and a.assStatus = 1 and current_date between coalesce(a.startsAt, current_date ) and coalesce(a.endsAt, current_date)")
    boolean roleHasValidPrivilege(Long roleId, Long privilegeId);

    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId = ?2 and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))")
    boolean roleHasPrivilege(long roleId, long prvId);

    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId in ?2 and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    boolean roleHasAnyPrivilege(long roleId, List<Long> prvIds);

    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = ?1 and ptrAss.role.roleId in ?2 and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    boolean prvBelongToAnyRole(long prvId, Set<Long> roleIds);
}