package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteDetailsRepo;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteDetailsService;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service @RequiredArgsConstructor
public class CompteDetailsServiceImpl implements ICompteDetailsService
{
    private final CompteDetailsRepo compteDetailsRepo;
    @Override
    public CompteDetails saveCompteDetails(CompteDetailDto dto, Long compteCedanteId)
    {
        CompteDetails compteDetails = compteDetailsRepo.findByCompteCedIdAndtypeId(compteCedanteId, dto.getTypeId());
        compteDetails = compteDetails == null ? new CompteDetails() : compteDetails;
        CompteDetailDto calculatedDto = this.calculateCompteDetails(dto.getUniqueCode(), dto.getDebit(), dto.getCredit(), compteCedanteId, null, 20);
        compteDetails.setDebit(calculatedDto.getDebit());
        compteDetails.setCredit(calculatedDto.getCredit());
        compteDetails.setTypeCompteDet(new Type(dto.getTypeId()));
        compteDetails.setTypeCode(calculatedDto.getUniqueCode());
        compteDetails = compteDetailsRepo.save(compteDetails);
        return compteDetails;
    }

    @Override
    public CompteDetailDto calculateCompteDetails(String uniqueCode, BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        CompteDetailDto dto = switch (uniqueCode) {
            case "PRIM_ORIG" -> this.calculatePrimeOrigine(debit, credit, compteCedId, compteDetId, precision);
            case "PRIM_APR_AJUST" -> this.calculatePrimeApresAjust(debit, credit, compteCedId, compteDetId, precision);
            case "PRIM_REC" -> this.calculatePrimeReconstitution(debit, credit, compteCedId, compteDetId, precision);
            case "SIN_PAYE" -> this.calculateSinistrePaye(debit, credit, compteCedId, compteDetId, precision);
            case "DEP_SAP_CONST" -> this.calculateDepoSapConstitue(debit, credit, compteCedId, compteDetId, precision);
            case "DEP_SAP_LIB" -> this.calculateDepoSapLibere(debit, credit, compteCedId, compteDetId, precision);
            case "INT_DEP_LIB" -> this.calculateIntereDepotLibere(debit, credit, compteCedId, compteDetId, precision);
            case "SOUS_TOTAL" -> this.calculateSousTotal(debit, credit, compteCedId, compteDetId, precision);
            case "SOLD_CED" -> this.calculateSousTotalCed(debit, credit, compteCedId, compteDetId, precision);
            case "SOLD_REA" -> this.calculateSousTotalRea(debit, credit, compteCedId, compteDetId, precision);
            case "TOTAL_MOUV" -> this.calculateTotalMouv(debit, credit, compteCedId, compteDetId, precision);
            default -> null;
        };
        return dto;
    }

    //TODO à implementer
    private CompteDetailDto calculateTotalMouv(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateSousTotalRea(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateSousTotalCed(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateSousTotal(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateIntereDepotLibere(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateDepoSapLibere(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateDepoSapConstitue(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculateSinistrePaye(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculatePrimeReconstitution(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }

    //TODO à implementer
    private CompteDetailDto calculatePrimeApresAjust(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        return null;
    }


    private CompteDetailDto calculatePrimeOrigine(BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision)
    {
        CompteDetailDto dto = new CompteDetailDto();
        dto.setDebit(debit);
        dto.setCredit(credit);
        dto.setUniqueCode("PRIM_ORIG");
        return dto;
    }

}
