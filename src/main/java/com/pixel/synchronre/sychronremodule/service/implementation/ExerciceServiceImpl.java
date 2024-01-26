package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.ExerciceRepository;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.CreateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.UpdateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ExerciceMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExerciceServiceImpl implements IserviceExercie {
    private final ExerciceRepository exoRepo;
    private final ExerciceMapper exoMapper;
    private final ObjectCopier<Exercice> exoCopier;
    private final ILogService logService;

    @Override @Transactional
    public ExerciceDetailsResp createExercice(CreateExerciceReq dto) throws UnknownHostException {
        Exercice exercice=exoMapper.mapToExercice(dto);
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
        logService.logg(SynchronReActions.CREATE_EXERCICE, null, exercice, SynchronReTables.EXERCICE);
        return exoMapper.mapToExerciceDetailsResp(exercice);
    }

    @Override @Transactional
    public ExerciceDetailsResp activateExercice(Long exeCode) throws UnknownHostException {

        Exercice exercice=exoRepo.findById(exeCode).orElseThrow(()->new AppException("Exercice introuvable"));
        if (exercice.isExeCourant()) return exoMapper.mapToExerciceDetailsResp(exercice);
        Exercice oldExercice = exoCopier.copy(exercice);
        exoRepo.setExerciceAsNoneCourant();
        exercice.setExeCourant(true);
        logService.logg(SynchronReActions.ACTIVATE_EXERCICE, oldExercice, exercice, SynchronReTables.EXERCICE);
        return exoMapper.mapToExerciceDetailsResp(exercice);
    }

    @Override @Transactional
    public ExerciceDetailsResp updateExercice(UpdateExerciceReq dto) throws UnknownHostException {
        Exercice exercice = exoRepo.findById(dto.getExeCode()).orElseThrow(()->new AppException("Exercice introuvable"));
        Exercice oldExo = exoCopier.copy(exercice);
        exercice.setExeLibelle(dto.getExeLibelle());
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
        logService.logg(SynchronReActions.UPDATE_XERCICE, oldExo, exercice, SynchronReTables.EXERCICE);
        return exoMapper.mapToExerciceDetailsResp(exercice);
    }

    @Override
    public List<ExerciceListResp> searchExercice(String key) {
        return exoRepo.searchExercice(StringUtils.stripAccentsToUpperCase(key));
    }

    @Override
    public Exercice getExerciceCourant()
    {
        List<Exercice> exoCourants = exoRepo.getExeCourant();
        if(exoCourants == null || exoCourants.isEmpty()) return exoRepo.getLastExo();
        return exoCourants.get(0);
    }

    @Override
    public List<ExerciceDetailsResp> getExerciceCourantAndPlus1() {
        Exercice exoCourant = this.getExerciceCourant();
        Exercice exoN1 = exoRepo.findById(exoCourant.getExeCode()+1).orElse(null);
        List<ExerciceDetailsResp> exercies = Stream.of(exoCourant, exoN1).filter(Objects::nonNull)
                .map(e->exoMapper.mapToExerciceDetailsResp(e))
                .collect(Collectors.toList());
        return exercies;
    }
}
