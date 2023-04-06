package com.pixel.synchronre.sychronremodule.model.dao;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BanqueRepository extends JpaRepository<Banque, Long> {


}
