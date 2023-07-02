package com.pixel.synchronre.logmodule.controller.repositories;

import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.logmodule.model.entities.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface LogRepo extends JpaRepository<Log, Long>
{
    @Query("""
        select new com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList(
        l.userId, l.userEmail, u.firstName, u.lastName, l.action, l.actionDateTime, 
        l.ipAddress, l.hostName, l.connectionId, f.id, f.name)
        from Log l left join l.function f left join f.user u where l.action = 'Login' and
        (:userId is null or :userId = l.userId) and l.actionDateTime between coalesce(:debut, current_date) and coalesce(:fin, current_date)
""")
    Page<ConnexionList> getConnnexionLogs(@Param("userId") Long userId,
                                          @Param("debut") LocalDate debut,
                                          @Param("fin") LocalDate fin, Pageable pageable);
}
