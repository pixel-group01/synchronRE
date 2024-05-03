package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.nature.response.NatureListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Nature;

import java.util.List;

public interface IserviceNature {
    public List<NatureListResp> getListeNatures(String forme);
}
