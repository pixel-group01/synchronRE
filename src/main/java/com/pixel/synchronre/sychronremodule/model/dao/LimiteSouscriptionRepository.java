package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.entities.LimiteSouscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LimiteSouscriptionRepository extends JpaRepository<LimiteSouscription, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp(
    l.limiteSouscriptionId, l.limSousMontant, r.risqueId, r.description, cou.couId, cou.couLibelle, 
    cou.couLibelleAbrege, ct.cedanteTraiteId, ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale, 
    tnp.traiteNPId, tnp.traiReference, tnp.traiNumero, tr.trancheId, tr.trancheLibelle
    )
    from LimiteSouscription l 
    left join l.risqueCouvert r 
    left join r.couverture cou
    left join l.cedanteTraite ct 
    left join ct.cedante ced 
    left join ct.traiteNonProportionnel tnp
    left join l.tranche tr
    where l.limiteSouscriptionId = ?1
    """)
    LimiteSouscriptionResp findLimiteSouscriptionRespById(Long limiteSouscriptionId );

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp(
    l.limiteSouscriptionId, l.limSousMontant, r.risqueId, r.description, cou.couId, cou.couLibelle, cou.couLibelleAbrege,
    ct.cedanteTraiteId, ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale, tnp.traiteNPId,
    tnp.traiReference, tnp.traiNumero, tr.trancheId, tr.trancheLibelle
    )
    from LimiteSouscription l 
    left join l.risqueCouvert r 
    left join r.couverture cou
    left join l.cedanteTraite ct 
    left join ct.cedante ced 
    left join ct.traiteNonProportionnel tnp
    left join l.tranche tr
    left join l.statut s
    where (
    locate(upper(coalesce(:key, '') ), upper(cast(l.limSousMontant as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cou.couLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  cou.couLibelleAbrege) as string))) >0 or 
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ced.cedNomFiliale) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ced.cedSigleFiliale) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  tr.trancheLibelle) as string))) >0 
    )
    and tnp.traiteNPId = :traiteNPId and s.staCode = 'ACT'
    """)
    Page<LimiteSouscriptionResp> search(@Param("traiteNPId") Long traiteNPId, @Param("key")String key, Pageable pageable);
}