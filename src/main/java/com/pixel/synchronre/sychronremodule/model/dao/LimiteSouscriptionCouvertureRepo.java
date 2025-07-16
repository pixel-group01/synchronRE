package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LimiteSouscriptionCouvertureRepo extends JpaRepository<Association, Long>
{

    /**
     * Les id de couverture que la limiteSouscriptionId possède mais qui ne sont pas dans couIds
     */
    @Query("""
        select c.couId from Couverture c where c.couId not in ?2 and 
        exists(select a from Association a where a.limiteSouscription.limiteSouscriptionId = ?1 and a.couverture.couId = c.couId and a.type.uniqueCode = 'LIMIT-SOUS-COUV')
        """)
    List<Long> getCouIdsToRemove(Long limiteSouscriptionId, List<Long> couIds);

    /**
     * Les id de couverture que la limiteSouscriptionId ne possède pas mais qui sont dans couIds
     */
    @Query("""
        select c.couId from Couverture c where c.couId in ?2 and 
        not exists(select a from Association a where a.limiteSouscription.limiteSouscriptionId = ?1 and a.couverture.couId = c.couId and a.type.uniqueCode = 'LIM-SOU-COUV')
        """)
    List<Long> getCouIdsToAdd(Long limiteSouscriptionId, List<Long> couIds);

    @Modifying
    @Query("delete from Association a where a.limiteSouscription.limiteSouscriptionId = ?1 and a.couverture.couId = ?2 and a.type.uniqueCode = 'LIM-SOU-COUV'")
    void removeCouvertureOnLimite(Long limiteSouscriptionId, Long couId);
}
