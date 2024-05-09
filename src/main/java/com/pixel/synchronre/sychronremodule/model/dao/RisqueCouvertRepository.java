package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RisqueCouvertRepository extends JpaRepository<RisqueCouvert, Long> {
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp(
    r.risqueId, c.couId, c.couLibelle, r.description, tnp.traiId,tnp.traiReference, a.activiteId, a.activiteLibelle, s.staCode, s.staLibelle
    ) from RisqueCouvert r left join r.couverture c left join r.activite a left join r.statut s left join r.traiteNonProportionnel tnp
    where (locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  c.couLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  r.description) as string))) >0 or 
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents', a.activiteLibelle) as string))) >0)
    and tnp.traiId = :traiId and s.staCode = 'ACT'
""")
    Page<RisqueCouvertResp> search(Long traiId, String key, Pageable pageable);
}