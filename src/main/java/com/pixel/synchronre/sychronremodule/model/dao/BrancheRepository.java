package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrancheRepository extends JpaRepository<Branche, Long> {

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheListResp(br.branId, br.branLibelle, br.branLibelleAbrege, br.statut.staLibelle) 
        from Branche br where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(br.branLibelle, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(br.branLibelleAbrege, '') ) as string)) ) >0 ) 
                                         and br.statut.staCode = 'ACT'     
""")
    Page<BrancheListResp> searchBranches(String key, Pageable pageable);

    @Query("select c.branche.branLibelleAbrege from Couverture c where c.couId = ?1")
    String getBranCheByCouId(Long couId);
}
