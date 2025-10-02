package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TraiteNPMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.ReconduireTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheCedanteIdsDto;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import com.pixel.synchronre.sychronremodule.model.views.VTrancheCategorieRepo;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCategorie;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceLimiteSouscription;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRisque;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSousLimite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTraiteNP;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReconstitution;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@Service @RequiredArgsConstructor
public class ServiceTraiteNPImpl implements IServiceTraiteNP
{
    private final TraiteNPRepository traiteNPRepo;
    private final TraiteNPMapper traiteNPMapper;
    private final ILogService logService;
    private final ObjectCopier<TraiteNonProportionnel> traiteNPCopier;
    private final IServiceCalculsComptablesTraite traiteComptaService;
    private final TrancheCedanteRepository tcRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final IJwtService jwtService;
    private final IServiceMouvement mvtService;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final IserviceExercie exeService;
    private final IServiceTerritorialite territorialiteService;
    private final TerritorialiteDetailsRepository territorialiteDetailsRepository;
    private final TerritorialiteRepository territorialiteRepository;
    private final IServiceRisque risqueService;
    private final RisqueCouvertRepository risqueRepo;
    private final RisqueDetailsRepo risqueDetailsRepo;
    private final IServiceCategorie categorieService;
    private final IServiceTranche trancheService;
    private final CategorieRepository categorieRepository;
    private final TrancheRepository trancheRepository;
    private final VTrancheCategorieRepo trancheCatRepo;
    private final ITrancheCedanteService trancheCedanteService;
    private final IServiceLimiteSouscription limiteSouscriptionService;
    private final IServiceSousLimite sousLimiteService;
    private final IserviceReconstitution reconstitutionService;
    private final LimiteSouscriptionRepository limiteSouscriptionRepository;
    private final SousLimiteRepository sousLimiteRepository;
    private final ReconstitutionRepository reconstitutionRepo;
    private final IServiceRepartitionTraiteNP repartitionTraiteNPService;
    private final RepartitionTraiteRepo repartitionTraiteRepo;
    private final RisqueCouvertRepository risqueCouvertRepo;

    @Override @Transactional
    public TraiteNPResp create(CreateTraiteNPReq dto)
    {
        boolean isCourtier = jwtService.UserIsCourtier();
        TraiteNonProportionnel traiteNP = traiteNPMapper.mapToTraiteNP(dto);
        Statut statut = isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode);
        traiteNP.setStatut(statut);
        traiteNP = traiteNPRepo.save(traiteNP);
        logService.logg(TraitesActions.CREATE_TNP, null, traiteNP, "TraiteNonProportionnel");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.CREATE_TNP, traiteNP.getTraiteNpId(), traiteNP.getStatut().getStaCode(), null));
        TraiteNPResp traiteNPResp = traiteNPRepo.findTraiteById(traiteNP.getTraiteNpId());
        return traiteNPResp;
    }

    @Override
    public Page<TraiteNPResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? Collections.singletonList("SAI-CRT") : staCodes;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, fncId, userId, cedId, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public List<TraiteNPResp> list(Long cedId, List<String> staCodes, Long exeCode)
    {
        staCodes = staCodes == null || staCodes.isEmpty() ? Collections.singletonList("SAI-CRT") : staCodes;
        return traiteNPRepo.getList(cedId, staCodes, exeCode);
    }

    @Override @Transactional
    public TraiteNPResp update(UpdateTraiteNPReq dto)
    {
        TraiteNonProportionnel traiteNP = traiteNPRepo.findById(dto.getTraiteNpId()).orElseThrow(()->new AppException("Traité introuvable"));
        TraiteNonProportionnel oldTraiteNP = traiteNPCopier.copy(traiteNP);
        BeanUtils.copyProperties(dto, traiteNP);
        traiteNP.setTraiEcerciceRattachement(EnumUtils.getEnum(EXERCICE_RATTACHEMENT.class, dto.getTraiEcerciceRattachement()));
        traiteNP.setTraiPeriodicite(EnumUtils.getEnum(PERIODICITE.class, dto.getTraiPeriodicite()));
        traiteNP.setNature(new Nature(dto.getNatCode()));
        traiteNP.setTraiDevise(new Devise(dto.getDevCode()));
        traiteNP.setTraiCompteDevise(new Devise(dto.getTraiCompteDevCode()));
        traiteNP.setCourtierPlaceur(new Cessionnaire(dto.getCourtierPlaceurId()));
        traiteNP.setTraiTauxCourtier(dto.getTraiTauxCourtier());
        traiteNP.setTraiTauxCourtierPlaceur(dto.getTraiTauxCourtierPlaceur());
        traiteNP = traiteNPRepo.save(traiteNP);
        if((dto.getTraiTauxCourtier() == null && traiteNP.getTraiTauxCourtier() != null) || dto.getTraiTauxCourtier().compareTo(traiteNP.getTraiTauxCourtier()) != 0)
        {
            eventPublisher.publishEvent(new SimpleEvent<TraiteNonProportionnel>(this, "Modifier du taux courtier sur un traité", traiteNP));
        }
        if((dto.getTraiTauxCourtierPlaceur() == null && traiteNP.getTraiTauxCourtierPlaceur() != null) || dto.getTraiTauxCourtierPlaceur().compareTo(traiteNP.getTraiTauxCourtierPlaceur()) != 0)
        {
            eventPublisher.publishEvent(new SimpleEvent<TraiteNonProportionnel>(this, "Modifier du taux courtier placeur sur un traité", traiteNP));
        }
        logService.logg("Modification d'un traité non proportionnel", oldTraiteNP, traiteNP, "TraiteNonProportionnel");
        TraiteNPResp traiteNPResp = traiteNPMapper.mapToTraiteNPResp(traiteNP);
        return traiteNPResp;
    }

    @Override
    public UpdateTraiteNPReq edit(Long traiId) {
        return traiteNPRepo.getEditDtoById(traiId);
    }

    @Override
    public TraiteNPResp getTraiteDetails(Long traiId)
    {
        TraiteNPResp details = traiteNPRepo.findTraiteById(traiId);
        if(details == null) return null;
        details.setTraiTauxDejaPlace(traiteComptaService.calculateTauxDejaPlace(traiId));
        details.setTraiTauxRestantAPlacer(traiteComptaService.calculateTauxRestantAPlacer(traiId));
        PmdGlobalResp pmdGlobalResp = tcRepo.getPmdGlobal(traiId);
        if(pmdGlobalResp == null) return details;
        details.setTraiPmd(pmdGlobalResp.getTraiPmd());
        details.setTraiPmdCourtier(pmdGlobalResp.getTraiPmdCourtier());
        details.setTraiPmdCourtierPlaceur(pmdGlobalResp.getTraiPmdCourtierPlaceur());
        details.setTraiPmdNette(pmdGlobalResp.getTraiPmdNette());
        return details;
    }

    @Override
    public Page<TraiteNPResp> searchSaisieSouscripteur(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabSaisieSouscripteur : staCodes;
        Exercice exeCourant = exeService.getExerciceCourant();
        exeCode = exeCode == null ? exeCourant.getExeCode() : exeCode;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> enCoursDeValidation(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabEncoursValidattion : staCodes;
        Exercice exeCourant = exeService.getExerciceCourant();
        exeCode = exeCode == null ? exeCourant.getExeCode() : exeCode;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> enCoursDeReglement(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabEnReglement : staCodes;
        Exercice exeCourant = exeService.getExerciceCourant();
        exeCode = exeCode == null ? exeCourant.getExeCode() : exeCode;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> solde(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabSolde : staCodes;
        Exercice exeCourant = exeService.getExerciceCourant();
        exeCode = exeCode == null ? exeCourant.getExeCode() : exeCode;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public Page<TraiteNPResp> archive(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);
        staCodes = staCodes == null || staCodes.isEmpty() ? TraiteStatutGroup.tabArchive : staCodes;
        Exercice exeCourant = exeService.getExerciceCourant();
        exeCode = exeCode == null ? exeCourant.getExeCode() : exeCode;
        Page<TraiteNPResp> traiteNPResps = traiteNPRepo.search(key, jwtService.getConnectedUserCedId(), null, null, staCodes, exeCode, pageable);
        return traiteNPResps;
    }

    @Override
    public void transmettreTraiteAuValidateur(Long traiteNpId, int returnPageSize) {
            mvtService.createMvtTraite(new MvtReq(TraitesActions.TRANSMETTRE_AU_VALIDATEUR, traiteNpId, EN_ATTENTE_DE_VALIDATION.staCode, null));
            userRepo.getUserByTypeFunction("TYF_VAL").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDeValidationAuValidateur(u.getEmail(), u.getFirstName()));
    }

    @Override
    public void retournerAuSouscripteur(MvtReq dto) {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.RETOURNER_AU_SOUSCRIPTEUR, dto.getObjectId(), RETOURNER_VALIDATEUR.staCode, motif));
        Optional<TraiteNonProportionnel> traiteNpId = traiteNPRepo.findById(dto.getObjectId());
        //userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerEmailRetourSinistreAuSouscripteur(u.getEmail(), u.getFirstName(), traiteNpId, dto.getMvtObservation()));
    }

    @Override
    public void retournerAuValidateur(MvtReq dto)
    {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtTraite(new MvtReq(TraitesActions.RETOURNER_AU_VALIDATEUR, dto.getObjectId(), RETOURNER_COMPTABLE.staCode, motif));
        Optional<TraiteNonProportionnel> traiteNpId = traiteNPRepo.findById(dto.getObjectId());
        //userRepo.getUserByTypeFunctionAndCedId("TYF_VAL", cedId).forEach(u->emailSenderService.envoyerEmailRetourSinistreAuValidateur(u.getEmail(), u.getFirstName(), sinCode, dto.getMvtObservation()));
    }

    @Override
    public void valider(Long traiteNpId) {
        mvtService.createMvtTraite(new MvtReq(TraitesActions.VALIDER_TRAITE, traiteNpId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
        //userRepo.getUserByTypeFunction("TYF_COMPTA").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDePaiementAuComptable(u.getEmail(), u.getFirstName()));
        //envoyerNoteCessionSinistreEtNoteDebit(sinId);
    }

    private TraiteNPResp reconduireTraites(ReconduireTraiteReq dto)
    {
        CreateTraiteNPReq createTraiteNPReq = traiteNPRepo.findCreateTraiteNPReqById(dto.getTraiteNpId()).orElseThrow(()->new AppException("Traité introuvable " + dto.getTraiteNpId()));
        createTraiteNPReq.setTraiReference(dto.getTraiReference());
        createTraiteNPReq.setTraiNumero(dto.getTraiNumero());
        return this.create(createTraiteNPReq);
    }
    @Override @Transactional
    public TraiteNPResp reconduireTraiteAndCondtions(ReconduireTraiteReq dto)
    {
        Long traiteNpId = dto.getTraiteNpId();
        TraiteNPResp newTraite = this.reconduireTraites(dto);
        Long newTraiteNpId = newTraite.getTraiteNpId();

        this.reconduireTerritorialites(newTraiteNpId, traiteNpId);
        this.reconduireRisqueCouverts(newTraiteNpId, traiteNpId);
        this.reconduireCategories(newTraiteNpId, traiteNpId);
        this.reconduireTranche(newTraiteNpId, traiteNpId);
        this.reconduireAssiettePrime(newTraiteNpId, traiteNpId);
        this.reconduireLimitesSouscription(newTraiteNpId, traiteNpId);
        this.reconduireSousLimites(newTraiteNpId, traiteNpId);
        this.reconduireReconstitutions(newTraiteNpId, traiteNpId);
        this.reconduirePlacement(newTraiteNpId, traiteNpId);
        return newTraite;
    }

    private void reconduireTerritorialites(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all territorialities for the original treaty
        List<Territorialite> territorialites = territorialiteRepository.findByTnpId(traiteNpId);

        // For each territoriality, create a new one for the new treaty
        territorialites.forEach(territorialite ->
        {
            // Get country codes and organization codes for the territoriality
            List<String> paysCodes = territorialiteDetailsRepository.getPaysCodesByTerrId(territorialite.getTerrId());
            paysCodes = paysCodes == null ? new ArrayList<>() : paysCodes.stream().filter(Objects::nonNull).distinct().toList();
            List<String> orgCodes = territorialiteDetailsRepository.getOrgCodesByTerrId(territorialite.getTerrId());
            orgCodes = orgCodes == null ? new ArrayList<>() : orgCodes.stream().filter(Objects::nonNull).distinct().toList();
            // Create a new territoriality for the new treaty
            TerritorialiteReq territorialiteReq = new TerritorialiteReq();
            territorialiteReq.setTerrLibelle(territorialite.getTerrLibelle());
            territorialiteReq.setTerrTaux(territorialite.getTerrTaux());
            territorialiteReq.setTerrDescription(territorialite.getTerrDescription());
            territorialiteReq.setPaysCodes(paysCodes);
            territorialiteReq.setOrgCodes(orgCodes);
            territorialiteReq.setTraiteNpId(newTraiteNpId);

            territorialiteService.create(territorialiteReq);
        });
    }

    private void reconduireRisqueCouverts(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all risk coverages for the original treaty
        List<RisqueCouvertResp> risqueCouvertList = risqueRepo.getRisqueList(traiteNpId);

        // For each risk coverage, create a new one for the new treaty
        risqueCouvertList.forEach(risqueCouvert -> {
            try {
                // Get sub-coverage IDs for the risk coverage
                List<Long> sousCouIds = risqueDetailsRepo.getSousCouIds(risqueCouvert.getRisqueId());

                // Create a new risk coverage for the new treaty
                CreateRisqueCouvertReq createRisqueCouvertReq = new CreateRisqueCouvertReq(
                        risqueCouvert.getCouId(),
                        sousCouIds,
                        risqueCouvert.getDescription(),
                        newTraiteNpId
                );

                risqueService.create(createRisqueCouvertReq);
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des risques couverts: " + e.getMessage());
            }
        });
    }

    private void reconduireCategories(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all categories for the original treaty
        List<CategorieResp> categorieList = categorieService.getCategorieList(traiteNpId);

        // For each category, create a new one for the new treaty
        categorieList.forEach(categorie -> {
            try {
                // Create a new category request for the new treaty
                CategorieReq categorieReq = new CategorieReq();
                categorieReq.setCategorieLibelle(categorie.getCategorieLibelle());
                categorieReq.setCategorieCapacite(categorie.getCategorieCapacite());
                categorieReq.setTraiteNpId(newTraiteNpId);

                // Get cedant IDs for the original category
                List<Long> cedIds = categorieRepository.getCedIdsByCatId(categorie.getCategorieId());
                categorieReq.setCedIds(cedIds);

                // Create the new category
                categorieService.create(categorieReq);
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des catégories: " + e.getMessage());
            }
        });
    }

    private void reconduireTranche(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all tranches for the original treaty
        List<TrancheResp> trancheList = trancheRepository.getTrancheList(traiteNpId);

        // For each tranche, create a new one for the new treaty
        trancheList.forEach(tranche -> {
            try {
                // Create a new tranche request for the new treaty
                TrancheReq trancheReq = new TrancheReq();
                trancheReq.setTrancheType(tranche.getTrancheType());
                trancheReq.setTrancheLibelle(tranche.getTrancheLibelle());
                trancheReq.setTranchePriorite(tranche.getTranchePriorite());
                trancheReq.setTranchePorte(tranche.getTranchePorte());
                trancheReq.setTrancheTauxPrime(tranche.getTrancheTauxPrime());
                trancheReq.setRisqueId(tranche.getRisqueId());
                trancheReq.setTraiteNpId(newTraiteNpId);

                // Get category IDs for the original tranche
                List<Long> oldTraiteCatIds = trancheCatRepo.getCatIdsByTrancheId(tranche.getTrancheId());
                List<Long> newTraiteCatIds = categorieRepository.findCorrespondingCatIdsByTnpIdsAndCatIdsIn(newTraiteNpId, oldTraiteCatIds);
                trancheReq.setCategorieIds(newTraiteCatIds);

                // Create the new tranche
                trancheService.create(trancheReq);
            } catch (Exception e) {
                e.printStackTrace();
                throw new AppException("Erreur lors de la reconduction des tranches: " + e.getMessage());
            }
        });
    }

    private void reconduireAssiettePrime(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all premium bases for the original treaty
        List<TrancheCedante> trancheCedantes = tcRepo.findByTnpId(traiteNpId);

        List<Tranche> newTranches = trancheRepository.findByTnpId(newTraiteNpId);

        // For each premium base, create a new one for the new treaty
        trancheCedantes.stream().filter(Objects::nonNull).forEach(trancheCedante -> {
            Tranche oldTranche = trancheCedante.getTranche();
            try {
                // Get the tranche ID for the new treaty
                Long trancheCedanteId = trancheCedante.getTrancheCedanteId();
                TrancheCedanteIdsDto tcIdsDto = tcRepo.findTrancheIdAndCedIdByTrancheCedanteId(trancheCedanteId);
                Long oldTrancheId = tcIdsDto.getTrancheId();
                Long cedId = tcIdsDto.getCedId();

                // Find the corresponding tranche in the new treaty
                Tranche newTranche = newTranches.stream()
                        .filter(t -> t.getTrancheNumero().equals(oldTranche.getTrancheNumero()))
                        .findFirst()
                        .orElse(null);

                if (newTranche != null)
                {
                    // Create a new premium base request for the new treaty
                    TrancheCedanteReq trancheCedanteReq = new TrancheCedanteReq();
                    trancheCedanteReq.setTraiteNpId(newTraiteNpId);
                    trancheCedanteReq.setCedId(cedId);

                    // Create a TranchePrimeDto with the same properties as the original
                    TranchePrimeDto tranchePrimeDto = new TranchePrimeDto();
                    tranchePrimeDto.setTrancheId(newTranche.getTrancheId());
                    tranchePrimeDto.setTrancheLibelle(newTranche.getTrancheLibelle());
                    tranchePrimeDto.setAssiettePrime(trancheCedante.getAssiettePrime());
                    tranchePrimeDto.setAssiettePrimeRealisee(trancheCedante.getAssiettePrimeRealsee());
                    tranchePrimeDto.setTrancheTauxPrime(newTranche.getTrancheTauxPrime());
                    tranchePrimeDto.setCedId(cedId);
                    tranchePrimeDto.setTraiteNpId(newTraiteNpId);
                    tranchePrimeDto.setChanged(true);

                    // Add the TranchePrimeDto to the request
                    trancheCedanteReq.setTranchePrimeDtos(Collections.singletonList(tranchePrimeDto));

                    // Save the new premium base
                    trancheCedanteService.save(trancheCedanteReq);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AppException("Erreur lors de la reconduction des assiettes de prime: " + e.getMessage());
            }
        });
    }

    private void reconduireLimitesSouscription(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all subscription limits for the original treaty
        List<LimiteSouscription> limiteSouscriptions = limiteSouscriptionRepository.findByTnpId(traiteNpId);

        // For each subscription limit, create a new one for the new treaty
        limiteSouscriptions.forEach(ls -> {
            try {
                // Get the edit DTO for the original subscription limit
                LimiteSouscriptionReq limiteSouscriptionReq = limiteSouscriptionService.edit(ls.getLimiteSouscriptionId());
                Categorie oldTraiteCat = categorieRepository.findById(ls.getCategorie().getCategorieId()).orElseThrow(()->new AppException("Categorie introuvable " + ls.getCategorie().getCategorieId()));
                RisqueCouvert oldTraiteRisque = risqueCouvertRepo.findById(ls.getRisqueCouvert().getRisqueId()).orElseThrow(()->new AppException("Risque introuvable " + ls.getRisqueCouvert().getRisqueId()));
                Long catId = categorieRepository.findCatIdByTnpIdAndLibelleAndCapacite(newTraiteNpId, oldTraiteCat.getCategorieLibelle(), oldTraiteCat.getCategorieCapacite());
                Long risqueId = risqueCouvertRepo.findRisqueIdByTnpIdAndCouverture(newTraiteNpId, oldTraiteRisque.getCouverture().getCouId());
                List<Long> couIds = risqueCouvertRepo.getActiviteIdsByRisqueId(oldTraiteRisque.getRisqueId());
                // Create a new subscription limit request for the new treaty
                // We need to set the ID to null to create a new entity instead of updating an existing one
                limiteSouscriptionReq.setLimiteSouscriptionId(null);
                limiteSouscriptionReq.setCategorieId(catId);
                limiteSouscriptionReq.setRisqueId(risqueId);


                limiteSouscriptionReq.setCouIds(couIds);

                // Create the new subscription limit
                limiteSouscriptionService.create(limiteSouscriptionReq);
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des limites de souscription: " + e.getMessage());
            }
        });
    }

    private void reconduireSousLimites(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all subscription sub-limits for the original treaty
        List<SousLimite> sousLimites = sousLimiteRepository.findByTnpId(traiteNpId);

        // For each subscription sub-limit, create a new one for the new treaty
        sousLimites.forEach(sousLimite -> {
            try {
                // Create a new subscription sub-limit request for the new treaty
                CreateSousLimiteReq sousLimiteReq = new CreateSousLimiteReq();
                sousLimiteReq.setSousLimMontant(sousLimite.getSousLimMontant());
                sousLimiteReq.setCouId(sousLimite.getActivite().getCouId());
                sousLimiteReq.setTraiteNpId(newTraiteNpId);

                // Create the new subscription sub-limit
                sousLimiteService.create(sousLimiteReq);
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des sous-limites de souscription: " + e.getMessage());
            }
        });
    }

    private void reconduireReconstitutions(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all reconstitutions for the original treaty
        List<Reconstitution> reconstitutions = reconstitutionRepo.findByTnpId(traiteNpId);

        // Get all tranches for the new treaty
        List<Tranche> newTranches = trancheRepository.findByTnpId(newTraiteNpId);

        // For each reconstitution, create a new one for the new treaty
        reconstitutions.forEach(reconstitution -> {
            try {
                // Get the tranche ID for the original treaty
                Long oldTrancheId = reconstitution.getTranche().getTrancheId();

                // Find the corresponding tranche in the new treaty
                // We need to get the old tranche to find its number
                Tranche oldTranche = trancheRepository.findById(oldTrancheId).orElse(null);

                if (oldTranche != null) {
                    // Find the new tranche with the same number
                    Tranche newTranche = newTranches.stream()
                            .filter(t -> t.getTrancheNumero().equals(oldTranche.getTrancheNumero()))
                            .findFirst()
                            .orElse(null);

                    if (newTranche != null) {
                        // Create a new reconstitution request for the new treaty
                        ReconstitutionReq reconstitutionReq = new ReconstitutionReq();
                        reconstitutionReq.setNbrReconstitution(reconstitution.getNbrReconstitution());
                        reconstitutionReq.setModeCalculReconstitution(reconstitution.getModeCalculReconstitution());
                        reconstitutionReq.setTrancheId(newTranche.getTrancheId());
                        reconstitutionReq.setTraiteNpId(newTraiteNpId);

                        // Create the new reconstitution
                        reconstitutionService.create(reconstitutionReq);
                    }
                }
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des reconstitutions: " + e.getMessage());
            }
        });
    }

    private void reconduirePlacement(Long newTraiteNpId, Long traiteNpId)
    {
        // Get all placements for the original treaty
        List<PlacementTraiteNPReq> placements = repartitionTraiteRepo.findPlacementTraiteDtos(traiteNpId);

        // For each placement, create a new one for the new treaty
        placements.forEach(placement -> {
            try {
                // Create a new placement request for the new treaty
                PlacementTraiteNPReq placementReq = new PlacementTraiteNPReq();
                placementReq.setRepTaux(placement.getRepTaux());
                placementReq.setCesId(placement.getCesId());
                placementReq.setTraiteNpId(newTraiteNpId);
                placementReq.setAperiteur(placement.isAperiteur());

                // Create the new placement
                repartitionTraiteNPService.create(placementReq);
            } catch (Exception e) {
                throw new AppException("Erreur lors de la reconduction des placements: " + e.getMessage());
            }
        });
    }
}
