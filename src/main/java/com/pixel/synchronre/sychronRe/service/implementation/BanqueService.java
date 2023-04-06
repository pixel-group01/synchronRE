package com.pixel.synchronre.sychronRe.service.implementation;


import com.pixel.synchronre.sychronRe.model.dao.BanqueRepository;
import com.pixel.synchronre.sychronRe.model.dto.mapper.BanqueMapper;
import com.pixel.synchronre.sychronRe.model.dto.projection.BanqueInfo;
import com.pixel.synchronre.sychronRe.model.dto.request.BanqueReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Banque;
import com.pixel.synchronre.sychronRe.service.interfac.IserviceBanque;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BanqueService implements IserviceBanque {

    private final BanqueRepository banqueRepository;
    private final BanqueMapper banqueMapper;


    @Override
    public Banque saveBanque(BanqueReqDTO banqueReqDTO) {
        Banque banque = new Banque();
        banque = banqueMapper.mapToBanqueToBanqueReqDTO(banqueReqDTO);
       return banqueRepository.save(banque);
    }

    @Override
    public List<BanqueInfo> getAllBanque() {
        return banqueRepository.findByOrderByBanLibelleAsc();
    }

    @Override
    public BanqueReqDTO updateBanque(Long banId, BanqueReqDTO banqueReqDTO) {
        //return banqueRepository.findByBanIdOrderByBanLibelleAsc(banId).map();
        return null;
    }

    @Override
    public BanqueReqDTO delete(Long banId) {
        return null;
    }
}
