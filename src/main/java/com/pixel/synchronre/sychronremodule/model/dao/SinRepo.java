package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SinRepo extends JpaRepository<Sinistre, Long>
{

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp(
        s.sinId, s.sinMontant100, s.sinMontantHonoraire, s.sinDateSurvenance, s.sinDateDeclaration, s.sinCommentaire, aff.affId, aff.affCode, aff.affAssure, aff.affActivite, aff.affCapitalInitial,s.statut.staCode,s.statut.staLibelle) 
        from  Sinistre s left join s.statut st left join s.affaire aff left join s.userCreator u left join s.functionCreator fnc left join aff.cedante ced
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(aff.affCode, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(aff.affAssure, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(aff.affActivite, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  s.sinCommentaire ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(s.sinMontant100 as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(aff.affCapitalInitial as string))) = 1
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.statut.staCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.statut.staLibelle, '') ) as string))) >0
        ) 
       
        and (:fncId is null or :fncId = fnc.id)
        and (:userId is null or :userId = u.userId)
        and (:cedId is null or :cedId = ced.cedId)
        and (:affTypeCode = aff.affType.uniqueCode)
        and s.statut.staCode in :staCodes order by s.sinId desc
           """)
    Page<SinistreDetailsResp> searchSinistres(@Param("key") String key,
                                              @Param("fncId") Long fncId,
                                              @Param("userId") Long userId,
                                              @Param("cedId") Long cedId,
                                              @Param("affTypeCode") String affTypeCode,
                                              @Param("staCodes") List<String> staCodes, Pageable pageable);

    @Query("select s.sinMontant100 + s.sinMontantHonoraire from Sinistre s where s.sinId = ?1")
    BigDecimal getMtSinistre(Long sinId);

    @Query("select s.affaire from Sinistre s where s.sinId = ?1")
    Optional<Affaire> getAffairedBySinId(Long sinId);

    @Query("""
        select r.cessionnaire from Repartition r 
        where r.repStaCode.staCode not in('REFUSE','RET','SAI', 'SAI-CRT') 
        and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true 
        and r.affaire.affId = (select s.affaire.affId from Sinistre s where s.sinId = ?1)
    """)
    List<Cessionnaire> getCessionnaireBySinId(Long sinId);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.typeReglement.uniqueCode = 'paiements' and r.regStatut = true")
    BigDecimal calculateMtDejaPayeBySin(Long sinId);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.typeReglement.uniqueCode = 'reversements' and r.regStatut = true")
    BigDecimal calculateMtDejaReverseBySin(Long sinId);

    @Query("""
        select (s.sinMontant100 + s.sinMontantHonoraire) * r.repTaux/100 from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId 
        where s.sinId = ?1 and r.cessionnaire.cesId = ?2 
        and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE') and a.affStatutCreation = 'REALISEE'
    """)
    BigDecimal calculateMtAPayerBySinAndCes(Long sinId, Long cesId);

    @Query("""
        select sum(r.repCapital) from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId 
        where s.sinId = ?1 and r.type.uniqueCode = 'REP_SIN' 
         and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE') and a.affStatutCreation = 'REALISEE'
    """)
    BigDecimal calculateMtotPlacement(Long sinId); //Le montant total à payer sur un sinistre peut s'obtenir en faisant la somme du sinMontant100 et des honoraires

   @Query("""
    select sum((s.sinMontant100 + s.sinMontantHonoraire) * r.repTaux/100) from Sinistre s join s.affaire a join Repartition r 
    on r.affaire.affId = a.affId where r.type.uniqueCode = 'REP_PLA' and r.repStatut = true 
    and r.repStaCode.staCode not in ('REFUSE', 'SUPP', 'SUP', 'ANNULE', 'ANNULEE') and a.affStatutCreation = 'REALISEE' and s.sinId = ?1
""")
   BigDecimal calculateMtTotalCessionnairesSurSinistre(Long sinId);
                                                            //sinMontant100 + sinMontantHonoraire
    @Query("""
        select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.cessionnaire.cesId = ?2 and r.regStatut = true and r.typeReglement.uniqueCode = 'paiements'
""")
    BigDecimal calculateMtDejaReglerBySinAndCes(Long sinId, Long cesId);

    @Query("select sum(s.sinMontant100 + s.sinMontantHonoraire) from Sinistre s where s.affaire.exercice = ?1 and s.affaire.affStatutCreation = 'REALISEE'")
    BigDecimal calculateMtTotalSinistreByExercice(Long exeCode);


    @Query("""
       select sum(r.regMontant) from Reglement r where r.sinistre.affaire.exercice.exeCode = ?1 and r.sinistre.sinId is not null and r.typeReglement.uniqueCode = 'paiements' and r.regStatut = true  and r.sinistre.affaire.affStatutCreation = 'REALISEE'
          """)
    BigDecimal calculateMtTotalSinistreDejaReglerByExercice(Long exeCode);

    @Query("""
        select sum(r.regMontant) from Reglement r where r.cessionnaire.cesId = ?1 and r.sinistre.affaire.exercice.exeCode = ?2 and r.sinistre.sinId is not null and r.typeReglement.uniqueCode = 'paiements' and r.regStatut = true
          """)
    BigDecimal calculateMtSinistreDejaRegleByCesAndExercice(Long cesId, Long exeCode);

    @Query("""
        select (s.sinMontant100 + s.sinMontantHonoraire) * r.repTaux/100 from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId
        where r.cessionnaire.cesId = ?1 and a.exercice = ?2
        and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE') and a.affStatutCreation = 'REALISE'
    """)
    BigDecimal calculateMtSinistreAReglerByCesAndExercice(Long cesId, Long exeCode);

    @Query("""
        select sum(s.sinMontant100 + s.sinMontantHonoraire) from Sinistre s where s.affaire.cedante.cedId = ?1 and s.affaire.affStatutCreation = 'REALISEE'
        """)
    BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId);


    @Query("""
        select sum(r.regMontant) from Reglement r where r.sinistre.sinId is not null and r.sinistre.affaire.cedante.cedId = ?1 and r.regStatut = true and r.typeReglement.uniqueCode = 'reversements' and r.sinistre.affaire.affStatutCreation = 'REALISEE'
""")
    BigDecimal calculateMtSinistreTotalDejaReverserByCed(Long cedId);

    @Query("select s.sinCode from Sinistre s where s.sinId = ?1")
    String getSinCode(Long sinId);


    @Query("select s.statut.staCode from Sinistre s where s.sinId = ?1")
    String getSinStatut(Long sinId);
}
