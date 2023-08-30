package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.archivemodule.controller.service.ReglementDocUploader;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.constants.ReglementActions;
import com.pixel.synchronre.sychronremodule.model.constants.SinistreActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static java.math.BigDecimal.ZERO;

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
    private final ReglementDocUploader regDocUploader;
    private final SinRepo sinRepo;
    private final IServiceCalculsComptables comptaAffaireService;
    private final IServiceCalculsComptablesSinistre comptaSinistreService;
    private final String PAIEMENT = "paiements";
    private final String REVERSEMENT = "reversements";


    @Override @Transactional
    public ReglementDetailsResp createReglementAffaire(String typeReg, CreateReglementReq dto) throws UnknownHostException
    {
        switch (typeReg)
        {
            case PAIEMENT: return this.createPaiementAffaire(dto);
            case REVERSEMENT:return this.createReversementAffaire(dto);
            default: return null;
        }
    }
    @Override @Transactional
    public ReglementDetailsResp createPaiementAffaire(CreateReglementReq dto) throws UnknownHostException
    {
        boolean hasReglement = regRepo.affaireHasReglement(dto.getAffId(), PAIEMENT);
        Reglement paiement = reglementMapper.mapToReglement(dto);

        //paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement.setTypeReglement(typeRepo.findByUniqueCode(PAIEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        paiement.setRegMontantLettre(ConvertMontant.numberToLetter(paiement.getRegMontant().longValue()));
        //NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE);
        //String formattedNumber = numberFormat.format(paiement.getRegMontant());
        //paiement.setRegMontantTemp(formattedNumber);
        paiement = regRepo.save(paiement); Long regId = paiement.getRegId();
        logService.logg(ReglementActions.CREATE_PAIEMENT_AFFAIRE, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));

        if(!hasReglement) {
            mvtService.createMvtAffaire(new MvtReq(AffaireActions.PAYER_FAC, dto.getAffId(), EN_COURS_DE_PAIEMENT.staCode, null));
        }
        return reglementMapper.mapToReglementDetailsResp(paiement);
    }

    @Override @Transactional
    public ReglementDetailsResp createReversementAffaire(CreateReglementReq dto) throws UnknownHostException
    {
        BigDecimal restAReverser = comptaAffaireService.calculateRestAReverser(dto.getAffId());
        if(dto.getRegMontant().compareTo(restAReverser)>0) throw new AppException("Le montant du reversement ne peut exéder le reste à reverser (" + restAReverser + ")");
        Reglement reversement = reglementMapper.mapToReglement(dto);
        //reversement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        reversement.setTypeReglement(typeRepo.findByUniqueCode(REVERSEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        reversement.setRegMontantLettre(ConvertMontant.numberToLetter(reversement.getRegMontant().longValue()));
        reversement = regRepo.save(reversement); Long regId = reversement.getRegId();
        logService.logg(ReglementActions.CREATE_REVERSEMENT_AFFAIRE, null, reversement, SynchronReTables.REGLEMENT);
        reversement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        BigDecimal restARegler = comptaAffaireService.calculateRestARegler(dto.getAffId());

        if(restARegler.compareTo(ZERO) == 0 && restAReverser.compareTo(ZERO) == 0) {
            mvtService.createMvtAffaire(new MvtReq(AffaireActions.REVERSER_FAC,dto.getAffId(), SOLDE.staCode, null));
        }
        return reglementMapper.mapToReglementDetailsResp(reversement);
    }
    @Override @Transactional
    public ReglementDetailsResp createReglementSinistre(String typeReg, CreateReglementReq dto) throws UnknownHostException
    {
        switch (typeReg)
        {
            case PAIEMENT: return this.createPaiementSinistre(dto);
            case REVERSEMENT:return this.createReversementSinistre(dto);
            default: return null;
        }
    }

    @Override @Transactional
    public ReglementDetailsResp createPaiementSinistre(CreateReglementReq dto) throws UnknownHostException
    {
        boolean hasPaiement = regRepo.sinistreHasReglement(dto.getSinId(), PAIEMENT);
        Reglement paiement = reglementMapper.mapToReglement(dto);

        //paiement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        paiement.setTypeReglement(typeRepo.findByUniqueCode(PAIEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        paiement.setRegMontantLettre(ConvertMontant.numberToLetter(paiement.getRegMontant().longValue()));
        paiement = regRepo.save(paiement);
        logService.logg(ReglementActions.CREATE_PAIEMENT_SINISTRE, null, paiement, SynchronReTables.REGLEMENT);
        paiement.setSinistre(sinRepo.findById(dto.getSinId()).orElse(new Sinistre(dto.getSinId())));
        if(!hasPaiement)
        {
            mvtService.createMvtSinistre(new MvtReq(SinistreActions.PAYER_SIN, dto.getSinId(), EN_COURS_DE_PAIEMENT.staCode, null));
        }
        if(comptaSinistreService.calculateResteAPayerBySin(dto.getSinId()).compareTo(ZERO) == 0) {
            mvtService.createMvtSinistre(new MvtReq(SinistreActions.PAYER_SIN, dto.getSinId(), EN_COURS_DE_REVERSEMENT.staCode, null));
        }
        return reglementMapper.mapToReglementDetailsResp(paiement);
    }

    @Override @Transactional
    public ReglementDetailsResp createReversementSinistre(CreateReglementReq dto) throws UnknownHostException
    {
        boolean hasReversement = regRepo.sinistreHasReglement(dto.getSinId(), REVERSEMENT);
        Reglement reversement = reglementMapper.mapToReglement(dto);

        //reversement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        reversement.setTypeReglement(typeRepo.findByUniqueCode(REVERSEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        reversement.setRegMontantLettre(ConvertMontant.numberToLetter(reversement.getRegMontant().longValue()));
        reversement = regRepo.save(reversement);
        logService.logg(ReglementActions.CREATE_REVERSEMENT_SINISTRE, null, reversement, SynchronReTables.REGLEMENT);
        reversement.setSinistre(sinRepo.findById(dto.getSinId()).orElse(new Sinistre(dto.getSinId())));

        BigDecimal restAPayer = comptaSinistreService.calculateResteAPayerBySin(dto.getSinId());
        if(!hasReversement) //Si c'est le premier reversement
        {
            //Si les paiements sont terminés
            if(restAPayer.compareTo(ZERO) == 0)
            {
                mvtService.createMvtSinistre(new MvtReq(SinistreActions.REVERSER_SIN,dto.getSinId(), EN_COURS_DE_REVERSEMENT.staCode, null));
            }
            else //Si les paiements ne sont pas terminés
            {
                mvtService.createMvtSinistre(new MvtReq(SinistreActions.REVERSER_SIN,dto.getSinId(), EN_COURS_DE_PAIEMENT_REVERSEMENT.staCode, null));
            }

        }
        BigDecimal restAReverser = comptaSinistreService.calculateMtSinistreEnAttenteDeAReversement(dto.getSinId());

        // Si le reste à payer et le reste à reverser sont égaux à zero
        if(restAPayer.compareTo(ZERO) == 0 && restAReverser.compareTo(ZERO) == 0) {
            mvtService.createMvtSinistre(new MvtReq(SinistreActions.REVERSER_SIN,dto.getSinId(), SOLDE.staCode, null));
        }
        return reglementMapper.mapToReglementDetailsResp(reversement);
    }


    @Override @Transactional
    public ReglementDetailsResp updateReglement(UpdateReglementReq dto) throws UnknownHostException
    {
        Reglement ref = regRepo.findById(dto.getRegId()).orElseThrow(()->new AppException("Reglement introuvable"));
        Reglement oldPai = paiCopier.copy(ref);
        ref.setRegReference(dto.getRegReference());
        ref.setRegMontant(dto.getRegMontant());
        ref.setRegDate(dto.getRegDate());
        ref=regRepo.save(ref);
        logService.logg(ReglementActions.UPDATE_REGLEMENT, oldPai, ref, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToReglementDetailsResp(ref);
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, Long affId, Long sinId, String typRegUniqueCode, Pageable pageable) {
        return regRepo.searchReglement(StringUtils.stripAccentsToUpperCase(key), affId, sinId,typRegUniqueCode, pageable);
    }
}
