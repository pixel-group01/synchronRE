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
        and upper(cast(function('unaccent', stat.statutEnvoie) as string)) = upper(COALESCE(cast(function('unaccent',  ?4) as string), cast(function('unaccent',  stat.statutEnvoie) as string)))
        and upper(cast(function('unaccent', stat.statutEncaissement) as string)) = upper(COALESCE(cast(function('unaccent',  ?5) as string), cast(function('unaccent', stat.statutEncaissement) as string)))
        """)
Page<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement, Pageable pageable);
}
