package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CedanteTraiteRepository extends JpaRepository<CedanteTraite, Long>
{
    @Query("select c.cedanteTraiteId from CedanteTraite c where c.traiteNonProportionnel.traiteNpId = ?1 and c.cedante.cedId = ?2 and c.statut.staCode = 'ACT'")
    Long getCedanteTraiteIdByTraiIdAndCedId(Long traiteNpId, Long cedId);
    @Query("select (count(c) > 0) from CedanteTraite c where c.traiteNonProportionnel.traiteNpId = ?1 and c.cedante.cedId = ?2 and c.statut.staCode='ACT'")
    boolean traiteHasCedante(Long traiteNpId, Long cedId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp(
    c.cedanteTraiteId, c.assiettePrime, c.tauxPrime, c.pmd, ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale,
    tnp.traiteNpId, tnp.traiReference, tnp.traiNumero, s.staCode, s.staLibelle) 
    from CedanteTraite c left join c.cedante ced left join c.traiteNonProportionnel tnp left join c.statut s
    where (locate(upper(coalesce(:key, '') ), upper(cast(c.assiettePrime as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(c.tauxPrime as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(c.pmd as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ced.cedNomFiliale) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ced.cedSigleFiliale) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  ced.cedNomFiliale) as string))) >0 )
    and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
""")
    Page<CedanteTraiteResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key")String key, Pageable pageable);

    @Query("select ct from CedanteTraite ct where ct.traiteNonProportionnel.traiteNpId = ?1")
    Long getTraiteIdByCedTraiId(Long cedTraiId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp(
    c.cedanteTraiteId, c.assiettePrime, c.tauxPrime, c.pmd, ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale,
    tnp.traiteNpId, tnp.traiReference, tnp.traiNumero, s.staCode, s.staLibelle) 
    from CedanteTraite c left join c.cedante ced left join c.traiteNonProportionnel tnp left join c.statut s
    where tnp.traiteNpId = ?1 
    and s.staCode = 'ACT'
""")
    List<CedanteTraiteResp> getCedanteTraitelist(Long traiteNpId);
}