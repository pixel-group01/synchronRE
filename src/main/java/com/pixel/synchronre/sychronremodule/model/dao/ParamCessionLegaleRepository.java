package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParamCessionLegaleRepository extends JpaRepository<ParamCessionLegale, Long> {

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp(pr.paramCesLegId, pr.paramCesLegLibelle, pr.paramCesLegCapital, 
        pr.pays.paysNom, pr.cedante.cedNomFiliale, pr.statut.staLibelle) 
        from ParamCessionLegale pr where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.paramCesLegLibelle, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.cedante.cedNomFiliale, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(pr.pays.paysNom, '') ) as string)) ) >0 ) 
                                         and pr.statut.staCode = 'ACT'     
""")
    Page<ParamCessionLegaleListResp> searchParams(String key, Pageable pageable);
}