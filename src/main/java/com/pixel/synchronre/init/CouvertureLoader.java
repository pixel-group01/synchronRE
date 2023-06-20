package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class CouvertureLoader implements Loader
{
    private final CouvertureRepository couRepo;
    @Override
    public void load()
    {
        Couverture c1 = new Couverture(1L, "Multirisques professionnelle", "Multirisques professionnelle", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Couverture c2 = new Couverture(2L, "Globale Dommage", "Globale Dommage", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        Couverture c3 = new Couverture(3L, "Décès groupe", "Décès groupe", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now());
        couRepo.saveAll(Arrays.asList(c1, c2, c3));
    }
}
