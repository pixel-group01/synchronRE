package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepo extends JpaRepository<Compte, Long> {
}