package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteCessionnaireDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.TrancheCompteDto;
import com.pixel.synchronre.sychronremodule.model.entities.Periode;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CompteTraiteRepo extends JpaRepository<Repartition, Long>
{
    @Query("select sum(r.repTaux) from Repartition r where r.type.uniqueCode = 'REP_PLA_TNP' and r.repStatut = true and r.repStaCode.staCode not in ('REFUSE', 'SUP', 'ANNULE') and r.traiteNonProportionnel.traiteNpId = ?1")
    BigDecimal calculateTauxDejaPlace(Long traiteNpId);

    @Query("""
            select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto(tnp.traiteNpId,e.exeCode,tnp.traiReference,tnp.traiNumero,nat.natCode,nat.natLibelle,
            tnp.traiPeriodicite,tnp.traiEcerciceRattachement)
             from TraiteNonProportionnel tnp left join tnp.exercice e 
             left join tnp.nature nat
             where tnp.traiteNpId = ?1
            """)
    CompteTraiteDto getCompteByTraite(Long traiteNpId);

    @Query("""
            select new com.pixel.synchronre.sychronremodule.model.dto.compte.TrancheCompteDto(tr.trancheId,tr.trancheLibelle)
            from Tranche tr 
            where tr.traiteNonProportionnel.traiteNpId = ?1
            """)
    List<TrancheCompteDto> getCompteTranches(Long traitNpId);
    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO(ced.cedId, ced.cedNomFiliale, ced.cedSigleFiliale)
        from Cedante ced where ced.cedId in 
        (select catCed.cedante.cedId from Association catCed where catCed.type.uniqueCode = 'CAT-CED' and catCed.categorie.categorieId in
            (select trCat.categorie.categorieId from Association trCat where trCat.type.uniqueCode = 'TRAN-CAT' and trCat.tranche.trancheId = ?1) )
""")
    List<ReadCedanteDTO> getCompteCedantes(Long trancheId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(t.typeId, t.name)
        from Type t 
        where t.typeGroup = 'TYPE_DET_COMPTE' 
        order by t.typeOrdre
""")
    List<CompteDetailDto> getDetailComptes();

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteCessionnaireDto(ces.cesId, ces.cesNom, ces.cesSigle, rep.repTaux) 
        from Repartition rep join rep.cessionnaire ces 
        where rep.traiteNonProportionnel.traiteNpId = (select tr.traiteNonProportionnel.traiteNpId from Tranche tr where tr.trancheId = ?1) 
        and rep.type.uniqueCode = 'REP_PLA_TNP'
        and rep.repStatut = true and rep.repStaCode.staCode not in ('REFUSE', 'SUP', 'SUPP', 'ANNULE')
""")
    List<CompteCessionnaireDto> getCompteCessionnaires(Long trancheId);


    @Query("select p from Periode p where year(p.periode) = ?1 and p.type.typeId = ?2")
    List<Periode> getPeriodesByTypeId(Long exeCode, Long typeId);
}
