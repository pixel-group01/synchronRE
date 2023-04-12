package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AffaireRepository extends JpaRepository<Affaire, Long>
{
    @Query("select coalesce(a.affCapitalInitial, 0)  from Affaire a where a.affId = ?1")
    BigDecimal getCapitalInitial(Long affId);
}
