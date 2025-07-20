package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.entities.LimiteSouscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LimiteSouscriptionRepository extends JpaRepository<LimiteSouscription, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp(
    l.limiteSouscriptionId, l.limSousMontant, r.risqueId, r.description, cou.couId, cou.couLibelle,
    cou.couLibelleAbrege, cat.categorieId, cat.categorieLibelle, cat.categorieCapacite, tnp.traiteNpId, 
    tnp.traiReference, tnp.traiNumero)
    from LimiteSouscription l 
    left join l.risqueCouvert r 
    left join r.couverture cou
    left join l.categorie cat 
    left join l.categorie.traiteNonProportionnel tnp
    where l.limiteSouscriptionId = ?1
    """)
    LimiteSouscriptionResp findLimiteSouscriptionRespById(Long limiteSouscriptionId );

    @Query("""
    
    select new com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp(
    l.limiteSouscriptionId, l.limSousMontant, r.risqueId, r.description, cou.couId, cou.couLibelle,
    cou.couLibelleAbrege, cat.categorieId, cat.categorieLibelle, cat.categorieCapacite, tnp.traiteNpId, 
    tnp.traiReference, tnp.traiNumero)
    from LimiteSouscription l 
    left join l.risqueCouvert r 
    left join r.couverture cou
    left join l.categorie cat 
    left join l.categorie.traiteNonProportionnel tnp
    left join l.statut s
    
    where (
    locate(upper(coalesce(:key, '') ), upper(cast(l.limSousMontant as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cou.couLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cou.couLibelleAbrege) as string))) >0 or 
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cat.categorieLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(cat.categorieCapacite as string))) =1
    )
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
    """)
    Page<LimiteSouscriptionResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key")String key, Pageable pageable);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq(
    l.limiteSouscriptionId,l.limSousMontant,r.risqueId,cat.categorieId
    )
    from LimiteSouscription l 
    left join l.risqueCouvert r 
    left join l.categorie cat 
    where l.limiteSouscriptionId = ?1
    """)
    LimiteSouscriptionReq getEditDtoById(Long limiteSouscriptionId);

    @Query("""
        select ls from LimiteSouscription ls where ls.risqueCouvert.risqueId = ?1 and ls.categorie.categorieId = ?2
    """)
    List<LimiteSouscription> findByRisqueIdAndCatId(Long risqueId, Long categorieId);

    @Query("""
    SELECT ls FROM LimiteSouscription ls 
    WHERE ls.risqueCouvert.risqueId = :risqueId 
    AND ls.categorie.categorieId = :categorieId 
    AND (
        SELECT COUNT(a.couverture.couId) 
        FROM Association a 
        WHERE a.limiteSouscription.limiteSouscriptionId = ls.limiteSouscriptionId 
        AND a.type.uniqueCode = 'LIM-SOU-COUV'
    ) = :couverturesTotalCount
    AND (
        SELECT COUNT(a.couverture.couId) 
        FROM Association a 
        WHERE a.limiteSouscription.limiteSouscriptionId = ls.limiteSouscriptionId 
        AND a.type.uniqueCode = 'LIM-SOU-COUV'
        AND a.couverture.couId IN :couIds
    ) = :couverturesTotalCount
""")
    Optional<LimiteSouscription> findByRisqueIdAndCatIdAndExactCouvertures(
            @Param("risqueId") Long risqueId,
            @Param("categorieId") Long categorieId,
            @Param("couIds") List<Long> couIds,
            @Param("couverturesTotalCount") int couverturesTotalCount);
}