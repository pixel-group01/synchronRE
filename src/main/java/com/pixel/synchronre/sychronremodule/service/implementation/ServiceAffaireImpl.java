package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.AffStatutGroup;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.constants.STATUT_CREATION;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.BordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.EN_COURS_DE_REPARTITION;

@Service @RequiredArgsConstructor
public class ServiceAffaireImpl implements IserviceAffaire
{
    private final AffaireRepository affRepo;
    private final FacultativeMapper affMapper;
    private final ObjectCopier<Affaire> affCopier;
    private final ILogService logService;
    private final EmailSenderService mailSenderService;
    private final CedRepo cedRepo;
    private final IServiceReport reportService;
    @Value("${spring.mail.username}")
    private String synchronreEmail;
    private final IServiceMouvement mvtService;
    private final IJwtService jwtService;
    private final IserviceExercie exoService;
    private final IServiceCalculsComptables comptaAffaireService;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final IserviceBordereau bordService;
    private final BordereauRepository bordRep;

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

    @Override
    public boolean senNoteDebitFac(Long affId) throws Exception {
        //Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Cedante ced = cedRepo.getCedanteByAffId(affId);
        mailSenderService.sendNoteDebitFacEmail(synchronreEmail, ced.getCedEmail(),affId);
        if(bordRep.noteDebExistsByAffId(affId)) return true;
        bordService.createNoteDebit(affId);
        return true;
    }

    @Override
    public Base64FileDto printNoteCreditFac(Long affId, Long cesId) throws Exception
    {

        byte[] bytes = this.reportService.generateNoteCreditFac(affId, cesId);
        String base64String = Base64ToFileConverter.convertBytesToJSBase64UrlString(bytes);
        Base64FileDto dto = new Base64FileDto(base64String, bytes);
        return dto;
    }

    @Override
    public Page<FacultativeListResp> transmettreAffaireAuSouscripteur(Long affId, Pageable pageable) throws UnknownHostException {
            String statutCreation = affRepo.getAffStatutCreation(affId);
            String cedName = cedRepo.getCedNameByAffId(affId);
            if(!STATUT_CREATION.REALISEE.name().equals(statutCreation)) throw new AppException("Impossible de transmettre une affaire en intance ou non réalisée");
            mvtService.createMvtAffaire(new MvtReq(AffaireActions.TRANSMETTRE_AU_SOUSCRIPTEUR, affId, EN_ATTENTE_DE_PLACEMENT.staCode, null));
            Page<FacultativeListResp> facPages = affRepo.searchAffaires("", null, null,
                    jwtService.getConnectedUserCedId(),
                    Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exoService.getExerciceCourant().getExeCode(), pageable);
            List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerEmailTransmissionAffaireAuSouscripteur(u.getEmail(), u.getFirstName(), cedName));
            return new PageImpl<>(facList, pageable, facPages.getTotalElements());
    }

    @Override
    public Page<FacultativeListResp> retournerAffaireALaCedante(MvtReq dto, Pageable pageable) throws UnknownHostException
    {
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.RETOURNER_A_CEDANTE, dto.getObjectId(), RETOURNE.staCode, dto.getMvtObservation()));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires("", null, null,
                affRepo.getAffCedId(dto.getObjectId()),
                Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        String affCode = affRepo.getAffCode(dto.getObjectId());
        Long cedId = cedRepo.getCedIdByAffId(dto.getObjectId());
        userRepo.getUserByTypeFunctionAndCedId("TYF_SAI_CED", cedId).forEach(u->emailSenderService.envoyerEmailRetourAffaireALaCedante(u.getEmail(), u.getFirstName(), affCode, dto.getMvtObservation()));

        return new PageImpl<>(facList, pageable, facPages.getTotalElements());
    }

    @Override
    public Page<FacultativeListResp> validerAffaire(Long affId, Pageable pageable) throws UnknownHostException {
        String statutCreation = affRepo.getAffStatutCreation(affId);
        if(!STATUT_CREATION.REALISEE.name().equals(statutCreation)) throw new AppException("Impossible de valider une affaire en intance ou non réalisée");
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.VALIDER_FAC, affId, EN_COURS_DE_PAIEMENT.staCode,null));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires("", null, null, null,
                Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        userRepo.getUserByTypeFunction("TYF_COMPTA").forEach(u->emailSenderService.envoyerEmailTransmissionAffaireALaCompta(u.getEmail(), u.getFirstName()));
        return new PageImpl<>(facList, pageable, facPages.getTotalElements());
    }

    @Override
    public Page<FacultativeListResp> searchAffaires(Long exeCode, String key, List<String> staCodes, Pageable pageable)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), AffStatutGroup.tabEnCoursPaiement, exeCode, pageable);
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, pageable, facPages.getTotalElements());
    }

    @Override
    public boolean placementIsFinished(Long affId)
    {
        BigDecimal besFac = this.comptaAffaireService.calculateRestARepartir(affId);
        besFac = besFac == null ? BigDecimal.ZERO : besFac;
        return besFac.compareTo(BigDecimal.ZERO) == 0;
    }
}
