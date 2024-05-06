package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import com.pixel.synchronre.sychronremodule.model.entities.OrganisationPays;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class OrganisationPaysLoader implements Loader
{
    private final OrganisationPaysRepository orgPaysRepo;
    @Override
    public void load()
    {
       //CIMA
        OrganisationPays cimaBenin = orgPaysRepo.save(new OrganisationPays(null, new Pays("BNN"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaBurkinaFaso = orgPaysRepo.save(new OrganisationPays(null, new Pays("BFO"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaCameroun = orgPaysRepo.save(new OrganisationPays(null, new Pays("CMR"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaCentrafrique = orgPaysRepo.save(new OrganisationPays(null, new Pays("CAF"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
//        OrganisationPays cimaComores = orgPaysRepo.save(new OrganisationPays(null, new Pays("COM"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaCongo = orgPaysRepo.save(new OrganisationPays(null, new Pays("COG"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaCotedIvoire = orgPaysRepo.save(new OrganisationPays(null, new Pays("CIV"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaGabon = orgPaysRepo.save(new OrganisationPays(null, new Pays("GAB"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaGuineeBissau = orgPaysRepo.save(new OrganisationPays(null, new Pays("GNB"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaGuineeEquatoriale = orgPaysRepo.save(new OrganisationPays(null, new Pays("GNQ"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaMali = orgPaysRepo.save(new OrganisationPays(null, new Pays("MAL"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaNiger = orgPaysRepo.save(new OrganisationPays(null, new Pays("NER"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaSenegal = orgPaysRepo.save(new OrganisationPays(null, new Pays("SEN"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaTchad = orgPaysRepo.save(new OrganisationPays(null, new Pays("TCD"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cimaTogo = orgPaysRepo.save(new OrganisationPays(null, new Pays("TGO"), new Organisation("CIMA"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));

        //CEDEAO
        OrganisationPays cdeaoBenin = orgPaysRepo.save(new OrganisationPays(null, new Pays("BNN"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoBurkinaFaso = orgPaysRepo.save(new OrganisationPays(null, new Pays("BFO"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoCapVert = orgPaysRepo.save(new OrganisationPays(null, new Pays("CPV"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoCotedIvoire = orgPaysRepo.save(new OrganisationPays(null, new Pays("CIV"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoGambie = orgPaysRepo.save(new OrganisationPays(null, new Pays("GMB"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoGhana = orgPaysRepo.save(new OrganisationPays(null, new Pays("GHA"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoGuinee = orgPaysRepo.save(new OrganisationPays(null, new Pays("GUI"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoGuineeBissau = orgPaysRepo.save(new OrganisationPays(null, new Pays("GNB"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoLiberia = orgPaysRepo.save(new OrganisationPays(null, new Pays("LBR"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoMali = orgPaysRepo.save(new OrganisationPays(null, new Pays("MAL"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoNiger = orgPaysRepo.save(new OrganisationPays(null, new Pays("NER"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoNigeria = orgPaysRepo.save(new OrganisationPays(null, new Pays("NGA"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoSenegal = orgPaysRepo.save(new OrganisationPays(null, new Pays("SEN"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        //OrganisationPays cdeaoSierraLeone = orgPaysRepo.save(new OrganisationPays(null, new Pays("SLE"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));
        OrganisationPays cdeaoTogo = orgPaysRepo.save(new OrganisationPays(null, new Pays("TGO"), new Organisation("CEDEAO"), new Statut("ACT"), new AppUser(1L), new AppFunction(1L), LocalDateTime.now(), LocalDateTime.now()));

    }
}
