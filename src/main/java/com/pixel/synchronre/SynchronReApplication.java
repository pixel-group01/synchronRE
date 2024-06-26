package com.pixel.synchronre;

import com.pixel.synchronre.init.*;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@SpringBootApplication
public class SynchronReApplication {

    @Bean
    public DecimalFormat decimalFormat()
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Set white space as grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

        return decimalFormat;
    }

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }
    //@Bean(name = "commandLineRunner") //@Profile("dev")
    public CommandLineRunner start(TypeLoader typeLoader, FoldersIniter foldersIniter, AdminLoader adminLoader, StatutLoader statutLoader,
                                   DeviseLoader deviseLoader, PaysLoader paysLoader, PclLoader pclLoader,
                                   BrancheLoader brancheLoader, CouvertureLoader couvertureLoader,
                                   CedCesAffLoader cedCesAffLoader, ExerciceLoader exerciceLoader
                                  ,NatureLoader natureLoader, PrvLoader prvLoader, RoleLoader roleLoader, MenuLoader menuLoader, AssLoader assLoader,OrganisationLoader organisationLoader,
                                  OrganisationPaysLoader organisationPaysLoader)
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
            organisationLoader.load();
            paysLoader.load();
            organisationPaysLoader.load();
            pclLoader.load();
            exerciceLoader.load();
            cedCesAffLoader.load();
            natureLoader.load();
        };
     }
}
