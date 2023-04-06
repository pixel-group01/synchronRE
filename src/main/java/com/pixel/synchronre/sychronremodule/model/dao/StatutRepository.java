package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatutRepository extends JpaRepository<Statut, Long> {

    @Query("select (count(s) > 0) from Statut s where s.staLibelle = ?1")
    boolean existsByStaCode(String staLibelle);

    @Query("select s from Statut s where s.staCode = ?1")
    Statut findByStaCode(String staCode);
}
