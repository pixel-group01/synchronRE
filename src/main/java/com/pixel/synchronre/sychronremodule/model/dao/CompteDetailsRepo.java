package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompteDetailsRepo extends JpaRepository<CompteDetails, Long>
{
    @Query("select c from CompteDetails c where c.compteCedante.compteCedId = ?1 and c.typeCompteDet.typeId = ?2")
    CompteDetails findByCompteCedIdAndtypeId(Long compteCedId, Long cedId);
}