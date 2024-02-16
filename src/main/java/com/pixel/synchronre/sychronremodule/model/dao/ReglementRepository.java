package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    @Query("select (count(r) > 0) from Reglement r where r.regReference = ?1")
    boolean alreadyExistsByReference(String regReference);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp(r.regId,r.regReference, r.regDate,r.regMontant,r.regCommission,r.regMode, r.cessionnaire.cesId, concat(r.cessionnaire.cesNom, '(', r.cessionnaire.cesSigle, ')') ) 
        from Reglement r left join r.affaire a left join r.sinistre s left join r.typeReglement t left join r.cessionnaire c where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(r.regReference, '') ) as string)) ) >0 
                                         or cast(r.regMontant as string) like concat(coalesce(concat(?1), ''), '%'))
                                         and (a.affId = ?2 or ?2 is null)
                                         and (s.sinId = ?3 or ?3 is null)
                                         and upper(r.typeReglement.uniqueCode) = upper(?4) 
                                         and r.regStatut=true   
        """)
    Page<ReglementListResp> searchReglement(String key, Long affId, Long sinId, String typeReg, Pageable pageable);

    @Query("select (count(r.regId)>0) from Reglement r where r.affaire.affId = ?1 and r.typeReglement.uniqueCode = ?2")
    boolean affaireHasReglement(Long affId, String typeReg);

    @Query("select coalesce(sum(r.regMontant), 0) from Reglement r where r.affaire.affId = ?1 and r.typeReglement.uniqueCode = ?2 and r.regStatut = true")
    BigDecimal calculateMontantRegleByAffId(Long affId, String typeRegUniqueCode);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.regStatut = true")
    BigDecimal calculateMtRegleBySinistre(Long sinId);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.cessionnaire.cesId = ?2 and r.regStatut = true")
    BigDecimal calculateMtRegleBySinistreAndCes(Long sinId, Long cesId);

    @Query("select r.repTaux * s.sinMontant100 from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId where s.sinId = ?1 and r.cessionnaire.cesId = ?2 and r.repStatut = true")
    BigDecimal calculateMtAReglerBySinistreAndCes(Long sinId, Long cesId);


    @Query("select (count(r.sinistre.sinId)>0) from Reglement r where r.sinistre.sinId = ?1 and r.typeReglement.uniqueCode = ?2 and r.regStatut = true")
    boolean sinistreHasReglement(Long sinId, String typeReg);


    @Query("select sum(r.regMontant) from Reglement r join r.cessionnaire c join Repartition rep on rep.cessionnaire.cesId = c.cesId where rep.repId = ?1 and r.typeReglement.uniqueCode = 'reversements' and r.regStatut = true")
    BigDecimal calculateMtDejaReverseByCes(Long plaId);

    @Query("select sum(r.regCommissionCed) from Reglement r where r.affaire.affId = ?1 and r.regStatut = true and r.typeReglement.uniqueCode = 'paiements'")
    BigDecimal calculateMtComCedDejaEncaisse(Long affId);

    @Query("select sum(r.regCommissionCourt) from Reglement r where r.affaire.affId = ?1 and r.regStatut = true and r.typeReglement.uniqueCode = 'paiements'")
    BigDecimal calculateMtComCourtierDejaEncaisse(Long affId);

    @Query("select r from Reglement r where r.affaire.affId = ?1 and r.regStatut = true")
    List<Long> findRegIdByAffId(Long affId);
}
