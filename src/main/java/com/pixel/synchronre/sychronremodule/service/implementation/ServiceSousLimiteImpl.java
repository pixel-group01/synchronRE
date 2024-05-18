package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.SousLimiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SousLimiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import com.pixel.synchronre.sychronremodule.model.entities.SousLimite;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSousLimite;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class ServiceSousLimiteImpl implements IServiceSousLimite {
  private final ILogService logService;
  private final SousLimiteRepository sslRepo;
  private final SousLimiteMapper sslMapper;
  private final ObjectCopier<SousLimite> sousLimiteCopier;


    @Override @Transactional
    public SousLimiteDetailsResp create(CreateSousLimiteReq dto) throws UnknownHostException {
        SousLimite ssl = sslMapper.mapToSousLimite(dto);
        ssl = sslRepo.save(ssl);
        logService.logg("Création d'une sous-limite",null,ssl, "SousLimite");
        SousLimiteDetailsResp sslResp = sslMapper.mapToSousLimiteResp(ssl);
        return sslResp;
    }

    @Override
    public Page<SousLimiteDetailsResp> search(String key, Long traiteNpId, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);

        Page<SousLimiteDetailsResp> sslResps = sslRepo.search(key, traiteNpId, pageable);
        return sslResps;
    }


    @Override @Transactional
    public SousLimiteDetailsResp update(UpdateSousLimite dto) throws UnknownHostException {
        SousLimite ssl = sslRepo.findById(dto.getSousLimiteSouscriptionId()).orElseThrow(()->new AppException("Sous-Limite introuvable"));
        SousLimite oldSsl = sousLimiteCopier.copy(ssl);
        BeanUtils.copyProperties(dto,ssl);
        ssl.setRisqueCouvert(new RisqueCouvert(dto.getRisqueCouvertId()));
        ssl.setTraiteNonProportionnel(new TraiteNonProportionnel(dto.getTraiteNonProportionnelId()));
        ssl.setTranche(new Tranche(dto.getTrancheId()));
        ssl = sslRepo.save(ssl);
        logService.logg("Modification d'une sous-limite", oldSsl, ssl, "SousLimite");
        SousLimiteDetailsResp sslResp = sslMapper.mapToSousLimiteResp(ssl);
        return sslResp;
    }

    public UpdateSousLimite edit(Long sousLimiteSouscriptionId){
        return sslRepo.getEditDtoById(sousLimiteSouscriptionId);
    }

   public void delete(Long sousLimiteSouscriptionId){
       SousLimite sousLimite = sslRepo.findById(sousLimiteSouscriptionId).orElseThrow(()->new AppException("Sous-Limite introuvable"));
       SousLimite oldSousLimite = sousLimiteCopier.copy(sousLimite);
       sslRepo.delete(sousLimite);
       logService.logg(SynchronReActions.DELETE_SOUS_LIMITE, oldSousLimite, new SousLimite(), SynchronReTables.SOUS_LIMITE);
   }
}
