package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TrancheRepository extends JpaRepository<Tranche, Long>
{
    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
        t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte
        , t.trancheTauxPrime, t.trancheNumero, t.traiteNpId, t.traiReference, t.traiNumero
            , t.couvertures, t.couIds, t.risqueIds)
    from VTranche t  
    where t.trancheId = ?1
    """)
    TrancheResp getTrancheResp(Long trancheId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
    t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte
        , t.trancheTauxPrime, t.trancheNumero, t.traiteNpId, t.traiReference, t.traiNumero
            , t.couvertures, t.couIds, t.risqueIds)
    from VTranche t 
    where (
    locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  t.searchText) as string))) >0 or
    locate(upper(coalesce(:key, '') ), upper(cast(t.tranchePriorite as string))) =1 or
    locate(upper(coalesce(:key, '') ), upper(cast(t.tranchePorte as string))) =1  
    )
    and t.traiteNpId = :traiteNpId and t.staCode = 'ACT' order by t.trancheNumero
    """)
    Page<TrancheResp> search(@Param("traiteNpId") Long traiteNpId, @Param("key")String key, Pageable pageable);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq(
        t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte
        , t.trancheTauxPrime, t.trancheNumero, t.traiteNpId, t.risqueIds)
        from VTranche t
        where t.trancheId = ?1
    """)
    TrancheReq getEditDtoById(Long trancheId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp(
    t.trancheId,t.trancheType, t.trancheLibelle, t.tranchePriorite, t.tranchePorte
        , t.trancheTauxPrime, t.trancheNumero, t.traiteNpId, t.traiReference, t.traiNumero
            , t.couvertures, t.couIds, t.risqueIds)
    from VTranche t 
    where t.traiteNpId = ?1 and t.staCode = 'ACT'
    """)
    List<TrancheResp> getTrancheList(Long traiteNpId);

    @Query("select tr.traiteNonProportionnel.traiteNpId from Tranche tr where tr.trancheId = ?1")
    Long getTraiteNpIdByTrancheId(Long tranchepId);

    @Query("select tr.trancheTauxPrime from Tranche tr where tr.trancheId = ?1")
    BigDecimal getTrancheTauxPrime(Long tranchepId);

    @Query("select (count(tr)>0) from Tranche tr where tr.traiteNonProportionnel.traiteNpId =?1 and tr.trancheNumero = ?2")
    boolean existsByTnpIdAndNumero(Long traiteNpId, int trancheNumero);

    @Query("select( coalesce(max(tr.trancheNumero), 0) + 1) from Tranche tr where tr.traiteNonProportionnel.traiteNpId =?1 ")
    Long getNextTrancheNum(Long traiteNpId);
}
