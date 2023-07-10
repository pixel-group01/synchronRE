package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontantEnLettres;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SinMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@Service @RequiredArgsConstructor
public class ServiceSinistreImpl implements IServiceSinistre
{
    private final ObjectCopier<Sinistre> sinCopier;
    private final ILogService logService;
    private final SinRepo sinRepo;
    private final SinMapper sinMapper;
    private final IJwtService jwtService;
    private final AffaireRepository affRepo;
    private final IServiceMouvement mvtService;
    private final EmailSenderService mailSenderService;
    private  final CessionnaireRepository cesRepo;
    private final IServiceCalculsComptablesSinistre sinComptaService;
    private final TypeRepo typeRepo;
    private final RepartitionRepository repRepo;
    private final String FAC_UNIQUE_CODE = "FAC";
    private final String TRAITE_UNIQUE_CODE = "TRAITE";
    @Value("${spring.mail.username}")
    private String synchronreEmail;
    private final IserviceExercie exeService;

    private void doRepartitionSinistre(Affaire aff, Long sinId, CessionnaireListResp ces)
    {
        Long cesId = ces.getCesId();
        BigDecimal repCaptital = sinComptaService.calculateMtAPayerBySinAndCes(sinId, cesId);
        Repartition sinRep = new Repartition();

        sinRep.setSinistre(new Sinistre(sinId));
        sinRep.setRepStatut(true);
        sinRep.setRepCapital(repCaptital);
        sinRep.setRepCapitalLettre(ConvertMontantEnLettres.convertir(repCaptital.doubleValue()));
        sinRep.setType(typeRepo.findByUniqueCode("REP_SIN"));
        sinRep.setRepInterlocuteur(ces.getCesInterlocuteur());

        Repartition placement = repRepo.findByAffaireAndTypeRepAndCesId(aff.getAffId(), "REP_PLA", cesId);
        if(placement == null) throw new AppException("Placement introuvable");
        sinRep.setRepTaux(repRepo.getTauRep(placement.getRepId()));
        sinRep.setCessionnaire(new Cessionnaire(cesId));
        repRepo.save(sinRep);
    }

    @Override @Transactional
    public SinistreDetailsResp createSinistre(CreateSinistreReq dto) throws UnknownHostException
    {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de déclarer un sinistre sur une affaire non réalisée ou en instance");
        boolean isCourtier = jwtService.UserIsCourtier();
        Sinistre sinistre = sinMapper.mapToSinistre(dto);
        Statut sinStatut = isCourtier ? new Statut(SAISIE_CRT.staCode) : new Statut(SAISIE.staCode);
        sinistre.setStatut(sinStatut);
        sinistre = sinRepo.save(sinistre);
        Long sinId = sinistre.getSinId();
        cesRepo.findByAffId(dto.getAffId()).forEach(ces->this.doRepartitionSinistre(affaire, sinId, ces));
        sinistre.setSinCode(this.generateSinCode(sinistre.getSinId()));
        BigDecimal mtTotSinPlacement = sinComptaService.calculateMtTotalCessionnairesSurSinistre(sinId);
        sinistre.setSinMontantTotPlacement(mtTotSinPlacement);
        sinistre.setSinMontantTotPlacementLettre(ConvertMontantEnLettres.convertir(mtTotSinPlacement.doubleValue()));
        mvtService.createMvtSinistre(new MvtReq(sinistre.getSinId(), sinStatut.getStaCode(), null));
        logService.logg(SynchronReActions.CREATE_SINISTRE, null, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    public String generateSinCode(Long sinId)
    {
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));
        String affCode = affRepo.getAffCode(sinistre.getAffaire().getAffId());
        return "SIN." + affCode + "." +
                String.format("%05d", sinId);
    }

    @Override
    public SinistreDetailsResp updateSinistre(UpdateSinistreReq dto) throws UnknownHostException
    {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        Sinistre sinistre = sinRepo.findById(dto.getSinId()).orElseThrow(()->new AppException("Sinistre introuvable"));
        Sinistre oldSin = sinCopier.copy(sinistre);
        BeanUtils.copyProperties(dto, sinistre);
        sinistre = sinRepo.save(sinistre);
       if(oldSin.getSinMontant100().compareTo(dto.getSinMontant100()) != 0 || oldSin.getSinMontantHonoraire().compareTo(dto.getSinMontantHonoraire()) != 0)
          cesRepo.findByAffId(dto.getAffId()).forEach(ces->this.doRepartitionSinistre(affaire, dto.getSinId(), ces));
        logService.logg(SynchronReActions.UPDATE_SINISTRE, oldSin, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    @Override
     public  Page<SinistreDetailsResp> transmettreSinistreAuSouscripteur(Long sinId, int returnPageSize) throws UnknownHostException
     {
         boolean isCourtier = jwtService.UserIsCourtier() ;
         String newStatut = isCourtier ? SAISIE_CRT.staCode : TRANSMIS.staCode;
         mvtService.createMvtSinistre(new MvtReq(sinId, newStatut, null));
         Page<SinistreDetailsResp> sinPages = this.searchSinFacSaisiByCedante("", PageRequest.of(0, returnPageSize));
         return sinPages;
     }
    @Override
    public  Page<SinistreDetailsResp> transmettreSinistreAuValidateur(Long sinId, int returnPageSize) throws UnknownHostException
    {
        mvtService.createMvtSinistre(new MvtReq(sinId, EN_ATTENTE_DE_VALIDATION.staCode, null));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerALaCedante(Long sinId, int returnPageSize)
    {
        mvtService.createMvtSinistre(new MvtReq(sinId, RETOURNE.staCode, null));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerAuSouscripteur(Long sinId, int returnPageSize)
    {
        mvtService.createMvtSinistre(new MvtReq(sinId, RETOURNER_VALIDATEUR.staCode, null));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerAuValidateur(Long sinId, int returnPageSize)
    {
        mvtService.createMvtSinistre(new MvtReq(sinId, RETOURNER_COMPTABLE.staCode, null));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> valider(Long sinId, int returnPageSize) throws UnknownHostException
    {
        mvtService.createMvtSinistre(new MvtReq(sinId, VALIDE.staCode, null));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }


    void envoyerNoteCessionSinistreEtNoteDebit(Long sinId) throws UnknownHostException {
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de transmettre la note de cession de ce placement car l'affaire est non réalisée ou en instance");
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));

        List<Cessionnaire> cessionnaires = sinRepo.getCessionnaireBySinId(sinId);
        if(cessionnaires == null || cessionnaires.size() == 0) throw new AppException("Aucun cessionnaire sur cette affaire");
        cessionnaires.forEach(ces->
        {
            try {
                mailSenderService.sendNoteCessionSinistreEmail(synchronreEmail, ces.getCesEmail(), ces.getCesInterlocuteur(), affaire.getAffCode(), sinId, "Note de cession sinistre");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sinistre.setStatut(new Statut(EN_ATTENTE_DE_PAIEMENT.staCode));
        });


        mvtService.createMvtSinistre(new MvtReq(sinId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
    logService.saveLog(SinistreActions.TRANSMETTRE_NOTE_CESSION_SINISTRE_ET_NOTE_DEBIT);
    }

    @Override
    public Page<SinistreDetailsResp> searchSinistre(String key, List<String> staCodes, Pageable pageable)
    {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, null, FAC_UNIQUE_CODE, staCodes == null || staCodes.isEmpty() ? SinStatutGroup.tabAllSinistres : staCodes, pageable);
        return sinPage;
    }

    @Override
    public Page<SinistreDetailsResp> searchSinFacSuivi(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabAllSinistres, pageable);
        return sinPage;
    }

    @Override
    public Page<SinistreDetailsResp> searchSinFacArch(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabArchive, pageable);
        return sinPage;
    }

    @Override
   public Page<SinistreDetailsResp> searchSinFacSaisiByCedante(String key, Pageable pageable)
   {
       Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabSaisie, pageable);
       return sinPage;
   }

    @Override
    public Page<SinistreDetailsResp> searchSinFacTransmiByCedante(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabAtrans, pageable);
        return sinPage;
    }

    @Override
    public Page<SinistreDetailsResp> searchSinFacAttenteValidation(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabAValidation, pageable);
        return sinPage;
    }

    @Override
    public Page<SinistreDetailsResp> searchSinFacEnReglement(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabEnReglement, pageable);
        return sinPage;
    }

    @Override
    public Page<SinistreDetailsResp> searchSinFacSolde(String key, Pageable pageable) {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, jwtService.getConnectedUserCedId(), FAC_UNIQUE_CODE, SinStatutGroup.tabSolde, pageable);
        return sinPage;
    }

    @Override
    public EtatComptableSinistreResp getEtatComptable(Long sinId) {
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));
        sinistre.setAffaire(affRepo.findById(sinistre.getAffaire().getAffId()).orElseThrow(()->new AppException("Affaire introuvable")));
        return sinMapper.mapToEtatComptableSinistre(sinistre);
    }
}
