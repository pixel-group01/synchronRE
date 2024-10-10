package com.pixel.synchronre.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainLoader
{
    //@Bean(name = "commandLineRunner") //@Profile("dev")
    public CommandLineRunner start(TypeLoader typeLoader, FoldersIniter foldersIniter, AdminLoader adminLoader,
                                   StatutLoader statutLoader, DeviseLoader deviseLoader, PaysLoader paysLoader, PclLoader pclLoader,
                                   BrancheLoader brancheLoader, CouvertureLoader couvertureLoader,
                                   CedCesAffLoader cedCesAffLoader, ExerciceLoader exerciceLoader,
                                   NatureLoader natureLoader, PrvLoader prvLoader, RoleLoader roleLoader,
                                   MenuLoader menuLoader, AssLoader assLoader, OrganisationLoader organisationLoader,
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
