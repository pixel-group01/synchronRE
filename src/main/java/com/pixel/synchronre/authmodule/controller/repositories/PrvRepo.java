package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.dtos.appprivilege.PrvByTypeDTO;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PrvRepo extends JpaRepository<AppPrivilege, Long>
{
    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where upper(p.privilegeCode) = upper(?1) ")
    boolean existsByCode(String prvCode);

    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where upper(p.privilegeCode) = upper(?1) and p.privilegeId <> ?2")
    boolean existsByCode(String prvCode, Long prvId);


    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where upper(p.privilegeName) = upper(?1)")
    boolean existsByName(String name);

    @Query("select (count(p.privilegeId)>0) from AppPrivilege p where upper(p.privilegeName) = upper(?1) and p.privilegeId <> ?2")
    boolean existsByName(String name, Long prvId);

    @Query("SELECT prv FROM AppPrivilege prv WHERE " +
            "locate(upper(coalesce(:searchKey, '')) , upper(CAST(FUNCTION('strip_accents', prv.privilegeCode) AS string )) ) > 0 OR " +
            "locate(upper(coalesce(:searchKey, '')), upper(CAST(FUNCTION('strip_accents', prv.privilegeName) as string))) > 0")
    Set<AppPrivilege> searchPrivileges(@Param("searchKey") String searchKey);

    @Query("SELECT prv FROM AppPrivilege prv WHERE " +
            "locate(upper(coalesce(:searchKey, '') ) , upper(CAST(FUNCTION('strip_accents', prv.privilegeCode) AS string )) ) > 0 OR " +
            "locate(upper(coalesce(:searchKey, '')), upper(CAST(FUNCTION('strip_accents', prv.privilegeName) as string))) > 0")
    Page<AppPrivilege> searchPrivileges(@Param("searchKey") String searchKey, Pageable pageable);


    @Query("""
    select prv0 from AppPrivilege  prv0 where 
    (
     prv0.privilegeId in 
        (select ptr.privilege.privilegeId from PrvToRoleAss ptr where ptr.role.roleId in 
            (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date) ) and 
            ptr.assStatus = 1 and 
            prv0.privilegeId not in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 3)
        ) or 
     prv0.privilegeId in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date) and coalesce(ptf.endsAt, current_date))
    ) 
    """)
    Set<AppPrivilege> getFunctionPrvs(Long fctId);

    @Query("""
    select prv0.privilegeCode from AppPrivilege  prv0 where 
    (
     prv0.privilegeId in 
        (select ptr.privilege.privilegeId from PrvToRoleAss ptr where ptr.role.roleId in 
            (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date) ) and 
            ptr.assStatus = 1 and 
            prv0.privilegeId not in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 3)
        ) or 
     prv0.privilegeId in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date) and coalesce(ptf.endsAt, current_date))
    ) 
    """)
    Set<String> getFunctionPrvCodes(Long fctId);

    @Query("""
    select prv0.privilegeId from AppPrivilege  prv0 where 
    (
     prv0.privilegeId in 
        (select ptr.privilege.privilegeId from PrvToRoleAss ptr where ptr.role.roleId in 
            (select rtf.role.roleId from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date) ) and 
            ptr.assStatus = 1 and 
            prv0.privilegeId not in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 3)
        ) or 
     prv0.privilegeId in (select ptf.privilege.privilegeId from PrvToFunctionAss ptf where ptf.function.id = ?1 and ptf.assStatus = 1 and current_date between coalesce(ptf.startsAt, current_date) and coalesce(ptf.endsAt, current_date))
    ) 
    """)
    Set<Long> getFunctionPrvIds(Long fctId);

    @Query("""
            select new com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO
            (
                p.privilegeId, p.privilegeCode, p.privilegeName, p.prvType.name
            ) from AppPrivilege p where p.prvType.typeId = ?1
             """)
    Set<ReadPrvDTO> getTypePriveleges(Long typeId);

    @Query("""
            select new com.pixel.synchronre.authmodule.model.dtos.appprivilege.PrvByTypeDTO
            (
                p.prvType.typeId, p.prvType.name, p.prvType.uniqueCode
            ) from AppPrivilege p where p.prvType.typeId = ?1
             """)
    Set<PrvByTypeDTO> getPrvByTypeDTOS(Long typeId);
}