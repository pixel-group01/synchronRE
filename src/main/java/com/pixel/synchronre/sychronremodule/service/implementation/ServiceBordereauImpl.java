package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.BordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.DetailsBordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBordereau;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
//Bordereaux de cession = BC+CODE CESSIONNAIRE+CODE FAC+"-"+Numéro d'ordre

@Service @RequiredArgsConstructor
public class ServiceBordereauImpl implements IserviceBordereau {
    private final BordereauRepository bordRepo;
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
    private final IServiceCalculsComptables comptaService;
    private final DetailsBordereauRepository detailBordRepo;
    private final ObjectCopier<Bordereau> bordCopier;
    private final ObjectCopier<DetailBordereau> detailBordCopier;
    private final ILogService logService;

    @Override @Transactional
    public Bordereau createNoteCession(Long plaId)
    {
        Long affId =  repRepo.repartFindByRep(plaId);
        Type bordType = typeRepo.findByUniqueCode("NOT_CES_FAC").orElseThrow(()->new AppException("Type introuvable"));
        Bordereau bordereau = new Bordereau();
        bordereau.setRepartition(new Repartition(plaId));
        bordereau.setAffaire(new Affaire(affId));
        bordereau.setType(bordType);
        bordereau.setStatut(new Statut("ACT"));
        bordereau = bordRepo.save(bordereau);
        bordereau.setBordNum(this.generateBordNum(bordereau.getBordId(), plaId));
       bordRepo.save(bordereau);
       return bordereau;
    }

    @Override
    public void updateDetailsBordereaux(Repartition updatedPlacement)
    {
        if(updatedPlacement == null) throw  new AppException("Les données sur la mise à jour du placement ne sont pas parvenues");
        Long plaId = updatedPlacement.getRepId();
        if(plaId == null) throw new AppException("L'ID du placement n'est pas parvenu");
        DetailBordereau details = detailBordRepo.findByPlaId(plaId);

        BigDecimal debCommission = comptaService.calculateMtCmsCedByCes(plaId);
        BigDecimal debPrimeAreverser = comptaService.calculateMtPrimeNetteComCedByCes(plaId);
        debPrimeAreverser = debPrimeAreverser == null ? ZERO : debPrimeAreverser.setScale(0, RoundingMode.HALF_UP);

        details.setDebCommission(debCommission);
        details.setDebPrimeAreverser(debPrimeAreverser);
        details.setDebTaux(updatedPlacement.getRepTaux());
        details.setDebPrime(updatedPlacement.getRepPrime());
        Bordereau bordereau = bordRepo.findById(details.getBordereau().getBordId()).orElseThrow(()->new AppException("Bordereau introuvable"));

        Long affId = bordereau.getAffaire().getAffId();

        BigDecimal bordMontantTotalPrime = comptaService.calculateMtTotalPrimeBruteByAffId(affId);
        BigDecimal bordMontantTotalCommission = comptaService.calculateMtTotaleCmsCed(affId);
        BigDecimal bordMontantTotalPrimeAreverser = comptaService.calculateMtTotalPrimeCessionnaireNetteComCed(affId);
        bordMontantTotalPrimeAreverser = bordMontantTotalPrimeAreverser == null ? ZERO : bordMontantTotalPrimeAreverser.setScale(0, RoundingMode.HALF_UP);
        String bordMontantTotalPrimeAreverserLette = ConvertMontant.numberToLetter(bordMontantTotalPrimeAreverser);

        bordereau.setBordMontantTotalPrime(bordMontantTotalPrime);
        bordereau.setBordMontantTotalCommission(bordMontantTotalCommission);
        bordereau.setBordMontantTotalPrimeAreverser(bordMontantTotalPrimeAreverser);
        bordereau.setBordMontantTotalPrimeAreverserLette(bordMontantTotalPrimeAreverserLette);
        detailBordRepo.save(details);
    }

    private String generateBordNum(Long borId, Long plaId)
    {
        //Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Répartition introuvable"));
        Long cesId = repRepo.getCesIdByRepId(plaId);
        if(cesId == null) throw new AppException("Auncun cessionnaire trouvé sur le placement " + plaId);
        Long affId = affRepo.getAffIdByRepId(plaId);
        if(affId == null) throw new AppException("Affaire introuvable " + affId);
        String bordNum = "BC." + String.format("%04d", cesId)+ "."+ affRepo.getAffCode(affId) + "." + String.format("%05d", borId);
        return bordNum;
    }


    @Override @Transactional
    public Bordereau createNoteDebit(Long affId)
    {
        if(bordRepo.noteDebExistsByAffId(affId)) return null;
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Type bordType = typeRepo.findByUniqueCode("NOT_DEB_FAC").orElseThrow(()->new AppException("Type introuvable"));
        BigDecimal bordMontantTotalPrime = comptaService.calculateMtTotalPrimeBruteByAffId(affId);
        BigDecimal bordMontantTotalCommission = comptaService.calculateMtTotaleCmsCed(affId);
        BigDecimal bordMontantTotalPrimeAreverser = comptaService.calculateMtTotalPrimeCessionnaireNetteComCed(affId);
        bordMontantTotalPrimeAreverser = bordMontantTotalPrimeAreverser == null ? ZERO : bordMontantTotalPrimeAreverser.setScale(0, RoundingMode.HALF_UP);
        String bordMontantTotalPrimeAreverserLette = ConvertMontant.numberToLetter(bordMontantTotalPrimeAreverser);

        Bordereau bordereau = new Bordereau();

        bordereau.setAffaire(affaire);
        bordereau.setBordMontantTotalPrime(bordMontantTotalPrime);
        bordereau.setBordMontantTotalCommission(bordMontantTotalCommission);
        bordereau.setBordMontantTotalPrimeAreverser(bordMontantTotalPrimeAreverser);
        bordereau.setBordMontantTotalPrimeAreverserLette(bordMontantTotalPrimeAreverserLette);
        bordereau.setStatut(new Statut("ACT"));
        bordereau.setCreatedAt(LocalDateTime.now());
        bordereau  = bordRepo.save(bordereau);
        bordereau.setBrodDateLimite(this.calculateDateLimite(affaire, bordereau));
        bordereau.setBordNum("ND." + affaire.getAffCode() + "." + String.format("%05d", bordereau.getBordId()));
        bordereau.setType(bordType);
        this.saveDetailsNoteDebit(affaire, bordereau);
        return bordereau;
    }

    @Override @Transactional
    public void deleteBordereau(Long bordId)
    {
        Bordereau bordereau = bordRepo.findById(bordId).orElseThrow(()->new AppException("Bordereau introuvable"));
        detailBordRepo.findByBordId(bordId).forEach(debId->this.deleteDetailBordereau(debId));
        Bordereau oldBordereau = bordCopier.copy(bordereau);
        logService.logg(SynchronReActions.DELETE_BORDEREAU, oldBordereau, new Bordereau(), SynchronReTables.BORDEREAU);
        bordRepo.deleteById(bordereau.getBordId());
    }

    @Override @Transactional
    public void deleteDetailBordereau(Long debId)
    {
        DetailBordereau detailBordereau = detailBordRepo.findById(debId).orElseThrow(()->new AppException("Detail bordereau introuvable"));
        DetailBordereau oldDetailBordereau = detailBordCopier.copy(detailBordereau);
        logService.logg(SynchronReActions.DELETE_DETAIL_BORDEREAU, oldDetailBordereau, null, SynchronReTables.DETAIL_BORDEREAU);
        detailBordRepo.deleteById(debId);
    }

    private LocalDate calculateDateLimite(Affaire affaire, Bordereau bordereau)
    {
        if(affaire == null || bordereau == null) return  null;
        LocalDate dateEffet = affaire.getAffDateEffet();
        LocalDate dateCreationBordereau = bordereau.getCreatedAt().toLocalDate();
        if(dateEffet == null) throw new AppException("La date d'effet de l'affaire n'est pas connue");
        if(dateCreationBordereau == null) throw new AppException("La date de création du bordereau n'est pas connue");
        if(dateEffet.isBefore(dateCreationBordereau) && dateEffet.plusDays(30).isBefore(dateCreationBordereau))
            return LocalDate.now();
        return dateCreationBordereau.plusDays(8);
    }

    private void saveDetailsNoteDebit(Affaire aff, Bordereau bordereau)
    {
        if(aff == null || bordereau == null) return;
        List<Repartition> placements = repRepo.getActivePlacementsByAffId(aff.getAffId());
        if(placements == null || placements.isEmpty() || bordereau == null) return;
        placements.stream()
                .filter(p-> !detailBordRepo.detailsBordereauExistsByPlaId(p.getRepId()))
                .forEach(p->
        {
            BigDecimal debCommission = comptaService.calculateMtCmsCedByCes(p.getRepId());
            BigDecimal debPrimeAreverser = comptaService.calculateMtPrimeNetteComCedByCes(p.getRepId());
            debPrimeAreverser = debPrimeAreverser == null ? ZERO : debPrimeAreverser.setScale(0, RoundingMode.HALF_UP);
            DetailBordereau details = new DetailBordereau();
            details.setBordereau(bordereau);
            details.setDebCommission(debCommission);
            details.setDebPrimeAreverser(debPrimeAreverser);
            details.setDebTaux(p.getRepTaux());
            details.setDebPrime(p.getRepPrime());
            details.setDebStatut(true);
            details.setRepartition(p);
            Long cesId = repRepo.getCesIdByRepId(p.getRepId());
            if(cesId == null) throw new AppException("Auncun cessionnaire trouvé sur le placement " + p.getRepId());
            details.setDebCesId(cesId);
            detailBordRepo.save(details);
        });
    }
}
