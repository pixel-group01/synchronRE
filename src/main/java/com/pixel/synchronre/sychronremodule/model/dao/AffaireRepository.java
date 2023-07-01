package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCedLegRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
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

public interface AffaireRepository extends JpaRepository<Affaire, Long>
{

    @Query("select a.affStatutCreation from Affaire a where a.affId = ?1")
    String getAffStatutCreation(Long affId);

    @Query("select coalesce(a.affCapitalInitial, 0)  from Affaire a where a.affId = ?1")
    BigDecimal getCapitalInitial(Long affId);

    @Query("select coalesce(a.facSmpLci, 0)  from Affaire a where a.affId = ?1")
    BigDecimal getFacSmpLci(Long affId);

    @Query("select r from Affaire r where r.affId = ?1")
    Optional<Affaire> findById(Long affId);

    @Query("select a from Affaire a where a.affCode = :affCode and a.cedante.cedNomFiliale = :cedNomFiliale")
    List<Affaire> test(@Param("affCode") String affCode, @Param("cedNomFiliale") String cedNomFiliale);
    //from Repartition r left join r.cessionnaire c left join r.affaire a

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp(
        f.affId, f.affCode, f.affAssure, f.affActivite, f.affDateEffet, f.affDateEcheance, f.facNumeroPolice, f.affCapitalInitial,
        f.facSmpLci, f.facPrime, d.devCode, f.affStatutCreation, ced.cedId, s.staCode, s.staLibelle, c.couLibelle, ced.cedNomFiliale, ced.cedSigleFiliale, f.exercice.exeCode) 
        from Affaire f left join f.statut s left join f.couverture c left join f.affUserCreator u left join f.affFonCreator fnc 
            left join f.cedante ced left join f.devise d 
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(f.affCode, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.affAssure, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.affActivite, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  f.facNumeroPolice ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(f.affCapitalInitial as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(f.facSmpLci as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(f.facPrime as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(c.couLibelle, '') ) as string))) >0
        )       
        and (:fncId is null or :fncId = fnc.id) 
        and (:userId is null or :userId = u.userId) 
        and (:cedId is null or :cedId = ced.cedId) 
        and (:exeCode is null or :exeCode = f.exercice.exeCode) 
        and s.staCode in :staCodes 
""")
    Page<FacultativeListResp> searchAffaires(@Param("key") String key,
                                             @Param("fncId") Long fncId,
                                             @Param("userId") Long userId,
                                             @Param("cedId") Long cedId,
                                             @Param("staCodes") List<String> staCodes,
                                             @Param("exeCode")Long exeCode, Pageable pageable);

    @Query("select aff.cedante.cedId from Affaire aff where aff.affId = ?1")
    Long getAffCedId(Long affId);

    @Query("select aff.facPrime from Affaire aff where aff.affId = ?1")
    BigDecimal getFacPrime(Long affId);

    @Query("select a.affCode from Affaire a where a.affId = ?1")
    String getAffCode(Long affId);

    @Query("select s.affaire from Sinistre s where s.sinId = ?1")
    Optional<Affaire> getAffaireBySinId(Long sinId);

    @Query("""
        select sum(r.repCapital) from Repartition r join r.affaire a on r.affaire.affId = a.affId 
        where a.affId = ?1 and r.type.uniqueCode = 'REP_PLA' 
         and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE') and a.affStatutCreation = 'REALISEE'
    """)
    BigDecimal calculateMtotAPayerByCesByAff(Long affId); // capital100*(1-ccm)

    //@Query("select aff.affTauxCommissionReassureur from Affaire aff where aff.affId = ?1")
    //BigDecimal getTauxCommissionReassureur(Long affId);




    /*
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp(
        f.affId, f.affCode, f.affAssure, f.affActivite, f.affDateEffet, f.affDateEcheance, f.facNumeroPolice, f.affCapitalInitial,
        f.facSmpLci, f.facPrime, s.staCode, s.staLibelle, c.couLibelle)
        from Facultative f left join f.statut s left join f.couverture c left join f.affUserCreator u left join f.affFonCreator fnc left join f.cedante ced
                                        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(f.affCode, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.affAssure, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.affActivite, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.facNumeroPolice, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.affCapitalInitial, '') ) as string))) =1
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.facSmpLci, '') ) as string))) =1
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(f.facPrime, '') ) as string))) =1
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staCode, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelle, '') ) as string))) >0
                                         or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(c.couLibelle, '') ) as string))) >0)
                                         and fnc.id = coalesce(:fncId, fnc.id)
                                         and u.userId = coalesce(:userId, u.userId)
                                         and ced.cedId = coalesce(:cedId, ced.cedId)
                                         and ced.cedParenId = coalesce(:cedParenId, ced.cedParenId)
                                         and s.staCode in coalesce(:staCodes, s.staCode)
""")
    Page<CessionnaireListResp> searchCessionnaires(@Param("key") String key, @Param("fncId") Long userFncId, @Param("userId") Long userId,
                                                   @Param("cedId")Long cedId, @Param("cedParentId")Long cedParentId,
                                                   @Param("staCodes")List<String> staCodes, Pageable pageable);

     */


}
