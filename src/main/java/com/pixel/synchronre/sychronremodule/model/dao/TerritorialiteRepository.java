package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.Territorialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
