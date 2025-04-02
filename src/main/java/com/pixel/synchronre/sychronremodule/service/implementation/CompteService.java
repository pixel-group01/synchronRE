package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.compte.*;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedMapper;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteCalculService;
import com.pixel.synchronre.sychronremodule.service.interfac.ICompteDetailsService;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteService implements IserviceCompte {
    private final CompteTraiteRepo compteTraiteRepo;
    private final CedMapper cedMapper;
    private final CompteCedanteRepo compteCedanteRepo;
    private final PeriodeRepo periodeRepo;
    private final ICompteDetailsService compteDetailsService;
    private final CompteCessionnaireRepo compteCesRepo;
    private final ICompteCalculService compteCalculService;

    @Override
    public CompteTraiteDto getCompteTraite(Long traiteNpId)
    {
        CompteTraiteDto compteTraiteDto = compteTraiteRepo.getCompteByTraite(traiteNpId);
        if(compteTraiteDto == null) return null;
        List<TrancheCompteDto> trancheComptes = compteTraiteRepo.getCompteTranches(traiteNpId);
        if(trancheComptes == null || trancheComptes.isEmpty()) return compteTraiteDto;
        trancheComptes.stream().filter(Objects::nonNull).forEach(tc->
        {
            List<ReadCedanteDTO> cedantes = compteTraiteRepo.getCompteCedantes(tc.getTrancheId());
            List<ReadCedanteDTO.ReadCedanteDTOLite> liteCedantes = cedantes == null ? Collections.emptyList() :
                    cedantes.stream().map(cedMapper::mapToReadCedenteDTOLite).collect(Collectors.toList());
            tc.setCedantes(liteCedantes);
            List<CompteDetailDto> compteDetails = compteTraiteRepo.getDetailComptes();
            tc.setCompteDetails(compteDetails);
            List<CompteCessionnaireDto> cessionnaires = compteTraiteRepo.getCompteCessionnaires(tc.getTrancheId());
            tc.setCompteCessionnaires(cessionnaires);
        });
        compteTraiteDto.setTrancheCompteDtos(trancheComptes);
        return compteTraiteDto;
    }

    @Override @Transactional
    public CompteTraiteDto save(CompteTraiteDto dto)
    {
        CompteTraiteDto compteTraiteDto = this.getCompteTraite(dto, 20);
        Long trancheId = compteTraiteDto.getTrancheIdSelected();
        Long periodeId = compteTraiteDto.getPeriodeId();
        List<TrancheCompteDto> trancheCompteDtos = compteTraiteDto.getTrancheCompteDtos();

        TrancheCompteDto trancheCompteDto = trancheCompteDtos.stream()
                .filter(tc->tc.getTrancheId().equals(trancheId))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables"));
        Long cedId = trancheCompteDto.getCedIdSelected();
        Compte compte = compteTraiteRepo.findByTrancheIdAndPeriodeId(trancheId, periodeId);
        compte = compte == null ? new Compte(new Tranche(trancheId), new Periode(compteTraiteDto.getPeriodeId())) : compte;
        compte = compteTraiteRepo.save(compte);
        Long compteId = compte.getCompteId();

        CompteCedante compteCedante = compteCedanteRepo.findByCompteIdAndCedId(compteId, cedId);
        compteCedante = compteCedante == null ? new CompteCedante(new Compte(compteId), new Cedante(cedId)) : compteCedante;
        compteCedante = compteCedanteRepo.save(compteCedante);
        Long compteCedanteId = compteCedante.getCompteCedId();

        List<CompteDetailDto> compteDetails = trancheCompteDto.getCompteDetails();
        if(compteDetails != null && !compteDetails.isEmpty())
        {
            compteDetails.stream().map(cd->compteDetailsService.saveCompteDetails(cd, compteCedanteId)).collect(Collectors.toList());
        }

        List<CompteCessionnaireDto> compteCessionnaires = trancheCompteDto.getCompteCessionnaires();
        if(compteCessionnaires != null && !compteCessionnaires.isEmpty())
        {
            compteCessionnaires.stream().peek(cc->cc.setCompteCedId(compteCedanteId)).map(cc->this.saveCompteCessionnaire(cc)).collect(Collectors.toList());
        }
        //TODO setter les champs du dto
        return compteTraiteDto;
    }

    private CompteCessionnaire saveCompteCessionnaire(CompteCessionnaireDto dto)
    {
        Long compteCedId = dto.getCompteCedId();
        Long cesId = dto.getCesId();
        CompteCessionnaire compteCessionnaire = compteCesRepo.findByCompteCedIdAndCesId(dto.getCompteCedId(), dto.getCesId());
        BigDecimal primeCes = compteCalculService.calculatePrimeCessionnaireOnCompte(compteCedId, cesId);
        compteCessionnaire = compteCessionnaire == null ?
                new CompteCessionnaire(dto.getTaux(), primeCes, new CompteCedante(compteCedId), new Cessionnaire(cesId)) :
                compteCessionnaire;

        compteCessionnaire = compteCesRepo.save(compteCessionnaire);
        return compteCessionnaire;
    }

    @Override
    public List<Periode> getPeriode(Long exeCode, Long typeId)
    {
        return periodeRepo.getPeriodesByTypeId(exeCode, typeId);
    }

    CompteDetailDto getCompteDetailsItem(List<CompteDetailDto> compteDetails, String typeCode)
    {
        if(compteDetails == null || compteDetails.isEmpty()) return new CompteDetailDto(ZERO, ZERO, "");
        return compteDetails.stream().filter(cd->cd.getUniqueCode().equals(typeCode)).findFirst().orElse(new CompteDetailDto());
    }

    @Override
    public CompteTraiteDto getCompteTraite(CompteTraiteDto dto, int precision)
    {
        CompteTraiteDto compteTraiteDto = compteTraiteRepo.getCompteByTraite(dto.getTraiteNpId());
        Long trancheIdSelected = compteTraiteDto.getTrancheIdSelected();
        Long periodeId = dto.getPeriodeId();

        TrancheCompteDto trancheCompteDto = compteTraiteDto.getTrancheCompteDtos().stream()
                .filter(tc->tc.getTrancheId().equals(trancheIdSelected))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables")); //Récupération de la tranche sélctionnée
        Long cedId = trancheCompteDto.getCedIdSelected(); //Récupération de la cédante sélctionnée
        Compte compte = compteTraiteRepo.findByTrancheIdAndPeriodeId(trancheIdSelected, periodeId);
        Long compteCedanteId = null;

        List<CompteDetailDto> compteDetailsDtoList = trancheCompteDto.getCompteDetails();
        if(compteDetailsDtoList != null && !compteDetailsDtoList.isEmpty()) //Si des détails de compte ont été saisi
        {
            if(compte == null)
            {
                CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(trancheIdSelected, periodeId, cedId, compteDetailsDtoList);
                CompteDetailsItems calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
            }
            else
            {
                Long compteId = compte.getCompteId();
                CompteCedante compteCedante = compteCedanteRepo.findByCompteIdAndCedId(compteId, cedId);
                if(compteCedante == null)
                {
                    CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(trancheIdSelected, periodeId, cedId, compteDetailsDtoList);
                    CompteDetailsItems calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                    List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                    trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
                }
                else
                {
                    compteCedanteId = compteCedante.getCompteCedId();
                    CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(compteCedanteId, compteDetailsDtoList);
                    CompteDetailsItems calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                    List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                    trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
                }
            }
        }

        List<CompteCessionnaireDto> compteCessionnaires = trancheCompteDto.getCompteCessionnaires();
        Long finalCompteCedanteId = compteCedanteId;
        if(compteCessionnaires != null && !compteCessionnaires.isEmpty())
        {
            compteCessionnaires.stream().peek(cc->cc.setCompteCedId(finalCompteCedanteId)).map(cc->this.saveCompteCessionnaire(cc)).collect(Collectors.toList());
        }
        return compteTraiteDto;
    }

    private CompteDetailsItems mapToCompteDetailsItems(Long compteCedanteId, List<CompteDetailDto> compteDetails) {
        BigDecimal primeOrigine = this.getCompteDetailsItem(compteDetails, "PRIM_ORIG").getDebit();
        BigDecimal primeApresAjustement = this.getCompteDetailsItem(compteDetails, "PRIM_APR_AJUST").getCredit();
        BigDecimal sinistrePaye = this.getCompteDetailsItem(compteDetails, "SIN_PAYE").getDebit();
        BigDecimal depotSapConst = this.getCompteDetailsItem(compteDetails, "DEP_SAP_CONST").getDebit();
        BigDecimal depotSapLib = this.getCompteDetailsItem(compteDetails, "DEP_SAP_LIB").getCredit();
        BigDecimal interetDepotLib = this.getCompteDetailsItem(compteDetails, "INT_DEP_LIB").getCredit();
        CompteDetailsItems compteDetailsItems = new CompteDetailsItems(compteCedanteId, primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        return compteDetailsItems;
    }
    private CompteDetailsItems mapToCompteDetailsItems(Long trancheIdSelected, Long periodeId, Long cedId, List<CompteDetailDto> compteDetails) {
        BigDecimal primeOrigine = this.getCompteDetailsItem(compteDetails, "PRIM_ORIG").getDebit();
        BigDecimal primeApresAjustement = this.getCompteDetailsItem(compteDetails, "PRIM_APR_AJUST").getCredit();
        BigDecimal sinistrePaye = this.getCompteDetailsItem(compteDetails, "SIN_PAYE").getDebit();
        BigDecimal depotSapConst = this.getCompteDetailsItem(compteDetails, "DEP_SAP_CONST").getDebit();
        BigDecimal depotSapLib = this.getCompteDetailsItem(compteDetails, "DEP_SAP_LIB").getCredit();
        BigDecimal interetDepotLib = this.getCompteDetailsItem(compteDetails, "INT_DEP_LIB").getCredit();
        CompteDetailsItems compteDetailsItems = new CompteDetailsItems(trancheIdSelected, periodeId, cedId, primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        return compteDetailsItems;
    }

    private List<CompteDetailDto> mapCompteDetailsItemsToCompteDetailsDtoList(CompteDetailsItems calculatedCompteDetailsItems) {
        CompteDetailDto primeOrigineCompteDetails = new CompteDetailDto(calculatedCompteDetailsItems.getPrimeOrigine(), ZERO, "PRIM_ORIG");
        CompteDetailDto primeApresAjustementCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getPrimeApresAjustement(), "PRIM_APR_AJUST");
        CompteDetailDto sinistrePayeCompteDetails = new CompteDetailDto(calculatedCompteDetailsItems.getSinistrePaye(), ZERO, "SIN_PAYE");
        CompteDetailDto depotSapConstCompteDetails = new CompteDetailDto(calculatedCompteDetailsItems.getDepotSapConst(), ZERO, "DEP_SAP_CONST");
        CompteDetailDto depotSapLibCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getDepotSapLib(), "DEP_SAP_LIB");
        CompteDetailDto interetDepotLibCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getInteretDepotLib(), "INT_DEP_LIB");
        CompteDetailDto sousTotalDebitCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getSousTotalDebit(), "SOUS_TOTAL_DEBIT");
        CompteDetailDto sousTotalCreditCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getSousTotalCredit(), "SOUS_TOTAL_CREDIT");
        CompteDetailDto soldeCedanteCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getSoldeCedante(), "SOLD_CED");
        CompteDetailDto soldeReaCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getSoldeRea(), "SOLD_REA");
        CompteDetailDto totalMouvementCompteDetails = new CompteDetailDto(ZERO, calculatedCompteDetailsItems.getTotalMouvement(), "TOTAL_MOUV");

        List<CompteDetailDto> compteDetailsList = Arrays.asList(
                primeOrigineCompteDetails,
                primeApresAjustementCompteDetails,
                sinistrePayeCompteDetails,
                depotSapConstCompteDetails,
                depotSapLibCompteDetails,
                interetDepotLibCompteDetails,
                sousTotalDebitCompteDetails,
                sousTotalCreditCompteDetails,
                soldeCedanteCompteDetails,
                soldeReaCompteDetails,
                totalMouvementCompteDetails
        );
        return compteDetailsList;
    }
}
