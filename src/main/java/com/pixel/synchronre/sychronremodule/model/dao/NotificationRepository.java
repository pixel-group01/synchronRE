package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Mouvement, Long>
{
    @Query("select (count(a.affId)) from Affaire a where a.statut.staCode in ?1 and  (?2 is null or ?2 = a.cedante.cedId)")
    Long countAffaires(List<String> staCodes, Long cedId);

    @Query("select (count(r.repId)) from Repartition r where r.repStaCode.staCode in ?1 and  (?2 is null or ?2 = r.affaire.cedante.cedId) and r.repStatut = true")
    Long countPlacements(List<String> staCodes, Long cedId);

    @Query("select (count(s.sinId)) from Sinistre s where s.statut.staCode in ?1 and  (?2 is null or ?2 = s.affaire.cedante.cedId)")
    Long countSinistres(List<String> staCodes, Long cedId);
}
