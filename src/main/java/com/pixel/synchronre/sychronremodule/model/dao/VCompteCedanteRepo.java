package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.views.VCompteCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface VCompteCedanteRepo extends JpaRepository<VCompteCedante, Long>
{
    @Query("select vcc.primeApresAjustement from VCompteCedante vcc where vcc.compteCedId = ?1")
    BigDecimal getPrimeApresAjustement(BigDecimal assiettePrime, Long compteCedId);
}
