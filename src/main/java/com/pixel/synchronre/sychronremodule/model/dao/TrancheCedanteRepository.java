package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheCedanteRepository extends JpaRepository<TrancheCedante, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto
        (t.trancheId, t.trancheLibelle, t.trancheTauxPrime) 
        from Tranche t 
        where t.trancheId in ( 
                                select a.tranche.trancheId 
                                from Association a 
                                where a.type.uniqueCode = 'TRAN-CAT' and a.tranche.trancheId = t.trancheId and 
                                exists(
                                        select a2 
                                        from Association a2 
                                        where a2.type.uniqueCode = 'CAT-CED' 
                                        and a2.cedante.cedId = ?1 
                                        and a2.categorie.categorieId = a.categorie.categorieId
                                      ) 
                                and a.tranche.traiteNonProportionnel.traiteNpId = ?2
                             )
              order by t.trancheLibelle asc
""")//07 09 16 48 89 caire
    List<TranchePrimeDto> findNaturalTranchePmdForCedanteAndTraite(Long cedId, Long traiteNpId);

    @Query("select tc.trancheCedanteId from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.cedante.cedId = ?2")
    Long getTrancheCedanteIdByTrancheIdAndCedId(Long trancheId, Long cedId);

    @Query("select tc from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.cedante.cedId = ?2")
    TrancheCedante findByTrancheIdAndCedId(Long trancheId, Long cedId);

    //Recupère les ids des tranche qui sont persistées qui ne sont pas dans les tranches naturelles
    @Query("""
        select tc.trancheCedanteId from TrancheCedante tc where tc.tranche.trancheId in 
        (select tc.tranche.trancheId from TrancheCedante tc where tc.cedante.cedId = ?2) and 
        tc.tranche.trancheId not in 
        (
            select tr1.trancheId from Tranche tr1 where tr1.trancheId in 
            (
                select a.tranche.trancheId 
                                from Association a 
                                where a.type.uniqueCode = 'TRAN-CAT' and a.tranche.trancheId = tr1.trancheId and 
                                exists(
                                        select a2 
                                        from Association a2 
                                        where a2.type.uniqueCode = 'CAT-CED' 
                                        and a2.cedante.cedId = tc.cedante.cedId 
                                        and a2.categorie.categorieId = a.categorie.categorieId
                                      ) 
                                and a.tranche.traiteNonProportionnel.traiteNpId = tc.tranche.traiteNonProportionnel.traiteNpId
                                and tc.tranche.traiteNonProportionnel.traiteNpId = ?1
            )
        )
""")
    List<Long> getTrancheCedanteIdsToRemove(Long traiteNpId, Long cedId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto
        (tr1.trancheId, tr1.trancheLibelle, tr1.trancheTauxPrime)  
        from Tranche tr1 where tr1.traiteNonProportionnel.traiteNpId = ?1 and tr1.trancheId in 
            (
                select a.tranche.trancheId 
                                from Association a 
                                where a.type.uniqueCode = 'TRAN-CAT'  and a.tranche.trancheId = tr1.trancheId and 
                                exists(
                                        select a2 
                                        from Association a2 
                                        where a2.type.uniqueCode = 'CAT-CED' 
                                        and a2.cedante.cedId = ?2 
                                        and a2.categorie.categorieId = a.categorie.categorieId
                                      ) 
            )
            and tr1.trancheId not in 
            (
                select tc.tranche.trancheId from TrancheCedante tc where tc.tranche.traiteNonProportionnel.traiteNpId = ?1 and tc.cedante.cedId = ?2
            )
        """)
    List<TranchePrimeDto> getTranchePmdToAdd(Long traiteNpId, Long cedId);

    @Query("""
    select tc from TrancheCedante tc where tc.tranche.trancheId = ?1
""")
    List<TrancheCedante> findByTrancheId(Long trancheId);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO(c.cedId, c.cedNomFiliale, c.cedSigleFiliale) 
    from Cedante c where c.cedId in 
    (select catCed.cedante.cedId from Association catCed
    where catCed.type.uniqueCode = 'CAT-CED' and
     exists (select tranCat from Association tranCat where tranCat.categorie.categorieId = catCed.categorie.categorieId and tranCat.tranche.traiteNonProportionnel.traiteNpId  = ?1 and tranCat.type.uniqueCode = 'TRAN-CAT' ) ) 
""")
    List<ReadCedanteDTO> getListCedanteAsaisirSurTraite(Long traiteNpId);

    @Query("""
        select  new com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp(
        sum(tc.pmd), sum(tc.pmdCourtier), sum(tc.pmdCourtierPlaceur), sum(tc.pmdNette))
        from TrancheCedante tc  where tc.tranche.traiteNonProportionnel.traiteNpId = ?1 group by tc.tranche.traiteNonProportionnel.traiteNpId 
        """)
    PmdGlobalResp getPmdGlobal(Long traiteNpId);

    @Query("select tc.tranche.traiteNonProportionnel.traiteNpId from TrancheCedante tc where tc.trancheCedanteId = ?1")
    Long getTraiteIdByTrancheCedanteId(Long trancheCedanteId);
}