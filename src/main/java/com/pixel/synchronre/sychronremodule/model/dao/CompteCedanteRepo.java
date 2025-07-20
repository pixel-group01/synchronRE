package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.compte.StatCompteIds;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.views.VStatCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompteCedanteRepo extends JpaRepository<CompteCedante, Long>
{
    @Query("select c from CompteCedante c where c.compte.compteId = ?1 and c.cedante.cedId = ?2")
    CompteCedante findByCompteIdAndCedId(Long compteId, Long cedId);

    @Query("select new com.pixel.synchronre.sychronremodule.model.dto.compte.StatCompteIds(cc.cedante.cedId, cc.compte.tranche.trancheId, cc.compte.periode.periodeId) from CompteCedante  cc where cc.compteCedId = ?1")
    StatCompteIds getStatCompteIdsByCompteCedId(Long compteCedId);

    @Query("select cd.compteCedId from CompteCedante cd join cd.compte.tranche tran join cd.compte.periode per where tran.trancheId = ?1 and per.periodeId = ?2 and cd.cedante.cedId = ?3")
    Long findByTrancheIdAndPeriodeIdAndCedId(Long trancheIdSelected, Long periodeId, Long cedId);
}