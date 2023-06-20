package com.pixel.synchronre.init;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class PclLoader implements Loader
{
    private final ParamCessionLegaleRepository pclRepo;
    @Override
    public void load() {
        ParamCessionLegale franc1 = new ParamCessionLegale(null,"Cession légale au 1er franc SEN RE",new BigDecimal(0),new BigDecimal(6.5),new Pays("SEN"),new Statut("ACT"),1L, LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc2 = new ParamCessionLegale(null,"Cession légale Fac SEN RE",new BigDecimal(0),new BigDecimal(10),new Pays("SEN"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc3 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("SEN"),new Statut("ACT"),3L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc4 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("SEN"),new Statut("ACT"),4L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc1, franc2,franc3,franc4));

        ParamCessionLegale franc5 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("CIV"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc6 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("CIV"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc5,franc6));

        ParamCessionLegale franc7 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("BNN"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc8 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("BNN"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc7, franc8));

        ParamCessionLegale franc9 = new ParamCessionLegale(null,"Cession légale au 1er franc SGC RE",new BigDecimal(0),new BigDecimal(15),new Pays("GAB"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc10 = new ParamCessionLegale(null,"Cession légale Fac SGC RE",new BigDecimal(0),new BigDecimal(5),new Pays("GAB"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc11 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("GAB"),new Statut("ACT"),3L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc12 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("GAB"),new Statut("ACT"),4L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc9, franc10,franc11,franc12));

        ParamCessionLegale franc13 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("CMR"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc14 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("CMR"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc13, franc14));

        ParamCessionLegale franc15 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("GUI"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc16 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("GUI"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc15, franc16));

        ParamCessionLegale franc17 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("BFO"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc18 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("BFO"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc17, franc18));

        ParamCessionLegale franc19 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("TGO"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc20 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("TGO"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc19, franc20));

        ParamCessionLegale franc21 = new ParamCessionLegale(null,"Cession légale au 1er franc CICA RE",new BigDecimal(0),new BigDecimal(2.25),new Pays("MAL"),new Statut("ACT"),1L,LocalDateTime.now(), LocalDateTime.now());
        ParamCessionLegale franc22 = new ParamCessionLegale(null,"Cession légale Fac CICA RE",new BigDecimal(0),new BigDecimal(5),new Pays("MAL"),new Statut("ACT"),2L,LocalDateTime.now(), LocalDateTime.now());
        pclRepo.saveAll(Arrays.asList(franc21, franc22));
    }
}
