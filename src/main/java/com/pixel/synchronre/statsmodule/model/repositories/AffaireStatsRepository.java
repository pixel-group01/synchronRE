package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AffaireStatsRepository extends JpaRepository<Affaire, Long>
{
    @Query("""
        select new com.pixel.synchronre.statsmodule.model.dtos.AffaireStats(
        count(distinct a.affId), 
        sum(a.affCapitalInitial), sum(a.facSmpLci)) 
        from Affaire a  left join a.statut s
         where s.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE') and a.exercice.exeCode in :exes and a.cedante.cedId in :cedIds  
         and  a.affStatutCreation = coalesce(:statCrea, a.affStatutCreation) and s.staCode in :staCodes and a.couverture.couId in :couIds 
         and a.devise.devCode in :devCodes and (a.affDateEffet <= :dateEcheance and a.affDateEcheance >= :dateEffet) and (:dateEffet <= :dateEcheance)
""")
    AffaireStats getAffaireStats(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds,
                                 @Param("statCrea")String statutCreation, @Param("staCodes")List<String> staCodes, @Param("couIds")List<Long> couIds,
                                 @Param("devCodes")List<String> devCodes, @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);

    @Query("""
        select new com.pixel.synchronre.statsmodule.model.dtos.AffaireStats(
        count(distinct a.affId), 
        sum(a.affCapitalInitial), sum(a.facSmpLci), sum(r.repCapital)) 
        from Affaire a  left join a.statut s left join Repartition r on r.affaire.affId = a.affId left join r.cessionnaire ces left join r.type t
         where s.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE') and a.exercice.exeCode in :exes and (ces is null or ces.cesId in :cesIds) 
         and (r is null or r.repStatut = true) and (t is null or t.uniqueCode = 'REP_PLA')
         and  a.affStatutCreation = coalesce(:statCrea, a.affStatutCreation) and s.staCode in :staCodes and a.couverture.couId in :couIds 
         and a.devise.devCode in :devCodes and (a.affDateEffet <= :dateEcheance and a.affDateEcheance >= :dateEffet) and (:dateEffet <= :dateEcheance)
""")
    AffaireStats getAffaireStatsParCessionnaires(@Param("exes") List<Long> exercices, @Param("cesIds")List<Long> cesIds,
                                 @Param("statCrea")String statutCreation, @Param("staCodes")List<String> staCodes, @Param("couIds")List<Long> couIds,
                                 @Param("devCodes")List<String> devCodes, @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);



    @Query(""" 
        select a0.affId from Affaire a0 where a0.affId not in
        (select distinct a.affId
        from Affaire a left join Repartition r on  a.affId  =  r.affaire.affId left join r.type t left join r.repStaCode s left join r.cessionnaire ces
         where s.staCode not in ('SUP', 'SUPP') and a.exercice.exeCode in :exes and a.cedante.cedId in :cedIds and (ces is null or ces.cesId in :cesIds) 
         and  a.affStatutCreation = coalesce(:statCrea, a.affStatutCreation) and s.staCode in :staCodes and a.couverture.couId in :couIds 
         and (r is null or r.repStatut = true) and (t is null or t.uniqueCode = 'REP_PLA') and (s is null or s.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE'))
         and a.devise.devCode in :devCodes and a.affDateEffet between :dateEffet and :dateEcheance)
""")
    List<Long> getAffaireStats2(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds, @Param("cesIds")List<Long> cesIds,
                                @Param("statCrea")String statutCreation, @Param("staCodes")List<String> staCodes, @Param("couIds")List<Long> couIds,
                                @Param("devCodes")List<String> devCodes, @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);
}
