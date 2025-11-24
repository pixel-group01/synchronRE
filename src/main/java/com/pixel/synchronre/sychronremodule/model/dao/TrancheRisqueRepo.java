package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.views.VTrancheRisque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheRisqueRepo extends JpaRepository<VTrancheRisque, Long>
{
    /**
     * Liste des risqueIds présents dans ?2 mais qui ne sont pas enfants de la tranche dont ?1 est la trancheId
     * @param trancheId
     * @param risqueIds
     * @return
     */
    @Query("""
    select r.risqueId from RisqueCouvert r where r.risqueId in ?2 and 
    not exists (
        select a from VTrancheRisque a 
        where a.trancheId = ?1 
          and a.risqueId = r.risqueId 
    )
    """)
    List<Long> getRisqueIdsToAdd(Long trancheId, List<Long> risqueIds);

    /**
     * Liste des risqueIds non présents dans ?2 mais qui sont des enfants de la tranche dont ?1 est la trancheId
     *
     * @param trancheId
     * @param risqueIds
     * @return
     */
    @Query("""
    select a.risqueId from VTrancheRisque a 
    where a.trancheId = ?1 
      and a.risqueId not in ?2
    """)
    List<Long> getRisqueIdsToRemove(Long trancheId, List<Long> risqueIds);
}