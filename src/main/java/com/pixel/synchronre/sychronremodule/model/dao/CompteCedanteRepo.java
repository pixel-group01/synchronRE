package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompteCedanteRepo extends JpaRepository<CompteCedante, Long>
{
    @Query("select c from CompteCedante c where c.compte.compteId = ?1 and c.cedante.cedId = ?2")
    CompteCedante findByCompteIdAndCedId(Long compteId, Long cedId);
}