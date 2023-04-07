package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedente.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface ICedenteService
{
    ReadCedenteDTO createCedente(CreateCedenteDTO dto) throws UnknownHostException;
    ReadCedenteDTO updateCedente(UpdateCedenteDTO dto) throws UnknownHostException;
    Page<ReadCedenteDTO> searchCedente(String key, Pageable pageable);
}
