package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RisqueDetailsRepo extends JpaRepository<Association, Long>
{
    @Query("select r from Association r where r.risqueCouvert.risqueId = ?1 and r.couverture.couId = ?2 and r.type.uniqueCode = 'RISQ-DET'")
    Association findByRisqueIdAndSousCouId(Long risqueId, Long couId);

    @Query("select (count(r.assoId) > 0) from Association r where r.risqueCouvert.risqueId = ?1 and r.couverture.couId = ?2 and r.type.uniqueCode = 'RISQ-DET'")
    boolean risqueHasSousCouverture(Long risqueId, Long couId);


    @Query("""
    select c.couId from Couverture c where c.couId not in ?2 and 
    exists(select rd from Association rd where rd.risqueCouvert.risqueId = ?1 and rd.couverture.couId = c.couId and rd.type.uniqueCode = 'RISQ-DET')
    """)
    List<Long> getCouIdsToRemove(Long risqueId, List<Long> couIds);

    @Query("""
    select c.couId from Couverture c where c.couId in ?2 and 
    not exists(select rd from Association rd where rd.risqueCouvert.risqueId = ?1 and rd.couverture.couId = c.couId and rd.type.uniqueCode = 'RISQ-DET')
    """)
    List<Long> getCouIdsToAdd(Long risqueId, List<Long> couIds);

    @Query("select rd.couverture.couId from Association rd where rd.risqueCouvert.risqueId = ?1 and rd.type.uniqueCode = 'RISQ-DET'")
    List<Long> getSousCouIds(Long risqueId);
}