package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AssociationRepository extends JpaRepository<Association, Long>
{
    @Modifying
    @Query("delete from Association a where a.tranche.trancheId = ?1 and a.risqueCouvert.risqueId = ?2 and a.type.uniqueCode = 'TRAN-RISQ'")
    void deleteByTrancheIdAndRisqueId(Long trancheId, Long risqueId);
}
