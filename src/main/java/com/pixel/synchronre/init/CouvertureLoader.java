package com.pixel.synchronre.init;

import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class CouvertureLoader implements Loader
{
    private final CouvertureRepository couRepo;
    @Override
    public void load()
    {
        Couverture c1 = couRepo.save(new Couverture(null, "Multirisques professionnelle", "Multirisques professionnelle", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Couverture c2 = couRepo.save(new Couverture(null, "Globale Dommage", "Globale Dommage", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
        Couverture c3 = couRepo.save(new Couverture(null, "Décès groupe", "Décès groupe", new Branche(1L), new Statut("ACT"), LocalDateTime.now(), LocalDateTime.now()));
    }
}

