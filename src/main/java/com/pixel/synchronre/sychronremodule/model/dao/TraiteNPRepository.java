package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TraiteNPRepository extends JpaRepository<TraiteNonProportionnel, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp(tnp.traiteNpId,
        tnp.traiReference, tnp.traiNumero, tnp.traiLibelle,tnp.traiEcerciceRattachement, 
        tnp.traiDateEffet, tnp.traiDateEcheance, tnp.traiCoursDevise, tnp.traiPeriodicite, tnp.traiDelaiEnvoi,
        tnp.traiDelaiConfirmation,tnp.traiDelaiPaiement, tnp.traiTauxCourtier, tnp.traiTauxCourtierPlaceur,tnp.traiTauxAbattement, e.exeCode, src.traiReference, 
        src.traiLibelle, n.natCode, n.natLibelle,tnp.courtierPlaceur.cesId,tnp.courtierPlaceur.cesNom, d.devCode, dc.devCode, s.staCode, s.staLibelle, u.email, 
        concat(u.firstName, ' ', u.lastName), f.name, tnp.createdAt, tnp.updatedAt) 
        from TraiteNonProportionnel tnp 
        left join tnp.exercice e 
        left join tnp.traiSource src 
        left join tnp.nature n 
        left join tnp.traiDevise d 
        left join tnp.statut s 
        left join tnp.traiUserCreator u 
        left join tnp.traiFonCreator f 
        left join tnp.traiCompteDevise dc 
        left join CedanteTraite ct on ct.traiteNonProportionnel.traiteNpId = tnp.traiteNpId left join ct.cedante ced
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(tnp.traiReference, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiNumero, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.courtierPlaceur.cesNom, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiEcerciceRattachement, '') ) as string))) >0
        or 
        (
        case
            when (e.exeCode is null) then true
            else locate(upper(coalesce(:key, '') ), upper(cast(e.exeCode as string))) >0
        end
        )
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(src.traiReference, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(src.traiLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(n.natCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(n.natLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(d.devCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(dc.devCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staCode, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(s.staLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(u.email, '') ) as string))) >0
        )       
        and (:fncId is null or :fncId = f.id) 
        and (:userId is null or :userId = u.userId) 
        and (:cedId is null or :cedId = ced.cedId) 
        and (:exeCode is null or (tnp.traiDateEffet <= cast(CONCAT(:exeCode, '-12-31') as date)   and tnp.traiDateEcheance  >= cast(CONCAT(:exeCode, '-01-01') as date))) 
        and s.staCode in :staCodes 
""")
    Page<TraiteNPResp> search(@Param("key") String key,
                              @Param("fncId") Long fncId,
                              @Param("userId") Long userId,
                              @Param("cedId") Long cedId,
                              @Param("staCodes") List<String> staCodes,
                              @Param("exeCode")Long exeCode, Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp(tnp.traiteNpId,
        tnp.traiReference, tnp.traiNumero, tnp.traiLibelle,tnp.traiEcerciceRattachement, 
        tnp.traiDateEffet, tnp.traiDateEcheance, tnp.traiCoursDevise, tnp.traiPeriodicite, tnp.traiDelaiEnvoi,
        tnp.traiDelaiConfirmation,tnp.traiDelaiPaiement, tnp.traiTauxCourtier, tnp.traiTauxCourtierPlaceur,tnp.traiTauxAbattement, e.exeCode, src.traiReference, 
        src.traiLibelle, n.natCode, n.natLibelle, d.devCode, dc.devCode,tnp.courtierPlaceur.cesId,tnp.courtierPlaceur.cesNom) 
        from TraiteNonProportionnel tnp left join tnp.exercice e left join tnp.traiSource src left join tnp.nature n left join tnp.traiDevise d 
        left join tnp.traiCompteDevise dc
        where tnp.traiteNpId = ?1
""")
    TraiteNPResp findTraiteById(Long traiteNpId);

    @Query("select t from TraiteNonProportionnel t where t.traiReference = ?1")
    TraiteNonProportionnel findByRef(String traiSourceRef);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp(tnp.traiteNpId,
        tnp.traiReference, tnp.traiNumero)
        from TraiteNonProportionnel tnp where tnp.traiteNpId = ?1
    """)
    TraiteNPResp getShortTraiteById(Long traiteNpId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq(tnp.traiteNpId,
        tnp.traiReference, tnp.traiNumero,tnp.traiLibelle,tnp.traiEcerciceRattachement,
        tnp.traiDateEffet, tnp.traiDateEcheance,tnp.traiCoursDevise,tnp.traiPeriodicite,tnp.traiDelaiEnvoi,
        tnp.traiDelaiConfirmation,tnp.traiDelaiPaiement, tnp.traiTauxCourtier,tnp.traiTauxCourtierPlaceur,tnp.traiTauxAbattement,scr.traiReference,nat.natCode,dev.devCode,comp.devCode,court.cesId)
        from TraiteNonProportionnel tnp 
        left join tnp.traiSource scr
        left join tnp.nature nat
        left join tnp.traiDevise dev
        left join tnp.traiCompteDevise comp
        left join tnp.courtierPlaceur court
        where tnp.traiteNpId = ?1
    """)
    UpdateTraiteNPReq getEditDtoById(Long traiteNpId);

    @Query("select (count(tnp.traiteNpId)>0) from TraiteNonProportionnel tnp where upper(tnp.traiReference) = upper(?1) ")
    boolean existsByRef(String ref);

    @Query("select (count(tnp.traiteNpId)>0) from TraiteNonProportionnel tnp where upper(tnp.traiReference) = upper(?1) and tnp.traiteNpId <> ?2")
    boolean existsByRef(String ref, Long traiteNpId);

    @Query("select (count(tnp.traiteNpId)>0) from TraiteNonProportionnel tnp where upper(tnp.traiNumero) = upper(?1)")
    boolean existsByNumero(String numero);

    @Query("select (count(tnp.traiteNpId)>0) from TraiteNonProportionnel tnp where upper(tnp.traiNumero) = upper(?1) and tnp.traiteNpId <> ?2")
    boolean existsByNumero(String numero, Long traiteNpId);

    @Query("select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp(tnp.traiTauxCourtier, tnp.traiTauxCourtierPlaceur) tnp from TraiteNonProportionnel tnp where tnp.traiteNpId = ?1")
    TauxCourtiersResp getTauxCourtiers(Long traiteNpId);

    @Query("select t from TraiteNonProportionnel t where t.traiteNpId = ?1")
    Optional<TraiteNonProportionnel> findById(Long traiteNpId);

    @Query("select t.traiTauxAbattement from TraiteNonProportionnel t where t.traiteNpId = ?1")
    BigDecimal getTauxAbattement(Long traiteNpId);
}