package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.dtos.VStatSituationFinParReaCed;
import com.pixel.synchronre.sychronremodule.model.views.V_StatStuationFinCed;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface V_StatStuationFinCedRepository extends org.springframework.data.jpa.repository.JpaRepository<com.pixel.synchronre.sychronremodule.model.views.V_StatStuationFinCed, Long> {
    @Query("""
        select stat from V_StatStuationFinCed stat 
        where stat.exeCode= COALESCE(?1, stat.exeCode) 
        and stat.cedId= COALESCE(?2, stat.cedId)
        and stat.statutEnvoie= COALESCE(?3, stat.statutEnvoie)
        and stat.statutEncaissement= COALESCE(?4, stat.statutEncaissement)
        """)
    List<V_StatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId,String statutEnvoie, String statutEncaissement);
}