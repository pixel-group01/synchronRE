package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaysRepository extends JpaRepository<Pays, String> {

    @Query("select (count(p) > 0) from Pays p where p.paysCode= ?1")
    boolean existsByPaysCode(String paysCode);

    @Query("select (count(p) > 0) from Pays p where upper(p.paysCode) = upper(?1)")
    boolean alreadyExistsByCode(String paysCode);




    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(p.paysCode, p.paysIndicatif, p.paysNom,  
        p.statut.staLibelle,p.devise.devCode,p.devise.devLibelle) 
        from Pays p where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(p.paysCode, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(p.paysIndicatif, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(p.paysNom, '') ) as string)) ) >0 ) 
                                         and p.statut.staCode = 'ACT'     
""")
    Page<PaysListResp> searchPays(String key, Pageable pageable);

    @Query("""
select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from Pays p where p.paysCode = ?1
""")
    PaysListResp getPaysByPaysCode(String paysCode);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from Pays p where p.paysCode in ?1
""")
    List<PaysListResp> getPaysByPaysCodes(List<String> paysCodes);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from Pays p where p.statut.staCode = 'ACT'
""")
    List<PaysListResp> getAllPays();
}
