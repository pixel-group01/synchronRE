package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    @Query("select (count(r) > 0) from Reglement r where r.regReference = ?1")
    boolean alreadyExistsByReference(String regReference);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp(r.regId,r.regReference, r.regDate,r.regMontant,r.regCommission,r.regMode) 
        from Reglement r where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(r.regReference, '') ) as string)) ) >0 
                                         or cast(r.regMontant as string) like concat(coalesce(concat(?1), ''), '%'))
                                         and r.affaire.affId = ?2
                                         and upper(r.typeReglement.uniqueCode) = upper(?3)     
        """)
    Page<ReglementListResp> searchReglement(String key, Long affId, String typeReg, Pageable pageable);

    @Query("select (count(r.regId)>0) from Reglement r where r.affaire.affId = ?1 and r.typeReglement.uniqueCode = ?2 ")
    boolean affaireHasReglement(Long affId, String typeReg);

    @Query("select coalesce(sum(r.regMontant), 0) from Reglement r where r.affaire.affId = ?1 and r.typeReglement.uniqueCode = ?2 and r.regStatut = true")
    BigDecimal getMontantRegleByAffId(Long affId, String typeRegUniqueCode);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.regStatut = true")
    BigDecimal getMtRegleBySinistre(Long sinId);

    @Query("select sum(r.regMontant) from Reglement r where r.sinistre.sinId = ?1 and r.cessionnaire.cesId = ?2 and r.regStatut = true")
    BigDecimal getMtRegleBySinistreAndCes(Long sinId, Long cesId);

    @Query("select r.repTaux * s.sinMontant100 from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId where s.sinId = ?1 and r.cessionnaire.cesId = ?2 and r.repStatut = true")
    BigDecimal getMtAReglerBySinistreAndCes(Long sinId, Long cesId);


}
