package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.Territorialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TerritorialiteRepository extends JpaRepository<Territorialite, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp(
    ter.terrId, ter.terrLibelle, ter.terrTaux, ter.terrDescription, tnp.traiteNpId,tnp.traiReference
    ) from Territorialite ter left join ter.traiteNonProportionnel tnp
    where (locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ter.terrLibelle) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(ter.terrTaux as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ter.terrDescription) as string))) >0)
    and tnp.traiteNpId = :traiteNpId and ter.statut.staCode = 'ACT'
""")
    Page<TerritorialiteResp> search(@Param("traiteNpId")Long traiteNpId, @Param("key") String key, Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq(tnp.traiteNpId,
        tnp.traiReference, tnp.traiNumero,tnp.traiLibelle,court.cesNom,tnp.traiEcerciceRattachement,
        tnp.traiDateEffet, tnp.traiDateEcheance,tnp.traiCoursDevise,tnp.traiPeriodicite,tnp.traiDelaiEnvoi,
        tnp.traiDelaiConfirmation, tnp.traiTauxCourtier,tnp.traiTauxCourtierPlaceur,scr.traiReference,nat.natCode,dev.devCode,comp.devCode)
        from TraiteNonProportionnel tnp 
        left join tnp.traiSource scr
        left join tnp.nature nat
        left join tnp.traiDevise dev
        left join tnp.traiCompteDevise comp
        left join tnp.courtierPlaceur court
        where tnp.traiteNpId = ?1
    """)
    TerritorialiteReq getEditDtoById(Long terrId);

    @Query("select t from Territorialite t where t.traiteNonProportionnel.traiteNpId = ?1")
    List<Territorialite> findByTnpId(Long traiteNpId);
}