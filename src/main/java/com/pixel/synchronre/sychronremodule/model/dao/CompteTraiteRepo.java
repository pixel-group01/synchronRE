package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CompteTraiteRepo extends JpaRepository<Repartition, Long>
{
    @Query("select sum(r.repTaux) from Repartition r where r.type.uniqueCode = 'REP_PLA_TNP' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'ANNULE')")
    BigDecimal calculateTauxDejaPlace(Long traiteNpId);
}
