package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AffaireStatsRepository extends JpaRepository<Affaire, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats(
        count(a.affId), sum(a.affCapitalInitial), sum(a.facSmpLci)) 
        from Affaire a left join Repartition r on  a.affId  =  r.affaire.affId left join r.type t left join r.repStaCode s left join r.cessionnaire ces
         where a.statut.staCode not in ('SUP', 'SUPP') and a.exercice.exeCode in :exes and a.cedante.cedId in :cedIds and (ces is null or ces.cesId in :cesIds) 
         and  a.affStatutCreation = coalesce(:statCrea, a.affStatutCreation) and a.statut.staCode in :staCodes and a.couverture.couId in :couIds 
         and (r is null or r.repStatut = true) and (t is null or t.uniqueCode = 'REP_PLA') and (s is null or s.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE'))
         and a.devise.devCode in :devCodes and a.affDateEffet between :dateEffet and :dateEcheance
""")
    AffaireStats getAffaireStats(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds, @Param("cesIds")List<Long> cesIds,
                                 @Param("statCrea")String statutCreation, @Param("staCodes")List<String> staCodes, @Param("couIds")List<Long> couIds,
                                 @Param("devCodes")List<String> devCodes, @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats(
        count(a.affId), sum(a.affCapitalInitial), sum(a.facSmpLci)) 
        from Affaire a left join Repartition r on  a.affId  =  r.affaire.affId left join r.type t left join r.repStaCode s left join r.cessionnaire ces
         where a.statut.staCode not in ('SUP', 'SUPP') and a.exercice.exeCode in :exes and a.cedante.cedId in :cedIds and (ces.cesId in :cesIds) 
         and  a.affStatutCreation = coalesce(:statCrea, a.affStatutCreation) and a.statut.staCode in :staCodes and a.couverture.couId in :couIds 
         and (r.repStatut = true) and (t.uniqueCode = 'REP_PLA') and (s.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE'))
         and a.devise.devCode in :devCodes and a.affDateEffet between :dateEffet and :dateEcheance
""")
    AffaireStats getAffaireStatsParCessionnaires(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds, @Param("cesIds")List<Long> cesIds,
                                 @Param("statCrea")String statutCreation, @Param("staCodes")List<String> staCodes, @Param("couIds")List<Long> couIds,
                                 @Param("devCodes")List<String> devCodes, @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);

}
