package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.views.V_CedanteTraite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CedanteTraiteRepo extends JpaRepository<V_CedanteTraite, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO
        (ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale)
        from Cedante ced where ced.cedId 
        in (select cedTrai.cedId from V_CedanteTraite cedTrai where cedTrai.traiteNpId = ?1)
           """)
    List<ReadCedanteDTO> findCedanteByTraite(Long traiteNpId);
}
