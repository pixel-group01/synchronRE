package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteCessionnaireDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.DetailCompte;
import com.pixel.synchronre.sychronremodule.model.dto.compte.TrancheCompteDto;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompteService implements IserviceCompte {
    private final CompteTraiteRepo compteTraiteRepo;
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
            tc.setCedantes(cedantes);
            List<DetailCompte> detailComptes = compteTraiteRepo.getDetailComptes();
            tc.setDetailComptes(detailComptes);
            List<CompteCessionnaireDto> cessionnaires = compteTraiteRepo.getCompteCessionnaires(tc.getTrancheId());
            tc.setCompteCessionnaires(cessionnaires);
        });
        compteTraiteDto.setTrancheCompteDtos(trancheComptes);
        return compteTraiteDto;
    }
}
