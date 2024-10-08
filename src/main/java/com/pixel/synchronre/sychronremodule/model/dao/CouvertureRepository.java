package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouvertureRepository extends JpaRepository<Couverture, Long>
{
    @Query("select c.couId from Couverture c")
    List<Long> findAllIds();

    @Query("select (count(c) > 0) from Couverture c where upper(c.couLibelleAbrege) = upper(?1)")
    boolean alreadyExistsByCouLibelleAbrege(String couLibelleAbrege);

    @Query("select (count(c) > 0) from Couverture c where c.couLibelleAbrege = ?1 and c.couId <> ?2")
    boolean alreadyExistsByCouLibelleAbrege(String couLibelleAbrege, Long couId);


    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp(cv.couId, cv.couLibelle, cv.couLibelleAbrege, 
       parent.couId, parent.couLibelle, cv.branche.branId,cv.branche.branLibelle, cv.statut.staLibelle) 
        from Couverture cv left join cv.couParent parent where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(cv.couLibelle, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(cv.couLibelleAbrege, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(cv.branche.branLibelle, '') ) as string)) ) >0 ) 
                                         and cv.statut.staCode = 'ACT'     
""")
    Page<CouvertureListResp> searchCouvertures(String key, Pageable pageable);

    @Query("select (count(c.couId)>0) from Couverture c where c.couId = ?1 and c.couParent is null")
    boolean existsParentId(Long couId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp(cv.couId, cv.couLibelle, cv.couLibelleAbrege) 
        from Couverture cv where cv.couParent is null 
        and exists 
                    (select c from Couverture c where c.couParent.couId = cv.couId and c.couId not in 
                        (select a.couverture.couId from Association a where a.type.uniqueCode = 'RISQ-DET' 
                          and a.risqueCouvert.couverture.couId = cv.couId 
                          and a.risqueCouvert.traiteNonProportionnel.traiteNpId = ?1 
                          and a.couverture.couId = c.couId)
                    )
        and cv.statut.staCode = 'ACT' order by cv.couLibelle
""")
    List<CouvertureListResp> getCouerturesParents(Long traiteNpId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp(cv.couId, cv.couLibelle, cv.couLibelleAbrege) 
    from Couverture cv
    where  cv.couParent.couId = ?2 
    AND cv.couId not in (
        select a.couverture.couId from Association a where a.type.uniqueCode = 'RISQ-DET' 
        and a.risqueCouvert.traiteNonProportionnel.traiteNpId = ?1 
        and a.risqueCouvert.couverture.couId = ?2
        and a.couverture.couId = cv.couId
         and cv.nature.natCode = a.couverture.nature.natCode
    )
   
    ORDER BY cv.couLibelle
""")
    List<CouvertureListResp> getCouerturesFilles(Long traiteNpId, Long couParentId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp(cv.couId, cv.couLibelle, cv.couLibelleAbrege) 
         from Couverture cv where cv.statut.staCode = 'ACT' AND cv.couId in ?1 
    """)
    List<CouvertureListResp> getShortCouverturesByIds(List<Long> couIds);



}

