package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.views.VStatSituationFinParReaCed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VStatSituationFinReaCedRepository extends JpaRepository<VStatSituationFinParReaCed, Long> {


@Query("""
        select stat from VStatSituationFinParReaCed stat 
        where stat.exeCode= COALESCE(?1, stat.exeCode) 
        and stat.cedId= COALESCE(?2, stat.cedId)
        and stat.cesId= COALESCE(?3, stat.cesId)
        and upper(cast(function('unaccent',  coalesce(stat.statutEnvoie, '') ) as string)) = COALESCE(upper(cast(function('unaccent',  coalesce(?4, '') ) as string)), upper(cast(function('unaccent',  coalesce(stat.statutEnvoie, '') ) as string)))
        and upper(cast(function('unaccent',  coalesce(stat.statutEncaissement, '') ) as string)) = COALESCE(upper(cast(function('unaccent',  coalesce(?5, '') ) as string)), upper(cast(function('unaccent',  coalesce(stat.statutEncaissement, '') ) as string)))
        """)
Page<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement, Pageable pageable);
}
