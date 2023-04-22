package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.EN_COURS_DE_REGLEMENT;

@Service @AllArgsConstructor
public class ServiceReglementImpl implements IserviceReglement {
    private final ReglementRepository regRepo;
    private final ReglementMapper reglementMapper;
    private final ObjectCopier<Reglement> paiCopier;
    private final ILogService logService;
    private final IJwtService jwtService;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
    private final IServiceMouvement mvtService;

    @Override @Transactional
    public ReglementDetailsResp createReglement(String typeReg, CreateReglementReq dto) throws UnknownHostException {
        boolean hasReglement = regRepo.affaireHasReglement(dto.getAffId(), typeReg);
        Reglement paiement = reglementMapper.mapToReglement(dto);
        paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement.setTypeReglement(typeRepo.findByUniqueCode(typeReg));
        paiement = regRepo.save(paiement);
        logService.logg(SynchronReActions.CREATE_REGLEMENT, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        if(!hasReglement)
        {
            mvtService.createMvtSuivant(new MvtSuivantReq(EN_COURS_DE_REGLEMENT.staCode, dto.getAffId()));
        }
        return reglementMapper.mapToReglementDetailsResp(paiement);
    }

    @Override @Transactional
    public ReglementDetailsResp updateReglement(UpdateReglementReq dto) throws UnknownHostException {
        Reglement ref = regRepo.findById(dto.getRegId()).orElseThrow(()->new AppException("Reglement introuvable"));
        Reglement oldPai = paiCopier.copy(ref);
        ref.setRegReference(dto.getRegReference());
        ref.setRegMontant(dto.getRegMontant());
        ref.setRegDate(dto.getRegDate());
        ref=regRepo.save(ref);
        logService.logg(SynchronReActions.UPDATE_REGLEMENT, oldPai, ref, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToReglementDetailsResp(ref);
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, String typRegUniqueCode, Pageable pageable) {
        return regRepo.searchReglement(StringUtils.stripAccentsToUpperCase(key),typRegUniqueCode, pageable);
    }
}
