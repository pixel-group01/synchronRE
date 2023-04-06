package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatutRepository extends JpaRepository<Statut, Long> {

    @Query("select s from Statut s where s.staCode = ?1")
    Statut findByStaCode(String staCode);

    @Query("select (count(s) > 0) from Statut s where s.staCode = ?1")
    boolean alreadyExistsByCode(String statCode);

    @Query("select (count(s) > 0) from Statut s where s.staCode = ?1 and s.staId <> ?2")
    boolean alreadyExistsByCode(String statCode,Long staId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp(s.staCode,s.staLibelle,s.staLibelleLong,s.staType) 
        from Statut  s where locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(s.staCode, '') ) as string)) ) >0 
                          or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelle, '') ) as string)) ) >0 
                          or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelleLong, '') ) as string)) ) >0 
                          or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(s.staType, '') ) as string)) ) >0
""")
    Page<StatutListResp> searchStatut(String key, Pageable pageable);

}
