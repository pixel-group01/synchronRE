package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailDto;
import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteDetailsItems;
import com.pixel.synchronre.sychronremodule.model.dto.compte.StatCompteIds;
import com.pixel.synchronre.sychronremodule.model.entities.CompteCedante;
import com.pixel.synchronre.sychronremodule.model.entities.CompteDetails;
import com.pixel.synchronre.sychronremodule.model.views.VStatCompte;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteDetailsService;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final TraiteNPRepository tnpRepo;

    @Override
    public CompteDetails saveCompteDetails(CompteDetailDto dto, Long compteCedanteId)
    {
        CompteDetails compteDetails = compteDetailsRepo.findByCompteCedIdAndtypeId(compteCedanteId, dto.getTypeId());
        if(dto.getUniqueCode().equals("SOLD_CED") && compteDetailsRepo.existsByCompteCedIdAndTypeCode(compteCedanteId, "SOLD_REA"))
        {
            compteDetailsRepo.deleteByCompteCedanteIdAndUniqueCode(compteCedanteId, "SOLD_REA");
        }
        if(dto.getUniqueCode().equals("SOLD_REA") && compteDetailsRepo.existsByCompteCedIdAndTypeCode(compteCedanteId, "SOLD_CED"))
        {
            compteDetailsRepo.deleteByCompteCedanteIdAndUniqueCode(compteCedanteId, "SOLD_CED");
        }
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
        if(items == null) return null;
        Long trancheId = items.getTrancheIdSelected();
        Long periodeId = items.getPeriodeId();
        Long cedId = items.getCedIdSelected();

        BigDecimal traiInteretDepotLib = tnpRepo.getIntereDepotLibByTrancheId(trancheId);

        BigDecimal depoSapLibereSaisi = items.getDepotSapLib();

        BigDecimal depotSapConstAnterieurBd = compteDetailsRepo.getDepotSapConstAnterieur(trancheId, periodeId, cedId);
        depotSapConstAnterieurBd = depotSapConstAnterieurBd == null ? ZERO : depotSapConstAnterieurBd;

        BigDecimal depotSapConstActuel = compteDetailsRepo.getDepotSapConstActuel(trancheId, periodeId, cedId);
        depotSapConstActuel = depotSapConstActuel == null ? ZERO : depotSapConstActuel;
        BigDecimal depoSapLibereRecupereBd = depotSapConstAnterieurBd.compareTo(ZERO) == 0 ? ZERO : depotSapConstAnterieurBd;

        BigDecimal depoSapLibere= depoSapLibereSaisi  == null || depoSapLibereSaisi.compareTo(ZERO) == 0 ? depoSapLibereRecupereBd : depoSapLibereSaisi;
        BigDecimal interetDepotLib = depoSapLibere.multiply(traiInteretDepotLib).divide(CENT, precision == 2 ? 0 : precision, RoundingMode.HALF_UP);



        Long compteCedId = items.getCompteCedId();
        StatCompteIds statCompteIds =compteCedId == null ? new StatCompteIds(items.getCedIdSelected(), items.getTrancheIdSelected(), items.getPeriodeId()) : ccRepo.getStatCompteIdsByCompteCedId(compteCedId);

        //VStatCompte vsc = vscRepo.getStatsCompte(statCompteIds.getCedId(), statCompteIds.getTrancheId(), statCompteIds.getPeriodeId());
        VStatCompte vsc = vscRepo.getStatsCompte(statCompteIds.getCedId(), statCompteIds.getTrancheId(), statCompteIds.getPeriodeId());
        BigDecimal primeOrigine = ZERO;
        BigDecimal primeApresAjustement = ZERO;
        if(vsc == null) vsc = vscRepo.getStatsCompte(statCompteIds.getCedId(), statCompteIds.getTrancheId());
        if(vsc != null)
        {
            primeOrigine = Optional.ofNullable(vsc.getPrimeOrigine()).orElse(ZERO);
            primeApresAjustement = primeOrigine.add(vsc.getRepartitionSurplusPmd());
        }
         //items.getPrimeOrigine() == null ? ZERO : items.getPrimeOrigine();

        ; //assiettePrimeExercice.multiply(trancheTauxPrime).divide(CENT, precision, RoundingMode.HALF_UP);
        items.setDepotSapConst(depotSapConstActuel);
        items.setDepotSapLib(depoSapLibere);
        items.setInteretDepotLib(interetDepotLib);

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
        items.setCompteCedId(compteCedId);
        return items;
    }
}
