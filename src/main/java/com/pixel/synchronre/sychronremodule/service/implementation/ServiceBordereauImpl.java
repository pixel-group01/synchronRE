package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.enums.StatutEnum;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.BordereauRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBordereau;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//Bordereaux de cession = BC+CODE CESSIONNAIRE+CODE FAC+"-"+Numéro d'ordre

@Service @RequiredArgsConstructor
public class ServiceBordereauImpl implements IserviceBordereau {
    private final BordereauRepository bordRepo;
    private final RepartitionRepository repRepo;
    private final AffaireRepository affRepo;
    @Override
    public Bordereau createBordereau(Long plaId)
    {
        Long affId =  repRepo.repartFindByRep(plaId);
        Bordereau bordereau = new Bordereau();
        bordereau.setRepartition(new Repartition(plaId));
        bordereau.setAffaire(new Affaire(affId));
        bordereau.setBordStatut(StatutEnum.SOLDE.staCode);
        bordereau = bordRepo.save(bordereau);
        bordereau.setBordNum(this.generateBordNum(bordereau.getBordId(), plaId));
       bordRepo.save(bordereau);
       return bordereau;
    }

    private String generateBordNum(Long borId, Long plaId)
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Répartition introuvable"));
        Long cesId = placement.getCessionnaire().getCesId();

        String bordNum = "BC." + String.format("%04d", cesId)+ "."+ affRepo.getAffCode(placement.getAffaire().getAffId()) + "." + String.format("%05d", borId);
        return bordNum;
    }
}
