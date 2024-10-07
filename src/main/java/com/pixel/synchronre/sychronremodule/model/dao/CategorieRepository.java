package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.entities.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp(
    c.categorieId, c.categorieLibelle, c.categorieCapacite, tnp.traiteNpId,
     tnp.traiReference, tnp.traiNumero) 
    from Categorie c  left join c.statut s 
    left join c.traiteNonProportionnel tnp
    where (locate(upper(coalesce(:key, '') ), cast(c.categorieCapacite as string)) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.categorieLibelle) as string))) >0)
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
""")
    Page<CategorieResp> search(@Param("traiteNpId") Long traiteNpId,  @Param("key")String key, Pageable pageable);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp(
    c.categorieId, c.categorieLibelle, c.categorieCapacite, tnp.traiteNpId,
     tnp.traiReference, tnp.traiNumero) 
    from Categorie c left join c.statut s 
    left join c.traiteNonProportionnel tnp
    where tnp.traiteNpId = ?1 and s.staCode = 'ACT'
""")
    List<CategorieResp> getCategorieList(Long traiteNpId);

    @Query("""
        select ct.cedanteTraiteId from CedanteTraite ct where ct.cedante.cedId = ?1 and ct.traiteNonProportionnel.traiteNpId = (select cat.traiteNonProportionnel.traiteNpId from Categorie cat where cat.categorieId = ?2)
""")
    Long getCedanteTraiteIdByCedIdAndCatId(Long cedId, Long catId);

    @Query("""
    select a.cedante.cedId from Association a where a.type.uniqueCode = 'CAT-CED' and a.categorie.categorieId = ?1
""")
    List<Long> getCedIdsByCatId(Long catId);

    @Query("select (count(c.categorieId) > 0) from Categorie c where c.traiteNonProportionnel.traiteNpId = ?1 and c.categorieId = ?2")
    boolean TraiteHasCategorie(Long traiteNpId, Long catId);
}