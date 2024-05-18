package com.pixel.synchronre.sychronremodule.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RisqueDetailsRepo extends JpaRepository<RisqueCouvertDetails, Long>
{
    @Query("select r from RisqueCouvertDetails r where r.risqueCouvert.risqueId = ?1 and r.risqueCouvert.couverture.couId = ?2")
    RisqueCouvertDetails findByRisqueIdAndSousCouId(Long risqueId, Long couId);

    @Query("select (count(r.risqueDetailsId) > 0) from RisqueCouvertDetails r where r.risqueCouvert.risqueId = ?1 and r.risqueCouvert.couverture.couId = ?2")
    boolean risqueHasSousCouverture(Long risqueId, Long couId);


    @Query("""
    select c.couId from Couverture c where c.couId not in ?2 and 
    exists(select rd from RisqueCouvertDetails rd where rd.risqueCouvert.risqueId = ?1 and rd.couverture.couId = c.couId)
    """)
    List<Long> getCouIdsToRemove(Long risqueId, List<Long> couIds);

    @Query("""
    select c.couId from Couverture c where c.couId not in ?2 and 
    not exists(select rd from RisqueCouvertDetails rd where rd.risqueCouvert.risqueId = ?1 and rd.couverture.couId = c.couId)
    """)
    List<Long> getCouIdsToAdd(Long risqueId, List<Long> couIds);
}