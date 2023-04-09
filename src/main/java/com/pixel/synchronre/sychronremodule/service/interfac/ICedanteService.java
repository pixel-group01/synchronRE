package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface ICedanteService
{
    ReadCedenteDTO createCedente(CreateCedenteDTO dto) throws UnknownHostException;
    ReadCedenteDTO updateCedente(UpdateCedanteDTO dto) throws UnknownHostException;
    Page<ReadCedenteDTO> searchCedente(String key, Pageable pageable);
}
