package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ConvertMontant;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.BordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.DetailsBordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import com.pixel.synchronre.sychronremodule.model.entities.DetailBordereau;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBordereau;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
//Bordereaux de cession = BC+CODE CESSIONNAIRE+CODE FAC+"-"+Numéro d'ordre

@Service @RequiredArgsConstructor
public class ServiceBordereauImpl implements IserviceBordereau {
    private final BordereauRepository bordRepo;
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    private final TypeRepo typeRepo;
    private final IServiceCalculsComptables comptaService;
    private final DetailsBordereauRepository detailBordRepo;
//    @Override
//    public Bordereau createBordereau(Long plaId)
//    {
//        Long affId =  repRepo.repartFindByRep(plaId);
//        Bordereau bordereau = new Bordereau();
//        bordereau.setRepartition(new Repartition(plaId));
//        bordereau.setAffaire(new Affaire(affId));
//        bordereau.setBordStatut();
//        bordereau = bordRepo.save(bordereau);
//        bordereau.setBordNum(this.generateBordNum(bordereau.getBordId(), plaId));
//       bordRepo.save(bordereau);
//       return bordereau;
//    }

    private String generateBordNum(Long borId, Long plaId)
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Répartition introuvable"));
        Long cesId = placement.getCessionnaire().getCesId();

        String bordNum = "BC." + String.format("%04d", cesId)+ "."+ affRepo.getAffCode(placement.getAffaire().getAffId()) + "." + String.format("%05d", borId);
        return bordNum;
    }


    @Override @Transactional
    public Bordereau createNoteDebit(Long affId)
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        Type bordType = typeRepo.findByUniqueCode("NOT_DEB_FAC").orElseThrow(()->new AppException("Type introuvable"));
        BigDecimal bordMontantTotalPrime = comptaService.calculateMtTotalAReverseAuxCes(affId);
        BigDecimal bordMontantTotalCommission = comptaService.calculateMtTotaleCmsCed(affId);
        BigDecimal bordMontantTotalPrimeAreverser = comptaService.calculateMtTotalPrimeCessionnaireNetteComCed(affId);
        String bordMontantTotalPrimeAreverserLette = ConvertMontant.numberToLetter(bordMontantTotalPrimeAreverser);

        Bordereau bordereau = new Bordereau();
        bordereau.setAffaire(affaire);
        bordereau.setBordMontantTotalPrime(bordMontantTotalPrime);
        bordereau.setBordMontantTotalCommission(bordMontantTotalCommission);
        bordereau.setBordMontantTotalPrimeAreverser(bordMontantTotalPrimeAreverser);
        bordereau.setBordMontantTotalPrimeAreverserLette(bordMontantTotalPrimeAreverserLette);
        bordereau  = bordRepo.save(bordereau);
        bordereau.setBordNum("ND." + affaire.getAffCode() + "." + String.format("%05d", bordereau.getBordId()));
        bordereau.setType(bordType);
        this.saveDetailNoteDebit(affaire, bordereau);
        return bordereau;
    }

    private void saveDetailNoteDebit(Affaire aff, Bordereau bordereau)
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
            DetailBordereau details = new DetailBordereau();
            details.setBordereau(bordereau);
            details.setDebCommission(debCommission);
            details.setDebPrimeAreverser(debPrimeAreverser);
            details.setDebPrime(p.getRepPrime());
            details.setDebStatut(true);
            details.setRepartition(p);
            detailBordRepo.save(details);
        });
    }
}
