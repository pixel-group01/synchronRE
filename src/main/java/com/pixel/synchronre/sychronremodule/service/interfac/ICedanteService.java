package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface ICedanteService
{
    ReadCedanteDTO createCedente(CreateCedanteDTO dto) throws UnknownHostException;
    ReadCedanteDTO updateCedente(UpdateCedanteDTO dto) throws UnknownHostException;
    Page<ReadCedanteDTO> searchCedente(String key, Pageable pageable);
}
