package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepartitionTraiteRepo extends JpaRepository<Repartition, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp(
        r.repId, r.repPrime, r.repTaux, tnp.traiTauxCourtierPlaceur, tnp.traiTauxCourtier, c.cesId,
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero,
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
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero,
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
        and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
    """)
    Page<RepartitionTraiteNPResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key")String key, Pageable pageable);

    @Modifying
    @Query("update Repartition r set r.isAperiteur = false where r.repId <> ?1 and r.cedanteTraite.cedanteTraiteId = (select ct.cedanteTraiteId from Repartition r join r.cedanteTraite ct where r.repId = ?1)")
    void setAsTheOnlyAperiteur(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg(
        r.repId, r.repTaux, r.repPrime, r.paramCessionLegale.paramCesLegLibelle, r.paramCessionLegale.paramCesLegId, r.repStatut
        ) from Repartition r where r.repStatut = true and r.repStaCode.staCode = 'ACT' and r.cedanteTraite.cedanteTraiteId = ?1 and r.type.uniqueCode = 'REP_CES_LEG_TRAI'
    """)
    List<CesLeg> findCesLegsByCedTraiId(Long cedanteTraiteId);

    @Query("""
        select r.repId from Repartition r where r.repStatut = true and r.repStaCode.staCode = 'ACT' and r.cedanteTraite.cedanteTraiteId = ?1 and r.type.uniqueCode = 'REP_CES_LEG_TRAI'
    """)
    List<Long> findCesLegIdsByCedTraiId(Long cedanteTraiteId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg(
        r.repId, r.repTaux, r.repPrime, r.paramCessionLegale.paramCesLegLibelle, r.paramCessionLegale.paramCesLegId, r.repStatut
        ) from Repartition r 
        join r.cedanteTraite ct
        join ct.traiteNonProportionnel tnp
        join ct.cedante ced
        where  tnp.traiteNpId = ?1 and ced.cedId = ?2 
        and r.repStatut = true and r.repStaCode.staCode = 'ACT' 
        and r.type.uniqueCode = 'REP_CES_LEG_TRAI'
    """)
    List<CesLeg> findCesLegsByTraiIdAndCedId(Long traiteNpId, Long cedId);
}
