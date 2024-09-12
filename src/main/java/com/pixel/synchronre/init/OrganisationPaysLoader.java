package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import com.pixel.synchronre.sychronremodule.model.entities.Association;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class OrganisationPaysLoader implements Loader
{
    private final OrganisationPaysRepository orgPaysRepo;
    private final TypeRepo typeRepo;
    @Override
    public void load()
    {
       //CIMA
        Association cimaBenin = orgPaysRepo.save(new Association(new Pays("BNN"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaBurkinaFaso = orgPaysRepo.save(new Association(new Pays("BFO"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaCameroun = orgPaysRepo.save(new Association(new Pays("CMR"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaCentrafrique = orgPaysRepo.save(new Association(new Pays("CAF"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
//        Association cimaComores = orgPaysRepo.save(new Association(null, new Pays("COM"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaCongo = orgPaysRepo.save(new Association(new Pays("COG"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaCotedIvoire = orgPaysRepo.save(new Association(new Pays("CIV"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaGabon = orgPaysRepo.save(new Association(new Pays("GAB"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaGuineeBissau = orgPaysRepo.save(new Association(new Pays("GNB"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaGuineeEquatoriale = orgPaysRepo.save(new Association(new Pays("GNQ"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaMali = orgPaysRepo.save(new Association(new Pays("MAL"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaNiger = orgPaysRepo.save(new Association(new Pays("NER"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaSenegal = orgPaysRepo.save(new Association(new Pays("SEN"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaTchad = orgPaysRepo.save(new Association(new Pays("TCD"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cimaTogo = orgPaysRepo.save(new Association(new Pays("TGO"), new Organisation("CIMA"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));

        //CEDEAO
        Association cdeaoBenin = orgPaysRepo.save(new Association(new Pays("BNN"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoBurkinaFaso = orgPaysRepo.save(new Association(new Pays("BFO"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoCapVert = orgPaysRepo.save(new Association(new Pays("CPV"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoCotedIvoire = orgPaysRepo.save(new Association(new Pays("CIV"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoGambie = orgPaysRepo.save(new Association(new Pays("GMB"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoGhana = orgPaysRepo.save(new Association(new Pays("GHA"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoGuinee = orgPaysRepo.save(new Association(new Pays("GUI"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoGuineeBissau = orgPaysRepo.save(new Association(new Pays("GNB"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoLiberia = orgPaysRepo.save(new Association(new Pays("LBR"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoMali = orgPaysRepo.save(new Association(new Pays("MAL"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoNiger = orgPaysRepo.save(new Association(new Pays("NER"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoNigeria = orgPaysRepo.save(new Association(new Pays("NGA"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoSenegal = orgPaysRepo.save(new Association(new Pays("SEN"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        //Association cdeaoSierraLeone = orgPaysRepo.save(new Association(null, new Pays("SLE"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        Association cdeaoTogo = orgPaysRepo.save(new Association(new Pays("TGO"), new Organisation("CEDEAO"), new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));

    }
}
