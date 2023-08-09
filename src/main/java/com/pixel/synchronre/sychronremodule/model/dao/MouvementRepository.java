package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long>
{


    @Query("""
            select new com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp(
            m.mvtId, a.affId, a.affAssure, a.affActivite, m.statut.staLibelle, m.statut.staLibelleLong,
            c.cedNomFiliale, c.cedSigleFiliale, 
            s.sinId,s.sinCode,s.sinMontant100,s.sinMontantHonoraire,s.sinDateSurvenance,s.sinDateDeclaration,
            m.mvtObservation,m.mvtUser.email,concat(m.mvtUser.firstName, ' ', m.mvtUser.lastName),m.mvtFunction.name, m.mvtDate)
            From Mouvement m left join m.affaire a left join a.cedante c left join m.sinistre s where (a.affId = ?1 or ?1 is null) and (s.sinId = ?2 or ?2 is null)order by m.mvtDate desc""")
    List<MouvementListResp> findMouvementById(Long affId, Long sinId);

    @Query("select m.mvtObservation from Mouvement m where m.affaire.affId = ?1 and m.statut.staCode = 'RET' and m.mvtDate = (select max(m.mvtDate) from Mouvement m where m.affaire.affId = ?1 and m.statut.staCode = 'RET')")
    String getMessageRetourForAffaire(Long affId);

    @Query("select m.mvtObservation from Mouvement m where m.placement.repId = ?1 and m.statut.staCode = 'RET' and m.mvtDate = (select max(m.mvtDate) from Mouvement m where m.placement.repId = ?1 and m.statut.staCode = 'RET')")
    String getMessageRetourForPlacement(Long affId);

    @Query("select m.mvtObservation from Mouvement m where m.placement.repId = ?1 and m.statut.staCode = 'REFUS' and m.mvtDate = (select max(m.mvtDate) from Mouvement m where m.placement.repId = ?1 and m.statut.staCode = 'REFUS')")
    String getMessageRefusForPlacement(Long plaId);

    @Query("""
        select m.mvtObservation from Mouvement m 
        where (m.affaire.affId is null or m.affaire.affId = :affId) 
        and (m.placement.repId is null or m.placement.repId = :plaId) 
        and (m.sinistre.sinId is null or m.sinistre.sinId = :sinId) 
        and m.statut.staCode = :staCode 
        and m.mvtDate = (select max(m.mvtDate) from Mouvement m where 
        (m.placement.repId is null or m.placement.repId = :plaId) 
        and (m.sinistre.sinId is null or m.sinistre.sinId = :sinId) 
        and m.statut.staCode = :staCode )
        """)
    String getMvtMessage(@Param("affId") Long affId, @Param("plaId") Long plaId, @Param("sinId") Long sinId, @Param("staCode") String staCode);




}
