package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteCessionnaireDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.TrancheCompteDto;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedMapper;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteCalculService;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteDetailsService;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteService implements IserviceCompte {
    private final CompteTraiteRepo compteTraiteRepo;
    private final CedMapper cedMapper;
    private final CompteCedanteRepo compteCedanteRepo;
    private final PeriodeRepo periodeRepo;
    private final ICompteDetailsService compteDetailsService;
    private final CompteCessionnaireRepo compteCesRepo;
    private final ICompteCalculService compteCalculService;

    @Override
    public CompteTraiteDto getCompteTraite(Long traiteNpId)
    {
        CompteTraiteDto compteTraiteDto = compteTraiteRepo.getCompteByTraite(traiteNpId);
        if(compteTraiteDto == null) return null;
        List<TrancheCompteDto> trancheComptes = compteTraiteRepo.getCompteTranches(traiteNpId);
        if(trancheComptes == null || trancheComptes.isEmpty()) return compteTraiteDto;
        trancheComptes.stream().filter(Objects::nonNull).forEach(tc->
        {
            List<ReadCedanteDTO> cedantes = compteTraiteRepo.getCompteCedantes(tc.getTrancheId());
            List<ReadCedanteDTO.ReadCedanteDTOLite> liteCedantes = cedantes == null ? Collections.emptyList() :
                    cedantes.stream().map(cedMapper::mapToReadCedenteDTOLite).collect(Collectors.toList());
            tc.setCedantes(liteCedantes);
            List<CompteDetailDto> compteDetails = compteTraiteRepo.getDetailComptes();
            tc.setCompteDetails(compteDetails);
            List<CompteCessionnaireDto> cessionnaires = compteTraiteRepo.getCompteCessionnaires(tc.getTrancheId());
            tc.setCompteCessionnaires(cessionnaires);
        });
        compteTraiteDto.setTrancheCompteDtos(trancheComptes);
        return compteTraiteDto;
    }


    private CompteTraiteDto create(CompteTraiteDto dto)
    {
        Long trancheId = dto.getTrancheIdSelected();
        Long periodeId = dto.getPeriodeId();
        List<TrancheCompteDto> trancheCompteDtos = dto.getTrancheCompteDtos();

        TrancheCompteDto trancheCompteDto = trancheCompteDtos.stream()
                .filter(tc->tc.getTrancheId().equals(trancheId))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables"));
        Long cedId = trancheCompteDto.getCedIdSelected();
        Compte compte = compteTraiteRepo.findByTrancheIdAndPeriodeId(trancheId, periodeId);
        compte = compte == null ? new Compte(new Tranche(trancheId), new Periode(dto.getPeriodeId())) : compte;
        compte = compteTraiteRepo.save(compte);
        Long compteId = compte.getCompteId();

        CompteCedante compteCedante = compteCedanteRepo.findByCompteIdAndCedId(compteId, cedId);
        compteCedante = compteCedante == null ? new CompteCedante(new Compte(compteId), new Cedante(cedId)) : compteCedante;
        compteCedante = compteCedanteRepo.save(compteCedante);
        Long compteCedanteId = compteCedante.getCompteCedId();

        List<CompteDetailDto> compteDetails = trancheCompteDto.getCompteDetails();
        if(compteDetails != null && !compteDetails.isEmpty())
        {
            compteDetails.stream().map(cd->compteDetailsService.saveCompteDetails(cd, compteCedanteId)).collect(Collectors.toList());
        }

        List<CompteCessionnaireDto> compteCessionnaires = trancheCompteDto.getCompteCessionnaires();
        if(compteCessionnaires != null && !compteCessionnaires.isEmpty())
        {
            compteCessionnaires.stream().map(cc->this.saveCompteCessionnaire(cc)).collect(Collectors.toList());
        }

        return dto;
    }

    private CompteCessionnaire saveCompteCessionnaire(CompteCessionnaireDto dto)
    {
        Long compteCedId = dto.getCompteCedId();
        Long cesId = dto.getCesId();
        CompteCessionnaire compteCessionnaire = compteCesRepo.findByCompteCedIdAndCesId(dto.getCompteCedId(), dto.getCesId());
        BigDecimal primeCes = compteCalculService.calculatePrimeCessionnaireOnCompte(compteCedId, cesId);
        compteCessionnaire = compteCessionnaire == null ?
                new CompteCessionnaire(dto.getTaux(), primeCes, new CompteCedante(compteCedId), new Cessionnaire(cesId)) :
                compteCessionnaire;

        compteCessionnaire = compteCesRepo.save(compteCessionnaire);
        return compteCessionnaire;
    }


    private CompteTraiteDto update(CompteTraiteDto dto)
    {
        return null;
    }

    @Override
    public CompteTraiteDto save(CompteTraiteDto dto)
    {
        if(dto.getCompteId() == null) return this.create(dto);
        else return this.update(dto);
    }

    @Override
    public List<Periode> getPeriode(Long exeCode, Long typeId)
    {
        return periodeRepo.getPeriodesByTypeId(exeCode, typeId);
    }
}
