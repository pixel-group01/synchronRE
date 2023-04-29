package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.archivemodule.controller.service.IServiceDocument;
import com.pixel.synchronre.archivemodule.controller.service.ReglementDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
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
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
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
    private final IServiceDocument docService;
    private final ReglementDocUploader regDocUploader;

    @Override @Transactional
    public ReglementDetailsResp createReglement(String typeReg, CreateReglementReq dto) throws UnknownHostException {
        boolean hasReglement = regRepo.affaireHasReglement(dto.getAffId(), typeReg);
        Reglement paiement = reglementMapper.mapToReglement(dto);
        paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement.setTypeReglement(typeRepo.findByUniqueCode(typeReg));
        paiement = regRepo.save(paiement); Long regId = paiement.getRegId();
        logService.logg(SynchronReActions.CREATE_REGLEMENT, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        if(!hasReglement)
        {
            mvtService.createMvtSuivant(new MvtSuivantAffaireReq(EN_COURS_DE_REGLEMENT.staCode, dto.getAffId()));
        }

        dto.getRegDocReqs().forEach(docDto->
        {
            String uniqueCode = typeRepo.getUniqueCode(docDto.getDocTypeId());
            UploadDocReq uploadDocReq = new UploadDocReq(regId, uniqueCode, dto.getRegReference(), docDto.getDescription(), docDto.getRegDoc());
            if(docDto.getRegDoc() != null && !docDto.getRegDoc().getOriginalFilename().equals("")) regDocUploader.uploadDocument(uploadDocReq);
        });

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
    public Page<ReglementListResp> searchReglement(String key, Long affId, String typRegUniqueCode, Pageable pageable) {
        return regRepo.searchReglement(StringUtils.stripAccentsToUpperCase(key), affId, typRegUniqueCode, pageable);
    }
}
