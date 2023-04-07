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
import com.pixel.synchronre.sychronremodule.model.entities.Cedente;
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
    private final ObjectCopier<Cedente> cedCopier;
    private final ILogService logService;

    @Override @Transactional
    public ReadCedenteDTO createCedente(CreateCedenteDTO dto) throws UnknownHostException
    {
        Cedente cedente = cedMapper.mapToCedente(dto);
        cedente = cedRepo.save(cedente);
        logService.logg(SynchronReActions.CREATE_CEDENTE, null, cedente, SynchronReTables.CEDENTE);
        return cedMapper.mapToReadCedenteDTO(cedente);
    }

    @Override @Transactional
    public ReadCedenteDTO updateCedente(UpdateCedenteDTO dto) throws UnknownHostException
    {
        Cedente oldCed = cedCopier.copy(cedRepo.findById(dto.getCedId()).orElseThrow(()->new AppException("Cedente introuvable")));
        Cedente cedente = cedMapper.mapToCedente(dto);
        cedente = cedRepo.save(cedente);
        logService.logg(SynchronReActions.UPDATE_CEDENTE, oldCed, cedente, SynchronReTables.CEDENTE);
        return cedMapper.mapToReadCedenteDTO(cedente);
    }

    @Override
    public Page<ReadCedenteDTO> searchCedente(String key, Pageable pageable) {
        return cedRepo.searchCedentes(key, pageable);
    }
}
