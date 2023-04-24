package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParamCessionLegaleRepository extends JpaRepository<ParamCessionLegale, Long> {

    @Query("select count(pcl.paramCesLegId) from ParamCessionLegale pcl where pcl.pays.paysCode = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?1)")
    Long countByAffId(Long affId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(
    pcl.paramCesLegId, pcl.paramCesLegLibelle, pcl.paramCesLegCapital, pcl.paramCesLegTaux, pcl.pays.paysNom, pcl.statut.staLibelle, pcl.pays.paysCode, pcl.numOdre)
    from ParamCessionLegale pcl where pcl.pays.paysCode = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?1) order by pcl.numOdre
    """)
    List<ParamCessionLegaleListResp> findByAffId(Long affId);

    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where pcl.pays.paysCode = ?1 and ?1 = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?2)")
    boolean existsByPaysAndAffaire(Long paysId, Long affId);

    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where (select pcl2.pays.paysCode from ParamCessionLegale pcl2 where pcl2.paramCesLegId = ?1) = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?2)")
    boolean existsByPclIdAndAffaire(Long pclId, Long affId);
}
