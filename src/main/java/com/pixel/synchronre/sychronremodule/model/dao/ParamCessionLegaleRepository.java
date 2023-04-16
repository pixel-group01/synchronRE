package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParamCessionLegaleRepository extends JpaRepository<ParamCessionLegale, Long> {

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(pr.paramCesLegId, pr.paramCesLegLibelle, pr.paramCesLegCapital, 
        pr.pays.paysNom, pr.statut.staLibelle) 
        from ParamCessionLegale pr where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.paramCesLegLibelle, '') ) as string)) ) >0  
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.pays.paysNom, '') ) as string)) ) >0 ) 
                                         and pr.statut.staCode = 'ACT'     
""")
    Page<ParamCessionLegaleListResp> searchParams(String key, Pageable pageable);

    @Query("select count(pcl.paramCesLegId) from ParamCessionLegale pcl where pcl.pays.paysId = (select a.cedante.pays.paysId from Affaire a where a.affId = ?1)")
    Long countByAffId(Long affId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(
    pcl.paramCesLegId, pcl.paramCesLegLibelle, pcl.paramCesLegCapital, pcl.paramCesLegTaux, pcl.pays.paysNom, pcl.statut.staLibelle)
    from ParamCessionLegale pcl where pcl.pays.paysId = (select a.cedante.pays.paysId from Affaire a where a.affId = ?1)
    """)
    List<ParamCessionLegaleListResp> findByAffId(Long affId);

    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where pcl.pays.paysId = ?1 and ?1 = (select a.cedante.pays.paysId from Affaire a where a.affId = ?2)")
    boolean existsByPaysAndAffaire(Long paysId, Long affId);

    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where (select pcl2.pays.paysId from ParamCessionLegale pcl2 where pcl2.paramCesLegId = ?1) = (select a.cedante.pays.paysId from Affaire a where a.affId = ?2)")
    boolean existsByPclIdAndAffaire(Long pclId, Long affId);
}