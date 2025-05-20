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

import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;
import static java.math.BigDecimal.ZERO;

import java.math.RoundingMode;
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
    private final TypeRepo typeRepo;
    private final CompteDetailsRepo cdRepo;
    private final VStatCompteRepository vscRepo;

    @Override
    public CompteTraiteDto getCompteTraite(Long traiteNpId, Long periodeId, int precision)
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
            List<CompteDetailDto> compteDetails = periodeId == null ? cdRepo.getDetailComptes() : cdRepo.findByTrancheIdAndCedIdAndPeriodeId(tc.getTrancheId(), tc.getCedIdSelected(), periodeId);
            //List<CompteDetailDto> compteDetailsDto = cdRepo.findByTrancheIdAndCedIdAndPeriodeId(tc.getTrancheId(), tc.getCedIdSelected(), periodeId);
            List<String> notStartingCompteDetails = Arrays.asList("SOLD_REA", "SOUS_TOTAL_CREDIT", "SOLD_CED", "SOUS_TOTAL_DEBIT");
            if(compteDetails != null)
            {
                compteDetails = compteDetails.stream().filter(cd->!notStartingCompteDetails.contains(cd.getUniqueCode())).collect(Collectors.toList());
            }
            BigDecimal pmdOrigine = vscRepo.getPrimeOrigine(tc.getTrancheId(), tc.getCedIdSelected(), periodeId);

            if(pmdOrigine != null && pmdOrigine.compareTo(ZERO) != 0)
            {
                CompteDetailsItems compteDetailsItems = new CompteDetailsItems();
                compteDetailsItems.setCedIdSelected(tc.getCedIdSelected());
                compteDetailsItems.setTrancheIdSelected(tc.getTrancheId());
                compteDetailsItems.setPeriodeId(periodeId);
                compteDetailsItems.setPrimeOrigine(pmdOrigine);
                CompteDetailsItems calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                compteDetails = this.mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
            }
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
        if(compteDetails == null || compteDetails.isEmpty()) return new CompteDetailDto(null, "", ZERO, ZERO, "", 0, true, true);
        return compteDetails.stream().filter(cd->cd.getUniqueCode().equals(typeCode)).findFirst().orElse(new CompteDetailDto());
    }

    @Override
    public CompteTraiteDto getCompteTraite(CompteTraiteDto dto, int precision)
    {
        CompteTraiteDto compteTraiteDto = this.getCompteTraite(dto.getTraiteNpId(), dto.getPeriodeId(), precision);
        Long trancheIdSelected = dto.getTrancheIdSelected();
        if(trancheIdSelected == null) return compteTraiteDto;
        Long periodeId = dto.getPeriodeId();
        compteTraiteDto.setTrancheIdSelected(trancheIdSelected);
        compteTraiteDto.setPeriodeId(periodeId);

        List<ReadCedanteDTO.ReadCedanteDTOLite> cedantes = compteTraiteDto.getTrancheCompteDtos().stream()
                .filter(tc->tc.getTrancheId().equals(trancheIdSelected))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables")).getCedantes();

        TrancheCompteDto trancheCompteDto = dto.getTrancheCompteDtos().stream()
                .filter(tc->tc.getTrancheId().equals(trancheIdSelected))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables")); //Récupération de la tranche sélctionnée
        Long cedIdSelected = trancheCompteDto.getCedIdSelected(); //Récupération de la cédante sélctionnée
        Compte compte = compteTraiteRepo.findByTrancheIdAndPeriodeId(trancheIdSelected, periodeId);
        Long compteCedanteId = null;

        List<CompteDetailDto> compteDetailsDtoList = trancheCompteDto.getCompteDetails();
        CompteDetailsItems calculatedCompteDetailsItems = null;
        if(compteDetailsDtoList != null && !compteDetailsDtoList.isEmpty()) //Si des détails de compte ont été saisi
        {
            if(compte == null)
            {
                CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(trancheIdSelected, periodeId, cedIdSelected, compteDetailsDtoList);
                calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
            }
            else
            {
                Long compteId = compte.getCompteId();
                compteTraiteDto.setCompteId(compteId);
                CompteCedante compteCedante = compteCedanteRepo.findByCompteIdAndCedId(compteId, cedIdSelected);
                if(compteCedante == null)
                {
                    CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(trancheIdSelected, periodeId, cedIdSelected, compteDetailsDtoList);
                    calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                    List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                    trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
                }
                else
                {
                    compteCedanteId = compteCedante.getCompteCedId();
                    CompteDetailsItems compteDetailsItems = mapToCompteDetailsItems(compteCedanteId, compteDetailsDtoList);
                    calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                    List<CompteDetailDto> calculatedCompteDetailsDtoList = mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                    List<CompteDetailDto> finalCompteDetailsDtoList = compteDetailsDtoList;
                    calculatedCompteDetailsDtoList.forEach(ccddl->
                    {
                        CompteDetailDto compteDetailDto = finalCompteDetailsDtoList.stream().filter(cddl->ccddl.getTypeId().equals(cddl.getTypeId())).findFirst().orElse(null);
                        ccddl.setCompteDetId(compteDetailDto == null ? null : compteDetailDto.getCompteDetId());
                    });
                    trancheCompteDto.setCompteDetails(calculatedCompteDetailsDtoList);
                }
            }
        }
        else //Si des détails de compte n'ont pas été saisi
        {
            if(compte == null)
            {
                compteDetailsDtoList = cdRepo.getDetailComptes();
                trancheCompteDto.setCompteDetails(compteDetailsDtoList);
            }
            else
            {
                Long compteId = compte.getCompteId();
                compteTraiteDto.setCompteId(compteId);
                CompteCedante compteCedante = compteCedanteRepo.findByCompteIdAndCedId(compteId, cedIdSelected);
                if(compteCedante == null)
                {
                    compteDetailsDtoList = cdRepo.getDetailComptes();
                    trancheCompteDto.setCompteDetails(compteDetailsDtoList);
                }
                else
                {
                    compteCedanteId = compteCedante.getCompteCedId();
                    compteDetailsDtoList = cdRepo.findByCompteCedI(compteCedanteId);
                    if(compteDetailsDtoList == null || compteDetailsDtoList.isEmpty()) compteDetailsDtoList = cdRepo.getDetailComptes();
                    trancheCompteDto.setCompteDetails(compteDetailsDtoList);
                }
            }
            BigDecimal pmdOrigine = vscRepo.getPrimeOrigine(cedIdSelected, trancheIdSelected, periodeId);
            if(pmdOrigine != null && pmdOrigine.compareTo(ZERO) != 0 )
            {
                CompteDetailDto primOrigCompteDetailDto = this.getCompteDetailsItem(compteDetailsDtoList, "PRIM_ORIG");
                if(compteDetailsDtoList == null || compteDetailsDtoList.isEmpty() || primOrigCompteDetailDto == null || primOrigCompteDetailDto.getDebit() == null || primOrigCompteDetailDto.getDebit().compareTo(ZERO) == 0)
                {
                    CompteDetailsItems compteDetailsItems = new CompteDetailsItems();
                    compteDetailsItems.setCedIdSelected(cedIdSelected);
                    compteDetailsItems.setTrancheIdSelected(trancheIdSelected);
                    compteDetailsItems.setPeriodeId(periodeId);
                    compteDetailsItems.setPrimeOrigine(pmdOrigine);
                    calculatedCompteDetailsItems = compteDetailsService.calculateDetailsComptesItems(compteDetailsItems, precision);
                    compteDetailsDtoList = this.mapCompteDetailsItemsToCompteDetailsDtoList(calculatedCompteDetailsItems);
                    trancheCompteDto.setCompteDetails(compteDetailsDtoList);
                }

            }
        }
        trancheCompteDto.setCedIdSelected(cedIdSelected);
        trancheCompteDto.setCedantes(cedantes);

        List<CompteCessionnaireDto> compteCessionnaires  = compteTraiteDto.getTrancheCompteDtos().stream()
                .filter(tc->tc.getTrancheId().equals(trancheIdSelected))
                .findFirst().orElseThrow(()->new AppException("Données de tranche introuvables")).getCompteCessionnaires();
        Long finalCompteCedanteId = compteCedanteId;
        if(compteCessionnaires != null && !compteCessionnaires.isEmpty())
        {
            compteCessionnaires = compteCessionnaires.stream().peek(cc->cc.setCompteCedId(finalCompteCedanteId)).collect(Collectors.toList());
            if(calculatedCompteDetailsItems != null)
            {
                BigDecimal soldeCedante = calculatedCompteDetailsItems.getSoldeCedante() == null ? ZERO : calculatedCompteDetailsItems.getSoldeCedante();
                BigDecimal soldeRea = calculatedCompteDetailsItems.getSoldeRea() == null ? ZERO : calculatedCompteDetailsItems.getSoldeRea();
                BigDecimal solde = soldeCedante.max(soldeRea);
                compteCessionnaires = compteCessionnaires.stream().peek(cc->cc.setPrime(solde.multiply(cc.getTaux()).divide(CENT, precision == 2 ? 0 : precision, RoundingMode.HALF_UP)))
                        .sorted(Comparator.comparing(CompteCessionnaireDto::getTaux, Comparator.nullsLast(BigDecimal::compareTo)).reversed()).collect(Collectors.toList());
                compteCessionnaires.add(new CompteCessionnaireDto("TOTAL", CENT, solde));
            }
            trancheCompteDto.setCompteCessionnaires(compteCessionnaires);
        }
        compteTraiteDto.getTrancheCompteDtos().replaceAll(
                tc -> tc.getTrancheId().equals(trancheIdSelected) ? trancheCompteDto : tc
        );
        return compteTraiteDto;
    }

    private CompteDetailsItems mapToCompteDetailsItems(Long compteCedanteId, List<CompteDetailDto> compteDetails)
    {
        BigDecimal primeOrigine = this.getCompteDetailsItem(compteDetails, "PRIM_ORIG").getDebit();
        BigDecimal primeApresAjustement = this.getCompteDetailsItem(compteDetails, "PRIM_APR_AJUST").getCredit();
        BigDecimal sinistrePaye = this.getCompteDetailsItem(compteDetails, "SIN_PAYE").getDebit();
        BigDecimal depotSapConst = this.getCompteDetailsItem(compteDetails, "DEP_SAP_CONST").getDebit();
        BigDecimal depotSapLib = this.getCompteDetailsItem(compteDetails, "DEP_SAP_LIB").getCredit();
        BigDecimal interetDepotLib = this.getCompteDetailsItem(compteDetails, "INT_DEP_LIB").getCredit();
        CompteDetailsItems compteDetailsItems = new CompteDetailsItems(compteCedanteId, primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        return compteDetailsItems;
    }
    private CompteDetailsItems mapToCompteDetailsItems(Long trancheIdSelected, Long periodeId, Long cedId, List<CompteDetailDto> compteDetails)
    {
        BigDecimal primeOrigine = this.getCompteDetailsItem(compteDetails, "PRIM_ORIG").getDebit();
        BigDecimal primeApresAjustement = this.getCompteDetailsItem(compteDetails, "PRIM_APR_AJUST").getCredit();
        BigDecimal sinistrePaye = this.getCompteDetailsItem(compteDetails, "SIN_PAYE").getDebit();
        BigDecimal depotSapConst = this.getCompteDetailsItem(compteDetails, "DEP_SAP_CONST").getDebit();
        BigDecimal depotSapLib = this.getCompteDetailsItem(compteDetails, "DEP_SAP_LIB").getCredit();
        BigDecimal interetDepotLib = this.getCompteDetailsItem(compteDetails, "INT_DEP_LIB").getCredit();
        CompteDetailsItems compteDetailsItems = new CompteDetailsItems(trancheIdSelected, periodeId, cedId, primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        return compteDetailsItems;
    }


    private List<CompteDetailDto> mapCompteDetailsItemsToCompteDetailsDtoList(CompteDetailsItems calculatedCompteDetailsItems)
    {
        Type primOrigType = typeRepo.findByUniqueCode("PRIM_ORIG").orElseThrow(()->new AppException("Type introuvable : PRIM_ORIG"));
        CompteDetailDto primeOrigineCompteDetails = new CompteDetailDto(primOrigType.getTypeId(), primOrigType.getName(), calculatedCompteDetailsItems.getPrimeOrigine(), ZERO, primOrigType.getUniqueCode(), primOrigType.getTypeOrdre(), primOrigType.isDebitDisabled(), primOrigType.isCreditDisabled());

        Type primAprAjustType = typeRepo.findByUniqueCode("PRIM_APR_AJUST").orElseThrow(()->new AppException("Type introuvable : PRIM_APR_AJUST"));
        CompteDetailDto primeApresAjustementCompteDetails = new CompteDetailDto(primAprAjustType.getTypeId(), primAprAjustType.getName(), ZERO, calculatedCompteDetailsItems.getPrimeApresAjustement(), primAprAjustType.getUniqueCode(), primAprAjustType.getTypeOrdre(), primAprAjustType.isDebitDisabled(), primAprAjustType.isCreditDisabled());

        Type sinPayeType = typeRepo.findByUniqueCode("SIN_PAYE").orElseThrow(()->new AppException("Type introuvable : SIN_PAYE"));
        CompteDetailDto sinistrePayeCompteDetails = new CompteDetailDto(sinPayeType.getTypeId(), sinPayeType.getName(), calculatedCompteDetailsItems.getSinistrePaye(), ZERO, sinPayeType.getUniqueCode(), sinPayeType.getTypeOrdre(), sinPayeType.isDebitDisabled(), sinPayeType.isCreditDisabled());

        Type depSapConstType = typeRepo.findByUniqueCode("DEP_SAP_CONST").orElseThrow(()->new AppException("Type introuvable : DEP_SAP_CONST"));
        CompteDetailDto depotSapConstCompteDetails = new CompteDetailDto(depSapConstType.getTypeId(), depSapConstType.getName(), calculatedCompteDetailsItems.getDepotSapConst(), ZERO, depSapConstType.getUniqueCode(), depSapConstType.getTypeOrdre(), depSapConstType.isDebitDisabled(), depSapConstType.isCreditDisabled());

        Type depSapLibType = typeRepo.findByUniqueCode("DEP_SAP_LIB").orElseThrow(()->new AppException("Type introuvable : DEP_SAP_LIB"));
        CompteDetailDto depotSapLibCompteDetails = new CompteDetailDto(depSapLibType.getTypeId(), depSapLibType.getName(), ZERO, calculatedCompteDetailsItems.getDepotSapLib(), depSapLibType.getUniqueCode(), depSapLibType.getTypeOrdre(), depSapLibType.isDebitDisabled(), depSapLibType.isCreditDisabled());

        Type intDepLibLibType = typeRepo.findByUniqueCode("INT_DEP_LIB").orElseThrow(()->new AppException("Type introuvable : INT_DEP_LIB"));
        CompteDetailDto interetDepotLibCompteDetails = new CompteDetailDto(intDepLibLibType.getTypeId(), intDepLibLibType.getName(), ZERO, calculatedCompteDetailsItems.getInteretDepotLib(), intDepLibLibType.getUniqueCode(), intDepLibLibType.getTypeOrdre(), intDepLibLibType.isDebitDisabled(), intDepLibLibType.isCreditDisabled());

        Type sousTotalDebitType = typeRepo.findByUniqueCode("SOUS_TOTAL_DEBIT").orElseThrow(()->new AppException("Type introuvable : SOUS_TOTAL_DEBIT"));
        CompteDetailDto sousTotalDebitCompteDetails = new CompteDetailDto(sousTotalDebitType.getTypeId(), sousTotalDebitType.getName(), calculatedCompteDetailsItems.getSousTotalDebit(), ZERO, sousTotalDebitType.getUniqueCode(), sousTotalDebitType.getTypeOrdre(), sousTotalDebitType.isDebitDisabled(), sousTotalDebitType.isCreditDisabled());

        Type sousTotalCreditType = typeRepo.findByUniqueCode("SOUS_TOTAL_CREDIT").orElseThrow(()->new AppException("Type introuvable : SOUS_TOTAL_CREDIT"));
        CompteDetailDto sousTotalCreditCompteDetails = new CompteDetailDto(sousTotalCreditType.getTypeId(), sousTotalCreditType.getName(), ZERO, calculatedCompteDetailsItems.getSousTotalCredit(), sousTotalCreditType.getUniqueCode(), sousTotalCreditType.getTypeOrdre(), sousTotalCreditType.isDebitDisabled(), sousTotalCreditType.isCreditDisabled());

        Type sousTotalType = typeRepo.findByUniqueCode("SOUS_TOTAL").orElseThrow(()->new AppException("Type introuvable : SOUS_TOTAL"));
        CompteDetailDto sousTotalCompteDetails = new CompteDetailDto(sousTotalCreditType.getTypeId(), sousTotalType.getName(), calculatedCompteDetailsItems.getSousTotalDebit(), calculatedCompteDetailsItems.getSousTotalCredit(), sousTotalCreditType.getUniqueCode(), sousTotalCreditType.getTypeOrdre(), sousTotalCreditType.isDebitDisabled(), sousTotalCreditType.isCreditDisabled());

        Type soldCedType = typeRepo.findByUniqueCode("SOLD_CED").orElseThrow(()->new AppException("Type introuvable : SOLD_CED"));
        CompteDetailDto soldeCedanteCompteDetails = new CompteDetailDto(soldCedType.getTypeId(), soldCedType.getName(), ZERO, calculatedCompteDetailsItems.getSoldeCedante(), soldCedType.getUniqueCode(), soldCedType.getTypeOrdre(), soldCedType.isDebitDisabled(), soldCedType.isCreditDisabled());

        Type soldReaType = typeRepo.findByUniqueCode("SOLD_REA").orElseThrow(()->new AppException("Type introuvable : SOLD_REA"));
        CompteDetailDto soldeReaCompteDetails = new CompteDetailDto(soldReaType.getTypeId(), soldReaType.getName(), calculatedCompteDetailsItems.getSoldeRea(), ZERO, soldReaType.getUniqueCode(), soldReaType.getTypeOrdre(), soldReaType.isDebitDisabled(), soldReaType.isCreditDisabled());

        Type soldReaNeutre = typeRepo.findByUniqueCode("SOLD_NEUTRE").orElseThrow(()->new AppException("Type introuvable : SOLD_NEUTRE"));
        CompteDetailDto soldeNeutreCompteDetails = new CompteDetailDto(soldReaNeutre.getTypeId(), soldReaNeutre.getName(), ZERO, ZERO, soldReaNeutre.getUniqueCode(), soldReaNeutre.getTypeOrdre(), soldReaNeutre.isDebitDisabled(), soldReaNeutre.isCreditDisabled());


        Type totalMouvType = typeRepo.findByUniqueCode("TOTAL_MOUV").orElseThrow(()->new AppException("Type introuvable : TOTAL_MOUV"));
        CompteDetailDto totalMouvementCompteDetails = new CompteDetailDto(totalMouvType.getTypeId(), totalMouvType.getName(), calculatedCompteDetailsItems.getTotalMouvement(), calculatedCompteDetailsItems.getTotalMouvement(), totalMouvType.getUniqueCode(), totalMouvType.getTypeOrdre(), totalMouvType.isDebitDisabled(), totalMouvType.isCreditDisabled());

        CompteDetailDto solde = soldeCedanteCompteDetails.getCredit().compareTo(ZERO) == 0 && soldeReaCompteDetails.getDebit().compareTo(ZERO) == 0 ? soldeNeutreCompteDetails : soldeCedanteCompteDetails.getCredit().compareTo(ZERO) == 0 ?  soldeReaCompteDetails : soldeCedanteCompteDetails;

        List<CompteDetailDto> compteDetailsList = Arrays.asList(
                primeOrigineCompteDetails,
                primeApresAjustementCompteDetails,
                sinistrePayeCompteDetails,
                depotSapConstCompteDetails,
                depotSapLibCompteDetails,
                interetDepotLibCompteDetails,
                sousTotalCompteDetails,
                solde,
                totalMouvementCompteDetails
        );
        return compteDetailsList;
    }
}