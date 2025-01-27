package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.views.VCedanteTraite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CedanteTraiteRepo extends JpaRepository<VCedanteTraite, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO
        (ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale)
        from Cedante ced where ced.cedId 
        in (select cedTrai.cedId from VCedanteTraite cedTrai where cedTrai.traiteNpId = ?1)
           """)
    List<ReadCedanteDTO> findCedanteByTraite(Long traiteNpId);
}
