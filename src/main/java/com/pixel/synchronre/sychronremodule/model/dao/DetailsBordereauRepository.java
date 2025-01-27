package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import com.pixel.synchronre.sychronremodule.model.entities.DetailBordereau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DetailsBordereauRepository extends JpaRepository<DetailBordereau, Long>
{
    @Query("select (count(db.debId)>0) from DetailBordereau db where db.repartition.repId = ?1")
    boolean detailsBordereauExistsByPlaId(Long plaId);

    @Query("select db.debId from DetailBordereau db where db.bordereau.bordId = ?1")
    List<Long> findByBordId(Long bordId);

    @Query("select db from DetailBordereau db where db.repartition.repId = ?1 and db.debStatut = true")
    DetailBordereau findByPlaId(Long plaId);
}