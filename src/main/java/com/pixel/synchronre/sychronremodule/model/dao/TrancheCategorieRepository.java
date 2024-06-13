package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.entities.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheCategorieRepository extends JpaRepository<Association, Long>
{
    @Query("select t from Association t where t.tranche.trancheId = ?1 and t.categorie.categorieId = ?2 and t.type.uniqueCode = 'TRAN-CAT'")
    Association findByTrancheIdAndCatId(Long trancheId, Long catId);
    @Query("""
    select cat.categorieId from Categorie cat where cat.categorieId not in ?2 and 
    exists(select tc from Association tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = cat.categorieId and tc.type.uniqueCode = 'TRAN-CAT')
    """)
    List<Long> getCatIdsToRemove(Long trancheId, List<Long> catIds);

    @Query("""
    select cat.categorieId from Categorie cat where cat.categorieId in ?2 and 
    not exists(select tc from Association tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = cat.categorieId and tc.type.uniqueCode = 'TRAN-CAT')
    """)
    List<Long> getCatIdsToAdd(Long trancheId, List<Long> catCedIds);

    @Query(" select (count(tc.assoId)>0) from Association tc where tc.tranche.trancheId = ?1 and tc.categorie.categorieId = ?2 and tc.type.uniqueCode = 'TRAN-CAT'")
    boolean trancheHasCat(Long trancheId, Long catId);

    @Query("select tc.categorie.categorieId from Association tc where tc.tranche.trancheId = ?1 and tc.type.uniqueCode = 'TRAN-CAT'")
    List<Long> getCatIdsByTrancheId(Long trancheId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp(
        c.categorieId, c.categorieLibelle, c.categorieCapacite, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero)
         from Association tc join tc.tranche t 
         join tc.categorie c join t.traiteNonProportionnel tnp 
         where tc.tranche.trancheId = ?1
         and tc.type.uniqueCode = 'TRAN-CAT'
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