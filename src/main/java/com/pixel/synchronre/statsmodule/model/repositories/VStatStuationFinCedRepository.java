package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface VStatStuationFinCedRepository extends org.springframework.data.jpa.repository.JpaRepository<VStatStuationFinCed, Long> {
    @Query("""
    SELECT stat FROM VStatStuationFinCed stat 
    WHERE stat.exeCode = COALESCE(?1, stat.exeCode) 
    AND stat.cedId = COALESCE(?2, stat.cedId)
    AND upper(cast(function('unaccent', stat.statutEnvoie) as string)) = 
        upper(COALESCE(cast(function('unaccent', ?3) as string), cast(function('unaccent', stat.statutEnvoie) as string)))
    AND stat.statutEncaissementNormalise = COALESCE(?4, stat.statutEncaissementNormalise)
    """)
    Page<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement, Pageable pageable);
}