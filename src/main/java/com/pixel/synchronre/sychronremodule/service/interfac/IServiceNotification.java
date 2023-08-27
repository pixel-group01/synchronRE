package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationBody;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationsResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationUnitaire;

import java.util.List;

public interface IServiceNotification
{
    long countTotalNotifications();
    long countAffairesEnAttenteDePlacement();
    long countAffairesRetourneesALaCedante();
    long countAffairesEnAttenteDeReglement();
    long countPlacementsEnAttenteDeValidation();
    long countPlacementRetourneAuSouscripteur();
    long countSinistreTransmisAuSouscripteur();
    long countSinistreRetournesALaCedante();
    long countSinistreEnAttenteDeValidation();
    long countSinistreRetournesAuSouscripteur();
    long countSinistreEnAttenteDeReglement();
    long countSinistreRetourneAuValidateur();
    long countSinistreEnCoursDeReglement();
    NotificationsResp getNotifications();
    NotificationBody getNotificationUnitaires();
}