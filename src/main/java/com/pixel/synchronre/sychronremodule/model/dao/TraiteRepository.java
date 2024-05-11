package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraiteRepository extends JpaRepository<TraiteNonProportionnel, Long>
{
}
