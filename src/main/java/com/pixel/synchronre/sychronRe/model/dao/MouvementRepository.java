package com.pixel.synchronre.sychronRe.model.dao;

import com.pixel.synchronre.sychronRe.model.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouvementRepository extends JpaRepository<Mouvement, Long> {
}
