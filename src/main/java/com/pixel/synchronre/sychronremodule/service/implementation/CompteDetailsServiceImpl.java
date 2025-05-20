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
import java.util.Optional;

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
        VStatCompte vsc = vscRepo.getStatsCompte(statCompteIds.getCedId(), statCompteIds.getTrancheId(), statCompteIds.getPeriodeId());


        BigDecimal assiettePrimeExercice = vsc.getAssiettePrimeExercice();
        BigDecimal trancheTauxPrime = vsc.getTrancheTauxPrime();

        BigDecimal primeOrigine = Optional.ofNullable(vsc.getPrimeOrigine()).orElse(ZERO); //items.getPrimeOrigine() == null ? ZERO : items.getPrimeOrigine();

        BigDecimal primeApresAjustement = primeOrigine.add(vsc.getRepartitionSurplusPmd()); //assiettePrimeExercice.multiply(trancheTauxPrime).divide(CENT, precision, RoundingMode.HALF_UP);

        BigDecimal sousTotalDebit = primeOrigine.add(Optional.ofNullable(items.getSinistrePaye()).orElse(ZERO)).add(Optional.ofNullable(items.getDepotSapConst()).orElse(ZERO));
        BigDecimal sousTotalCredit = primeApresAjustement.add(Optional.ofNullable(items.getDepotSapLib()).orElse(ZERO)).add(Optional.ofNullable(items.getInteretDepotLib()).orElse(ZERO));
        BigDecimal soldeCedante = sousTotalDebit.compareTo(sousTotalCredit) >= 0 ? sousTotalDebit.subtract(sousTotalCredit) : ZERO;
        BigDecimal soldeRea = sousTotalCredit.compareTo(sousTotalDebit) >= 0 ? sousTotalCredit.subtract(sousTotalDebit) : ZERO;
        BigDecimal totalMouvement = sousTotalDebit.max(sousTotalCredit);

        items.setPrimeOrigine(primeOrigine.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setPrimeApresAjustement(primeApresAjustement.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setSousTotalDebit(sousTotalDebit.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setSousTotalCredit(sousTotalCredit.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setSoldeCedante(soldeCedante.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setSoldeRea(soldeRea.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        items.setTotalMouvement(totalMouvement.setScale(precision == 2 ? 0 : precision, RoundingMode.HALF_UP));
        return items;
    }

}
