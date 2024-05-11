package com.pixel.synchronre.sychronremodule.model.dao;

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
        select new com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp(slm.sousLimiteSouscriptionId, trnp.traiId,
        slm.sousLimMontant, trnp.traiReference, trnp.traiNumero, trnp.traiLibelle, rs.description, tr.trancheLibelle, tr.tranchePriorite, s.staCode, s.staLibelle, u.email, 
        concat(u.firstName, ' ', u.lastName), f.name, slm.createdAt, slm.updatedAt) 
        from SousLimite slm left join slm.risqueCouvert rs 
        left join slm.fonCreator f 
        left join slm.traiteNonProportionnel trnp 
        left join slm.tranche tr 
        left join slm.statut s 
        left join slm.userCreator u 
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(slm.sousLimiteSouscriptionId, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(slm.sousLimMontant, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp .traiNumero, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tr.trancheLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(rs.description, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(trnp.traiLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(slm.fonCreator, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(u.email, '') ) as string))) >0
        )       
        and (:fncId is null or :fncId = f.id) 
        and (:userId is null or :userId = u.userId)  
""")
    Page<SousLimiteDetailsResp> search(@Param("key") String key,
                                       @Param("fncId") Long fncId,
                                       @Param("userId") Long userId,
                                       Pageable pageable);

}
