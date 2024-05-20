package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheCedanteRepository extends JpaRepository<TrancheCedante, Long>
{
    @Query("select t from TrancheCedante t where t.tranche.trancheId = ?1 and t.categorieCedante.catCedId = ?2")
    TrancheCedante findByTrancheIdAndCatCedId(Long trancheId, Long catCedId);
    @Query("""
    select catCed.catCedId from CategorieCedante catCed where catCed.catCedId not in ?2 and 
    exists(select tc from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.categorieCedante.catCedId = catCed.catCedId)
    """)
    List<Long> getCatCedIdsToRemove(Long trancheId, List<Long> catCedIds);

    @Query("""
    select catCed.catCedId from CategorieCedante catCed where catCed.catCedId in ?2 and 
    not exists(select tc from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.categorieCedante.catCedId = catCed.catCedId)
    """)
    List<Long> getCatCedIdsToAdd(Long trancheId, List<Long> catCedIds);

    @Query(" select (count(tc.trancheCedanteId)>0) from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.categorieCedante.catCedId = ?2")
    boolean trancheHasCatCed(Long trancheId, Long catCedId);
}

//07 09 07 96 68 //M. Emil Zola
/**
 * getIdsToAdd(dto.getTrancheId(), dto.getCategorieCedanteIds());
 *             List<Long> catCedIdsToRemove = trancheCedRepo.getIdsToRemove
 */