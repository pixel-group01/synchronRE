package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;

public interface IserviceCompte {
    CompteTraiteDto getCompteTraite(Long traiteNpId);
}
