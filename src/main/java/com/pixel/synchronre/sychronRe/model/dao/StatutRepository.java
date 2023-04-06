package com.pixel.synchronre.sychronRe.model.dao;

import com.pixel.synchronre.sychronRe.model.entities.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatutRepository extends JpaRepository<Statut, Long> {

    @Query("select (count(s) > 0) from Statut s where s.staCode = ?1")
    boolean existsByStaCode(String staCode);
}
