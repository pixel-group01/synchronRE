package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrancheCedanteRepository extends JpaRepository<TrancheCedante, Long>
{
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto(
        tc.trancheCedanteId, tc.tranche.trancheId, tc.tranche.trancheLibelle, tc.tranche.trancheTauxPrime, 
        tc.pmd, tc.pmdCourtier, tc.pmdCourtierPlaceur, tc.pmdNette
        ) from TrancheCedante tc where tc.cedanteTraite.cedanteTraiteId = ?1
    """)
    List<TranchePmdDto> findPersistedTranchePmdByCedanteTraiteId(Long cedanteTraiteId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto
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
""")//07 09 16 48 89 caire
    List<TranchePmdDto> findNaturalTranchePmdForCedanteAndTraite(Long cedId, Long traiteNpId);

    @Query("select tc.trancheCedanteId from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.cedanteTraite.cedanteTraiteId = ?2")
    Long getTrancheCedanteIdByTrancheIdAndCedanteTraiteId(Long trancheId, Long cedanteTraiteId);

    @Query("select tc from TrancheCedante tc where tc.tranche.trancheId = ?1 and tc.cedanteTraite.cedanteTraiteId = ?2")
    TrancheCedante findByTrancheIdAndCedanteTraiteId(Long trancheId, Long cedanteTraiteId);

    //Recupère les ids des tranche qui sont persistées qui ne sont pas dans les tranches naturelles
    @Query("""
        select tc.trancheCedanteId from TrancheCedante tc where tc.tranche.trancheId in 
        (select tc.tranche.trancheId from TrancheCedante tc where tc.cedanteTraite.cedanteTraiteId = ?1) and 
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
                                        and a2.cedante.cedId = tc.cedanteTraite.cedante.cedId 
                                        and a2.categorie.categorieId = a.categorie.categorieId
                                      ) 
                                and a.tranche.traiteNonProportionnel.traiteNpId = tc.cedanteTraite.traiteNonProportionnel.traiteNpId
            )
        )
         
""")
    List<Long> getTrancheCedanteIdsToRemove(Long cedanteTraiteId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto
        (tr1.trancheId, tr1.trancheLibelle, tr1.trancheTauxPrime)  
        from Tranche tr1 where tr1.trancheId in 
            (
                select a.tranche.trancheId 
                                from Association a 
                                where a.type.uniqueCode = 'TRAN-CAT' and a.tranche.trancheId = tr1.trancheId and 
                                exists(
                                        select a2 
                                        from Association a2 
                                        where a2.type.uniqueCode = 'CAT-CED' 
                                        and a2.cedante.cedId = (select ct.cedante.cedId from CedanteTraite ct where ct.cedanteTraiteId = ?1) 
                                        and a2.categorie.categorieId = a.categorie.categorieId
                                      ) 
                                and a.tranche.traiteNonProportionnel.traiteNpId = (select ct.traiteNonProportionnel.traiteNpId from CedanteTraite ct where ct.cedanteTraiteId = ?1)
            )
            and tr1.trancheId not in 
            (
                select tc.tranche.trancheId from TrancheCedante tc where tc.cedanteTraite.cedanteTraiteId = ?1
            )
        """)
    List<TranchePmdDto> getTranchePmdToAdd(Long cedanteTraiteId);

    @Query("""
    select tc from TrancheCedante tc where tc.tranche.trancheId = ?1
""")
    List<TrancheCedante> findByTrancheId(Long trancheId);
//044917002
//131798692
}
