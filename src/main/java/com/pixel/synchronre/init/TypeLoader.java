package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service @RequiredArgsConstructor
public class TypeLoader implements Loader
{
    private final TypeRepo typeRepo;
    private final TypeParamRepo typeParamRepo;
    @Override
    public void load()
    {
        Type t1 = new Type(1l, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        Type t2 = new Type(2l, TypeGroup.TYPE_REP, "REP_CED", "Répartition de type part cédante", PersStatus.ACTIVE, null, null);
        Type t3 = new Type(3l, TypeGroup.TYPE_REP, "REP_PLA", "Répartition de type placement", PersStatus.ACTIVE, null, null);
        Type t4 = new Type(4l, TypeGroup.TYPE_REP, "REP_SIN", "Répartition de type sinistre", PersStatus.ACTIVE, null, null);

        //Type t4 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        //Type t5 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        //Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        Type t7 = new Type(7l, TypeGroup.TYPE_PRV, "PRV-AFF", "Privilège du module affaire", PersStatus.ACTIVE, null, null);
        Type t8 = new Type(8l, TypeGroup.TYPE_PRV, "PRV-ADM", "Privilège du module admin", PersStatus.ACTIVE, null, null);

        Type paiement = new Type(9l, TypeGroup.TYPE_REGLEMENT, "paiements", "Paiement reçu", PersStatus.ACTIVE, null, null);
        Type reversement = new Type(10l, TypeGroup.TYPE_REGLEMENT, "reversements", "Reversement", PersStatus.ACTIVE, null, null);
        Type reglement_sinistre = new Type(11l, TypeGroup.TYPE_REGLEMENT, "REG-SIN", "Reglement Sinistre", PersStatus.ACTIVE, null, null);


        Type fil= new Type(12l, TypeGroup.TYPE_CED, "FIL", "Filiale", PersStatus.ACTIVE, null, null);
        Type rea = new Type(13l, TypeGroup.TYPE_CED, "REA", "Réassureur", PersStatus.ACTIVE, null, null);
        Type facType= new Type(14l, TypeGroup.TYPE_AFFAIRE, "FAC", "Facultative", PersStatus.ACTIVE, null, null);
        Type trai = new Type(15l, TypeGroup.TYPE_AFFAIRE, "TRAITE", "Traite", PersStatus.ACTIVE, null, null);

        Type photo = new Type(16l, TypeGroup.DOCUMENT, "PHT", "Photo", PersStatus.ACTIVE, null, "user");


        Type recuReglement = new Type(18l, TypeGroup.DOCUMENT, "RECU_REG", "Recu de règlement", PersStatus.ACTIVE, null, "reglement");
        Type chequeRegelemnt = new Type(19l, TypeGroup.DOCUMENT, "CHEQ", "Chèque de règlement", PersStatus.ACTIVE, null, "reglement");
        Type bordereauVirement = new Type(20l, TypeGroup.DOCUMENT, "BORD_VIR", "Bordereau de virement", PersStatus.ACTIVE, null, "reglement");
        Type ordreVirement = new Type(21l, TypeGroup.DOCUMENT, "ORDE_VIR", "Ordre de virement", PersStatus.ACTIVE, null, "reglement");

        //Type noteCession = new Type()

        //Type avisModCession = new Type(25l, TypeGroup.DOCUMENT, "AVI_MOD_CES", "Avis de modification de cession", PersStatus.ACTIVE, null, "placement");
        //Type noteCession = new Type(26l, TypeGroup.DOCUMENT, "NOT_CES", "Note de cession", PersStatus.ACTIVE, null, "placement");

        Type virement = new Type(22l, TypeGroup.MODE_REGLEMENT, "VRG", "Virement bancaire", PersStatus.ACTIVE, null, null);
        Type Chèque = new Type(23l, TypeGroup.MODE_REGLEMENT, "CHE", "Chèque", PersStatus.ACTIVE, null, null);


        Type noteCessionFac = new Type(24l, TypeGroup.DOCUMENT, "NOT_CES_FAC", "Note de cession facultative", PersStatus.ACTIVE, null, "placement");
        Type noteCessionTraite = new Type(25l, TypeGroup.DOCUMENT, "NOT_CES_TRAI", "Note de cession en traité", PersStatus.ACTIVE, null, "placement");
        Type noteCessionSin = new Type(26l, TypeGroup.DOCUMENT, "NOT_CES_SIN", "Note de cession sur sinistre", PersStatus.ACTIVE, null, "placement");

        Type noteDebitFac = new Type(27l, TypeGroup.DOCUMENT, "NOT_DEB_FAC", "Note de débit en facultative", PersStatus.ACTIVE, null, "placement");
        Type noteDebitTraite = new Type(28l, TypeGroup.DOCUMENT, "NOT_DEB_TRAI", "Note de débit en traité", PersStatus.ACTIVE, null, "placement");
        Type noteDebitSin = new Type(29l, TypeGroup.DOCUMENT, "NOT_DEB_SIN", "Note de débit sur sinistre", PersStatus.ACTIVE, null, "placement");


        Type noteCreditFac = new Type(30l, TypeGroup.DOCUMENT, "NOT_CRED_FAC", "Note de crédit en facultative", PersStatus.ACTIVE, null, "placement");
        Type noteCreditTraite = new Type(31l, TypeGroup.DOCUMENT, "NOT_CRED_TRAI", "Note de crédit en traité", PersStatus.ACTIVE, null, "placement");

        typeRepo.saveAll(Arrays.asList(t1,t2,t3, t4, t7,t8,fil,rea, paiement, reversement, facType, trai, photo,
                virement,Chèque, recuReglement, chequeRegelemnt,
                bordereauVirement, noteCessionFac, noteCessionTraite, noteCessionSin, noteDebitFac, noteDebitTraite,
                noteDebitSin, noteCreditFac, noteCreditTraite));

        Type docReglement = new Type(null, TypeGroup.DOCUMENT, "DOC_REG", "Document de règlement", PersStatus.ACTIVE, null, "reglement");
        docReglement = typeRepo.save(docReglement);

        Type docAffaire = new Type(null, TypeGroup.DOCUMENT, "DOC_AFF", "Document d'affaire", PersStatus.ACTIVE, null, "affaire");
        docAffaire = typeRepo.save(docAffaire);

        Type docSinistre = new Type(null, TypeGroup.DOCUMENT, "DOC_SIN", "Document de sinistre", PersStatus.ACTIVE, null, "sinistre");
        docSinistre = typeRepo.save(docSinistre);

        Type pvConstat = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "PV_CONST", "Procès verbal de constat", PersStatus.ACTIVE, null, "sinistre"));

        typeParamRepo.save(new TypeParam(null, docSinistre, pvConstat, PersStatus.ACTIVE));

        ordreVirement = typeRepo.save(ordreVirement);
        typeParamRepo.save(new TypeParam(null, docReglement, recuReglement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, chequeRegelemnt, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, bordereauVirement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, ordreVirement, PersStatus.ACTIVE));
    }
}
