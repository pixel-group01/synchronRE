package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TrancheRepository extends JpaRepository<Tranche, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
    t.trancheId,t.trancheType,t.trancheLibelle, t.tranchePriorite, t.tranchePorte, t.trancheTauxPrime, r.risqueId, r.description,
    c.couId, c.couLibelle, c.couLibelleAbrege, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero)
    from Tranche t 
    left join t.risqueCouvert r 
    left join r.couverture c 
    left join t.traiteNonProportionnel tnp 
    where t.trancheId = ?1
    """)
    TrancheResp getTrancheResp(Long trancheId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
    t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte, t.trancheTauxPrime, r.risqueId, r.description,
    c.couId, c.couLibelle, c.couLibelleAbrege, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero)
    from Tranche t 
    left join t.risqueCouvert r 
    left join r.couverture c 
    left join t.traiteNonProportionnel tnp left join t.statut s
    where (
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  t.trancheLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(t.tranchePriorite as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(t.tranchePorte as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  r.description) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.couLibelle) as string))) >0 or 
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.couLibelleAbrege) as string))) >0 
    )
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
    """)
    Page<TrancheResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key")String key, Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq(
        t.trancheId, t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte, t.trancheTauxPrime, r.risqueId,tnp.traiteNpId)
        from Tranche t
        left join t.risqueCouvert r
        left join t.traiteNonProportionnel tnp 
        where t.trancheId = ?1
    """)
    TrancheReq getEditDtoById(Long trancheId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
    t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte, t.trancheTauxPrime, r.risqueId, r.description,
    c.couId, c.couLibelle, c.couLibelleAbrege, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero)
    from Tranche t 
    left join t.risqueCouvert r 
    left join r.couverture c 
    left join t.traiteNonProportionnel tnp left join t.statut s
    where tnp.traiteNpId = ?1 and s.staCode = 'ACT'
    """)
    List<TrancheResp> getTrancheList(Long traiteNpId);

    @Query("select tr.traiteNonProportionnel.traiteNpId from Tranche tr where tr.trancheId = ?1")
    Long getTraiteNpIdByTrancheId(Long tranchepId);

    @Query("select tr.trancheTauxPrime from Tranche tr where tr.trancheId = ?1")
    BigDecimal getTrancheTauxPrime(Long tranchepId);
}
