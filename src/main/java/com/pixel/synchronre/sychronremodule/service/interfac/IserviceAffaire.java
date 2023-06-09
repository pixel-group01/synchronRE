package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import java.net.UnknownHostException;

public interface IserviceAffaire {
    EtatComptableAffaire getEtatComptable(Long affId);

    void setAsNonRealisee(Long affId) throws UnknownHostException;

    void setAsRealisee(Long affId) throws UnknownHostException;
}