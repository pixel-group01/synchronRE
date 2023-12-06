package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BordereauRepository extends JpaRepository<Bordereau, String>
{
    @Query("select b from Bordereau b where b.affaire.statut.staCode in ?1")
    Page<Bordereau> getBordereauByStatut(List<String> staCodes, Pageable pageable);

    @Query("select (count(db.debId)>0) from DetailBordereau db where db.repartition.repId = ?1")
    boolean detailsBordereauExistsByPlaId(Long plaId);

    @Query("select (count(b.bordId) > 0) from Bordereau b where b.affaire.affId = ?1 and b.type.uniqueCode = 'NOT_DEB_FAC'")
    boolean noteDebExistsByAffId(Long affId);
}
