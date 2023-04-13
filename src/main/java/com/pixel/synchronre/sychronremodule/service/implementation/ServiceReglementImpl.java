package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service @AllArgsConstructor
public class ServiceReglementImpl implements IserviceReglement {
    private final ReglementRepository regRepo;
    private final ReglementMapper reglementMapper;
    private final ObjectCopier<Reglement> paiCopier;
    private final ILogService logService;
    private final IJwtService jwtService;
    private final AffaireRepository affRepo;

    @Override
    public ReglementDetailsResp createReglement(TypeReglement typeReg, CreateReglementReq dto) throws UnknownHostException {
        Reglement paiement = reglementMapper.mapToReglement(dto);
        paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement.setTypeReglement(typeReg);
        paiement = regRepo.save(paiement);
        logService.logg(SynchronReActions.CREATE_REGLEMENT, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        return reglementMapper.mapToReglementDetailsResp(paiement);
    }

    @Override
    public ReglementDetailsResp updateReglement(UpdateReglementReq dto) throws UnknownHostException {
        Reglement ref = regRepo.findById(dto.getRegId()).orElseThrow(()->new AppException("Paiement introuvable"));
        Reglement oldPai = paiCopier.copy(ref);
        ref.setRegReference(dto.getRegReference());
        ref.setRegMontant(dto.getRegMontant());
        ref.setRegDate(dto.getRegDate());
        ref=regRepo.save(ref);
        logService.logg(SynchronReActions.UPDATE_REGLEMENT, oldPai, ref, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToReglementDetailsResp(ref);
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, TypeReglement typReg, Pageable pageable) {
        return regRepo.searchReglement(StringUtils.stripAccentsToUpperCase(key),typReg.name(), pageable);
    }
}
