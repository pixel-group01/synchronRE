package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CategorieRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheCedanteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheCedanteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import com.pixel.synchronre.sychronremodule.model.events.TrancheEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;

@Service @RequiredArgsConstructor
public class TrancheCedanteService implements ITrancheCedanteService
{
    private final TrancheCedanteRepository trancheCedanteRepo;
    private final CedanteTraiteRepository cedTraiRepo;
    private final CategorieRepository catRepo;
    private final IServiceCedanteTraite cedanteTraiteService;
    private final TrancheCedanteMapper tcMapper;

    @Override @Transactional
    public void onAddOrRemoveCedanteToCategorie(Long cedId, Long catId)
    {
        Long cedanteTraiteId = catRepo.getCedanteTraiteIdByCedIdAndCatId(cedId, catId);
        if(cedanteTraiteId == null) return;
        List<Long> trancheCedantesIdsToRemove = trancheCedanteRepo.getTrancheCedanteIdsToRemove(cedanteTraiteId);
        this.removeObsoleteTrancheCedantes(trancheCedantesIdsToRemove);
        List<TranchePmdDto> tranchePmdsToAdd = trancheCedanteRepo.getTranchePmdToAdd(cedanteTraiteId);
        this.addTranchePmds(tranchePmdsToAdd, cedanteTraiteId, cedId);
    }

    private void addTranchePmds(List<TranchePmdDto> tranchePmdsToAdd, Long cedanteTraiteId, Long cedId)
    {
        if(tranchePmdsToAdd == null || tranchePmdsToAdd.isEmpty()) return;
        Long traiteNpId = cedTraiRepo.getTraiteIdByCedTraiId(cedanteTraiteId);
        BigDecimal assiettePrime = cedTraiRepo.getAssiettePrime(cedanteTraiteId);
        CedanteTraiteReq cedanteTraiteReq = new CedanteTraiteReq(cedanteTraiteId, traiteNpId, assiettePrime, cedId);

        tranchePmdsToAdd.forEach(trPmd->
        {
            TranchePmdDto tranchePmdDto = cedanteTraiteService.calculatePmds(trPmd, cedanteTraiteReq, 20);
            TrancheCedante tc = tcMapper.mapToTrancheCedante(tranchePmdDto, new CedanteTraite(cedanteTraiteId), new Tranche(trPmd.getTrancheId()));
            trancheCedanteRepo.save(tc);
        });

    }

    private void removeObsoleteTrancheCedantes(List<Long> trancheCedantesIdsToRemove) {
        if(trancheCedantesIdsToRemove == null || trancheCedantesIdsToRemove.isEmpty()) return;
        trancheCedanteRepo.deleteAllById(trancheCedantesIdsToRemove);
    }

    @Override
    public void onAddOrRemoveCategorieToTranche(Long catId, Long trancheId)
    {
        List<Long> cedIds = catRepo.getCedIdsByCatId(catId);
        if(cedIds == null || cedIds.isEmpty()) return;
        cedIds.forEach(cedId->
        {
            Long cedanteTraiteId = catRepo.getCedanteTraiteIdByCedIdAndCatId(cedId, catId);
            if(cedanteTraiteId != null)
            {
                List<Long> trancheCedantesIdsToRemove = trancheCedanteRepo.getTrancheCedanteIdsToRemove(cedanteTraiteId);
                this.removeObsoleteTrancheCedantes(trancheCedantesIdsToRemove);
                List<TranchePmdDto> tranchePmdsToAdd = trancheCedanteRepo.getTranchePmdToAdd(cedanteTraiteId);
                this.addTranchePmds(tranchePmdsToAdd, cedanteTraiteId, cedId);
            }
        });
    }

    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onTrancheUpdate(TrancheEvent trancheEvent)
    {
        Long trancheId = trancheEvent.getTranche().getTrancheId();
        List<TrancheCedante> trancheCedantes = trancheCedanteRepo.findByTrancheId(trancheId);
        trancheCedantes.forEach(tc->
        {
            Long cedanteTraiteId = tc.getTrancheCedanteId();
            Long traiteNpId = cedTraiRepo.getTraiteIdByCedTraiId(cedanteTraiteId);
            Long cedId = cedTraiRepo.getCedIdByCedanteTaiteId(cedanteTraiteId);
            BigDecimal assiettePrime = cedTraiRepo.getAssiettePrime(cedanteTraiteId);
            CedanteTraiteReq dto = new CedanteTraiteReq();
            dto.setCedanteTraiteId(cedanteTraiteId);
            dto.setTraiteNpId(traiteNpId);
            dto.setCedId(cedId);
            dto.setAssiettePrime(assiettePrime);
            List<TranchePmdDto> tranchePmdDtos = cedanteTraiteService.getTranchePmdDtos(dto, 20);
            if(tranchePmdDtos == null || tranchePmdDtos.isEmpty()) return;
            tranchePmdDtos.forEach(trPmd->
            {
                tc.setPmd(trPmd.getPmd());
                tc.setPmdCourtier(trPmd.getPmdCourtier());
                tc.setPmdCourtierPlaceur(trPmd.getPmdCourtierPlaceur());
                tc.setPmdNette(trPmd.getPmdNette());
                trancheCedanteRepo.save(tc);
            });
        });
    }
}
