package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SinMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
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
import java.math.RoundingMode;
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
    private final String FAC_UNIQUE_CODE = "FAC";
    private final String TRAITE_UNIQUE_CODE = "TRAITE";
    @Value("${spring.mail.username}")
    private String synchronreEmail;
    private final CedRepo cedRepo;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final IserviceRepartition repService;



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
        cesRepo.findByAffId(dto.getAffId()).forEach(ces->repService.doRepartitionSinistre(affaire, sinId, ces));
        sinistre.setSinCode(this.generateSinCode(sinistre.getSinId()));
        BigDecimal mtTotSinPlacement = sinComptaService.calculateMtTotalCessionnairesSurSinistre(sinId);
        sinistre.setSinMontantTotPlacement(mtTotSinPlacement);
        sinistre.setSinMontantTotPlacementLettre(ConvertMontant.numberToLetter(mtTotSinPlacement == null ? BigDecimal.ZERO : mtTotSinPlacement.setScale(0, RoundingMode.HALF_UP)));
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.CREATE_SINISTRE, sinistre.getSinId(), sinStatut.getStaCode(), null));
        logService.logg(SinistreActions.CREATE_SINISTRE, null, sinistre, SynchronReTables.SINISTRE);
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
          cesRepo.findByAffId(dto.getAffId()).forEach(ces->repService.doRepartitionSinistre(affaire, dto.getSinId(), ces));
        logService.logg(SinistreActions.UPDATE_SINISTRE, oldSin, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    @Override
     public  Page<SinistreDetailsResp> transmettreSinistreAuSouscripteur(Long sinId, int returnPageSize) throws UnknownHostException {
         boolean isCourtier = jwtService.UserIsCourtier() ;
         String newStatut = isCourtier ? SAISIE_CRT.staCode : TRANSMIS.staCode;
         mvtService.createMvtSinistre(new MvtReq(SinistreActions.TRANSMETTRE_AU_SOUSCRIPTEUR, sinId, newStatut, null));
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));
        userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerEmailTransmissionSinistreAuSouscripteur(u.getEmail(), u.getFirstName(), affaire.getCedante().getCedNomFiliale()));
         Page<SinistreDetailsResp> sinPages = this.searchSinFacSaisiByCedante("", PageRequest.of(0, returnPageSize));
         return sinPages;
     }
    @Override
    public  Page<SinistreDetailsResp> transmettreSinistreAuValidateur(Long sinId, int returnPageSize) throws UnknownHostException {
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.TRANSMETTRE_AU_VALIDATEUR, sinId, EN_ATTENTE_DE_VALIDATION.staCode, null));
        userRepo.getUserByTypeFunction("TYF_VAL").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDeValidationAuValidateur(u.getEmail(), u.getFirstName()));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacTransmiByCedante("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerALaCedante(MvtReq dto,  int returnPageSize) throws UnknownHostException {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.RETOURNER_A_CEDANTE, dto.getObjectId(), RETOURNE.staCode, motif));
        String sinCode = sinRepo.getSinCode(dto.getObjectId());
        Long cedId = cedRepo.getCedIdByAffId(dto.getObjectId());
        userRepo.getUserByTypeFunctionAndCedId("TYF_SAI_CED", cedId).forEach(u->emailSenderService.envoyerEmailRetourSinistreALaCedante(u.getEmail(), u.getFirstName(), sinCode, dto.getMvtObservation()));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacTransmiByCedante("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerAuSouscripteur(MvtReq dto, int returnPageSize) throws UnknownHostException {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.RETOURNER_AU_SOUSCRIPTEUR, dto.getObjectId(), RETOURNER_VALIDATEUR.staCode, motif));
        String sinCode = sinRepo.getSinCode(dto.getObjectId());
        Long cedId = cedRepo.getCedIdByAffId(dto.getObjectId());
        userRepo.getUserByTypeFunction("TYF_SOUS").forEach(u->emailSenderService.envoyerEmailRetourSinistreAuSouscripteur(u.getEmail(), u.getFirstName(), sinCode, dto.getMvtObservation()));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> retournerAuValidateur(MvtReq dto, int returnPageSize) throws UnknownHostException {
        String motif = dto.getMvtObservation();
        if(motif == null || motif.trim().equals("")) throw new AppException("Veuillez préciser le motif de retour");
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.RETOURNER_AU_VALIDATEUR, dto.getObjectId(), RETOURNER_COMPTABLE.staCode, motif));
        String sinCode = sinRepo.getSinCode(dto.getObjectId());
        Long cedId = cedRepo.getCedIdByAffId(dto.getObjectId());
        userRepo.getUserByTypeFunctionAndCedId("TYF_VAL", cedId).forEach(u->emailSenderService.envoyerEmailRetourSinistreAuValidateur(u.getEmail(), u.getFirstName(), sinCode, dto.getMvtObservation()));
        Page<SinistreDetailsResp> sinPages = this.searchSinFacEnReglement("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }
    @Override
    public  Page<SinistreDetailsResp> valider(Long sinId, int returnPageSize) throws Exception
    {
        mvtService.createMvtSinistre(new MvtReq(SinistreActions.VALIDER_SINISTRE, sinId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
        userRepo.getUserByTypeFunction("TYF_COMPTA").forEach(u->emailSenderService.envoyerEmailSinistreEnAttenteDePaiementAuComptable(u.getEmail(), u.getFirstName()));
        //envoyerNoteCessionSinistreEtNoteDebit(sinId);
        Page<SinistreDetailsResp> sinPages = this.searchSinFacAttenteValidation("", PageRequest.of(0, returnPageSize));
        return sinPages;
    }

    /*@Override
    public void envoyerNoteCessionSinistreEtNoteDebit(Long sinId) throws UnknownHostException {
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de transmettre la note de cession de ce placement car l'affaire est non réalisée ou en instance");
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));

        List<Cessionnaire> cessionnaires = sinRepo.getCessionnaireBySinId(sinId);
        if(cessionnaires == null || cessionnaires.size() == 0) throw new AppException("Aucun cessionnaire sur cette affaire");
        cessionnaires.forEach(ces->
        {
            try {
                mailSenderService.sendNoteCessionSinistreEmail(synchronreEmail, ces.getCesEmail(), ces.getCesInterlocuteur(), affaire.getAffCode(), sinId, ces.getCesId(), "Note de cession sinistre");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sinistre.setStatut(new Statut(EN_ATTENTE_DE_PAIEMENT.staCode));
        });

        mvtService.createMvtSinistre(new MvtReq(sinId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
    logService.saveLog(SinistreActions.TRANSMETTRE_NOTE_CESSION_SINISTRE_ET_NOTE_DEBIT);
    }*/

    public void envoyerNoteCessionSinistreEtNoteDebit(Long sinId) throws Exception {
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));

        Cedante cedante = cedRepo.getCedanteByAffId(affaire.getAffId());
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de transmettre la note de cession de ce placement car l'affaire est non réalisée ou en instance");
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));
        mailSenderService.sendNoteDebitSinistreEmail(synchronreEmail, cedante.getCedEmail(), cedante.getCedSigleFiliale(), affaire.getAffId());

        List<Cessionnaire> cessionnaires = sinRepo.getCessionnaireBySinId(sinId);
        if(cessionnaires == null || cessionnaires.size() == 0) throw new AppException("Aucun cessionnaire sur cette affaire");
        cessionnaires.forEach(ces->
        {
            try {
                mailSenderService.sendNoteCessionSinistreEmail(synchronreEmail,affaire.getAffCode(), sinId, ces.getCesId(), "Note de cession sinistre");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //sinistre.setStatut(new Statut(EN_ATTENTE_DE_PAIEMENT.staCode));
        });

        //mvtService.createMvtSinistre(new MvtReq(sinId, EN_ATTENTE_DE_PAIEMENT.staCode, null));
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
    public void envoyerNoteCessionSinistre(Long sinId, Long cesId) throws Exception {
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));
        mailSenderService.sendNoteCessionSinistreEmail(synchronreEmail, affaire.getAffCode(), sinId, cesId, "Note de cession sinistre");
    }

    @Override
    public void envoyerNoteDebitSinistre(Long sinId) throws Exception {
        Affaire affaire = sinRepo.getAffairedBySinId(sinId).orElseThrow(()->new AppException("Affaire introuvable"));
        Cedante cedante = cedRepo.getCedanteByAffId(affaire.getAffId());
        mailSenderService.sendNoteDebitSinistreEmail(synchronreEmail, cedante.getCedEmail(), cedante.getCedSigleFiliale(), sinId);
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
