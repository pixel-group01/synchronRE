package com.pixel.synchronre;

import com.pixel.synchronre.init.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SynchronReApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }
   @Bean(name = "commandLineRunner")
    public CommandLineRunner start(TypeLoader typeLoader, AdminLoader adminLoader, StatutLoader statutLoader,
                                   DeviseLoader deviseLoader, PaysLoader paysLoader, PclLoader pclLoader,
                                   BrancheLoader brancheLoader, CouvertureLoader couvertureLoader,
                                   CedCesAffLoader cedCesAffLoader, ExerciceLoader exerciceLoader, CessionnaireRepository cesRepo
                                  ,AffaireRepository affRepo, StatutRepository statutRepo)
    {
        return args->{
            typeLoader.load();
            adminLoader.load();
            statutLoader.load();
            deviseLoader.load();
            brancheLoader.load();
            couvertureLoader.load();
            paysLoader.load();
            pclLoader.load();
            exerciceLoader.load();
            cedCesAffLoader.load();
        };
    }
}
