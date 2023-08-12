package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCedLegRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.typemodule.model.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.*;

public interface RepartitionRepository extends JpaRepository<Repartition, Long>
{
    @Query("select r.repSousCommission from Repartition r where r.repId = ?1")
    BigDecimal getTauxSousCommission(Long repId);

    @Query("select coalesce(sum(r.repCapital), 0) from Repartition r left join r.repStaCode s where r.affaire.affId = ?1 and r.repStatut = true and (s.staCode is null or s.staCode not in('REFUSE'))")
    BigDecimal getRepartitionsByAffId(Long affId);

    @Query("select count(r.repId) from Repartition r where r.affaire.affId = ?1 and r.repStatut = true")
    Long countCesLegByAffaire(Long affId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2 and r.repStatut = true")
    boolean repExistsByAffaireAndPcl(Long affId, Long pclId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp(
        r.repId, r.repCapital, r.repTaux, r.repSousCommission, r.repTauxComCed, r.repTauxComCourt, r.repInterlocuteur, r.repStatut, a.affId,
        a.affCode, a.affAssure, a.affActivite, c.cesId, c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone
        ) from Repartition r left join r.cessionnaire c left join r.affaire a join r.type t
                                        where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affCode, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affAssure, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affActivite, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesNom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesEmail, '') ) as string)) ) >0
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesTelephone, '') ) as string)) ) >0 ) 
                                         and (?2 is null or a.affId = ?2)
                                         and coalesce(?3, t.uniqueCode) = t.uniqueCode
                                         and r.repStaCode.staCode in ?4
                                         and r.repStatut = true
    """)
    Page<RepartitionListResp> searchRepartition(String key, Long affId, String repType, List<String> staCodes, Pageable pageable);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2")
    boolean existsByAffIdAndPclId(Long affId, Long paramCesLegalId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2")
    Repartition findByAffIdAndPclId(Long affId, Long paramCesLegalId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2")
    boolean existsByAffaireAndTypeRep(Long affId, String typeUniqueCode);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2")
    List<Repartition> findByAffaireAndTypeRep(Long affId, String typeRep);

    @Query("""
            select new com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCesLegReq(
            r.repId, r.repCapital, r.repTaux, r.affaire.affId, r.paramCessionLegale.paramCesLegId, r.repStatut, r.paramCessionLegale.paramCesLegLibelle) 
            from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_CES_LEG'
            """)
    List<UpdateCesLegReq> findUpdateCesLegReqByAffaireAndTypeRep(Long affId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    Optional<Repartition> repartFindByAffaire(Long affId);

    @Query("select r.affaire.affId from Repartition r where r.repId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    Long repartFindByRep(Long plaId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2 and r.cessionnaire.cesId = ?3")
    boolean existsByAffaireAndTypeRepAndCesId(Long affId, String uniqueCode, Long cesId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2 and r.cessionnaire.cesId = ?3")
    Repartition findByAffaireAndTypeRepAndCesId(Long affId, String typeRep, Long cesId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    boolean affaireHasPlacement(Long affId);

    @Query("select r.cessionnaire.cesId from Repartition r left join r.repStaCode s where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and (s.staCode is null or s.staCode not in('REFUSE'))")
    Set<Long> getCesIdsByAffId(Long affId);

    @Query("select r.repTaux from Repartition r where r.repId = ?1")
    BigDecimal getTauRep(Long plaId);

    @Query("select r.repTauxComCed from Repartition r where r.repId = ?1")
    BigDecimal getTauxCmsCedante(Long plaId);

    @Query("select r.repTauxComCourt from Repartition r where r.repId = ?1")
    BigDecimal getTauxCmsCourtage(Long plaId);

    @Query("select (count(r.repId)>0) from Repartition r where r.repId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    boolean placementExists(Long plaId);

    @Query("select (count(r.repId)>0) from Repartition r where r.repId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut =true and r.repStaCode.staCode not in ('REFUSE')")
    boolean placementExistsAndIsActive(Long plaId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.cessionnaire.cesId = ?2 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE')")
    Optional<Repartition> getPlacementByAffIdAndCesId(Long affId, Long cesId);
    @Query("select r.repId from Repartition r where r.affaire.affId = ?1 and r.cessionnaire.cesId = ?2 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE')")
    Optional<Long> getPlacementIdByAffIdAndCesId(Long affId, Long cesId);

    @Query("select r from Repartition r where r.repId = ?1 and r.type.uniqueCode = 'REP_PLA'")
    Optional<Repartition> findPlacementById(Long plaId);

    @Query("select r.repId from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE')")
    List<Long> getPlaIdsByAffId(Long affId);

    @Query("select r.affaire.affId from Repartition r where r.repId = ?1")
    Long getAffIdByRepId(Long repId);

    @Query("select r.cessionnaire.cesId from Repartition r where r.repId = ?1")
    Long getCesIdByRepId(Long repId);

    @Query("select r.repCapital from Repartition r where r.repId = ?1")
    BigDecimal getRepCapitalByRepId(Long repId);

    @Query("select sum(r.repCapital) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE')")
    BigDecimal calculateMtTotalPlacementbyAffaire(Long affId);

    @Query("select r.cessionnaire.cesEmail from Repartition r where r.repId = ?1")
    String getInterlocuteurEmail(Long plaId);

    @Query("select r.cessionnaire.cesInterlocuteur from Repartition r where r.repId = ?1")
    String getInterlocuteur(Long plaId);

    @Query("select r.cessionnaire from Repartition r where r.repId = ?1")
    Optional<Cessionnaire> getCessionnaireByRepId(Long repId);

    @Query("select r.affaire from Repartition r where r.repId = ?1")
    Optional<Affaire> getAffairedByRepId(Long plaId);

    @Query("select r.affaire.facPrime from Repartition r where r.repId = ?1")
    BigDecimal getFacPrimeTotalByPlaId(Long plaId);

    @Query("select (a.facPrime * r.repTaux * r.repTauxComCed /10000) from Repartition r join r.affaire a where r.repId = ?1")
    BigDecimal calculateMtCmsCedByCes(Long plaId);

    @Query("select (a.facPrime * r.repTaux * r.repTauxComCourt /10000) from Repartition r join r.affaire a where r.repId = ?1")
    BigDecimal calculateMtCmsCourtByCes(Long plaId);

    @Query("select (a.facPrime * r.repTaux * r.repSousCommission /10000) from Repartition r join r.affaire a where r.repId = ?1")
    BigDecimal calculateMtSousCmsByCes(Long plaId);

    @Query("select sum(r.affaire.facPrime * r.repTaux * r.repSousCommission/10000) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true")
    BigDecimal calculateMtTotalSousCommission(Long affId);

    @Query("select (a.facPrime * r.repTaux /100) from Repartition r join r.affaire a where r.repId = ?1")
    BigDecimal calculateMtPrimeBruteByCes(Long plaId);

    @Query("select sum(a.facPrime * r.repTaux /100) from Repartition r join r.affaire a where a.affId = ?1 and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE') ")
    BigDecimal calculateMtPrimeBruteByAffaire(Long affId);


    @Query("select new com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCedLegRepartitionReq(r.repCapital,  r.repTaux, r.repSousCommission, r.affaire.affId) from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = 'REP_CED' and r.repStatut = true")
    CreateCedLegRepartitionReq getCedLegRepartitionDTO(Long affId);

    @Query("select (count(r.repId)>0) from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2 and r.type.uniqueCode = 'REP_CES_LEG' and r.repStatut = true and r.repStaCode not in ('REFUSE')")
    boolean existsValidByAffIdAndPclId(Long affId, Long pclId);

    @Query("select r from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = ?2 and r.type.uniqueCode = 'REP_CES_LEG' and r.repStatut =true")
    Repartition findValidByAffIdAndPclId(Long affId, Long pclId);


    @Query("select r.cessionnaire.cesId from Repartition r where r.affaire.affId = (select s.affaire.affId from Sinistre s where s.sinId = ?1) and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE')")
    List<Long> getCesIdsBySinId(Long sinId);//

    @Query("select (count(r.repId)>0) from Repartition r where r.repId = ?1 and r.affaire.affId = ?2 and r.type.uniqueCode = ?3")
    boolean existsByRepIdAndAffIdAndTypeRep(Long repId, Long affId, String rep_ces_leg);

    @Query("select r.repId from Repartition r where r.affaire.affId = ?1 and r.paramCessionLegale.paramCesLegId = 2 and r.type.uniqueCode = 'REP_CES_LEG'")
    Long getRepIdByAffIdAndPclId(Long affId, Long paramCesLegId);

    @Query("select r.repId from Repartition r where r.affaire.affId = ?1 and r.type.uniqueCode = ?2")
    Long getRepIdByAffIdAndTypeRep(Long affId, String typeStaCode);

    //List<Repartition> findByAffaireAndTypeRep(Long affId, String rep_ced);

    @Query("""
        select sum(r.repCapital) from Repartition r where r.affaire.affId = ?1 
        and r.type.uniqueCode in ('REP_RETENTION', 'REP_FACOB', 'REP_FACOB')
        and r.repStatut = true
    """)
    BigDecimal calculateSommeCapitalTraiteByAffId(Long affId);

    @Query("""
        select r from Repartition r where r.type.uniqueCode not in ('REP_PLA') and r.repStatut = true and r.repStaCode not in ('REFUSE')
""")
    boolean repartitionModeIsUpdate(Long affId);
}
