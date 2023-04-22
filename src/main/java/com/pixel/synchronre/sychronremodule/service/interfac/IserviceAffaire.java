package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;

import java.math.BigDecimal;

public interface IserviceAffaire {
    EtatComptableAffaire getEtatComptable(Long affId);
}
