package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteCedanteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CompteDetailsRepo;
import com.pixel.synchronre.sychronremodule.model.dao.VCompteCedanteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.VStatCompteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailsItems;
import com.pixel.synchronre.sychronremodule.model.dto.compte.StatCompteIds;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import com.pixel.synchronre.sychronremodule.model.views.VStatCompte;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteDetailsService;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;
import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class CompteDetailsServiceImpl implements ICompteDetailsService
{
    private final CompteDetailsRepo compteDetailsRepo;
    private final VCompteCedanteRepo vccRepo;
    private final VStatCompteRepository vscRepo;
    private final CompteCedanteRepo ccRepo;

    @Override
    public CompteDetails saveCompteDetails(CompteDetailDto dto, Long compteCedanteId)
    {
        CompteDetails compteDetails = compteDetailsRepo.findByCompteCedIdAndtypeId(compteCedanteId, dto.getTypeId());
        compteDetails = compteDetails == null ? new CompteDetails(new CompteCedante(compteCedanteId)) : compteDetails;
        compteDetails.setDebit(dto.getDebit());
        compteDetails.setCredit(dto.getCredit());
        compteDetails.setTypeCompteDet(new Type(dto.getTypeId()));
        compteDetails.setTypeCode(dto.getUniqueCode());
        compteDetails = compteDetailsRepo.save(compteDetails);
        return compteDetails;
    }



    @Override
    public CompteDetailsItems calculateDetailsComptesItems(CompteDetailsItems items, int precision)
    {
        Long compteCedId = items.getCompteCedId();
        StatCompteIds statCompteIds =compteCedId == null ? new StatCompteIds(items.getCedIdSelected(), items.getTrancheIdSelected(), items.getPeriodeId()) : ccRepo.getStatCompteIdsByCompteCedId(compteCedId);
        if(items == null) return null;
        //VStatCompte vsc = vscRepo.getStatsCompte(statCompteIds.getCedId(), statCompteIds.getTrancheId(), statCompteIds.getPeriodeId());
        VStatCompte vsc = vscRepo.getStatsCompte(statCompteIds.getTrancheId(), PageRequest.of(0, 1)).get(0);

        BigDecimal assiettePrimeExercice = vsc.getAssiettePrimeExercice();
        BigDecimal trancheTauxPrime = vsc.getTrancheTauxPrime();

        BigDecimal primeOrigine = items.getPrimeOrigine() == null ? ZERO : items.getPrimeOrigine();
        BigDecimal primeApresAjustement = assiettePrimeExercice.multiply(trancheTauxPrime).divide(CENT, precision, RoundingMode.HALF_UP);

        BigDecimal sousTotalDebit = primeOrigine.add(items.getSinistrePaye()).add(items.getDepotSapConst());
        BigDecimal sousTotalCredit = primeApresAjustement.add(items.getDepotSapLib()).add(items.getInteretDepotLib());
        BigDecimal soldeCedante = sousTotalDebit.compareTo(sousTotalCredit) >= 0 ? sousTotalDebit.subtract(sousTotalCredit) : ZERO;
        BigDecimal soldeRea = sousTotalCredit.compareTo(sousTotalDebit) >= 0 ? sousTotalCredit.subtract(sousTotalDebit) : ZERO;
        BigDecimal totalMouvement = soldeCedante.max(soldeRea);

        items.setPrimeApresAjustement(primeApresAjustement);
        items.setSousTotalDebit(sousTotalDebit);
        items.setSousTotalCredit(sousTotalCredit);
        items.setSoldeCedante(soldeCedante);
        items.setSoldeRea(soldeRea);
        items.setTotalMouvement(totalMouvement);
        return items;
    }

}
