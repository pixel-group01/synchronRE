package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RepartitionTraiteRepo extends JpaRepository<Repartition, Long>
{
    @Query("select r from Repartition r where r.repId = ?1")
    Optional<Repartition> findById(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp(
        r.repId, r.repPrime, r.repTaux, tnp.traiTauxCourtierPlaceur, tnp.traiTauxCourtier, c.cesId,
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero,
        tnp.traiLibelle, r.isAperiteur, r.repStatut, s.staCode)
        from Repartition r join r.cessionnaire c 
            join r.traiteNonProportionnel tnp
            join r.repStaCode s
        where r.repId = ?1
    """)
    RepartitionTraiteNPResp getRepartitionTraiteNPResp(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp(
        r.repId, r.repPrime, r.repTaux, tnp.traiTauxCourtierPlaceur, tnp.traiTauxCourtier, c.cesId,
        c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, tnp.traiteNpId, tnp.traiReference, tnp.traiNumero,
        tnp.traiLibelle, r.isAperiteur, r.repStatut, s.staCode)
        from Repartition r left join r.cessionnaire c 
            left join r.traiteNonProportionnel tnp
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

    @Modifying @Transactional
    @Query("update Repartition r set r.isAperiteur = false where r.repId <> ?1 and r.traiteNonProportionnel.traiteNpId = (select tnp.traiteNpId from Repartition r join r.traiteNonProportionnel tnp where r.repId = ?1)")
    void setAsTheOnlyAperiteur(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg(
        r.repId, r.repTaux, r.repPrime, r.paramCessionLegale.paramCesLegLibelle, 
        r.paramCessionLegale.paramCesLegId, r.repStatut, 
        tnp.traiTauxCourtier, tnp.traiTauxCourtierPlaceur, tc.trancheCedanteId
        ) from Repartition r join r.trancheCedante tc join tc.tranche.traiteNonProportionnel tnp 
        where r.repStatut = true and r.repStaCode.staCode = 'ACT' and r.trancheCedante.trancheCedanteId = ?1 and r.type.uniqueCode = 'REP_CES_LEG_TNP'
    """)
    List<CesLeg> findPersistedCesLegsByTrancheCedanteId(Long trancheCedanteId);

    @Query("""
        select r.repId from Repartition r where r.repStatut = true and r.repStaCode.staCode = 'ACT' and r.trancheCedante.trancheCedanteId = ?1 and r.type.uniqueCode = 'REP_CES_LEG_TNP'
    """)
    List<Long> findCesLegIdsByTrancheCedanteId(Long trancheCedanteId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg(
        r.repId, r.repTaux, r.repPrime, r.paramCessionLegale.paramCesLegLibelle, 
        r.paramCessionLegale.paramCesLegId, r.repStatut, tnp.traiTauxCourtier, 
        tnp.traiTauxCourtierPlaceur, tc.trancheCedanteId
        ) from Repartition r 
        join r.trancheCedante tc
        join tc.tranche.traiteNonProportionnel tnp
        join tc.cedante ced
        where  tnp.traiteNpId = ?1 and ced.cedId = ?2 
        and r.repStatut = true and r.repStaCode.staCode = 'ACT' 
        and r.type.uniqueCode = 'REP_CES_LEG_TNP'
    """)
    List<CesLeg> findPersistedCesLegsByTraiIdAndCedId(Long traiteNpId, Long cedId);

    @Query("select r.repId from Repartition r where r.traiteNonProportionnel.traiteNpId = ?1 and r.cessionnaire.cesId = ?2 and r.type.uniqueCode = 'REP_PLA_TNP' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE')")
    Optional<Long> getPlacementIdByTraiteNpIdAndCesId(Long traiteNpId, Long cesId);

    @Query("select r from Repartition r where r.trancheCedante.trancheCedanteId = ?1 and r.paramCessionLegale.paramCesLegId = ?2 and r.repStatut = true and r.repStaCode.staCode = 'ACT'")
    Repartition findByTrancheCedanteIdAndPclId(Long cedanteTraiteId, Long paramCesLegId);

    @Query("""
        select r from Repartition r where r.traiteNonProportionnel.traiteNpId = ?1 and r.type.uniqueCode = 'REP_PLA_TNP' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE')
    """)
    List<Repartition> getValidPlacementsOnTraiteNp(Long traiteNpId);

    @Query("""
        select r from Repartition r where r.repStatut = true and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE') and r.trancheCedante.tranche.traiteNonProportionnel.traiteNpId = ?1 and r.type.uniqueCode = 'REP_CES_LEG_TNP'
    """)
    List<Repartition> findCesLegsByTraiteNpId(Long traiteNpId);

    @Query("""
        select r.repId from Repartition r where r.repStatut = true 
        and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE') 
        and r.trancheCedante.tranche.traiteNonProportionnel.traiteNpId = ?1 
        and r.paramCessionLegale.paramCesLegId = ?2 and r.type.uniqueCode = 'REP_CES_LEG_TNP'
    """)
    List<Repartition> findCesLegByTraiteNpIdAndPclId(Long traiteNpId, Long paramCesLegalId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq(
            r.repId, r.repTaux, r.cessionnaire.cesId, ?1, r.isAperiteur) 
        from Repartition r where r.traiteNonProportionnel.traiteNpId = ?1 
        and r.type.uniqueCode = 'REP_PLA_TNP' 
        and r.repStatut = true 
        and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE')
""")
    List<PlacementTraiteNPReq> findPlacementTraiteDtos(Long traiteNpId);
}
