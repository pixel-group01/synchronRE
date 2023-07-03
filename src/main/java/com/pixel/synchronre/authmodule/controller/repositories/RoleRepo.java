package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleRepo extends JpaRepository<AppRole, Long>
{
    @Query("select r.roleName as roleName from AppRole r")
    Set<String> getRoleNames();

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1)")
    boolean existsByName(String roleName);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1)")
    boolean existsByCode(String roleCode);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1) and a.roleId <> ?2")
    boolean existsByName(String roleName, Long roleId);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1) and a.roleId <> ?2")
    boolean existsByCode(String roleCode, Long roleId);

    @Query("select a from AppRole a where upper(CAST(function('strip_accents', a.roleCode) as string)) like upper(concat('%', coalesce(?1, ''), '%')) or upper(CAST(function('strip_accents', a.roleName) AS string)) like upper(concat('%', coalesce(?1, ''), '%')) order by a.roleName")
    Page<AppRole> searchRoles(String searchKey, Pageable pageable);
    @Query("""
    select rtf.role from RoleToFncAss rtf where rtf.assStatus = 1 and rtf.function.id = ?1 and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date ) 
    """)
    Set<AppRole> getFunctionRoles(Long fncId);

    @Query("""
    select rtf.role.roleName from RoleToFncAss rtf where rtf.assStatus = 1 and rtf.function.id = ?1 and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date ) 
    """)
    Set<String> getFunctionRoleNames(Long fncId);

    @Query("select r from AppRole r where r.roleCode = ?1")
    AppRole findByRoleCode(String s);
}
