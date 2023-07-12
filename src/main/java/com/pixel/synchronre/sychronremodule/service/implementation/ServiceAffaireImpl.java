package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCedLegRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class ServiceAffaireImpl implements IserviceAffaire
{
    private final AffaireRepository affRepo;
    private final FacultativeMapper affMapper;
    private final ObjectCopier<Affaire> affCopier;
    private final ILogService logService;

    @Override
    public EtatComptableAffaire getEtatComptable(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return affMapper.mapToEtatComptableAffaire(affaire);
    }

    @Override @Transactional
    public void setAsNonRealisee(Long affId) throws UnknownHostException {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        affaire.setAffStatutCreation("NON_REALISEE");
        affRepo.save(affaire);
        logService.logg(AffaireActions.SET_AS_NON_REALISEE, oldAffaire, affaire, SynchronReTables.AFFAIRE);
    }

    @Override
    public void setAsRealisee(Long affId) throws UnknownHostException {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        affaire.setAffStatutCreation("REALISEE");
        affRepo.save(affaire);
        logService.logg(AffaireActions.SET_AS_REALISEE, oldAffaire, affaire, SynchronReTables.AFFAIRE);
    }
}