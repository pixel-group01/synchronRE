package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationRepository;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class OrganisationLoader implements Loader
{
    private final OrganisationRepository organRepo;
    @Override
    public void load()
    {
        Organisation CIMA=organRepo.save(new Organisation("CIMA","CIMA",new Statut("ACT"),new AppUser(1L),new AppFunction(1L),LocalDateTime.now(), LocalDateTime.now()));
        Organisation CEDEAO=organRepo.save(new Organisation("CEDEAO","CIMA",new Statut("ACT"),new AppUser(1L),new AppFunction(1L),LocalDateTime.now(), LocalDateTime.now()));

           }
}
