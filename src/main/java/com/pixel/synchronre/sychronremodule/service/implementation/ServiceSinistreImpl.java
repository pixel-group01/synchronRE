package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.notificationmodule.controller.services.EmailSenderService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.RepartitionActions;
import com.pixel.synchronre.sychronremodule.model.constants.SinistreActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SinMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
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
    @Value("${synchronre.email}")
    private String synchronreEmail;

    @Override @Transactional
    public SinistreDetailsResp createSinistre(CreateSinistreReq dto) throws UnknownHostException
    {
        Affaire affaire = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        String affStatutCrea = affRepo.getAffStatutCreation(affaire.getAffId());
        if(affStatutCrea == null || !affStatutCrea.equals("REALISEE"))  throw new AppException("Impossible de déclarer un sinistre sur une affaire non réalisée ou en instance");
        boolean isCourtier = jwtService.UserIsCourtier();
        Sinistre sinistre = sinMapper.mapToSinistre(dto);
        Statut sinStatut = isCourtier ? new Statut(TRANSMIS.staCode) : new Statut(SAISIE.staCode);
        sinistre.setStatut(sinStatut);
        sinistre = sinRepo.save(sinistre);
        sinistre.setSinCode(this.generateSinCode(sinistre.getSinId()));
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
        Sinistre sinistre = sinRepo.findById(dto.getSinId()).orElseThrow(()->new AppException("Sinistre introuvable"));
        Sinistre oldSin = sinCopier.copy(sinistre);
        BeanUtils.copyProperties(dto, sinistre);
        sinistre = sinRepo.save(sinistre);
        logService.logg(SynchronReActions.UPDATE_SINISTRE, oldSin, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    void transmettreSinistreAuCourtier(Long sinId) throws UnknownHostException {
        Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));
        sinistre.setStatut(new Statut(TRANSMIS.staCode));
        logService.saveLog(SinistreActions.TRANSMETTRE_SINISTRE);
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
            sinistre.setStatut(new Statut(EN_ATTENTE_DE_REGLEMENT.staCode));
        });


        mvtService.createMvtSinistre(new MvtReq(sinId, EN_ATTENTE_DE_REGLEMENT.staCode, null));
    logService.saveLog(SinistreActions.TRANSMETTRE_NOTE_CESSION_SINISTRE_ET_NOTE_DEBIT);
    }

    @Override
    public Page<SinistreDetailsResp> searchSinistre(String key, Pageable pageable)
    {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, null, jwtService.getConnectedUserCesId(), Arrays.asList(), pageable);
        return sinPage;
    }
}
