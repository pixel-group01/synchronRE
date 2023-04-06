package com.pixel.synchronre.logmodule.controller.repositories;

import com.pixel.synchronre.logmodule.model.entities.LogDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDetailsRepo extends JpaRepository<LogDetails, Long> {
}
