package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceFacultative;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.SAISIE;
import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.SAISIE_CRT;

@Service
@RequiredArgsConstructor
public class FacultativeServiceImpl implements IserviceFacultative {
    private final AffaireRepository affRepo;
    private final FacultativeMapper facultativeMapper;
    private final ObjectCopier<Affaire> affCopier;
    private final ILogService logService;
    private final IServiceMouvement mvtService;
    private final StatutRepository staRepo;
    private final RepartitionRepository repRepo;
    private final CedRepo cedRepo;
    private final CouvertureRepository couvRepo;
    private final IJwtService jwtService;
    private final IserviceExercie exoService;

    @Override @Transactional
    public FacultativeDetailsResp createFacultative(CreateFacultativeReq dto) throws UnknownHostException
    {
        boolean isCourtier = jwtService.UserIsCourtier();
        Affaire aff=facultativeMapper.mapToAffaire(dto);
        aff.setStatut(isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode));
        aff=affRepo.save(aff);
        aff.setAffCode(this.generateAffCode(aff.getAffId()));
        logService.logg(AffaireActions.CREATE_FAC, null, aff, SynchronReTables.AFFAIRE);
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.CREATE_FAC, aff.getAffId(), aff.getStatut().getStaCode(), null));
        aff.setCedante(cedRepo.findById(dto.getCedId()).orElse(new Cedante(dto.getCedId())));
        aff.setCouverture(couvRepo.findById(dto.getCouvertureId()).orElse(new Couverture(dto.getCouvertureId())));
        return facultativeMapper.mapToFacultativeDetailsResp(aff);
    }
    private final BrancheRepository branRepo;
    @Override //F+Code filiale+codeBranche+Exercice+numeroOrdre
    public String generateAffCode(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return "F." + cedRepo.getCedSigleById(affaire.getCedante().getCedId()) + "." +
                branRepo.getBranCheByCouId(affaire.getCouverture().getCouId()) + "." +
                exoService.getExerciceCourant().getExeCode() + "." +
                String.format("%05d", affId);
    }

    @Override @Transactional
    public FacultativeDetailsResp updateFacultative(UpdateFacultativeReq dto) throws UnknownHostException {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        affaire.setAffCapitalInitial(dto.getAffCapitalInitial());
        affaire.setFacPrime(dto.getFacPrime());
        affaire.setFacSmpLci(dto.getFacSmpLci());
        affaire.setAffActivite(dto.getAffActivite());
        affaire.setAffAssure(dto.getAffAssure());
        affaire.setAffDateEcheance(dto.getAffDateEcheance());
        affaire.setAffDateEffet(dto.getAffDateEffet());
        affaire.setAffStatutCreation(dto.getAffStatutCreation());
        if(dto.getCouvertureId() != null) affaire.setCouverture(new Couverture(dto.getCouvertureId()));
        if(dto.getCedId() != null) affaire.setCedante(new Cedante(dto.getCedId()));
        affaire=affRepo.save(affaire);
        logService.logg(AffaireActions.UPDATE_FAC, oldAffaire, affaire, SynchronReTables.AFFAIRE);
        return facultativeMapper.mapToFacultativeDetailsResp(affaire);
    }

    @Override
    public Page<FacultativeListResp> searchFacultative(String key, Pageable pageable) {
        return null;
    }

}
