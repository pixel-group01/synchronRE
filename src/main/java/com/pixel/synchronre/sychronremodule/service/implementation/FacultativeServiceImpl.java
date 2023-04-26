package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.MouvementReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceFacultative;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalDate;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.SAISIE;
import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.SAISIE_CRT;

@Service
@RequiredArgsConstructor
public class FacultativeServiceImpl implements IserviceFacultative {
    private final FacultativeRepository facRepo;
    private final AffaireRepository affRepo;
    private final FacultativeMapper facultativeMapper;
    private final ObjectCopier<Facultative> facCopier;
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
        logService.logg(SynchronReActions.CREATE_FAC, null, aff, SynchronReTables.AFFAIRE);
        mvtService.createMvtSuivant(new MvtSuivantReq(aff.getStatut().getStaCode(), aff.getAffId()));
        aff.setCedante(cedRepo.findById(dto.getCedId()).orElse(new Cedante(dto.getCedId())));
        aff.setCouverture(couvRepo.findById(dto.getCouvertureId()).orElse(new Couverture(dto.getCouvertureId())));
        return facultativeMapper.mapToFacultativeDetailsResp(aff);
    }
    private final BrancheRepository branRepo;
    @Override //F+Code filiale+codeBranche+Exercice+numeroOrdre
    public String generateAffCode(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return "F-" + cedRepo.getCedSigleById(affaire.getCedante().getCedId()) +
                branRepo.getBranCheByCouId(affaire.getCouverture().getCouId()) +
                exoService.getExerciceCourant().getExeCode() + "-" +
                String.format("%05d", affId);
    }

    @Override @Transactional
    public FacultativeDetailsResp updateFacultative(UpdateFacultativeReq dto) throws UnknownHostException {
        Facultative fac = facRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        Facultative oldFac = facCopier.copy(fac);
        //fac.setFacCapitaux(dto.getFacCapitaux());
        fac.setFacPrime(dto.getFacPrime());
        fac.setFacSmpLci(dto.getFacSmpLci());
        fac.setAffActivite(dto.getAffActivite());
        fac.setAffAssure(dto.getAffAssure());
        fac.setAffDateEcheance(dto.getAffDateEcheance());
        fac.setAffDateEffet(dto.getAffDateEffet());
        fac=facRepo.save(fac);
        logService.logg(SynchronReActions.UPDATE_FAC, oldFac, fac, SynchronReTables.AFFAIRE);
        return facultativeMapper.mapToFacultativeDetailsResp(fac);
    }

    @Override
    public Page<FacultativeListResp> searchFacultative(String key, Pageable pageable) {
        return null;
    }

}
