package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.typemodule.model.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Set;


public interface RepartitionRepository extends JpaRepository<Repartition, Long>
{
    @Query("select r.repSousCommission from Repartition r where r.affaire.affId = ?1 and r.cessionnaire.cesId = ?2 and r.repStatut = true")
    BigDecimal getTauxSousCommission(Long affId, Long cesId);

    @Query("select coalesce(sum(r.repCapital), 0) from Repartition r where r.affaire.affId = ?1 and r.repStatut = true")
    BigDecimal getRepartitionsByAffId(Long affId);

    @Query("select count(r.repId) from Repartition r where r.affaire.affId = ?1 and r.repStatut = true")
    Long countCesLegByAffaire(Long affId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2 and r.repStatut = true")
    boolean repExistsByAffaireAndPcl(Long affId, Long pclId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp(
        r.repId, r.repCapital, r.repTaux, r.repSousCommission, r.repInterlocuteur, r.repStatut, a.affId,
        a.affCode, a.affAssure, a.affActivite, c.cesId, c.cesNom, c.cesSigle, 
        c.cesEmail, c.cesTelephone
        ) from Repartition r left join r.cessionnaire c left join r.affaire a left join r.type t
                                        where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affCode, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affAssure, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affActivite, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesNom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesEmail, '') ) as string)) ) >0
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesTelephone, '') ) as string)) ) >0 ) 
                                         and (?2 is null or a.affId = ?2)
                                         and coalesce(?3, t.uniqueCode) = t.uniqueCode
                                         and r.repStatut = true
    """)
    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, Pageable pageable);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2")
    boolean existsByIdAffIdAndPclId(Long affId, Long paramCesLegalId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2")
    Repartition findByIdAffIdAndPclId(Long affId, Long paramCesLegalId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2")
    boolean existsByAffaireAndTypeRep(Long affId, String typeUniqueCodeCode);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2")
    Repartition findByAffaireAndTypeCed(Long affId, String rep_ced);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2 and r.cessionnaire.cesId = ?3")
    boolean existsByAffaireAndTypeRepAndCesId(Long affId, String rep_pla, Long cesId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2 and r.cessionnaire.cesId = ?3")
    Repartition findByAffaireAndTypeRepAndCesId(Long affId, String typeRep, Long cesId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    boolean affaireHasPlacement(Long affId);

    @Query("select r.cessionnaire.cesId from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true")
    Set<Long> getCesIdsByAffId(Long affId);

    @Query("select r.repTaux from Repartition r where r.affaire.affId = ?1 and r.cessionnaire.cesId = ?2 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true")
    BigDecimal getTauRep(Long affId, Long cesId);

    @Query("select r.repSousCommission from Repartition r where r.affaire.affId = ?1 and r.cessionnaire.cesId = ?2 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true")
    BigDecimal getTauxCms(Long affId, Long cesId);
}
