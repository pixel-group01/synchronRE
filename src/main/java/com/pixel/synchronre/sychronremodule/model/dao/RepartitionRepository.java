package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RepartitionRepository extends JpaRepository<Repartition, Long>
{
    @Query("select sum(r.repCapital) from Repartition r where r.affaire.affId = ?1 and r.repStatut = true")
    Float getRepartitionsByAffId(Long affId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp(
        r.repId, r.repCapital, r.repTaux, r.repSousCommission, r.repInterlocuteur, r.repStatut, r.affaire.affId,
        r.affaire.affCode, r.affaire.affAssure, r.affaire.affActivite, r.cessionnaire.cesId, r.cessionnaire.cesNom,
        r.cessionnaire.cesEmail, r.cessionnaire.cesTelephone
        ) from Repartition r left join r.cessionnaire c left join r.affaire a
                                        where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affCode, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affAssure, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(a.affActivite, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesNom, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesEmail, '') ) as string)) ) >0
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cesTelephone, '') ) as string)) ) >0 ) 
                                         and r.repStatut = true
    """)
    Page<RepartitionListResp> searchRepartition(String key, Pageable pageable);
}
