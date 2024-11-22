package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteCessionnaireDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.TrancheCompteDto;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Periode;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteService implements IserviceCompte {
    private final CompteTraiteRepo compteTraiteRepo;
    private final CedMapper cedMapper;
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
            tc.setDetailComptes(compteDetails);
            List<CompteCessionnaireDto> cessionnaires = compteTraiteRepo.getCompteCessionnaires(tc.getTrancheId());
            tc.setCompteCessionnaires(cessionnaires);
        });
        compteTraiteDto.setTrancheCompteDtos(trancheComptes);
        return compteTraiteDto;
    }

    @Override
    public CompteTraiteDto save(CompteTraiteDto dto)
    {
        //Creer le compte
        //Creer compteCedante
        //Creer les details comptes
        //Creer les comptes cessionnaires
        Long trancheId = dto.getTrancheIdSelected();
        List<TrancheCompteDto> trancheCompteDtos = dto.getTrancheCompteDtos();
        Long compteId = dto.getCompteId();
        Long periodeId = dto.getPeriodeId();
        trancheCompteDtos.stream().forEach(trc->
        {
            Long cedId = trc.getCedIdSelected();
            List<CompteDetailDto> detailComptes = trc.getDetailComptes();
        });
        return null;
    }

    @Override
    public List<Periode> getPeriode(Long exeCode, Long typeId)
    {
        return compteTraiteRepo.getPeriodesByTypeId(exeCode, typeId);
    }
}
