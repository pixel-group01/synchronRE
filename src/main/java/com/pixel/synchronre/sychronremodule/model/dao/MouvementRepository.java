package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long>
{
    @Query("""
            select new com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp(
            m.mvtId, m.affaire.affId, m.affaire.affAssure, m.affaire.affActivite, m.statut.staLibelle, m.statut.staLibelleLong,
            m.affaire.cedante.cedNomFiliale, m.affaire.cedante.cedSigleFiliale, m.mvtObservation, m.mvtDate)
            From Mouvement m where m.affaire.affId = ?1 order by m.mvtDate desc""")
    List<MouvementListResp> findByAffaire(Long affId);


    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp(
    m.mvtId, m.affaire.affId, m.affaire.affAssure, m.affaire.affActivite, m.statut.staLibelle, m.statut.staLibelleLong,
    m.affaire.cedante.cedNomFiliale, m.affaire.cedante.cedSigleFiliale, m.mvtObservation, m.mvtDate)
    From Mouvement m where m.placement.repId = ?1 order by m.mvtDate desc""")
    List<MouvementListResp> findByPlacement(Long plaId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp(
    m.mvtId, m.affaire.affId, m.affaire.affAssure, m.affaire.affActivite, m.statut.staLibelle, m.statut.staLibelleLong,
    m.affaire.cedante.cedNomFiliale, m.affaire.cedante.cedSigleFiliale, m.mvtObservation, m.mvtDate)
    From Mouvement m where m.sinistre.sinId = ?1 order by m.mvtDate desc""")
    List<MouvementListResp> findBySinistre(Long sinId);
}
