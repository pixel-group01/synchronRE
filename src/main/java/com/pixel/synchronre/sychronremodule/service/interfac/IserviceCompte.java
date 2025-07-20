package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.entities.Periode;
import com.pixel.synchronre.sychronremodule.model.views.VStatCompte;

import java.util.List;

public interface IserviceCompte {
    CompteTraiteDto getCompteTraite(Long traiteNpId, Long periodeId, int precision);

    CompteTraiteDto save(CompteTraiteDto dto);

    List<Periode> getPeriode(Long exeCode, Long typeId);

    CompteTraiteDto getCompteTraite(CompteTraiteDto dto, int precision);
}
