package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface CompteDetailsRepo extends JpaRepository<CompteDetails, Long>
{
    @Query("select c from CompteDetails c where c.compteCedante.compteCedId = ?1 and c.typeCompteDet.typeId = ?2 ")
    CompteDetails findByCompteCedIdAndtypeId(Long compteCedId, Long cedId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(t.typeId, t.name, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled)
        from Type t 
        where t.typeGroup = 'TYPE_DET_COMPTE' and t.uniqueCode not in ("SOLD_REA", "SOUS_TOTAL_CREDIT", "SOLD_CED", "SOUS_TOTAL_DEBIT")
        order by t.typeOrdre
    """)
    List<CompteDetailDto> getDetailComptes();

    @Query("""
        select cd 
        from CompteCedante cd 
        join cd.compte comp 
        join comp.tranche tran 
        join comp.periode per 
        join tran.traiteNonProportionnel.nature nat
""")
    CompteDetails getSapConstitue(Long trancheId, Long brancheId, Long exeCode, Long cedId, Long periode);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(c.compteDetId, t.name, c.debit, c.credit, t.typeId, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled, c.compteCedante.compteCedId)
         from CompteDetails c join c.typeCompteDet t where c.compteCedante.compte.tranche.trancheId = ?1 and 
        c.compteCedante.cedante.cedId = ?2 and c.compteCedante.compte.periode.periodeId =?3
    """)
    List<CompteDetailDto> findByTrancheIdAndCedIdAndPeriodeId(Long trancheId, Long cedIdSelected, Long periodeId);


    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(c.compteDetId, t.name, c.debit, c.credit, t.typeId, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled, c.compteCedante.compteCedId)
         from CompteDetails c join c.typeCompteDet t where c.compteCedante.compteCedId = ?1 order by t.typeOrdre
    """)
    List<CompteDetailDto> findByCompteCedI(Long compteCedId);

    @Modifying
    @Query("""
        delete from CompteDetails cd where cd.compteCedante.compteCedId = ?1 and cd.typeCode = ?2
    """)
    void deleteByCompteCedanteIdAndUniqueCode(Long compteCedanteId, String uniqueCode);

    @Query("select (count(cd)>0) from CompteDetails cd where cd.compteCedante.compteCedId = ?1 and cd.typeCode = ?2")
    boolean existsByCompteCedIdAndTypeCode(Long compteCedanteId, String uniqueCode);


    @Query(value = "SELECT get_depot_sap_const_anterieur(?1, ?2, ?3)", nativeQuery = true)
    BigDecimal getDepotSapConstAnterieur(Long trancheId, Long periodeId, Long cedId);

    @Query(value = "SELECT get_depot_sap_const_actuel(?1, ?2, ?3)", nativeQuery = true)
    BigDecimal getDepotSapConstActuel(Long trancheId, Long periodeId, Long cedId);
}