package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviseRepository extends JpaRepository<Devise, String> {

    @Query("select (count(d) > 0) from Devise d where d.devCode= ?1")
    boolean existsDevCode(String devCode);



    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp(dv.devCode, dv.devLibelle, dv.devLibelleAbrege, dv.devSymbole,dv.statut.staLibelle) 
        from Devise dv where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(dv.devCode, '') ) as string)) ) >0 
                                          or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(dv.devLibelle, '') ) as string)) ) >0 
                                           or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(dv.devSymbole, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(dv.devLibelleAbrege, '') ) as string)) ) >0 ) 
                                         and dv.statut.staCode = 'ACT'     
""")
    Page<DeviseListResp> searchDevises(String key, Pageable pageable);
}