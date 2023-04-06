package com.pixel.synchronre.sychronRe.model.dao;

import com.pixel.synchronre.sychronRe.model.dto.projection.BanqueInfo;
import com.pixel.synchronre.sychronRe.model.entities.Banque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BanqueRepository extends JpaRepository<Banque, Long> {


    @Query("select b from Banque b where b.banId = ?1 order by b.banLibelle")
    List<BanqueInfo> findByBanIdOrderByBanLibelleAsc(Long banId);


    @Query("select b from Banque b order by b.banLibelle")
    List<BanqueInfo> findByOrderByBanLibelleAsc();


}
