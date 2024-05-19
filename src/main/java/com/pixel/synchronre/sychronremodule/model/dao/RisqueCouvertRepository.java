package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}