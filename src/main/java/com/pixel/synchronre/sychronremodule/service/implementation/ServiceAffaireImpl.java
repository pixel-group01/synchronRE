package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.RenewFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.EN_COURS_DE_REPARTITION;
import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.UN;

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
    private final IserviceRepartition repService;
    private final RepartitionRepository repRepo;
    private final FacultativeMapper facultativeMapper;
    private final CouvertureRepository couvRepo;
    private final ReglementRepository regRepo;
    private final TypeRepo typeRepo;
    @PersistenceContext
    private EntityManager entityManager;


    @Override @Transactional
    public FacultativeDetailsResp createFacultative(CreateFacultativeReq dto, HistoDetails hd)
    {
        if(dto.getReserveCourtier() != null && dto.getFacSmpLci().compareTo(dto.getReserveCourtier())<=0) throw new AppException("La réserve courtier ne peut exéder la SMPLCI");
        boolean isCourtier = jwtService.UserIsCourtier();
        Affaire aff=facultativeMapper.mapToAffaire(dto);
        BeanUtils.copyProperties(hd, aff);
        if(dto.getReserveCourtier() == null) aff.setReserveCourtier(BigDecimal.ZERO);
        aff.setStatut(isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode));
        aff=affRepo.save(aff);
        aff.setAffCode(this.generateAffCode(aff.getAffId()));
        logService.logg(AffaireActions.CREATE_FAC, null, aff, SynchronReTables.AFFAIRE);
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.CREATE_FAC, aff.getAffId(), aff.getStatut().getStaCode(), null));
        aff.setCedante(cedRepo.findById(dto.getCedId()).orElse(new Cedante(dto.getCedId())));
        aff.setCouverture(couvRepo.findById(dto.getCouvertureId()).orElse(new Couverture(dto.getCouvertureId())));
        repService.saveReserveCourtier(aff.getAffId(), aff.getFacSmpLci(), dto.getFacPrime(), aff.getReserveCourtier());
        return facultativeMapper.mapToFacultativeDetailsResp(aff);
    }

    @Override
    public boolean deleteAffaire(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        String affStaCode = affaire.getStatut().getStaCode();
        if(!AffStatutGroup.statutsDeSuppression.contains(affStaCode)) throw new AppException("Impossible de supprimer une affaire au statut \"" + affaire.getStatut().getStaLibelle() + "\"");
        if(affaire.getAffUserCreator().getUserId() != jwtService.getConnectedUserId()) throw new AppException("Impossible de supprimer une affaire que vous n'avez pas saisie ");
        affaire.setStatut(new Statut("SUP"));
        repRepo.findRepIdByAffId(affId).forEach(repId->repService.annulerRepartition(repId));
        logService.logg(AffaireActions.DELETE_FAC, oldAffaire, affaire, SynchronReTables.AFFAIRE);
        return true;
    }

    private final BrancheRepository branRepo;
    @Override //F+Code filiale+codeBranche+Exercice+numeroOrdre
    public String generateAffCode(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return "F." + cedRepo.getCedSigleById(affaire.getCedante().getCedId()) + "." +
                branRepo.getBranCheByCouId(affaire.getCouverture().getCouId()) + "." +
                affaire.getExercice().getExeCode() + "." +
                String.format("%05d", affId);
    }

    @Override @Transactional
    public FacultativeDetailsResp updateFacultative(UpdateFacultativeReq dto, HistoDetails hd)
    {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        BeanUtils.copyProperties(hd, affaire);
        if(dto.getReserveCourtier() != null && dto.getFacSmpLci().compareTo(dto.getReserveCourtier())<=0) throw new AppException("La réserve courtier ne peut exéder la SMPLCI");
        boolean smpHasChanged = dto.getFacSmpLci() == null ? false : dto.getFacSmpLci().compareTo(affaire.getFacSmpLci()) != 0;
        boolean facPrimeHasChanged = dto.getFacPrime() == null ? false :  dto.getFacPrime().compareTo(affaire.getFacPrime()) != 0;
        boolean facHasReglement = regRepo.affaireHasValidReglement(dto.getAffId());
        if((smpHasChanged || facPrimeHasChanged) && facHasReglement) throw new AppException("Impossible de modifier la SMP ou la prime d'une affaire ayant déjà fait objet d'un règlement");
        Affaire oldAffaire = affCopier.copy(affaire);

        affaire.setAffCapitalInitial(dto.getAffCapitalInitial());
        affaire.setFacPrime(dto.getFacPrime());
        affaire.setFacSmpLci(dto.getFacSmpLci());
        affaire.setAffActivite(dto.getAffActivite());
        affaire.setAffAssure(dto.getAffAssure());
        affaire.setAffDateEcheance(dto.getAffDateEcheance());
        affaire.setAffDateEffet(dto.getAffDateEffet());
        affaire.setAffStatutCreation(dto.getAffStatutCreation());
        affaire.setAffCoursDevise(dto.getAffCoursDevise());
        affaire.setReserveCourtier(dto.getReserveCourtier());
        if(dto.getReserveCourtier() == null) affaire.setReserveCourtier(BigDecimal.ZERO);
        if(dto.getCouvertureId() != null) affaire.setCouverture(new Couverture(dto.getCouvertureId()));
        if(dto.getCedId() != null) affaire.setCedante(new Cedante(dto.getCedId()));
        affaire=affRepo.save(affaire);
        repService.saveReserveCourtier(affaire.getAffId(), affaire.getFacSmpLci(), affaire.getFacPrime(), affaire.getReserveCourtier());
        logService.logg(AffaireActions.UPDATE_FAC, oldAffaire, affaire, SynchronReTables.AFFAIRE);
        if(smpHasChanged || facPrimeHasChanged) //annuler les repartitons et le bordereau lié à l'affaire
        {
            repRepo.findRepIdByAffId(dto.getAffId()).forEach(repId->repService.annulerRepartition(repId));
            affaire.setStatut(new Statut(EN_COURS_DE_REPARTITION.staCode));
            bordRep.findBordIdByAffId(dto.getAffId()).forEach(bordId-> bordService.deleteBordereau(bordId));
        }
        return facultativeMapper.mapToFacultativeDetailsResp(affaire);
    }

    @Override
    public Page<FacultativeListResp> searchFacultative(String key, Pageable pageable) {
        return null;
    }

    @Override
    public FacultativeDetailsResp renewAffaire(RenewFacultativeReq dto) {
        Affaire oldAffaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        boolean isCourtier = jwtService.UserIsCourtier();
        entityManager.detach(oldAffaire);
        Affaire newAffaire = new Affaire();
        BeanUtils.copyProperties(oldAffaire, newAffaire, "affId");
        BeanUtils.copyProperties(dto, newAffaire, "affId");
        newAffaire.setAffSource(oldAffaire);
        newAffaire.setAffCode(null);
        newAffaire.setExercice(new Exercice(oldAffaire.getExercice().getExeCode() + 1));
        newAffaire.setStatut(isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode));
        newAffaire = affRepo.save(newAffaire);
        String newAffCode = this.generateAffCode(newAffaire.getAffId());
        newAffaire.setAffCode(newAffCode);

        logService.logg(AffaireActions.RENOUVELER_FAC, null, newAffaire, SynchronReTables.AFFAIRE);
        return facultativeMapper.mapToFacultativeDetailsResp(newAffaire);
    }

    @Override
    public EtatComptableAffaire getEtatComptable(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return affMapper.mapToEtatComptableAffaire(affaire);
    }

    @Override @Transactional
    public void setAsNonRealisee(Long affId) {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        affaire.setAffStatutCreation("NON_REALISEE");
        affRepo.save(affaire);
        logService.logg(AffaireActions.SET_AS_NON_REALISEE, oldAffaire, affaire, SynchronReTables.AFFAIRE);
    }

    @Override
    public void setAsRealisee(Long affId){
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAffaire = affCopier.copy(affaire);
        affaire.setAffStatutCreation("REALISEE");
        affRepo.save(affaire);
        logService.logg(AffaireActions.SET_AS_REALISEE, oldAffaire, affaire, SynchronReTables.AFFAIRE);
    }

    @Override
    public boolean senNoteDebitFac(Long affId) throws Exception
    {
        Cedante ced = cedRepo.getCedanteByAffId(affId);
        if(!bordRep.noteDebExistsByAffId(affId)) bordService.createNoteDebit(affId);
        mailSenderService.sendNoteDebitFacEmail(synchronreEmail, ced.getCedEmail(),affId);
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.ENVOYER_NOTE_DEBIT_A_LA_CEDANTE, affId, MAIL.staCode, null));
        return true;
    }

    @Override
    public Base64FileDto printNoteCreditFac(Long affId, Long cesId) throws Exception
    {

        byte[] bytes = this.reportService.generateNoteCreditFac(affId, cesId);
        String base64String = Base64ToFileConverter.convertBytesToBase64String(bytes);
        Base64FileDto dto = new Base64FileDto(base64String, bytes);
        return dto;
    }

    @Override
    public Page<FacultativeListResp> transmettreAffaireAuSouscripteur(Long affId, Pageable pageable)
    {
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
    public Page<FacultativeListResp> retournerAffaireALaCedante(MvtReq dto, Pageable pageable)
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
    public Page<FacultativeListResp> validerAffaire(Long affId, Pageable pageable)
    {
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
        return besFac.compareTo(UN) <= 0;
    }

    @Transactional
    @Override
    public Page<FacultativeListResp> supprimerAffaire(Long affId, Pageable pageable) {
            // Vérifie si l'affaire existe
            Affaire affaire = affRepo.findByAffId(affId)
                    .orElseThrow(() -> new AppException("Affaire non trouvée avec l'ID : " + affId));
            // Étape 0 : Supprimer les documents liés aux règlements
            affRepo.deleteDocumentsByAffaireId(affId);
            // Étape 1 : Supprimer les règlements
            affRepo.deleteReglementsByAffaireId(affId);
            // Étape 2 : Supprimer les mouvements liés à l'affaire
            affRepo.deleteMouvementsByAffaireId(affId);
            // Étape 3 : Supprimer les détails de bordereaux
            List<Long> bordereauIds = affRepo.findBordereauxIdsByAffaireId(affId);
            if (!bordereauIds.isEmpty()) {
                affRepo.deleteDetailBordereauxByBordereauIds(bordereauIds);
            }
            // Étape 4 : Supprimer les bordereaux
            affRepo.deleteBordereauxByAffaireId(affId);
            // Étape 5 : Supprimer les mouvements liés aux placements (repartitions)
            List<Long> repIds = affRepo.findRepartitionIdsByAffaireId(affId);
            if (!repIds.isEmpty()) {
                affRepo.deleteMouvementsByRepartitionIds(repIds);
            }
            // Étape 6 : Supprimer les répartitions
            affRepo.deleteRepartitionsByAffaireId(affId);
            // Étape 7 : Supprimer l'affaire
            affRepo.deleteAffaireById(affId);
           //Rechargement de la liste des affaires
        Page<FacultativeListResp> facPages = affRepo.searchAffaires("", null, null,
                jwtService.getConnectedUserCedId(),
                Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exoService.getExerciceCourant().getExeCode(), pageable);
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, pageable, facPages.getTotalElements());
    }
}
