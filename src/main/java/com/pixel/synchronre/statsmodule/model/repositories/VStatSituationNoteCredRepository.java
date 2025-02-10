package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.views.VStatSituationNoteCred;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VStatSituationNoteCredRepository extends JpaRepository<VStatSituationNoteCred, Long> {
    @Query("""
        select stat from VStatSituationNoteCred stat 
        where stat.exeCode= COALESCE(:exeCode, stat.exeCode) 
        and stat.cedId= COALESCE(:cedId, stat.cedId)
        and stat.cesId= COALESCE(:cesId, stat.cesId)
        """)
    Page<VStatSituationNoteCred> getSituationNoteCredit(@Param("exeCode") Long exeCode,
                                                        @Param("cedId") Long cedId,
                                                        @Param("cesId") Long cesId, Pageable pageable);
}