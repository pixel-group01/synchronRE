package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ParamCessionLegaleRepository extends JpaRepository<ParamCessionLegale, Long> {

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(pr.paramCesLegId, pr.paramCesLegLibelle, pr.paramCesLegCapital, 
        pr.paramCesLegTaux,pr.pays.paysNom, pr.pays.paysCode, pr.paramType.typeId, pr.paramType.name) 
        from ParamCessionLegale pr where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.paramCesLegLibelle, '') ) as string)) ) >0                                    
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.pays.paysNom, '') ) as string)) ) >0 ) 
                                         and pr.statut.staCode = 'ACT' order  by pr.pays.paysCode, pr.numOrdre    
""")
    Page<ParamCessionLegaleListResp> searchParams(String key, Pageable pageable);


    @Query("select count(pcl.paramCesLegId) from ParamCessionLegale pcl where pcl.pays.paysCode = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?1)")
    Long countPossiblePclByAffId(Long affId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(
    pcl.paramCesLegId, pcl.paramCesLegLibelle, pcl.paramCesLegCapital, pcl.paramCesLegTaux, pcl.pays.paysNom, pcl.statut.staLibelle, pcl.pays.paysCode, pcl.numOrdre, pcl.paramType.typeId, pcl.paramType.name)
    from ParamCessionLegale pcl where pcl.pays.paysCode = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?1) order by pcl.numOrdre
    """)
    List<ParamCessionLegaleListResp> findPossiblePclByAffId(Long affId);

    @Query("""
        select r.paramCessionLegale.paramCesLegId from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_CES_LEG' and r.repStatut = true and (r.repStaCode.staCode not in ('REFUSE') or r.repStaCode.staCode is null)
    """)
    List<Long> findPclIdsOnAffaire(Long affId);


    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(
    pcl.paramCesLegId, pcl.paramCesLegLibelle, pcl.paramCesLegCapital, pcl.paramCesLegTaux, pcl.pays.paysNom, pcl.statut.staLibelle, pcl.pays.paysCode, pcl.numOrdre, pcl.paramType.typeId, pcl.paramType.name)
    from ParamCessionLegale pcl where pcl.pays.paysCode = ?1 order by pcl.numOrdre
    """)
    List<ParamCessionLegaleListResp> findByPaysCode(String paysCode);


    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where pcl.pays.paysCode = ?1 and ?1 = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?2)")
    boolean existsByPaysAndAffaire(Long paysId, Long affId);

    @Query("select (count(pcl.paramCesLegId)>0) from ParamCessionLegale  pcl where (select pcl2.pays.paysCode from ParamCessionLegale pcl2 where pcl2.paramCesLegId = ?1) = (select a.cedante.pays.paysCode from Affaire a where a.affId = ?2)")
    boolean existsByPclIdAndAffaire(Long pclId, Long affId);

    @Query("select sum(pcl.paramCesLegTaux) from ParamCessionLegale pcl where pcl.paramCesLegId in ?1 and pcl.statut.staCode = 'ACT'")
    BigDecimal getSommeTauxParamCessionLegal(List<Long> pclIds);

    @Query("select (count(pcl)>0) from ParamCessionLegale pcl where pcl.paramCesLegId = ?1 and pcl.paramType.uniqueCode = 'PCL_PF'")
    boolean pclIsPf(Long pclId);

    @Query("select (count(pcl)>0) from ParamCessionLegale pcl where pcl.paramCesLegId = ?1 and pcl.paramType.uniqueCode = 'PCL_SIMPLE'")
    boolean pclIsSimple(Long pclId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg(
        pcl.paramCesLegTaux, pcl.paramCesLegLibelle, pcl.paramCesLegId, true)
        from ParamCessionLegale pcl 
        join Cedante ced on ced.pays.paysCode = pcl.pays.paysCode
        join pcl.paramType t 
        where ced.cedId = ?1 and t.uniqueCode='PCL_TRAI'
""")
    List<CesLeg> findTraiteCesLegsByCedId(Long cedId);
}
