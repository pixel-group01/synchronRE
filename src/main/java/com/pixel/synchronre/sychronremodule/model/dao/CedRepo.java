package com.pixel.synchronre.sychronremodule.model.dao;


import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CedRepo extends JpaRepository<Cedante, Long>
{
    @Query("select c.cessionnaire.cesId from Cedante c where c.cedId = ?1")
    Long getCedCesId(Long cedId);

    @Query("select (count(c) > 0) from Cedante c where upper(c.cedEmail) = upper(?1)")
    boolean alreadyExistsByEmail(String cedEmail);

    @Query("select (count(c) > 0) from Cedante c where c.cedEmail = ?1 and c.cedId <> ?2")
    boolean alreadyExistsByEmail(String cedEmail, Long cedId);

    @Query("select (count(c) > 0) from Cedante c where upper(c.cedTel) = upper(?1)")
    boolean alreadyExistsByTel(String tel);

    @Query("select (count(c) > 0) from Cedante c where upper(c.cedTel) = upper(?1) and c.cedId <> ?2")
    boolean alreadyExistsByTel(String tel, Long cedId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO(c.cedId, c.cedNomFiliale, c.cedSigleFiliale, c.cedTel, c.cedEmail, 
        c.cedAdressePostale,c.cedFax, c.cedSituationGeo, c.cedStatut.staLibelle) 
        from Cedante c where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedEmail, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedTel, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedSigleFiliale, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedAdressePostale, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedSituationGeo, '') ) as string)) ) >0 
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedFax, '') ) as string)) ) >0
                                         or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(c.cedNomFiliale, '') ) as string)) ) >0 ) 
                                         and c.cedStatut.staCode = 'ACT'     
""")
    Page<ReadCedanteDTO> searchCedentes(String key, Pageable pageable);

}
