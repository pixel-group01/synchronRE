package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.SousLimite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SousLimiteRepository extends JpaRepository<SousLimite, Long> {




    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp(
        slm.sousLimiteSouscriptionId, slm.sousLimMontant, a.couId,
         a.couLibelle, trnp.traiteNpId, trnp.traiReference, trnp.traiNumero, trnp.traiLibelle, s.staCode, s.staLibelle)
        from SousLimite slm left join slm.activite a 
        left join slm.traiteNonProportionnel trnp 
        left join slm.statut s 
        where (locate(upper(coalesce(:key, '') ), cast(slm.sousLimMontant as string)) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp.traiNumero, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(a.couLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp.traiLibelle, '') ) as string))) >0
        )       
        and (:traiteNpId = trnp.traiteNpId) 
        and s.staCode = 'ACT'
""")
    Page<SousLimiteDetailsResp> search(@Param("key") String key,
                                       @Param("traiteNpId") Long traiteNpId,
                                       Pageable pageable);

    @Query("""
    select DISTINCT new com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp(
    c.couId,c.couLibelle)
    from Association a 
    join a.limiteSouscription ls
    join a.couverture c
    join a.type t  
    join ls.categorie.traiteNonProportionnel tnp
    where t.uniqueCode='LIM-SOU-COUV' 
    and tnp.traiteNpId=?1
    AND c.couId NOT IN (
                select sl.activite.couId
                from SousLimite sl
                where sl.activite.couId = a.couverture.couId and sl.traiteNonProportionnel.traiteNpId=tnp.traiteNpId       
    )
    ORDER BY c.couLibelle
""")
    List<ActivitesResp> getActivite(Long traiteNpId);

    @Query("""
          select new com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite(
          slm.sousLimiteSouscriptionId,slm.sousLimMontant, a.couId,trnp.traiteNpId)
          from SousLimite slm left join slm.activite a
          left join slm.traiteNonProportionnel trnp
          left join slm.statut s
           where slm.sousLimiteSouscriptionId = ?1
     """)
    UpdateSousLimite getEditDtoById(Long sousLimiteSouscriptionId);

    @Query("select ssl from SousLimite ssl where ssl.traiteNonProportionnel.traiteNpId = ?1 and ssl.activite.couId = ?2")
    Optional<SousLimite> findByTraiteAndActivite(Long traiteNpId, Long couId);

    @Query("select sl from SousLimite  sl where sl.traiteNonProportionnel.traiteNpId  = ?1")
    List<SousLimite> findByTnpId(Long traiteNpId);
}
