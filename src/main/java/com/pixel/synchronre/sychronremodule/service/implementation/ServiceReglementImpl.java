package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.*;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ReglementMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
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
import java.math.RoundingMode;
import java.net.UnknownHostException;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;
import static java.math.BigDecimal.ZERO;

@Service @AllArgsConstructor
public class ServiceReglementImpl implements IserviceReglement {
    private final ReglementRepository regRepo;
    private final ReglementMapper reglementMapper;
    private final ObjectCopier<Reglement> regCopier;
    private final ILogService logService;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
    private final IServiceMouvement mvtService;
    private final SinRepo sinRepo;
    private final IServiceCalculsComptables comptaAffaireService;
    private final IServiceCalculsComptablesSinistre comptaSinistreService;
    private final String PAIEMENT = "paiements";
    private final String REVERSEMENT = "reversements";
    private final BigDecimal CENT = new BigDecimal(100);
    private final RepartitionRepository repRepo;

    @Override @Transactional
    public ReglementDetailsResp createReglementAffaire(String typeReg, CreateReglementReq dto) throws UnknownHostException
    {
        if(dto.getRegMontant() == null || dto.getRegMontant().compareTo(ZERO) == 0) throw new AppException("Le montant du règlement ne peut être nul");
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
        BigDecimal resteAPayer = comptaAffaireService.calculateRestARegler(dto.getAffId());
        BigDecimal primeNetteComCed = dto.getRegMontant() == null ? ZERO : dto.getRegMontant();
        BigDecimal futureResteApayer = resteAPayer.subtract(primeNetteComCed);
        if(futureResteApayer.compareTo(ZERO)<0 && futureResteApayer.abs().compareTo(PRECISION.TROIS_CHIFFRES) > 0) throw new AppException("Le montant du paiement ne peut exéder le reste à payer (" + resteAPayer.setScale(0, RoundingMode.HALF_UP) + " " + affRepo.getDevCodeByAffId(dto.getAffId()) + ")");
        if(futureResteApayer.abs().compareTo(PRECISION.TROIS_CHIFFRES)<0) primeNetteComCed = resteAPayer;
        boolean hasReglement = regRepo.affaireHasReglement(dto.getAffId(), PAIEMENT);
        Reglement paiement = reglementMapper.mapToReglement(dto);

        paiement.setTypeReglement(typeRepo.findByUniqueCode(PAIEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        paiement.setRegMontantLettre(ConvertMontant.numberToLetter(paiement.getRegMontant().longValue()));

        //BigDecimal commissionCedGlobale = comptaAffaireService.calculateMtTotaleCmsCed(dto.getAffId());
        BigDecimal commissionCedanteRestantAEncaisse = comptaAffaireService.calculateMtTotalCmsCedanteRestantAEncaisse(dto.getAffId());
        BigDecimal commissionCourtageRestantAEncaisse = comptaAffaireService.calculateMtTotalCmsCourtageRestantAEncaisse(dto.getAffId());

        BigDecimal commissionCourtageEncaisse;
        BigDecimal primeNetteComRea;
        if(primeNetteComCed.compareTo(commissionCourtageRestantAEncaisse)>0)
        {
            primeNetteComRea = primeNetteComCed.subtract(commissionCourtageRestantAEncaisse);
            commissionCourtageEncaisse = commissionCourtageRestantAEncaisse;
        }
        else
        {
            primeNetteComRea = ZERO;
            commissionCourtageEncaisse = primeNetteComCed;
        }

        BigDecimal commissionRea = commissionCedanteRestantAEncaisse.add(commissionCourtageEncaisse);

        paiement.setRegCommissionCed(commissionCedanteRestantAEncaisse);
        paiement.setRegCommissionCourt(commissionCourtageEncaisse);
        paiement.setRegCommission(commissionRea);
        paiement.setRegMontantNetteCommissionRea(primeNetteComRea);

        paiement = regRepo.save(paiement);
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
        Long plaId = repRepo.getPlacementIdByAffIdAndCesId(dto.getAffId(), dto.getCesId()).orElseThrow(()-> new AppException("Placement introuvable"));
        //if(dto.getRegMontant().compareTo(ZERO) == 0) throw new AppException("Impossible de faire un reversement à 0 ( " + affRepo.getDevCodeByAffId(dto.getAffId()) + ")");
        BigDecimal mtReversement = dto.getRegMontant();
        BigDecimal resteAReverser = comptaAffaireService.calculateRestAReverserbyCes(plaId);
        BigDecimal mtEnAttenteDeReversement = comptaAffaireService.calculateMtEnAttenteDeAReversement(dto.getAffId());

        if(mtReversement == null || mtReversement.compareTo(ZERO) == 0) throw new AppException("Le montant du reversement ne peut être null");

        BigDecimal futureMtEnAttenteDeReversement = mtEnAttenteDeReversement.subtract(mtReversement);
        //BigDecimal futureResteAReverser = resteAReverser.subtract(mtReversement);

        if(futureMtEnAttenteDeReversement.abs().compareTo(PRECISION.TROIS_CHIFFRES) > 0) throw new AppException("Le montant du reversement ne peut exéder le montant en attente de reversement (" + mtEnAttenteDeReversement.setScale(3, RoundingMode.HALF_UP) + " " + affRepo.getDevCodeByAffId(dto.getAffId()) + ")");
        if(futureMtEnAttenteDeReversement.abs().compareTo(PRECISION.TROIS_CHIFFRES) < 0) mtReversement = mtEnAttenteDeReversement;

        //if(futureResteAReverser.abs().compareTo(PRECISION.TROIS_CHIFFRES) > 0) throw new AppException("Le montant du reversement ne peut exéder le reste à reverser (" + resteAReverser.setScale(0, RoundingMode.HALF_UP) + " " + affRepo.getDevCodeByAffId(dto.getAffId()) + ")");
        //if(futureResteAReverser.abs().compareTo(PRECISION.TROIS_CHIFFRES) < 0) mtReversement = resteAReverser;

        dto.setRegMontant(mtReversement);
        Reglement reversement = reglementMapper.mapToReglement(dto);
        Repartition placement = repRepo.getPlacementByAffIdAndCesId(dto.getAffId(), dto.getCesId()).orElseThrow(()->new AppException("Placement introuvable"));

        reversement.setRegCommissionCourt(ZERO);
        reversement.setRegCommissionCed(ZERO);
        reversement.setRegCommission(ZERO);

        //reversement.setAppUser(new AppUser(jwtService.getUserInfosFromJwt().getUserId()));
        reversement.setTypeReglement(typeRepo.findByUniqueCode(REVERSEMENT).orElseThrow(()->new AppException("Type de document inconnu")));
        reversement.setRegMontantLettre(ConvertMontant.numberToLetter(reversement.getRegMontant().longValue()));
        reversement = regRepo.save(reversement);
        logService.logg(ReglementActions.CREATE_REVERSEMENT_AFFAIRE, null, reversement, SynchronReTables.REGLEMENT);
        reversement.setAffaire(affRepo.findById(dto.getAffId()).orElse(new Affaire(dto.getAffId())));
        BigDecimal restARegler = comptaAffaireService.calculateRestARegler(dto.getAffId());

        if(restARegler.compareTo(ZERO) == 0 && resteAReverser.compareTo(ZERO) == 0) {
            mvtService.createMvtAffaire(new MvtReq(AffaireActions.REVERSER_FAC,dto.getAffId(), SOLDE.staCode, null));
        }
        return reglementMapper.mapToReglementDetailsResp(reversement);
    }
    @Override @Transactional
    public ReglementDetailsResp createReglementSinistre(String typeReg, CreateReglementReq dto) throws UnknownHostException
    {
        if(dto.getRegMontant() == null || dto.getRegMontant().compareTo(ZERO) == 0) throw new AppException("Le montant du règlement ne peut être nul");
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
        BigDecimal resteAPayer = comptaSinistreService.calculateResteAPayerBySinAndCes(dto.getSinId(), dto.getCesId());
        if(dto.getRegMontant().compareTo(resteAPayer) > 0) throw new AppException("Le montant du paiement ne peut excéder le reste à payer (" + resteAPayer.setScale(0, RoundingMode.HALF_UP) + ")");
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
        BigDecimal mtEnAttenteDeReversement = comptaSinistreService.calculateMtSinistreEnAttenteDeAReversement(dto.getSinId());
        BigDecimal mtRestantAReverser = comptaSinistreService.calculateResteAReverserBySin(dto.getSinId());
        if(dto.getRegMontant().compareTo(mtEnAttenteDeReversement) > 0) throw new AppException("Le montant du reversement ne peut excéder le montant en attente de reversement (" + mtEnAttenteDeReversement.setScale(0, RoundingMode.HALF_UP) + ")");
        if(dto.getRegMontant().compareTo(mtRestantAReverser) > 0) throw new AppException("Le montant du reversement ne peut excéder le reste à reverser (" + mtRestantAReverser.setScale(0, RoundingMode.HALF_UP) + ")");
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
        Reglement reg = regRepo.findById(dto.getRegId()).orElseThrow(()->new AppException("Reglement introuvable"));
        Reglement oldPai = regCopier.copy(reg);
        reg.setRegReference(dto.getRegReference());
        reg.setRegMontant(dto.getRegMontant());
        reg.setRegDate(dto.getRegDate());
        reg=regRepo.save(reg);
        logService.logg(ReglementActions.UPDATE_REGLEMENT, oldPai, reg, SynchronReTables.REGLEMENT);
        return reglementMapper.mapToReglementDetailsResp(reg);
    }

    @Override
    public Page<ReglementListResp> searchReglement(String key, Long affId, Long sinId, String typRegUniqueCode, Pageable pageable) {
        return regRepo.searchReglement(StringUtils.stripAccentsToUpperCase(key), affId, sinId,typRegUniqueCode, pageable);
    }

    @Override @Transactional
    public int deleteReglement(Long regId) throws UnknownHostException {
        Reglement reglement = regRepo.findById(regId).orElseThrow(()->new AppException("Règlemement introuvable"));
        Reglement oldReg = regCopier.copy(reglement);
        reglement.setRegStatut(false);
        if(reglement.getAffId() != null)
        {
            Mouvement avantDernier = mvtService.getAvantDernierByAffId(reglement.getAffId());
            mvtService.createMvtAffaire(new MvtReq(SynchronReActions.DELETE_REGLEMENT_AFFAIRE, reglement.getAffId(), avantDernier == null ? null : avantDernier.getStatut().getStaCode(), null));
        }
        else if(reglement.getSinId() != null)
        {
            Mouvement avantDernier = mvtService.getAvantDernierBySinId(reglement.getSinId());
            mvtService.createMvtSinistre(new MvtReq(SynchronReActions.DELETE_REGLEMENT_SINISTRE, reglement.getSinId(), avantDernier == null ? null : avantDernier.getStatut().getStaCode(), null));
        }

        logService.logg(ReglementActions.DELETE_REGLEMENT, oldReg, reglement, SynchronReTables.REGLEMENT);
        return 1;
    }

    @Override
    public void annulerReglement(Long regId)
    {
        Reglement reglement = regRepo.findById(regId).orElseThrow(()->new AppException("Reglement introuvable"));
        Reglement oldReglement = regCopier.copy(reglement);
        reglement.setRegStatut(false);
        logService.logg(RepartitionActions.ANNULER_REPARTITION, oldReglement, reglement, SynchronReTables.REGLEMENT);
    }
}
