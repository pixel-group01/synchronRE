package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    @Query("select (count(r) > 0) from Reglement r where r.regReference = ?1")
    boolean alreadyExistsByReference(String regReference);


}
