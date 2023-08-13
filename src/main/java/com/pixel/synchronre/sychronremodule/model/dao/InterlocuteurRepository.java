package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterlocuteurRepository extends JpaRepository <Interlocuteur,Long> {

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intEmail) = upper(?1)")
    boolean alreadyExistsByEmail(String intEmail);

    @Query("select (count(i) > 0) from Interlocuteur i where i.intEmail = ?1 and i.intId <> ?2")
    boolean alreadyExistsByEmail(String intEmail, Long intId);

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intTel) = upper(?1)")
    boolean alreadyExistsByTel(String intTel);

    @Query("select (count(i) > 0) from Interlocuteur i where upper(i.intTel) = upper(?1) and i.intId <> ?2")
    boolean alreadyExistsByTel(String intTel, Long intId);

    @Query("select (count(i.intId)>0) from Interlocuteur i where i.intId = ?1")
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
}
