package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;

import java.math.BigDecimal;

public interface ICompteDetailsService {
    CompteDetails saveCompteDetails(CompteDetailDto dto, Long compteCedanteId);
    CompteDetailDto calculateCompteDetails(String uniqueCode, BigDecimal debit, BigDecimal credit, Long compteCedId, Long compteDetId, int precision);
}
