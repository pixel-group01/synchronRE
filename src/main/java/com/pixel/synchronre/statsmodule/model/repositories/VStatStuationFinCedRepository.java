package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VStatStuationFinCedRepository extends org.springframework.data.jpa.repository.JpaRepository<VStatStuationFinCed, Long> {
    @Query("""
        select stat from VStatStuationFinCed stat 
        where stat.exeCode= COALESCE(?1, stat.exeCode) 
        and stat.cedId= COALESCE(?2, stat.cedId)
        and upper(cast(function('unaccent', stat.statutEnvoie) as string)) = upper(COALESCE(cast(function('unaccent',  ?3) as string), cast(function('unaccent',  stat.statutEnvoie) as string) ))
        and upper(cast(function('unaccent', stat.statutEncaissement) as string))= upper(COALESCE(cast(function('unaccent',  ?4) as string), cast(function('unaccent',  stat.statutEncaissement) as string)))
        """)
    Page<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement, Pageable pageable);
}