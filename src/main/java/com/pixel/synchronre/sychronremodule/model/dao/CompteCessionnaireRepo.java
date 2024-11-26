package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCessionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompteCessionnaireRepo extends JpaRepository<CompteCessionnaire, Long>
{
    @Query("select c from CompteCessionnaire c where c.compteCedante.compteCedId = ?1 and c.cessionnaire.cesId = ?2")
    CompteCessionnaire findByCompteCedIdAndCesId(Long compteCedId, Long cesId);
}