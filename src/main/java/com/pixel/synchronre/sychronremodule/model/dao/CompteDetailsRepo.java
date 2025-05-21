package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompteDetailsRepo extends JpaRepository<CompteDetails, Long>
{
    @Query("select c from CompteDetails c where c.compteCedante.compteCedId = ?1 and c.typeCompteDet.typeId = ?2")
    CompteDetails findByCompteCedIdAndtypeId(Long compteCedId, Long cedId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(t.typeId, t.name, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled)
        from Type t 
        where t.typeGroup = 'TYPE_DET_COMPTE' 
        order by t.typeOrdre
""")
    List<CompteDetailDto> getDetailComptes();

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(c.compteDetId, t.name, c.debit, c.credit, t.typeId, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled)
         from CompteDetails c join c.typeCompteDet t where c.compteCedante.compte.tranche.trancheId = ?1 and 
        c.compteCedante.cedante.cedId = ?2 and c.compteCedante.compte.periode.periodeId =?3
    """)
    List<CompteDetailDto> findByTrancheIdAndCedIdAndPeriodeId(Long trancheId, Long cedIdSelected, Long periodeId);


    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto(c.compteDetId, t.name, c.debit, c.credit, t.typeId, t.uniqueCode, t.typeOrdre, t.debitDisabled, t.creditDisabled)
         from CompteDetails c join c.typeCompteDet t where c.compteCedante.compteCedId = ?1 and t.uniqueCode not in ("SOLD_REA", "SOUS_TOTAL_CREDIT", "SOLD_CED", "SOUS_TOTAL_DEBIT")
    """)
    List<CompteDetailDto> findByCompteCedI(Long compteCedId);
}