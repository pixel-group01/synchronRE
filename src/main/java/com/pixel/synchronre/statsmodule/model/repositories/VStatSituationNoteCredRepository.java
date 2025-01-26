package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.dtos.VStatSituationFinParReaCed;
import com.pixel.synchronre.sychronremodule.model.views.VStatSituationNoteCred;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VStatSituationNoteCredRepository extends JpaRepository<VStatSituationNoteCred, Long> {
    @Query("""
        select stat from VStatSituationNoteCred stat 
        where stat.exeCode= COALESCE(?1, stat.exeCode) 
        and stat.cedId= COALESCE(?2, stat.cedId)
        and stat.cesId= COALESCE(?3, stat.cesId)
        """)
    List<VStatSituationNoteCred> getSituationNoteCredit(Long exeCode, Long cedId, Long cesId);
}