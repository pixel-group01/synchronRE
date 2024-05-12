package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.ReconstitutionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReconstitutionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TrancheMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reconstitution;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReconstitution;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class ReconstitutionServiceImpl implements IserviceReconstitution
{
    private final ReconstitutionRepository reconstRepo;
    private final ILogService logService;
    private final ReconstitutionMapper reconstitutionMapper;
    private final ObjectCopier<Reconstitution> reconstitutionCopier;

    @Override
    public ReconstitutionResp save(ReconstitutionReq dto) {
        if(dto.getReconstitutionId() == null) return this.create(dto);
        return this.update(dto);

    }
    @Transactional
    public ReconstitutionResp update(ReconstitutionReq dto) {
        if(dto.getTrancheId() == null) throw new AppException("Veuillez sélectionner la tranche à modifier");
        Reconstitution reconstitution = reconstRepo.findById(dto.getReconstitutionId()).orElseThrow(()->new AppException("Reconstitution introuvable"));
        Reconstitution oldReconstitution = reconstitutionCopier.copy(reconstitution);
        BeanUtils.copyProperties(dto, reconstitution);
        reconstitution.setTranche(new Tranche(dto.getTrancheId()));
        logService.logg("Modification d'une reconstitution", oldReconstitution, reconstitution, "Reconstitution");
        return reconstRepo.getReconstitutionResp(dto.getReconstitutionId());

    }

    public ReconstitutionResp create(ReconstitutionReq dto) {
        Reconstitution reconstitution = reconstitutionMapper.mapToReconstitution(dto);
        reconstitution = reconstRepo.save(reconstitution);
        logService.logg("Création d'une reconstitution", new Reconstitution(), reconstitution, "Reconstitution");
        return reconstRepo.getReconstitutionResp(dto.getReconstitutionId());

    }

    @Override
    public boolean delete(Long reconstitutionId) {
        if(reconstitutionId == null) throw new AppException("Veuillez sélectionner la reconstitution à supprimer");
        Reconstitution reconstitution = reconstRepo.findById(reconstitutionId).orElseThrow(()->new AppException("Reconstitution introuvable"));
        Reconstitution oldReconstitutionId = reconstitutionCopier.copy(reconstitution);
        reconstitution.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'une reconstitution", oldReconstitutionId, reconstitution, "Reconstitution");
        return true;
    }

    @Override
    public Page<ReconstitutionResp> search(Long traiteNPId, String key, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<ReconstitutionResp> reconstitutionRespPage = reconstRepo.search(traiteNPId, key, pageable);
        return reconstitutionRespPage;

    }
}
