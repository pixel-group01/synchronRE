package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import com.pixel.synchronre.sychronremodule.model.entities.DetailBordereau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailsBordereauRepository extends JpaRepository<DetailBordereau, Long>
{
    @Query("select (count(db.debId)>0) from DetailBordereau db where db.repartition.repId = ?1")
    boolean detailsBordereauExistsByPlaId(Long plaId);
}
