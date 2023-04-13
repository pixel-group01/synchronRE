package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    @Query("select (count(r) > 0) from Reglement r where r.regReference = ?1")
    boolean alreadyExistsByReference(String regReference);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp(r.regId,r.regReference, r.regDate,r.regMontant,r.regCommission) 
        from Reglement r where (locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(r.regReference, '') ) as string)) ) >0 
                                         or cast(r.regMontant as string) like concat(coalesce(concat(?1), ''), '%'))
                                         and upper(r.typeReglement) = upper(?2)     
        """)
    Page<ReglementListResp> searchReglement(String key, String typeReg, Pageable pageable);

}
