package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheCategorieRepository extends JpaRepository<TrancheCategorie, Long>
{
    @Query("select t from TrancheCategorie t where t.tranche.trancheId = ?1 and t.categorie.categorieId = ?2")
    TrancheCategorie findByTrancheIdAndCatId(Long trancheId, Long catId);
    @Query("""
    select cat.categorieId from Categorie cat where cat.categorieId not in ?2 and 
    exists(select tc from TrancheCategorie tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = cat.categorieId)
    """)
    List<Long> getCatIdsToRemove(Long trancheId, List<Long> catIds);

    @Query("""
    select cat.categorieId from Categorie cat where cat.categorieId in ?2 and 
    not exists(select tc from TrancheCategorie tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = cat.categorieId)
    """)
    List<Long> getCatIdsToAdd(Long trancheId, List<Long> catCedIds);

    @Query(" select (count(tc.trancheCategorieId)>0) from TrancheCategorie tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = ?2")
    boolean trancheHasCat(Long trancheId, Long catId);

    @Query("select tc.categorie.categorieId from TrancheCategorie tc where tc.tranche.trancheId = ?1")
    List<Long> getCatIdsByTrancheId(Long trancheId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp(
        c.categorieId, c.categorieLibelle, c.categorieCapacite, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero)
         from TrancheCategorie tc join tc.tranche t 
         join tc.categorie c join t.traiteNonProportionnel tnp 
         where tc.tranche.trancheId = ?1
        """)
    List<CategorieResp> getCategoriesByTrancheId(Long trancheId);
}

//07 09 07 96 68 //M. Emil Zola
/**
 * getIdsToAdd(dto.getTrancheId(), dto.getCategorieCedanteIds());
 *             List<Long> catCedIdsToRemove = trancheCedRepo.getIdsToRemove
 *
 *
 *
 */