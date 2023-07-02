package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FunctionRepo extends JpaRepository<AppFunction, Long>
{
    @Query("select p from AppFunction p where p.user.userId= ?1 order by p.fncStatus asc")
    Set<AppFunction> findAllByUser(Long userId);

    @Query("select p from AppFunction p where p.user.userId= ?1 and p.fncStatus in (1, 2) and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)") //coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date
    Set<AppFunction> findNoneRevokedByUser(Long userId);

    @Query("select p from AppFunction p where p.user.userId= ?1 and (p.fncStatus = 1 or p.fncStatus = 2) and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date order by p.fncStatus asc")
    List<AppFunction> findActiveByUser(Long userId);

    @Query("select f.id from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<Long> getCurrentFncIds(Long userId);

    @Query("select f.name from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<String> getCurrentFncNames(Long userId);

    @Query("select f.visibilityId from AppFunction f where f.user.email= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<Long> getCurrentFncVisibilityIds(String username);

    @Query("select (count(ps)>0) from AppFunction ps where ps.user.userId = ?1")
    boolean userHasAnyAppFunction(Long userId);

    @Query("select (count(ps)>0) from AppFunction ps where ps.user.email = ?1")
    boolean userHasAnyAppFunction(String username);

    @Query("select f.user.userId from AppFunction f where f.id = ?1")
    Long getUserId(Long id);

    @Query("select (count(u.userId) = 1) from AppFunction f join f.user u where u.currentFunctionId = ?1 and u.userId = ?2 and f.fncStatus = 1 and current_date between coalesce(f.startsAt, current_date) and coalesce(f.endsAt, current_date)")
    boolean functionIsCurrentForUser(Long fncId, Long userId);
}