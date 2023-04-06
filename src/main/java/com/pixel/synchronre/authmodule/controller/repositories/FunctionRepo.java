package com.pixel.synchronre.authmodule.controller.repositories;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FunctionRepo extends JpaRepository<AppFunction, Long>
{
    @Query("select p from AppFunction p where p.user.userId= ?1")
    Set<AppFunction> findAllByUser(Long userId);

    @Query("select p from AppFunction p where p.user.userId= ?1 and p.fncStatus in (1, 2) and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)") //coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date
    Set<AppFunction> findNoneRevokedByUser(Long userId);

    @Query("select p from AppFunction p where p.user.userId= ?1 and p.fncStatus = 1 and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date")
    Set<AppFunction> findActiveByUser(Long userId);

    @Query("select f.id from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<Long> getCurrentFncIds(Long userId);

    @Query("select f.visibilityId from AppFunction f where f.user.email= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<Long> getCurrentFncVisibilityIds(String username);

    @Query("select (count(ps)>0) from AppFunction ps where ps.user.userId = ?1")
    boolean userHasAnyAppFunction(Long userId);

    @Query("select ps.user.userId from AppFunction ps where ps.id = ?1")
    Long getUserId(Long id);
}