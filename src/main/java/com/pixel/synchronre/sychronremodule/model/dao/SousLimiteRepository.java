package com.pixel.synchronre.sychronremodule.model.dao;

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

public interface SousLimiteRepository extends JpaRepository<SousLimite, Long> {




    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp(
        slm.sousLimiteSouscriptionId, slm.sousLimMontant, rs.risqueId,
         rs.description, trnp.traiteNpId, trnp.traiReference, trnp.traiNumero, trnp.traiLibelle, s.staCode, s.staLibelle)
        from SousLimite slm left join slm.risqueCouvert rs 
        left join slm.traiteNonProportionnel trnp 
        left join slm.statut s 
        where (locate(upper(coalesce(:key, '') ), cast(slm.sousLimMontant as string)) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp.traiNumero, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(rs.description, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp.traiLibelle, '') ) as string))) >0
        )       
        and (:traiteNpId = trnp.traiteNpId) 
        and s.staCode = 'ACT'
""")
    Page<SousLimiteDetailsResp> search(@Param("key") String key,
                                       @Param("traiteNpId") Long traiteNpId,
                                       Pageable pageable);

    @Query("""
          select new com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite(
          slm.sousLimiteSouscriptionId,slm.sousLimMontant,rs.risqueId,trnp.traiteNpId,s.staCode
          )
          from SousLimite slm left join slm.risqueCouvert rs
          left join slm.traiteNonProportionnel trnp
          left join slm.statut s
           where slm.sousLimiteSouscriptionId = ?1
     """)
    UpdateSousLimite getEditDtoById(Long sousLimiteSouscriptionId);
}
