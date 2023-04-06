package com.pixel.synchronre.logmodule.controller.repositories;

import com.pixel.synchronre.logmodule.model.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepo extends JpaRepository<Log, Long> {
}
