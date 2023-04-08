package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

@Service @RequiredArgsConstructor
public class CedenteService implements ICedenteService
{
    private final CedRepo cedRepo;
    private final CedMapper cedMapper;
    private final ObjectCopier<Cedante> cedCopier;
    private final ILogService logService;

    @Override @Transactional
    public ReadCedenteDTO createCedente(CreateCedenteDTO dto) throws UnknownHostException
    {
        Cedante cedante = cedMapper.mapToCedente(dto);
        cedante = cedRepo.save(cedante);
        logService.logg(SynchronReActions.CREATE_CEDENTE, null, cedante, SynchronReTables.CEDENTE);
        return cedMapper.mapToReadCedenteDTO(cedante);
    }

    @Override @Transactional
    public ReadCedenteDTO updateCedente(UpdateCedenteDTO dto) throws UnknownHostException
    {
        Cedante oldCed = cedCopier.copy(cedRepo.findById(dto.getCedId()).orElseThrow(()->new AppException("Cedente introuvable")));
        Cedante cedante = cedMapper.mapToCedente(dto);
        cedante = cedRepo.save(cedante);
        logService.logg(SynchronReActions.UPDATE_CEDENTE, oldCed, cedante, SynchronReTables.CEDENTE);
        return cedMapper.mapToReadCedenteDTO(cedante);
    }

    @Override
    public Page<ReadCedenteDTO> searchCedente(String key, Pageable pageable) {
        return cedRepo.searchCedentes(key, pageable);
    }
}
