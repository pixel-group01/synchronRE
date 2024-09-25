package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RisqueCouvertRepository extends JpaRepository<RisqueCouvert, Long> {
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp(
    r.risqueId, c.couId, c.couLibelle, r.description, tnp.traiteNpId,tnp.traiReference, s.staCode, s.staLibelle
    ) from RisqueCouvert r left join r.couverture c left join r.statut s left join r.traiteNonProportionnel tnp
    where (locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.couLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  r.description) as string))) >0 )
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
""")
    Page<RisqueCouvertResp> search(Long traiteNpId, String key, Pageable pageable);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp(
    rc.risqueId, cv.couId, cv.couLibelle, rc.description, 
    tnp.traiteNpId, tnp.traiReference, sta.staCode, sta.staLibelle)
    from RisqueCouvert rc 
    join rc.couverture cv 
    join rc.traiteNonProportionnel tnp
    join rc.statut sta
     where rc.risqueId = ?1
""")
    RisqueCouvertResp getFullRisqueCouvertById(Long risqueId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq(
        r.risqueId, r.couverture.couId, r.description
        )from RisqueCouvert r where r.risqueId = ?1
""")
    UpdateRisqueCouvertReq getEditDto(Long risqueId);

    //Affiche la liste des risques saisis à l'écran
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp(
    rc.risqueId, cv.couId, cv.couLibelle, rc.description, 
    tnp.traiteNpId, tnp.traiReference, sta.staCode, sta.staLibelle)
    from RisqueCouvert rc 
    join rc.couverture cv 
    join rc.traiteNonProportionnel tnp
    join rc.statut sta
     where tnp.traiteNpId = ?1 AND sta.staCode='ACT'
""")
    List<RisqueCouvertResp> getRisqueList(Long traiteNpId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp(c.couLibelle)
         from Association asso join asso.risqueCouvert r
         join asso.couverture c 
         where asso.risqueCouvert.risqueId = ?1
         and asso.type.uniqueCode = 'RISQ-DET'
        """)
    List<CouvertureListResp> getActivitesByrisqueId(Long risqueId);

    //Renvoie la liste des couvertures parents qui ont au moins une sous couverture qui n'est pas sur une sous limite du traité
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp(
    rc.risqueId, cv.couId, cv.couLibelle, rc.description, 
    tnp.traiteNpId, tnp.traiReference, sta.staCode, sta.staLibelle)
    from RisqueCouvert rc 
    join rc.couverture cv 
    join rc.traiteNonProportionnel tnp
    join rc.statut sta
     where tnp.traiteNpId = ?1 
     and rc.couverture.couId in (
     select c.couId from Couverture c 
    where exists (select c2 from Couverture c2 where c2.couParent.couId = c.couId 
    and not exists (select sl from SousLimite sl 
                    where sl.activite.couId = c2.couId 
                    and sl.traiteNonProportionnel.traiteNpId = ?1 
                    and sl.statut.staCode = 'ACT') 
                  ))
""")
    List<RisqueCouvertResp> getCouvertureParent(Long traiteNpId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp(
    c.couId,c.couLibelle)
    from Association a 
    join a.risqueCouvert rc
    join a.couverture c
    join a.type t
    where rc.risqueId = ?1 
    AND t.uniqueCode='RISQ-DET' 
    AND c.couId NOT IN (
                select sl.activite.couId
                from SousLimite sl
                where sl.traiteNonProportionnel.traiteNpId = rc.traiteNonProportionnel.traiteNpId 
                and sl.activite.couId = a.couverture.couId        
    )
    ORDER BY c.couLibelle
""")
    List<ActivitesResp> getActivite(Long risqueId);


    @Query("select rc from RisqueCouvert rc where rc.traiteNonProportionnel.traiteNpId = ?1 and rc.couverture.couId = ?2")
    Optional<RisqueCouvert> findByTraiteAndCouverture(Long traiteNpId, Long couId);
}