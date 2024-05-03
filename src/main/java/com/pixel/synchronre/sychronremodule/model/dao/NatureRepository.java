package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.nature.response.NatureListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Nature;
import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NatureRepository extends JpaRepository<Nature, String>
{
    @Query("select new com.pixel.synchronre.sychronremodule.model.dto.nature.response.NatureListResp(n.natCode, n.natLibelle, n.forme, n.statut.staCode, n.statut.staLibelle)  from Nature n where (n.forme = ?1 or ?1 is null) and n.statut.staCode = 'ACT'")
    List<NatureListResp> findByForme(FORME forme);
}