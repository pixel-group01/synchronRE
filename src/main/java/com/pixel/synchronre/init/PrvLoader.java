package com.pixel.synchronre.init;

import com.pixel.synchronre.authmodule.controller.repositories.PrvRepo;
import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import com.pixel.synchronre.logmodule.controller.repositories.LogRepo;
import com.pixel.synchronre.logmodule.model.entities.Log;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class PrvLoader implements Loader
{
    private final TypeRepo typeRepo;
    private final PrvRepo prvRepo;
    private final LogRepo logRepo;

    @Override
    public void load()
    {

        AppPrivilege getStatTrai = prvRepo.save(new AppPrivilege(null, "GET-STAT-TRAI", "Consulter les statistiques sur les traités", typeRepo.findByUniqueCode("PRV-STAT")));
        AppPrivilege getStatFac = prvRepo.save(new AppPrivilege(null, "GET-STAT-FAC", "Consulter les statistiques sur les facultatives", typeRepo.findByUniqueCode("PRV-STAT")));
        AppPrivilege getStatSin = prvRepo.save(new AppPrivilege(null, "GET-STAT-SIN", "Consulter les statistiques sur les sinistres", typeRepo.findByUniqueCode("PRV-STAT")));
        AppPrivilege getStatAdm = prvRepo.save(new AppPrivilege(null, "GET-STAT-ADM", "Consulter les statistiques d'administration", typeRepo.findByUniqueCode("PRV-STAT")));
        AppPrivilege getStatParam = prvRepo.save(new AppPrivilege(null, "GET-STAT-PARAM", "Consulter les statistiques des données paramètres", typeRepo.findByUniqueCode("PRV-STAT")));


        AppPrivilege getUserDet = prvRepo.save(new AppPrivilege(null, "GET-USER-DET", "Consulter les informations sur un utilisateurs", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege crtUserFnc = prvRepo.save(new AppPrivilege(null, "CRT-USER-FNC", "Créer un utilisateur", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege getUserLst = prvRepo.save(new AppPrivilege(null, "GET-USER-LST", "Voir la liste des utilisateurs", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege updUser = prvRepo.save(new AppPrivilege(null, "UPD-USER", "Modifier un utilisateur", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege blqUser = prvRepo.save(new AppPrivilege(null, "BLQ-USER", "Bloquer un utilisateur", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege ublqUser = prvRepo.save(new AppPrivilege(null, "UBLQ-USER", "Débloquer un utilisateur", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege sendActMail = prvRepo.save(new AppPrivilege(null, "SEND-ACT-MAIL", "Envoyer un mail d'activation de compte", typeRepo.findByUniqueCode("PRV-USER")));
        AppPrivilege crtFnc = prvRepo.save(new AppPrivilege(null, "CRT-FNC", "Créer une fonction", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege updFnc = prvRepo.save(new AppPrivilege(null, "UPD-FNC", "Modifier une fonction", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege setFncDflt = prvRepo.save(new AppPrivilege(null, "SET-FNC-DFLT", "Définir une fonction comme fonction courante", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege rvkFnc = prvRepo.save(new AppPrivilege(null, "RVK-FNC", "Revoquer une fonction", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege rstrFnc = prvRepo.save(new AppPrivilege(null, "RSTR-FNC", "Restaurer une fonction", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege getActFncLst = prvRepo.save(new AppPrivilege(null, "GET-ACT-FNC-LST", "Consulter la liste des fonctions actives d'un utilisateur", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege getAllFncLst = prvRepo.save(new AppPrivilege(null, "GET-ALL-FNC-LST", "Consulter la liste de toutes les fonctions actives d'un utilisateur", typeRepo.findByUniqueCode("PRV-FONC")));
        AppPrivilege crtPrv = prvRepo.save(new AppPrivilege(null, "CRT-PRV", "Créer un privilège", typeRepo.findByUniqueCode("PRV-PRV")));
        AppPrivilege getPrvLst = prvRepo.save(new AppPrivilege(null, "GET-PRV-LST", "Consulter la liste des privilèges", typeRepo.findByUniqueCode("PRV-PRV")));
        AppPrivilege crtRol = prvRepo.save(new AppPrivilege(null, "CRT-ROL", "Créer un rôle", typeRepo.findByUniqueCode("PRV-ROL")));
        AppPrivilege getRolLst = prvRepo.save(new AppPrivilege(null, "GET-ROL-LST", "Consulter la liste des rôles", typeRepo.findByUniqueCode("PRV-ROL")));
        AppPrivilege updRolLst = prvRepo.save(new AppPrivilege(null, "UPD-ROL", "Modifier un rôle", typeRepo.findByUniqueCode("PRV-ROL")));
        //FAC
        AppPrivilege getFacLst = prvRepo.save(new AppPrivilege(null, "GET-FAC-LST", "Consulter liste des affaires facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacCSai = prvRepo.save(new AppPrivilege(null, "GET-FAC-C-SAI", "Consulter liste des fac en cours de saisie", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacCPla = prvRepo.save(new AppPrivilege(null, "GET-FAC-C-PLA", "Consulter liste des fac en cours de placement", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacCReg = prvRepo.save(new AppPrivilege(null, "GET-FAC-C-REG", "Consulter liste des fac en cours de règlement", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacArch = prvRepo.save(new AppPrivilege(null, "GET-FAC-ARCH", "Consulter liste des fac archivées", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacHist = prvRepo.save(new AppPrivilege(null, "GET-FAC-HIST", "Consulter l'historique des facs", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege crtFac = prvRepo.save(new AppPrivilege(null, "CRT-FAC", "Enregistrer affaires facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege updFac = prvRepo.save(new AppPrivilege(null, "UPD-FAC", "Modifier affaires facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacDet = prvRepo.save(new AppPrivilege(null, "GET-FAC-DET", "Consulter détail affaire", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege sendFac = prvRepo.save(new AppPrivilege(null, "SEND-FAC", "Transmettre une affaires facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege crtPlaFac = prvRepo.save(new AppPrivilege(null, "CRT-PLA-FAC", "Enregistrer un placement sur une affaires facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege valFac = prvRepo.save(new AppPrivilege(null, "VAL-FAC", "Valider une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege retFac = prvRepo.save(new AppPrivilege(null, "RET-FAC", "Retourner une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege archFac = prvRepo.save(new AppPrivilege(null, "ARCH-FAC", "Archiver une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getEtaComptFac = prvRepo.save(new AppPrivilege(null, "GET-ETA-COMPT-FAC", "Consulter la situation comptable d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege markFacRea = prvRepo.save(new AppPrivilege(null, "MARK-FAC-REA", "Marquer une affaire facultative comme réalisée", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege markFacNonRea = prvRepo.save(new AppPrivilege(null, "MARK-FAC-NON-REA", "Marquer une affaire facultative comme non réalisée", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege addDocFac = prvRepo.save(new AppPrivilege(null, "ADD-DOC-FAC", "Joindre une pièce à une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        //TRAI
        AppPrivilege getTraiLst = prvRepo.save(new AppPrivilege(null, "GET- TRAI-LST", "Consulter liste des Traités", typeRepo.findByUniqueCode("PRV-TRAI")));
        AppPrivilege getTraiCSai = prvRepo.save(new AppPrivilege(null, "GET-TRAI-C-SAI", "Consulter liste des Traités en cours de saisie", typeRepo.findByUniqueCode("PRV-TRAI")));
        //
        AppPrivilege crtBank = prvRepo.save(new AppPrivilege(null, "CRT-BANK", "Enregistrer une banque", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updBank = prvRepo.save(new AppPrivilege(null, "UPD-BANK", "Modifier une banque", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getBankLst = prvRepo.save(new AppPrivilege(null, "GET-BANK-LST", "Consulter la liste des banques", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtBran = prvRepo.save(new AppPrivilege(null, "CRT-BRAN", "Enregistrer une branche", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updBran = prvRepo.save(new AppPrivilege(null, "UPD-BRAN", "Modifier une branche", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getBranLst = prvRepo.save(new AppPrivilege(null, "GET-BRAN-LST", "Consulter la liste des branches", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtCed = prvRepo.save(new AppPrivilege(null, "CRT-CED", "Enregistrer une Cedante", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updCed = prvRepo.save(new AppPrivilege(null, "UPD-CED", "Modifier une Cedante", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getCedLst = prvRepo.save(new AppPrivilege(null, "GET-CED-LST", "Consulter la liste des Cedantes", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtCes = prvRepo.save(new AppPrivilege(null, "CRT-CES", "Enregistrer un cessionnaire", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updCes = prvRepo.save(new AppPrivilege(null, "UPD-CES", "Modifier un cessionnaire", typeRepo.findByUniqueCode("PRV-PARAM")));

        AppPrivilege getCesLegParam = prvRepo.save(new AppPrivilege(null, "GET-CES-LEG-PARAM-LST", "Consulter la liste des paramètres de cession légale", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtCesLegParam = prvRepo.save(new AppPrivilege(null, "CRT-CES-LEG-PARAM", "Enregistrer un paramètre de cession légale", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updCesLegParam = prvRepo.save(new AppPrivilege(null, "UPD-CES-LEG-PARAM", "Modifier un paramètre de cession légale", typeRepo.findByUniqueCode("PRV-PARAM")));


        AppPrivilege getCesLst = prvRepo.save(new AppPrivilege(null, "GET-CES-LST", "Consulter la liste des cessionnaires", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getCesAffLst = prvRepo.save(new AppPrivilege(null, "GET-CES-AFF-LST", "Consulter la liste des cessionnaires sur une affaire", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege crtCouv = prvRepo.save(new AppPrivilege(null, "CRT-COUV", "Enregistrer une couverture", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updCouv = prvRepo.save(new AppPrivilege(null, "UPD-COUV", "Modifier une couverture", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getCouvLst = prvRepo.save(new AppPrivilege(null, "GET-COUV-LST", "Consulter la liste des couvertures", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtDev = prvRepo.save(new AppPrivilege(null, "CRT-DEV", "Enregistrer une devise", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updDev = prvRepo.save(new AppPrivilege(null, "UPD-DEV", "Modifier une devise", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getDevLst = prvRepo.save(new AppPrivilege(null, "GET-DEV-LST", "Consulter la liste des devises", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtExe = prvRepo.save(new AppPrivilege(null, "CRT-EXE", "Enregistrer un exercice", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updExe = prvRepo.save(new AppPrivilege(null, "UPD-EXE", "Modifier un exercice", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getExeLst = prvRepo.save(new AppPrivilege(null, "GET-EXE-LST", "Consulter la liste des exercices", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege crtPay = prvRepo.save(new AppPrivilege(null, "CRT-PAY", "Enregistrer un pays", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege updPay = prvRepo.save(new AppPrivilege(null, "UPD-PAY", "Modifier un pays", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getPayLst = prvRepo.save(new AppPrivilege(null, "GET-PAY-LST", "Consulter la liste des pays", typeRepo.findByUniqueCode("PRV-PARAM")));
        AppPrivilege getFacMouv = prvRepo.save(new AppPrivilege(null, "GET-FAC-MOUV", "Consulter la liste des mouvements d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacRefuMsg = prvRepo.save(new AppPrivilege(null, "GET-FAC-REFU-MSG", "Consulter le motif de refus d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getFacRetMsg = prvRepo.save(new AppPrivilege(null, "GET-FAC-RET-MSG", "Consulter le motif de retour d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege getPlaRetMsg = prvRepo.save(new AppPrivilege(null, "GET-PLA-RET-MSG", "Consulter le motif de retour d'un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege crtPaiFac = prvRepo.save(new AppPrivilege(null, "CRT-PAI-FAC", "Enregistrer un paiement sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege crtRevFac = prvRepo.save(new AppPrivilege(null, "CRT-REV-FAC", "Enregistrer un reversement sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));

        AppPrivilege getRegFacLst = prvRepo.save(new AppPrivilege(null, "GET-REG-FAC-LST", "Consulter la liste des règlements sur affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege getRegTraiLst = prvRepo.save(new AppPrivilege(null, "GET-REG-TRAI-LST", "Consulter la liste des règlements sur traité", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege getRegSinLst = prvRepo.save(new AppPrivilege(null, "GET-REG-SIN-LST", "Consulter la liste des règlements sur sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));


        AppPrivilege getPaiFacLst = prvRepo.save(new AppPrivilege(null, "GET-PAI-FAC-LST", "Consulter la liste des paiements sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege getRevFacLst = prvRepo.save(new AppPrivilege(null, "GET-REV-FAC-LST", "Consulter la liste des reversements sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege updPaiFac = prvRepo.save(new AppPrivilege(null, "UPD-PAI-FAC", "Modifier un paiement sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege updRevFac = prvRepo.save(new AppPrivilege(null, "UPD-REV-FAC", "Modifier un reversement sur une affaire facultative", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege crtPaiSin = prvRepo.save(new AppPrivilege(null, "CRT-PAI-SIN", "Enregistrer un paiement sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege crtRevSin = prvRepo.save(new AppPrivilege(null, "CRT-REV-SIN", "Enregistrer un reversement sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege getPaiSinLst = prvRepo.save(new AppPrivilege(null, "GET-PAI-SIN-LST", "Consulter la liste des paiements sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege getRevSinLst = prvRepo.save(new AppPrivilege(null, "GET-REV-SIN-LST", "Consulter la liste des reversements sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege updPaiSin = prvRepo.save(new AppPrivilege(null, "UPD-PAI-SIN", "Modifier un paiement sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege updRevSin = prvRepo.save(new AppPrivilege(null, "UPD-REV-SIN", "Modifier un reversement sur un sinistre", typeRepo.findByUniqueCode("PRV-COMPTA")));
        AppPrivilege crtSta = prvRepo.save(new AppPrivilege(null, "CRT-STA", "Enregistrer un Statut", typeRepo.findByUniqueCode("PRV-DEV")));
        AppPrivilege updSta = prvRepo.save(new AppPrivilege(null, "UPD-STA", "Modifier un Statut", typeRepo.findByUniqueCode("PRV-DEV")));
        AppPrivilege getStaLst = prvRepo.save(new AppPrivilege(null, "GET-STA-LST", "Consulter la liste des Statut", typeRepo.findByUniqueCode("PRV-DEV")));
        AppPrivilege acptPla = prvRepo.save(new AppPrivilege(null, "ACPT-PLA", "Accepter un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege anlPla = prvRepo.save(new AppPrivilege(null, "ANL-PLA", "Annler un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege refuPla = prvRepo.save(new AppPrivilege(null, "REFU-PLA", "Refuser un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege retPla = prvRepo.save(new AppPrivilege(null, "RET-PLA", "retourner un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege valPla = prvRepo.save(new AppPrivilege(null, "VAL-PLA", "Valider un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege getPlaCSai = prvRepo.save(new AppPrivilege(null, "GET-PLA-C-SAI", "Consulter la liste des placement en cours de saisie", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege getPlaAVal = prvRepo.save(new AppPrivilege(null, "GET-PLA-A-VAL", "Consulter la liste des placement en attente de validation", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege getPlaVal = prvRepo.save(new AppPrivilege(null, "GET-PLA-VAL", "Consulter la liste des placement validé", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege crtCedLegRep = prvRepo.save(new AppPrivilege(null, "CRT-CED-LEG-REP", "Enregistrer une répartition pour cedante et cessionnaires légaux", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege crtPla = prvRepo.save(new AppPrivilege(null, "CRT-PLA", "Enregistrer un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege dltPla = prvRepo.save(new AppPrivilege(null, "DLT-PLA", "Supprimer un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege getAffPcl = prvRepo.save(new AppPrivilege(null, "GET-AFF-PCL", "Consulter la liste des paramètres de cession légale sur une affaire", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege updPla = prvRepo.save(new AppPrivilege(null, "UPD-PLA", "Modifier un placement", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege transPla = prvRepo.save(new AppPrivilege(null, "TRANS-PLA", "Transmettre un placement pour validation", typeRepo.findByUniqueCode("PRV-REP")));
        AppPrivilege updRep = prvRepo.save(new AppPrivilege(null, "UPD-REP", "Modifier une repartition", typeRepo.findByUniqueCode("PRV-REP")));
        //SINISTRE FAC
        AppPrivilege crtSin = prvRepo.save(new AppPrivilege(null, "CRT-SIN", "Enregistrer un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege updSin = prvRepo.save(new AppPrivilege(null, "UPD-SIN", "Modifier un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege delSin = prvRepo.save(new AppPrivilege(null, "DEL-SIN", "Supprimer un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege transSin = prvRepo.save(new AppPrivilege(null, "TRANS-SIN", "Transmettre un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege getSinLst = prvRepo.save(new AppPrivilege(null, "GET-SIN-LST", "Consulter la liste des sinistres", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege getSinFacLst = prvRepo.save(new AppPrivilege(null, "GET-SIN-FAC-LST", "Consulter la liste des sinistres sur affaire facultative", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege getSinTraiLst = prvRepo.save(new AppPrivilege(null, "GET-SIN-TRAI-LST", "Consulter la liste des sinistres sur traité", typeRepo.findByUniqueCode("PRV-SIN")));



        AppPrivilege getEtaComptSin = prvRepo.save(new AppPrivilege(null, "GET-ETA-COMPT-SIN", "Consulter la situation comptable d'un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege valSin = prvRepo.save(new AppPrivilege(null, "VAL-SIN", "Valider un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege retSin = prvRepo.save(new AppPrivilege(null, "RET-SIN", "retourner un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege editChq = prvRepo.save(new AppPrivilege(null, "EDIT-CHQ", "Éditer un chèque de reversement sur une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege sendNotDebFac = prvRepo.save(new AppPrivilege(null, "SEND-NOT-DEB-FAC", "Envoyer Note de débit d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege sendNotCredFac = prvRepo.save(new AppPrivilege(null, "SEND-NOT-CRED-FAC", "Envoyer la note de crédit d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege sendNotCesFac = prvRepo.save(new AppPrivilege(null, "SEND-NOT-CES-FAC", "Envoyer la note de cession d'une affaire facultative", typeRepo.findByUniqueCode("PRV-FAC")));
        AppPrivilege editChqRevSin = prvRepo.save(new AppPrivilege(null, "EDIT-CHQ-REV-SIN", "Éditer un chèque de reversement sur un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege sendNotDebSin = prvRepo.save(new AppPrivilege(null, "SEND-NOT-DEB-SIN", "Envoyer Note de débit d'un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege sendNotCredSin = prvRepo.save(new AppPrivilege(null, "SEND-NOT-CRED-SIN", "Envoyer la note de crédit d'un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));
        AppPrivilege sendNotCesSin = prvRepo.save(new AppPrivilege(null, "SEND-NOT-CES-SIN", "Envoyer la note de cession d'un sinistre", typeRepo.findByUniqueCode("PRV-SIN")));

        //LOG
        AppPrivilege getLogHisto = prvRepo.save(new AppPrivilege(null, "GET-LOG-HISTO", "Consulter l'historique de connexion", typeRepo.findByUniqueCode("PRV-ADM")));
        AppPrivilege getLogSystem = prvRepo.save(new AppPrivilege(null, "GET-LOG-SYST", "Consulter le log système", typeRepo.findByUniqueCode("PRV-DEV")));
        AppPrivilege delLogSystem = prvRepo.save(new AppPrivilege(null, "DEL-LOG-SYST", "Supprimer le log système", typeRepo.findByUniqueCode("PRV-DEV")));

    }
}
