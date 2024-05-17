package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.entities.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategorieRepository extends JpaRepository<Categorie, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp(
    cat.categorieId, cat.categorieLibelle, cat.categorieCapacite, tnp.traiteNpId,
     tnp.traiReference, tnp.traiNumero) 
    from CategorieCedante c left join c.categorie cat left join cat.statut s 
    left join c.traiteNonProportionnel tnp
    where (locate(upper(coalesce(:key, '') ), upper(cast(cat.categorieCapacite as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cat.categorieLibelle) as string))) >0)
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
""")
    Page<CategorieResp> search(@Param("traiteNpId") Long traiteNpId,  @Param("key")String key, Pageable pageable);
}
