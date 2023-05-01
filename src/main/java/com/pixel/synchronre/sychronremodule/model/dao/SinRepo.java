package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SinRepo extends JpaRepository<Sinistre, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp(
        s.sinId, s.sinMontant100, s.sinDateSurvenance, s.sinDateDeclaration, s.sinCommentaire, aff.affCode, aff.affAssure, aff.affActivite, aff.affCapitalInitial) 
        from Sinistre s left join s.statut st left join s.affaire aff left join s.userCreator u left join s.functionCreator fnc left join aff.cedante ced
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(aff.affCode, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(aff.affAssure, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(aff.affActivite, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  s.sinCommentaire ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(s.sinMontant100 as string))) =1
        or locate(upper(coalesce(:key, '') ), upper(cast(aff.affCapitalInitial as string))) = 1
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.statut.staCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.statut.staLibelle, '') ) as string))) >0
        ) 
        and (:fncId is null or :fncId = fnc.id)
        and (:userId is null or :userId = u.userId)
        and (:cedId is null or :cedId = ced.cedId)
        and (:cedCesId is null or :cedCesId = ced.cessionnaire.cesId)
        and s.statut.staCode in :staCodes
           """)
    Page<SinistreDetailsResp> searchSinistres(@Param("key") String key,
                                              @Param("fncId") Long fncId,
                                              @Param("userId") Long userId,
                                              @Param("cedId") Long cedId,
                                              @Param("cedCesId") Long cedCesId,
                                              @Param("staCodes") List<String> staCodes, Pageable pageable);

    @Query("select s.sinMontant100 from Sinistre s where s.sinId = ?1")
    BigDecimal getMtSinistre(Long sinId);

    @Query("select s.affaire from Sinistre s where s.sinId = ?1")
    Optional<Affaire> getAffairedBySinId(Long sinId);

    @Query("""
        select r.cessionnaire from Repartition r 
        where r.repStaCode.staCode not in('REFUSE','RET','SAI', 'SAI-CRT') 
        and r.type.uniqueCode = 'REP_PLA' and r.repStatut = true 
        and r.affaire.affId = (select s.affaire.affId from Sinistre s where s.sinId = ?1)
    """)
    List<Cessionnaire> getCessionnaireBySinId(Long sinId);
}
