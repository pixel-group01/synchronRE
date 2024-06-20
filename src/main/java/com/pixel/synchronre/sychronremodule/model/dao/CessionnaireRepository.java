package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface CessionnaireRepository extends JpaRepository<Cessionnaire, Long> {

    @Query("select c.cesId from Cessionnaire c")
    List<Long> findAllIds();

    @Query("select (count(c) > 0) from Cessionnaire c where upper(c.cesEmail) = upper(?1)")
    boolean alreadyExistsByEmail(String cesEmail);

    @Query("select (count(c) > 0) from Cessionnaire c where c.cesEmail = ?1 and c.cesId <> ?2")
    boolean alreadyExistsByEmail(String cesEmail, Long cesId);

    @Query("select (count(c) > 0) from Cessionnaire c where upper(c.cesTelephone) = upper(?1)")
    boolean alreadyExistsByTel(String tel);

    @Query("select (count(c) > 0) from Cessionnaire c where upper(c.cesTelephone) = upper(?1) and c.cesId <> ?2")
    boolean alreadyExistsByTel(String tel, Long cesId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(c.cesId, c.cesNom, c.cesSigle, c.cesEmail, c.cesTelephone, 
        c.cesAdressePostale, c.cesSituationGeo, c.statut.staLibelle) 
        from Cessionnaire  c where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesEmail, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesTelephone, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesSigle, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesCellulaire, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesSituationGeo, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesNom, '') ) as string)) ) >0 ) 
                                         and c.statut.staCode = 'ACT'
                                         and c.type.uniqueCode = 'CES'
""")
    Page<CessionnaireListResp> searchCessionnaires(String key, Pageable pageable);

    @Query("""
        select new  com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(
            r.cessionnaire.cesId, r.cessionnaire.cesNom, r.cessionnaire.cesSigle, r.cessionnaire.cesEmail,
            r.cessionnaire.cesTelephone, r.cessionnaire.cesAdressePostale, r.cessionnaire.cesSituationGeo, 
            s.staLibelle)
        from Repartition r left join r.repStaCode s where r.affaire.affId = ?1 and r.repStatut = true and s.staCode not in('REFUSE') and r.type.uniqueCode = 'REP_PLA'
""")
    List<CessionnaireListResp> findByAffId(Long affId);

    @Query("select c.cesSigle from Cessionnaire c where c.cesId = ?1")
    String getCesSigleById(Long cedId);

    @Query("select c.cesNom from Cessionnaire c where c.cesId = ?1")
    String getCesNameById(Long cedId);

    @Query("""
        select new  com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(
            r.cessionnaire.cesId, r.cessionnaire.cesNom, r.cessionnaire.cesSigle, r.cessionnaire.cesEmail,
            r.cessionnaire.cesTelephone, r.cessionnaire.cesAdressePostale, r.cessionnaire.cesSituationGeo, 
            s.staLibelle)
        from Repartition r left join r.repStaCode s where r.sinistre.sinId = ?1 and r.repStatut = true and (s.staCode is null or s.staCode not in('REFUSE', 'SUP', 'SUPP', 'ANNULEE', 'ANNULE')) and r.type.uniqueCode = 'REP_SIN'
""")
    List<CessionnaireListResp> findBySinId(Long sinId);

    @Query("select c from Cessionnaire c where c.type.uniqueCode = 'COURT'")
    List<Cessionnaire> getCourtiers();

    @Query("select r.cessionnaire.cesNom from Repartition r where r.repId =?1")
    String getCesNomByPlaId(Long plaId);

    @Query("""
      select new  com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(
            r.cessionnaire.cesId, r.cessionnaire.cesNom, r.cessionnaire.cesSigle, r.cessionnaire.cesEmail,
            r.cessionnaire.cesTelephone, r.cessionnaire.cesAdressePostale, r.cessionnaire.cesSituationGeo, 
            s.staLibelle)
       from Repartition r left join r.repStaCode s where r.traiteNonProportionnel.traiteNpId = ?1 and r.repStatut = true and (s.staCode is null or s.staCode not in('REFUSE', 'SUP', 'SUPP', 'ANNULEE', 'ANNULE')) and r.type.uniqueCode = 'REP_PLA_TNP'
""")
    List<CessionnaireListResp> findByTraiteNpId(Long traiteNpId);

    @Query("""
      select new  com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(
            ces.cesId, ces.cesNom, ces.cesSigle, ces.cesEmail,
            ces.cesTelephone, ces.cesAdressePostale, ces.cesSituationGeo, 
            ces.statut.staLibelle)
       from Cessionnaire ces where ces.type.uniqueCode not in ('COURT','COURT_PLA') 
       and ces.cesId not in 
       (select ces2.cesId from  Repartition r left join r.cessionnaire ces2 left join r.repStaCode s where r.traiteNonProportionnel.traiteNpId = ?1 and r.repStatut = true and (s.staCode is null or s.staCode not in('REFUSE', 'SUP', 'SUPP', 'ANNULEE', 'ANNULE')) and r.type.uniqueCode = 'REP_PLA_TNP')
    """)
    List<CessionnaireListResp> findCessionnairesNotOnTraite(Long traiteNpId);

    @Query("""
         select new  com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp(c.cesId, c.cesNom, c.cesSigle)
         from Cessionnaire c where c.type.uniqueCode = 'COURT_PLA' and c.statut.staCode='ACT'
         """)
    List<CessionnaireListResp> getCourtierPlaceurs();
}
