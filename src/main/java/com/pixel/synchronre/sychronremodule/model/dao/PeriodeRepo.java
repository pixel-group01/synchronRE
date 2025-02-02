package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Compte;
import com.pixel.synchronre.sychronremodule.model.entities.Periode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PeriodeRepo extends JpaRepository<Compte, Long>
{

    @Query("select p from Periode p where year(p.periode) = ?1 and p.type.typeId = ?2 and p.periode <= CURRENT_DATE order by p.periode")
    List<Periode> getPeriodesByTypeId(Long exeCode, Long typeId);
}