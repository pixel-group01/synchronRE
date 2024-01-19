package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
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
        sum(cast(a.affCapitalInitial * coalesce(a.affCoursDevise, 1) as java.math.BigDecimal)),
        sum(cast(a.facSmpLci * coalesce(a.affCoursDevise, 1) as java.math.BigDecimal)))
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
    select r0 from Repartition r0 where r0.repId in
    (select distinct r.repId    
    from Repartition r left join r.type t left join r.cessionnaire ces  left  join r.affaire a left join a.cedante ced    
    left join r.repStaCode repStatut left join a.statut affStatut    where t.uniqueCode = 'REP_PLA' and ces.cesId in :cesIds and ced.cedId in :cedIds and a.exercice.exeCode in :exes     
    and repStatut.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE') and affStatut.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE')    
    and a.couverture.couId in :couIds and a.devise.devCode in :devCodes     and (a.affDateEffet <= :dateEcheance and a.affDateEcheance >= :dateEffet) and (:dateEffet <= :dateEcheance)) 
""")
    List<Repartition>  getRepartitionStats(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds, @Param("cesIds")List<Long> cesIds,
                                   @Param("couIds")List<Long> couIds, @Param("devCodes")List<String> devCodes,
                                   @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);

    @Query("""
        select r from Reglement r where r.affaire.affId =?1 and r.typeReglement.uniqueCode = 'paiements' and r.regStatut = true
""")
    List<Reglement> getPaiementsByAffaire(Long affId);

    @Query("""
        select new com.pixel.synchronre.statsmodule.model.dtos.CommissionStats(sum(cast(r.repSousCommission * r.repPrime * coalesce(a.affCoursDevise, 1) as java.math.BigDecimal)), sum(cast(r.repTauxComCourt *r.repPrime * coalesce(a.affCoursDevise, 1) as java.math.BigDecimal)), sum(cast(r.repTauxComCed * r.repPrime * coalesce(a.affCoursDevise, 1) as java.math.BigDecimal))) 
        from Repartition r left join r.type t left join r.cessionnaire ces  left  join r.affaire a left join a.cedante ced    
        left join r.repStaCode repStatut left join a.statut affStatut    where t.uniqueCode = 'REP_PLA' and ces.cesId in :cesIds and ced.cedId in :cedIds and a.exercice.exeCode in :exes     
        and repStatut.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE') and affStatut.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE', 'ANNULEE')    
        and a.couverture.couId in :couIds and a.devise.devCode in :devCodes     and (a.affDateEffet <= :dateEcheance and a.affDateEcheance >= :dateEffet) and (:dateEffet <= :dateEcheance)
""")
    CommissionStats getCommissionStats(@Param("exes") List<Long> exercices, @Param("cedIds")List<Long> cedIds, @Param("cesIds")List<Long> cesIds,
                        @Param("couIds")List<Long> couIds, @Param("devCodes")List<String> devCodes,
                        @Param("dateEffet")LocalDate dateEffet, @Param("dateEcheance")LocalDate dateEcheance);
}


