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
        Type t1 = typeRepo.save(new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null));
        Type t2 = typeRepo.save(new Type(null, TypeGroup.TYPE_REP, "REP_CED", "Répartition de type part cédante", PersStatus.ACTIVE, null, null));
        Type t3 = typeRepo.save(new Type(null, TypeGroup.TYPE_REP, "REP_PLA", "Répartition de type placement", PersStatus.ACTIVE, null, null));
        Type t4 = typeRepo.save(new Type(null, TypeGroup.TYPE_REP, "REP_SIN", "Répartition de type sinistre", PersStatus.ACTIVE, null, null));

        //Type t4 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        //Type t5 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        //Type t6 = new Type(null, TypeGroup.TYPE_REP, "REP_CES_LEG", "Répartition de type cession légale", PersStatus.ACTIVE, null, null);
        Type t7 = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-AFF", "Privilège du module affaire", PersStatus.ACTIVE, null, null));
        Type t8 = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-ADM", "Privilège du module admin", PersStatus.ACTIVE, null, null));

        Type paiement = typeRepo.save(new Type(null, TypeGroup.TYPE_REGLEMENT, "paiements", "Paiement reçu", PersStatus.ACTIVE, null, null));
        Type reversement = typeRepo.save(new Type(null, TypeGroup.TYPE_REGLEMENT, "reversements", "Reversement", PersStatus.ACTIVE, null, null));
        Type reglement_sinistre = typeRepo.save(new Type(null, TypeGroup.TYPE_REGLEMENT, "REG-SIN", "Reglement Sinistre", PersStatus.ACTIVE, null, null));


        Type typeCessionnaire= typeRepo.save(new Type(null, TypeGroup.CESSIONNAIRE, "CES", "Cessionnaire", PersStatus.ACTIVE, null, null));
        Type typeCourtier = typeRepo.save(new Type(null, TypeGroup.CESSIONNAIRE, "COURT", "Courtier", PersStatus.ACTIVE, null, null));

        Type fil= typeRepo.save(new Type(null, TypeGroup.TYPE_CED, "FIL", "Filiale", PersStatus.ACTIVE, null, null));
        Type rea = typeRepo.save(new Type(null, TypeGroup.TYPE_CED, "REA", "Réassureur", PersStatus.ACTIVE, null, null));
        Type facType= typeRepo.save(new Type(null, TypeGroup.TYPE_AFFAIRE, "FAC", "Facultative", PersStatus.ACTIVE, null, null));
        Type trai = typeRepo.save(new Type(null, TypeGroup.TYPE_AFFAIRE, "TRAITE", "Traite", PersStatus.ACTIVE, null, null));

        Type virement = typeRepo.save(new Type(null, TypeGroup.MODE_REGLEMENT, "VRG", "Virement bancaire", PersStatus.ACTIVE, null, null));
        Type Chèque = typeRepo.save(new Type(null, TypeGroup.MODE_REGLEMENT, "CHE", "Chèque", PersStatus.ACTIVE, null, null));

        //Doc user
        Type userDoc = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "DOC_USR", "Document d'utilisateur", PersStatus.ACTIVE, null, "user"));
        Type photo = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "PHT", "Photo", PersStatus.ACTIVE, null, "user"));
        typeParamRepo.save(new TypeParam(null, userDoc, photo, PersStatus.ACTIVE));

        //Doc reglement
        Type docReglement = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "DOC_REG", "Document de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type recuReglement = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "RECU_REG", "Recu de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type chequeRegelemnt = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "CHEQ", "Chèque de règlement", PersStatus.ACTIVE, null, "reglement"));
        Type bordereauVirement = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "BORD_VIR", "Bordereau de virement", PersStatus.ACTIVE, null, "reglement"));
        Type ordreVirement = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "ORD_VIR", "Ordre de virement", PersStatus.ACTIVE, null, "reglement"));
        typeParamRepo.save(new TypeParam(null, docReglement, recuReglement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, chequeRegelemnt, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, bordereauVirement, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docReglement, ordreVirement, PersStatus.ACTIVE));

        //Doc placement
        Type docPlacement = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "DOC_PLA", "Document de placement", PersStatus.ACTIVE, null, "placement"));
        Type noteCessionFac = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_CES_FAC", "Note de cession facultative", PersStatus.ACTIVE, null, "placement"));
        Type noteCessionTraite = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_CES_TRAI", "Note de cession en traité", PersStatus.ACTIVE, null, "placement"));
        Type noteCessionSin = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_CES_SIN", "Note de cession sur sinistre", PersStatus.ACTIVE, null, "placement"));
        Type noteDebitFac = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_DEB_FAC", "Note de débit en facultative", PersStatus.ACTIVE, null, "placement"));
        Type noteDebitTraite = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_DEB_TRAI", "Note de débit en traité", PersStatus.ACTIVE, null, "placement"));
        Type noteCreditFac = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_CRED_FAC", "Note de crédit en facultative", PersStatus.ACTIVE, null, "placement"));
        Type noteCreditTraite = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_CRED_TRAI", "Note de crédit en traité", PersStatus.ACTIVE, null, "placement"));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteCessionFac, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteCessionTraite, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteCessionSin, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteDebitFac, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteDebitTraite, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteCreditFac, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docPlacement, noteCreditTraite, PersStatus.ACTIVE));

        //Doc Affaire
        Type docAffaire = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "DOC_AFF", "Document d'affaire", PersStatus.ACTIVE, null, "affaire"));
        Type policeAssurance = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "PLC_ASS", "Police d'assurance", PersStatus.ACTIVE, null, "affaire"));
        typeParamRepo.save(new TypeParam(null, docAffaire, policeAssurance, PersStatus.ACTIVE));

        //Doc sinistre
        Type docSinistre = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "DOC_SIN", "Document de sinistre", PersStatus.ACTIVE, null, "sinistre"));
        Type noteDebitSin = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "NOT_DEB_SIN", "Note de débit sur sinistre", PersStatus.ACTIVE, null, "placement"));
        Type pvConstat = typeRepo.save(new Type(null, TypeGroup.DOCUMENT, "PV_CONST", "Procès verbal de constat", PersStatus.ACTIVE, null, "sinistre"));
        typeParamRepo.save(new TypeParam(null, docSinistre, noteDebitSin, PersStatus.ACTIVE));
        typeParamRepo.save(new TypeParam(null, docSinistre, pvConstat, PersStatus.ACTIVE));


        //Type de privilege
        Type prvUserType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-USER", "Utilisateur", PersStatus.ACTIVE, null, null));
        Type prvFoncType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-FONC", "Fonction", PersStatus.ACTIVE, null, null));
        Type prvPrvType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-PRV", "Privilège", PersStatus.ACTIVE, null, null));
        Type prvRolType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-ROL", "Rôle", PersStatus.ACTIVE, null, null));
        Type prvFacType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-FAC", "Affaire FAC", PersStatus.ACTIVE, null, null));
        Type prvTraiType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-TRAI", "Traté", PersStatus.ACTIVE, null, null));
        Type prvParamType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-PARAM", "Paramètre", PersStatus.ACTIVE, null, null));
        //Type prvPlaType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-PLA", "Placement", PersStatus.ACTIVE, null, null));
        Type prvComptaType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-COMPTA", "Comptabilité", PersStatus.ACTIVE, null, null));
        Type prvDevType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-DEV", "Développeur", PersStatus.ACTIVE, null, null));
        Type prvRepType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-REP", "Repartition", PersStatus.ACTIVE, null, null));
        Type prvSinType = typeRepo.save(new Type(null, TypeGroup.TYPE_PRV, "PRV-SIN", "Sinistre", PersStatus.ACTIVE, null, null));
    }
}
