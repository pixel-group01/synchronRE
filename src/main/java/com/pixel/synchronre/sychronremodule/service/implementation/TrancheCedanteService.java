package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheCedanteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePrimeDto;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import com.pixel.synchronre.sychronremodule.model.views.CedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;

@Service @RequiredArgsConstructor
public class TrancheCedanteService implements ITrancheCedanteService
{
    private final TrancheCedanteRepository trancheCedanteRepo;
    private final CategorieRepository catRepo;
    private final TrancheCedanteMapper tcMapper;
    private final TraiteNPRepository traiteRepo;
    private final RepartitionTraiteRepo repTraiRepo;
    private final ParamCessionLegaleRepository paramCesLegRepo;
    private final IServiceRepartitionTraiteNP repTnpService;
    private final CedanteTraiteRepo cedanteTraiteRepo;
    private final TrancheRepository trancheRepo;

    @Override
    public List<ReadCedanteDTO> getListCedanteAsaisirSurTraite(Long traiteNpId)
    {
        return trancheCedanteRepo.getListCedanteAsaisirSurTraite(traiteNpId);
    }

    @Override
    public TrancheCedanteReq getEditDto(TrancheCedanteReq dto, int scale)
    {
        if(dto == null) return null;
        Long traiteNpId = dto.getTraiteNpId();
        Long cedId = dto.getCedId();
        dto.setCedantes(cedanteTraiteRepo.findCedanteByTraite(traiteNpId));

        List<TranchePrimeDto> tranchePrimeDtos = this.getNaturalTranchePrimeDtos(dto, scale);

        dto.setTraiteNpId(traiteNpId);
        dto.setCedId(cedId);
        dto.setTranchePrimeDtos(tranchePrimeDtos);
        return dto;
    }
    @Override
    public TrancheCedanteReq save(TrancheCedanteReq dto)
    {
        dto = this.getEditDto(dto, 20);
        List<TranchePrimeDto> tranchePrimeDtos = dto.getTranchePrimeDtos();
        tranchePrimeDtos.stream().filter(TranchePrimeDto::isChanged).forEach(tranchePrime ->
        {
            this.saveTranchePrime(tranchePrime);
        });
        return dto;
    }

    @Override @Transactional
    public void onAddOrRemoveCedanteToCategorie(Long cedId, Long catId)
    {
        Long traiteNpId = catRepo.getTraiteIdByCatId(catId);
        if(traiteNpId == null) return;
        List<Long> trancheCedantesIdsToRemove = trancheCedanteRepo.getTrancheCedanteIdsToRemove(traiteNpId, cedId);
        this.removeObsoleteTrancheCedantes(trancheCedantesIdsToRemove);
        List<TranchePrimeDto> tranchePmdsToAdd = trancheCedanteRepo.getTranchePmdToAdd(traiteNpId, cedId);
        this.addTranchePmds(tranchePmdsToAdd);
    }

    @Override
    public void onAddOrRemoveCategorieToTranche(Long catId, Long trancheId)
    {
        List<Long> cedIds = catRepo.getCedIdsByCatId(catId);
        if(cedIds == null || cedIds.isEmpty()) return;
        cedIds.forEach(cedId->
        {
            Long traiteNpId = catRepo.getTraiteIdByCatId(catId);
            if(traiteNpId != null)
            {
                List<Long> trancheCedantesIdsToRemove = trancheCedanteRepo.getTrancheCedanteIdsToRemove(traiteNpId, cedId);
                this.removeObsoleteTrancheCedantes(trancheCedantesIdsToRemove);
                List<TranchePrimeDto> tranchePmdsToAdd = trancheCedanteRepo.getTranchePmdToAdd(traiteNpId, cedId);
                this.addTranchePmds(tranchePmdsToAdd);
            }
        });
    }

    @Override
    public List<CedanteTraite> getAllCedanteTraites()
    {
        List<CedanteTraite> cedanteTraites = cedanteTraiteRepo.findAll();
        return cedanteTraites;
    }


    private void saveTranchePrime(TranchePrimeDto dto)
    {
        TrancheCedante trancheCedante = trancheCedanteRepo.findByTrancheIdAndCedId(dto.getTrancheId(), dto.getCedId());
        boolean modeCreate = trancheCedante == null;
        if(modeCreate)
        {
            trancheCedante = new TrancheCedante();
            trancheCedante.setTranche(new Tranche(dto.getTrancheId()));
            trancheCedante.setCedante(new Cedante(dto.getCedId()));
            trancheCedante.setAssiettePrime(dto.getAssiettePrime());
        }

        trancheCedante.setPmd(dto.getPmd());
        trancheCedante.setPmdCourtier(dto.getPmdCourtier());
        trancheCedante.setPmdCourtierPlaceur(dto.getPmdCourtierPlaceur());
        trancheCedante.setPmdNette(dto.getPmdNette());
        trancheCedante = trancheCedanteRepo.save(trancheCedante);
        List<CesLeg> cesLegs= dto.getCessionsLegales();

        if(cesLegs != null && !cesLegs.isEmpty())
        {
            if(modeCreate)
            {
                cesLegs.stream().filter(cesLeg -> cesLeg.isAccepte()).forEach(cesLeg->repTnpService.createRepartitionCesLegTraite(cesLeg));
            }
            else
            {
                cesLegs.stream().filter(cesLeg -> cesLeg.isAccepte()).forEach(cesLeg->repTnpService.updateRepartitionCesLegTraite(cesLeg));
                cesLegs.stream().filter(cesLeg -> !cesLeg.isAccepte()).forEach(cesLeg->
                        repTnpService.desactivateCesLegByTraiteNpIdAndPclId(dto.getTraiteNpId(), cesLeg.getParamCesLegalId()));
            }
        }

        repTnpService.recalculateMontantPrimeForPlacementOnTraite(dto.getTraiteNpId());
    }

    private List<TranchePrimeDto> getNaturalTranchePrimeDtos(TrancheCedanteReq dto, int scale)
    {
        Long traiteNpId = dto.getTraiteNpId();
        Long cedId = dto.getCedId();
        List<TranchePrimeDto> naturalTranchePrimes = trancheCedanteRepo.findNaturalTranchePmdForCedanteAndTraite(cedId, traiteNpId);
        if(naturalTranchePrimes == null) return Collections.emptyList();
        naturalTranchePrimes.forEach(trPmd->
        {
            BigDecimal oldAssiettePrime = trancheCedanteRepo.getAssiettePrimeByTrancheIdAndCedId(trPmd.getTrancheId(), cedId);
            BigDecimal assiettePrime = this.getAssiettePrime(dto, trPmd.getTrancheId());
            trPmd.setTraiteNpId(traiteNpId);
            trPmd.setCedId(cedId);
            trPmd.setAssiettePrime(assiettePrime);
            this.calculatePrimesAndCesLegs(trPmd, scale);
            boolean hasChanged = this.trPmdHasChange(assiettePrime, oldAssiettePrime);
            trPmd.setChanged(hasChanged);
        });
        return naturalTranchePrimes;
    }

    private boolean trPmdHasChange(BigDecimal assiettePrime, BigDecimal oldAssiettePrime)
    {
        assiettePrime = assiettePrime == null ? BigDecimal.ZERO : assiettePrime;
        oldAssiettePrime = oldAssiettePrime == null ? BigDecimal.ZERO : oldAssiettePrime;
        if(assiettePrime == oldAssiettePrime) return false;
        return assiettePrime.compareTo(oldAssiettePrime) != 0;
    }

    private BigDecimal getAssiettePrime(TrancheCedanteReq dto, Long trancheId)
    {
        if(dto == null || dto.getTranchePrimeDtos() == null) return BigDecimal.ZERO;
        TranchePrimeDto tranchePrimeDto = dto.getTranchePrimeDtos().stream().filter(Objects::nonNull).filter(trPmd->trPmd.getTrancheId().equals(trancheId)).findFirst().orElse(null);
        return tranchePrimeDto == null ? BigDecimal.ZERO : tranchePrimeDto.getAssiettePrime();
    }

    private TranchePrimeDto calculatePrimesAndCesLegs(TranchePrimeDto trPime, int scale)
    {
        Long traiteNpId = trPime.getTraiteNpId();

        BigDecimal tauxAbattement = traiteRepo.getTauxAbattement(traiteNpId);
        BigDecimal finalTauxAbattement = tauxAbattement == null || tauxAbattement.compareTo(BigDecimal.ZERO) == 0 ? CENT : tauxAbattement;
        TauxCourtiersResp tauxCourtiersResp = traiteRepo.getTauxCourtiers(traiteNpId);
        BigDecimal pmd = trPime.getAssiettePrime() != null && trPime.getTrancheTauxPrime() != null ?
                trPime.getAssiettePrime().multiply(trPime.getTrancheTauxPrime()).multiply(CENT.subtract(finalTauxAbattement)).divide(CENT.multiply(CENT), 20, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal pmdCourtier = tauxCourtiersResp == null || tauxCourtiersResp.getTraiTauxCourtier() == null ? BigDecimal.ZERO : pmd.multiply(tauxCourtiersResp.getTraiTauxCourtier()).divide(CENT, scale, RoundingMode.HALF_UP);
        BigDecimal pmdCourtierPlaceur = tauxCourtiersResp == null || tauxCourtiersResp.getTraiTauxCourtier() == null ? BigDecimal.ZERO : pmd.multiply(tauxCourtiersResp.getTraiTauxCourtierPlaceur()).divide(CENT, scale, RoundingMode.HALF_UP);
        BigDecimal pmdNette = pmd.subtract(pmdCourtier.add(pmdCourtierPlaceur));
        trPime.setPmd(pmd);
        trPime.setPmdCourtier(pmdCourtier);
        trPime.setPmdCourtierPlaceur(pmdCourtierPlaceur);
        trPime.setPmdNette(pmdNette);
        Long trancheCedanteId = trancheCedanteRepo.getTrancheCedanteIdByTrancheIdAndCedId(trPime.getTrancheId(), trPime.getCedId());
        trPime.setTrancheCedanteId(trancheCedanteId);
        trPime.setCessionsLegales(this.getCesLegs(trPime, scale == 0 ? 2 : scale));
        return trPime;
    }

    private List<CesLeg> getCesLegs(TranchePrimeDto dto, int scale) {
        //Cessionns légales envoyées par le front dans le dto
        List<CesLeg> dtoCesLegs = dto.getCessionsLegales();

        //Cessionns légales lié au pays de la cedante concerné
        List<CesLeg> baseCesLegs = paramCesLegRepo.findTraiteCesLegsByCedId(dto.getCedId());

        //Cessionns légales Déjà persisté en base pour ce traité et cette cédante
        List<CesLeg> persistedCesLegs = dto.getTrancheCedanteId() != null ?
                repTraiRepo.findPersistedCesLegsByTrancheCedanteId(dto.getTrancheCedanteId()) :
                repTraiRepo.findPersistedCesLegsByTraiIdAndCedId(dto.getTraiteNpId(), dto.getCedId());

        List<CesLeg> cesLegs = null;

        //Le front n'envoie aucune donnée de cessions légales et il n'existe aucune cessionn légale déjà persistée
        if((dtoCesLegs == null || dtoCesLegs.isEmpty()) && (persistedCesLegs == null || persistedCesLegs.isEmpty()))
        {
            cesLegs = baseCesLegs;
        }

        //Le front n'envoie aucune donnée de cessions légales mais il existe des cessionns légales déjà persistées
        if((dtoCesLegs == null || dtoCesLegs.isEmpty()) && persistedCesLegs != null && !persistedCesLegs.isEmpty())
        {
            cesLegs = persistedCesLegs;
        }

        //Le front envoie des données de cessions légales mais il n'existe aucune cessionn légales déjà persistée
        if((dtoCesLegs != null && !dtoCesLegs.isEmpty()) && (persistedCesLegs == null || persistedCesLegs.isEmpty()))
        {
            cesLegs = dtoCesLegs;
        }

        //Le front envoie des données de cessions légales et il existe des cessionns légales déjà persistées
        if((dtoCesLegs != null && !dtoCesLegs.isEmpty()) && (persistedCesLegs != null && !persistedCesLegs.isEmpty()))
        {
            cesLegs = persistedCesLegs.stream()
                    .peek(persCL->setCesLegAccepte(persCL, dtoCesLegs)).collect(Collectors.toList());
        }

        if(cesLegs != null && dto.getAssiettePrime() != null)
        {
            cesLegs.forEach(cesLeg -> {
                BigDecimal cesLegPmd = dto.getAssiettePrime().multiply(cesLeg.getTauxCesLeg()).divide(CENT, scale, RoundingMode.HALF_UP);
                cesLeg.setPmd(cesLegPmd);
            });
        }
        return cesLegs;
    }

    private void setCesLegAccepte(CesLeg cesLeg, List<CesLeg> dtoCesLegs)
    {
        Optional<CesLeg> correspondingDtoCesLeg$ = dtoCesLegs.stream().filter(dtoCl-> Objects.equals(cesLeg.getParamCesLegalId(), dtoCl.getParamCesLegalId())).findFirst();

        cesLeg.setAccepte(correspondingDtoCesLeg$.isPresent() ? correspondingDtoCesLeg$.get().isAccepte() : false);
    }

    private void addTranchePmds(List<TranchePrimeDto> tranchePmdsToAdd)
    {
        if(tranchePmdsToAdd == null || tranchePmdsToAdd.isEmpty()) return;
        tranchePmdsToAdd.forEach(trPmd->
        {
            TranchePrimeDto tranchePmdDto = this.calculatePrimesAndCesLegs(trPmd, 20);
            TrancheCedante tc = tcMapper.mapToTrancheCedante(tranchePmdDto);
            trancheCedanteRepo.save(tc);
        });

    }

    private void removeObsoleteTrancheCedantes(List<Long> trancheCedantesIdsToRemove) {
        if(trancheCedantesIdsToRemove == null || trancheCedantesIdsToRemove.isEmpty()) return;
        trancheCedanteRepo.deleteAllById(trancheCedantesIdsToRemove);
    }
}
