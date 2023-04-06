package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.authmodule.model.entities.PrvToFunctionAss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PrvToFunctionAssRepo extends JpaRepository<PrvToFunctionAss, Long>
{
    @Query("select p from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeId = p.privilegeId and ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date ) and coalesce(ptf.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date ) and coalesce(rtf.endsAt, current_date)) and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where p.privilegeId = ptf2.privilege.privilegeId and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    Set<AppPrivilege> getFncPrivileges(Long fncId);

    @Query("select p from AppPrivilege p where (locate(?2, upper(cast(function('strip_accents', p.privilegeName) as string)) ) > 0 or locate(?2, upper(cast(function('strip_accents', p.privilegeCode) as string))) > 0) and (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeId = p.privilegeId and ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date ) and coalesce(ptf.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date ) and coalesce(rtf.endsAt, current_date)) and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where p.privilegeId = ptf2.privilege.privilegeId and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    Page<PrvToFunctionAss> searchActivePrvsByFnc(Long fncId, String key, Pageable pageable);

    @Query("select p.privilegeId from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeId = p.privilegeId and ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date ) and coalesce(ptf.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date ) and coalesce(rtf.endsAt, current_date)) and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where p.privilegeId = ptf2.privilege.privilegeId and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    Set<Long> getFncPrvIds(Long fncId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = ?2 and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date ) and coalesce(rtf.endsAt, current_date)) and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date)))")
    boolean anyFncRoleHasPrivilegeId(Long fncId, Long prvId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeId = ?2 and ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date ) and coalesce(ptf.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = ?2 and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date ) and coalesce(rtf.endsAt, current_date)) and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where ?2 = ptf2.privilege.privilegeId and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    boolean fncHasPrivilegeId(Long fncId, Long prvId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeCode = ?2 and ptf.function.id = ?1 and ptf.assStatus = 1 and coalesce(ptf.startsAt, current_date ) <= current_date and coalesce(ptf.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeCode = ?2 and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and coalesce(rtf.startsAt, current_date ) <= current_date and coalesce(rtf.endsAt, current_date) >= current_date) and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where ?2 = ptf2.privilege.privilegeCode and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    boolean fncHasPrivilegeCode(Long fncId, String prvCode);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeId in ?2 and ptf.function.id = ?1 and ptf.assStatus = 1 and coalesce(ptf.startsAt, current_date ) <= current_date and coalesce(ptf.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId in ?2 and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and coalesce(rtf.startsAt, current_date ) <= current_date and coalesce(rtf.endsAt, current_date) >= current_date) and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where ptf2.privilege.privilegeId in ?2  and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    boolean fncHasAnyPrivilege(Long fncId, List<Long> prvIds);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptf from PrvToFunctionAss ptf where ptf.privilege.privilegeCode in ?2 and ptf.function.id = ?1 and ptf.assStatus = 1 and coalesce(ptf.startsAt, current_date ) <= current_date and coalesce(ptf.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeCode in ?2 and ptrAss.role.roleId in (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and coalesce(rtf.startsAt, current_date ) <= current_date and coalesce(rtf.endsAt, current_date) >= current_date) and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)) and " +
            "not exists (select ptf2 from PrvToFunctionAss ptf2 where ptf2.privilege.privilegeCode in ?2  and ptf2.assStatus = 3 and ptf2.function.id = ?1)")
    boolean fncHasAnyPrivilegeCode(Long fncId, List<String> prvCodes);

    @Query("select p from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2")
    PrvToFunctionAss findByFncAndPrv(Long fncId, Long privilegeId);

    @Query("select p from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = ?3")
    PrvToFunctionAss findByFncAndPrvAndStatus(Long fncId, Long privilegeId, int assStatus);

    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2")
    boolean existsByFncAndPrv(Long fncId, Long privilegeId);

    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = ?3")
    boolean existsByFncAndPrvAndStatus(Long fncId, Long privilegeId, int assStatus);

    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = 1 and (coalesce(p.startsAt, current_date) <> coalesce(?3, current_date) or coalesce(p.endsAt, current_date) <> coalesce(?4, current_date))")
    boolean existsActiveByFncAndPrv_OtherDates(Long fncId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);


    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = 1 and (coalesce(p.startsAt, current_date) = coalesce(?3, current_date) and coalesce(p.endsAt, current_date) = coalesce(?4, current_date))")
    boolean existsActiveByFncAndPrv_SameDates(Long fncId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);

    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus <> 1 and (coalesce(p.startsAt, current_date) <> coalesce(?3, current_date) or coalesce(p.endsAt, current_date) <> coalesce(?4, current_date))")
    boolean existsNoneActiveByFncAndPrv_OtherDates(Long fncId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);

    @Query("select (count(p.assId)>0) from PrvToFunctionAss p where p.function.id = ?1 and p.privilege.privilegeId = ?2 and p.assStatus <> 1 and (coalesce(p.startsAt, current_date) = coalesce(?3, current_date) and coalesce(p.endsAt, current_date) = coalesce(?4, current_date))")
    boolean existsNoneActiveByFncAndPrv_SameDates(Long fncId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);

}