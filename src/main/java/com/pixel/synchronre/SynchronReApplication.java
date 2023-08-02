package com.pixel.synchronre;

import com.pixel.synchronre.init.*;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SynchronReApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }
   @Bean(name = "commandLineRunner")
    public CommandLineRunner start(TypeLoader typeLoader, FoldersIniter foldersIniter, AdminLoader adminLoader, StatutLoader statutLoader,
                                   DeviseLoader deviseLoader, PaysLoader paysLoader, PclLoader pclLoader,
                                   BrancheLoader brancheLoader, CouvertureLoader couvertureLoader,
                                   CedCesAffLoader cedCesAffLoader, ExerciceLoader exerciceLoader, CessionnaireRepository cesRepo
                                  ,PrvLoader prvLoader, RoleLoader roleLoader,MenuLoader menuLoader, AssLoader assLoader)
    {
        return args->{
            typeLoader.load();
            statutLoader.load();
            menuLoader.load();
            foldersIniter.load();
            prvLoader.load();
            roleLoader.load();
            adminLoader.load();
            assLoader.load();
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
