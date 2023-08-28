package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterlocuteurRepository extends JpaRepository <Interlocuteur,Long> {

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intEmail) = upper(?1) and i.statut.staCode='ACT'")
    boolean alreadyExistsByEmail(String intEmail);

    @Query("select (count(i) > 0) from Interlocuteur i where i.intEmail = ?1 and i.intId <> ?2 and i.statut.staCode='ACT'")
    boolean alreadyExistsByEmail(String intEmail, Long intId);

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intTel) = upper(?1) and i.statut.staCode='ACT'")
    boolean alreadyExistsByTel(String intTel);

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intTel) = upper(?1) and i.intId <> ?2 and i.statut.staCode='ACT'")
    boolean alreadyExistsByTel(String intTel, Long intId);

    @Query("select (count(i.intId)>0) from Interlocuteur i where i.intId = ?1 and i.statut.staCode='ACT'")
    boolean interlocuteurExists(Long intId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp(i.intId, i.intNom, i.intPrenom, i.intTel, i.intEmail,i.cessionnaire.cesId,i.cessionnaire.cesNom,i.cessionnaire.cesSigle,i.statut.staCode)
        from Interlocuteur i where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.intNom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.intPrenom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.intTel, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.intEmail, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.cessionnaire.cesNom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(i.cessionnaire.cesSigle, '') ) as string)) ) >0 ) 
                                         and i.cessionnaire.cesId = ?2
                                         and i.statut.staCode = 'ACT'     
""")
    Page<InterlocuteurListResp> searchInterlocuteur(String key, Long cesId, Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp(i.intId, i.intNom, i.intPrenom, i.intTel, i.intEmail,i.cessionnaire.cesId,i.cessionnaire.cesNom,i.cessionnaire.cesSigle,i.statut.staCode) from Interlocuteur i where i.cessionnaire.cesId = ?1
    """)
    List<InterlocuteurListResp> getInterlocuteursByCessionnaire(Long cesId);

    @Query("select r.autreInterlocuteurs from Repartition r where r.repId = ?1")
    String getAutreInterlocuteursIdsByPlacement(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp(
        r.interlocuteurPrincipal.intId, r.interlocuteurPrincipal.intNom,
        r.interlocuteurPrincipal.intPrenom, r.interlocuteurPrincipal.intTel, 
        r.interlocuteurPrincipal.intEmail,r.interlocuteurPrincipal.cessionnaire.cesId,
        r.interlocuteurPrincipal.cessionnaire.cesNom,r.interlocuteurPrincipal.cessionnaire.cesSigle,
        r.interlocuteurPrincipal.statut.staCode) 
        from Repartition r where r.repId = ?1
""")
    InterlocuteurListResp getInterlocuteursPrincipal(Long repId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp(
        i.intId, i.intNom, i.intPrenom, i.intTel, 
        i.intEmail, i.cessionnaire.cesId, i.cessionnaire.cesNom, i.cessionnaire.cesSigle, 
        i.statut.staCode) from Interlocuteur i where i.intId = ?1
""")
    InterlocuteurListResp findInterlocuteursById(Long intId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp(
        r.interlocuteurPrincipal.intId, r.interlocuteurPrincipal.intNom,
        r.interlocuteurPrincipal.intPrenom, r.interlocuteurPrincipal.intTel, 
        r.interlocuteurPrincipal.intEmail,r.interlocuteurPrincipal.cessionnaire.cesId,
        r.interlocuteurPrincipal.cessionnaire.cesNom,r.interlocuteurPrincipal.cessionnaire.cesSigle,
        r.interlocuteurPrincipal.statut.staCode) 
        from Repartition r join r.affaire a join Sinistre s on s.affaire.affId = a.affId where s.sinId = ?1 
        and r.cessionnaire.cesId = ?2 and r.repStatut = true and r.repStatut not in ('REFUSE') and r.type.uniqueCode = 'REP_PLA'                                                                
        """)
    InterlocuteurListResp getInterlocuteursPrincipalBySinAndCes(Long sinId, Long cesId);
}
