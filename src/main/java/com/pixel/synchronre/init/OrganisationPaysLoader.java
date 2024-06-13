package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.AssociationRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import com.pixel.synchronre.sychronremodule.model.entities.Association;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class OrganisationPaysLoader implements Loader
{
    private final AssociationRepository assoRepo;
    @Override
    public void load()
    {
       //CIMA
        Association cimaBenin = assoRepo.save(new Association(new Pays("BNN"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaBurkinaFaso = assoRepo.save(new Association(new Pays("BFO"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaCameroun = assoRepo.save(new Association(new Pays("CMR"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaCentrafrique = assoRepo.save(new Association(new Pays("CAF"), new Organisation("CIMA"), new Statut("ACT")));
//        Association cimaComores = assoRepo.save(new Association(null, new Pays("COM"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaCongo = assoRepo.save(new Association(new Pays("COG"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaCotedIvoire = assoRepo.save(new Association(new Pays("CIV"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaGabon = assoRepo.save(new Association(new Pays("GAB"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaGuineeBissau = assoRepo.save(new Association(new Pays("GNB"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaGuineeEquatoriale = assoRepo.save(new Association(new Pays("GNQ"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaMali = assoRepo.save(new Association(new Pays("MAL"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaNiger = assoRepo.save(new Association(new Pays("NER"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaSenegal = assoRepo.save(new Association(new Pays("SEN"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaTchad = assoRepo.save(new Association(new Pays("TCD"), new Organisation("CIMA"), new Statut("ACT")));
        Association cimaTogo = assoRepo.save(new Association(new Pays("TGO"), new Organisation("CIMA"), new Statut("ACT")));

        //CEDEAO
        Association cdeaoBenin = assoRepo.save(new Association(new Pays("BNN"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoBurkinaFaso = assoRepo.save(new Association(new Pays("BFO"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoCapVert = assoRepo.save(new Association(new Pays("CPV"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoCotedIvoire = assoRepo.save(new Association(new Pays("CIV"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoGambie = assoRepo.save(new Association(new Pays("GMB"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoGhana = assoRepo.save(new Association(new Pays("GHA"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoGuinee = assoRepo.save(new Association(new Pays("GUI"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoGuineeBissau = assoRepo.save(new Association(new Pays("GNB"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoLiberia = assoRepo.save(new Association(new Pays("LBR"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoMali = assoRepo.save(new Association(new Pays("MAL"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoNiger = assoRepo.save(new Association(new Pays("NER"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoNigeria = assoRepo.save(new Association(new Pays("NGA"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoSenegal = assoRepo.save(new Association(new Pays("SEN"), new Organisation("CEDEAO"), new Statut("ACT")));
        //Association cdeaoSierraLeone = assoRepo.save(new Association(null, new Pays("SLE"), new Organisation("CEDEAO"), new Statut("ACT")));
        Association cdeaoTogo = assoRepo.save(new Association(new Pays("TGO"), new Organisation("CEDEAO"), new Statut("ACT")));

    }
}
