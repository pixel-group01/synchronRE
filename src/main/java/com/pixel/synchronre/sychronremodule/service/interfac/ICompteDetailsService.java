package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailsItems;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;

import java.math.BigDecimal;

public interface ICompteDetailsService {
    CompteDetails saveCompteDetails(CompteDetailDto dto, Long compteCedanteId);
    CompteDetailsItems calculateDetailsComptesItems(CompteDetailsItems items, int precision);
}
