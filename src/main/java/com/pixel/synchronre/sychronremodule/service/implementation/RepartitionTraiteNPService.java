package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class RepartitionTraiteNPService implements IServiceRepartitionTraiteNP
{
    private final CompteTraiteRepo ctRepo;
    private final IServiceCalculsComptablesTraite comptaTraiteService;
    private final RepartitionTraiteRepo rtRepo;
    @Override
    public RepartitionTraiteNPResp save(PlacementTraiteNPReq dto)
    {
        if(dto.getRepId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override
    public Page<RepartitionTraiteNPResp> search(Long traiteNPId, String key, Pageable pageable)
    {
        Page<RepartitionTraiteNPResp> repartitionPage = rtRepo.search(traiteNPId, key, pageable);
        List<RepartitionTraiteNPResp> repartitionList = repartitionPage.stream()
                .peek(r->r.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaReparti(traiteNPId)))
                .toList();
        return new PageImpl<>(repartitionList, pageable, repartitionPage.getTotalElements());
    }

    @Override
    public RepartitionTraiteNPResp create(PlacementTraiteNPReq dto) {
        return null;
    }

    @Override
    public RepartitionTraiteNPResp update(PlacementTraiteNPReq dto) {
        return null;
    }
}
