package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepartitionTraiteRepo extends JpaRepository<Repartition, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp(
        r.repId, r.repPrime, r.repTaux, tnp.traiTauxCourtierPlaceur, tnp.traiTauxCourtier, c.cesId,
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNPId, tnp.traiReference, tnp.traiNumero,
        tnp.traiLibelle, r.isAperiteur, r.repStatut, s.staCode)
        from Repartition r left join r.cessionnaire c left join r.cedanteTraite cedTrai 
            left join cedTrai.traiteNonProportionnel tnp
            left join r.repStaCode s
        where r.repId = ?1
    """)
    RepartitionTraiteNPResp getRepartitionTraiteNPResp(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp(
        r.repId, r.repPrime, r.repTaux, tnp.traiTauxCourtierPlaceur, tnp.traiTauxCourtier, c.cesId,
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNPId, tnp.traiReference, tnp.traiNumero,
        tnp.traiLibelle, r.isAperiteur, r.repStatut, s.staCode)
        from Repartition r left join r.cessionnaire c 
            left join r.cedanteTraite cedTrai 
            left join cedTrai.traiteNonProportionnel tnp
            left join r.repStaCode s
        where (
        locate(upper(coalesce(:key, '') ), upper(cast(r.repPrime as string))) =1 or
        locate(upper(coalesce(:key, '') ), upper(cast(r.repTaux as string))) =1 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.cesNom) as string))) >0 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.cesSigle) as string))) >0 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.cesEmail) as string))) >0 or 
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.cesTelephone) as string))) >0
        )
        and tnp.traiteNPId = :traiteNPId and s.staCode = 'ACT'
    """)
    Page<RepartitionTraiteNPResp> search(@Param("traiteNPId") Long traiteNPId, @Param("key")String key, Pageable pageable);
}
