package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraiteNPRepository extends JpaRepository<TraiteNonProportionnel, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp(tnp.traiteNPId,
        tnp.traiReference, tnp.traiNumero, tnp.traiLibelle, tnp.traiAuteur, tnp.traiEcerciceRattachement, 
        tnp.traiDateEffet, tnp.traiDateEcheance, tnp.traiCoursDevise, tnp.traiPeriodicite, tnp.traiDelaiEnvoi,
        tnp.traiDelaiConfirmation, tnp.traiTauxCourtier, tnp.traiTauxCourtierPlaceur, e.exeCode, src.traiReference, 
        src.traiLibelle, n.natCode, n.natLibelle, d.devCode, dc.devCode, s.staCode, s.staLibelle, u.email, 
        concat(u.firstName, ' ', u.lastName), f.name, tnp.createdAt, tnp.updatedAt) 
        from TraiteNonProportionnel tnp left join tnp.exercice e left join tnp.traiSource src left join tnp.nature n left join tnp.traiDevise d 
        left join tnp.statut s left join tnp.traiUserCreator u left join tnp.traiFonCreator f left join tnp.traiCompteDevise dc 
        left join CedanteTraite ct on ct.traiteNonProportionnel.traiteNPId = tnp.traiteNPId left join ct.cedante ced
        where (locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(tnp.traiReference, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiNumero, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiLibelle, '') ) as string))) >0
        or locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(tnp.traiAuteur, '') ) as string))) >0
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

    @Query("select t from TraiteNonProportionnel t where t.traiReference = ?1")
    TraiteNonProportionnel findByRef(String traiSourceRef);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp(tnp.traiteNPId,
        tnp.traiReference, tnp.traiNumero)
        from TraiteNonProportionnel tnp where tnp.traiteNPId = ?1
    """)
    TraiteNPResp getShortTraiteById(Long traiteNPId);
}