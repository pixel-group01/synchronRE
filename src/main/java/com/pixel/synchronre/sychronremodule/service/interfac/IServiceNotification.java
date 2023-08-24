package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationResp;

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
    NotificationResp getNotifications();
}