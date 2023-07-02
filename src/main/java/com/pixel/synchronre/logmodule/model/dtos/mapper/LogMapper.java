package com.pixel.synchronre.logmodule.model.dtos.mapper;

import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.logmodule.model.entities.Log;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    ConnexionList mapConnexionListToLog(Log log);
}
