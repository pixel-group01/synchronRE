package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VStatStuationFinCedRepository extends org.springframework.data.jpa.repository.JpaRepository<VStatStuationFinCed, Long> {
    @Query("""
        select stat from VStatStuationFinCed stat 
        where stat.exeCode= COALESCE(?1, stat.exeCode) 
        and stat.cedId= COALESCE(?2, stat.cedId)
        and stat.statutEnvoie= COALESCE(?3, stat.statutEnvoie)
        and stat.statutEncaissement= COALESCE(?4, stat.statutEncaissement)
        """)
    List<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement);
}