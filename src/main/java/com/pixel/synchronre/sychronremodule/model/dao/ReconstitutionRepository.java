package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reconstitution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReconstitutionRepository extends JpaRepository<Reconstitution, Long> {
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp(
     r.reconstitutionId,r.nbrReconstitution, r.tauxReconstitution,r.tauxPrimeReconstitution,
        r.modeCalculReconstitution,tnp.traiteNpId,tnp.traiReference,tnp.traiNumero,t.trancheId,t.trancheLibelle)
    from Reconstitution r
    left join r.tranche t
    left join r.traiteNonProportionnel tnp
    where r.reconstitutionId = ?1
    """)
    ReconstitutionResp getReconstitutionResp(Long reconstitutionId);

    @Query("""
     select new com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp(
                r.reconstitutionId,r.nbrReconstitution, r.tauxReconstitution,r.tauxPrimeReconstitution,
        r.modeCalculReconstitution,tnp.traiteNpId,tnp.traiReference,tnp.traiNumero,s.staCode,s.staLibelle,t.trancheId,t.trancheLibelle)
                from Reconstitution r
                left join r.tranche t
                left join r.traiteNonProportionnel tnp
                left join r.statut s 
                where (
                locate(upper(coalesce(:key, '') ), cast(r.nbrReconstitution as string)) = 1 or
                locate(upper(coalesce(:key, '') ), cast(r.tauxReconstitution as string)) = 1 or
                locate(upper(coalesce(:key, '') ), cast(r.tauxPrimeReconstitution as string)) = 1 or
                locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  r.modeCalculReconstitution) as string))) >0 or
                locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  tnp.traiReference) as string))) >0 or
                locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  tnp.traiNumero) as string))) >0 or
                locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  t.trancheLibelle) as string))) >0
                )
                and tnp.traiteNpId = :traiteNpId and s.staCode = 'ACT'
     """)
    Page<ReconstitutionResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key") String key, Pageable pageable);
}