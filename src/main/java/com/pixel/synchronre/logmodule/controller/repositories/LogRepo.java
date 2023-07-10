package com.pixel.synchronre.logmodule.controller.repositories;

import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.logmodule.model.entities.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LogRepo extends JpaRepository<Log, Long>
{
    @Query("""
        select new com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList(l.id,
        l.userId, l.userEmail, u.firstName, u.lastName, l.action, l.actionDateTime, 
        l.ipAddress, l.hostName, l.connectionId, f.id, f.name, c.cedNomFiliale, c.cedSigleFiliale, l.errorMessage, l.stackTrace)
        from Log l left join l.function f left join f.user u left join Cedante c on (c.cedId = u.visibilityId or c.cedId is null) where 
        (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(l.userEmail, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(u.firstName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(u.lastName, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(l.action, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.name, '') ) as string))) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(c.cedNomFiliale, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(c.cedSigleFiliale, '') ) as string))) >0
        )
        and l.action in :actions and l.connectionId = coalesce(:connId, l.connectionId)
        and (:userId is null or :userId = l.userId) and l.actionDateTime between coalesce(:debut, current_date) and coalesce(:fin, current_date)
        order by l.actionDateTime desc
""")
    Page<ConnexionList> getConnnexionLogs(@Param("actions") List<String> actions,
                                          @Param("key") String key,
                                          @Param("userId") Long userId,
                                          @Param("debut") LocalDate debut,
                                          @Param("fin") LocalDate fin,
                                          @Param("connId") String connectionId,
                                          Pageable pageable);
    @Query("select distinct l.action from Log l")
    List<String> getAllActionTypes();
}
