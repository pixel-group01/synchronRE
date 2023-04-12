package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
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
    public PaiementDetailsResp createPaiement(CreatePaiementReq dto) throws UnknownHostException {
        Reglement paiement = reglementMapper.mapToPaiement(dto);
        paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement = regRepo.save(paiement);
        logService.logg(SynchronReActions.CREATE_PAIEMENT, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        return reglementMapper.mapToPaiementDetailsResp(paiement);
    }

    @Override
    public PaiementDetailsResp updatePaiement(UpdatePaiementReq dto) throws UnknownHostException {
        Reglement ref = regRepo.findById(dto.getRegId()).orElseThrow(()->new AppException("Paiement introuvable"));
        Reglement oldPai = paiCopier.copy(ref);
        ref.setRegReference(dto.getRegReference());
        ref.setRegMontant(dto.getRegMontant());
        ref.setRegDate(dto.getRegDate());
        ref=regRepo.save(ref);
        logService.logg(SynchronReActions.UPDATE_PAIEMENT, oldPai, ref, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToPaiementDetailsResp(ref);
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, Pageable pageable) {
        return null;
    }
}
