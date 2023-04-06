package com.pixel.synchronre.sychronRe.service.implementation;

import com.pixel.synchronre.sychronRe.model.dao.StatutRepository;
import com.pixel.synchronre.sychronRe.model.dto.mapper.StatutMapper;
import com.pixel.synchronre.sychronRe.model.dto.request.StatutReq;
import com.pixel.synchronre.sychronRe.model.entities.Statut;
import com.pixel.synchronre.sychronRe.service.interfac.StatutIservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class StatutService implements StatutIservice {
    private final StatutRepository statRepo;
    private final StatutMapper statutMapper;
    @Override
    public void save(StatutReq dto) throws UnknownHostException {
        Statut statut=statutMapper.mapToStatut(dto);
        statut= statRepo.save(statut);
    }
}
