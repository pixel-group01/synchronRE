package com.pixel.synchronre.init;

import com.pixel.synchronre.sychronremodule.model.dao.NatureRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import com.pixel.synchronre.sychronremodule.model.entities.Nature;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class NatureLoader implements Loader
{
    private final NatureRepository natureRepo;
    @Override
    public void load()
    {
        Nature xl = natureRepo.save(new Nature("XL", "Traité XL", FORME.NPROP, new Branche(2L),new Statut("ACT")));
        Nature facob = natureRepo.save(new Nature("FACOB", "Traité FACOB", FORME.NPROP,new Branche(2L), new Statut("ACT")));
        Nature prop1 = natureRepo.save(new Nature("PROP 1", "PROP 1", FORME.PROP, new Branche(2L),new Statut("ACT")));
        Nature prop2 = natureRepo.save(new Nature("PROP 2", "PROP 2", FORME.PROP,new Branche(2L), new Statut("ACT")));
    }
}