package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Association;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.SousLimite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SousLimiteCouvertureRepo extends JpaRepository<Association, Long>
{

    /**
     * Les id de couverture que la sousLimiteId possède mais qui ne sont pas dans couIds
     */
    @Query("""
        select c.couId from Couverture c where c.couId not in ?2 and 
        exists(select a from Association a where a.sousLimite.sousLimiteSouscriptionId = ?1 and a.couverture.couId = c.couId and a.type.uniqueCode = 'SOU-LIM-COUV')
        """)
    List<Long> getCouIdsToRemove(Long sousLimiteId, List<Long> couIds);

    @Modifying
    @Query("""
    delete from Association a
    where a.sousLimite.sousLimiteSouscriptionId = ?1
      and a.couverture.couId = ?2
      and a.type.typeId = ?3
""")
    void removeCouvertureOnSousLimite(Long sousLimiteId, Long couId, Long typeId);

    @Query("""
    select c.couId from Couverture c where c.couId in ?2 and not exists ( select a from Association a
          where a.sousLimite.sousLimiteSouscriptionId = ?1
            and a.couverture.couId = c.couId
            and a.type.uniqueCode = 'SOU-LIM-COUV'
      )
""")
    List<Long> findExistingCouIds(Long sousLimiteId, List<Long> couIds);


    @Query("""
    select sl.sousLimiteSouscriptionId
    from Association a
        join a.sousLimite sl
        join a.type t
    where t.uniqueCode = 'SOU-LIM-COUV'
      and sl.traiteNonProportionnel.traiteNpId = :traiteNpId
      and a.couverture.couId in :couIds
    group by sl.sousLimiteSouscriptionId
    having count(distinct a.couverture.couId) = :size
""")
    List<Long> findSousLimiteIdsByExactCouIds(
            @Param("traiteNpId") Long traiteNpId,
            @Param("couIds") Collection<Long> couIds,
            @Param("size") long size
    );




}


