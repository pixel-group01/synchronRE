package com.pixel.synchronre.sychronremodule.model.dto.mouvement.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationResp
{
    private long totalNotifications;
    private long affairesEnAttenteDePlacement;
    private long affairesRetourneesALaCedante;
    private long affairesEnAttenteDeReglement;
    private long placementsEnAttenteDeValidation;
    private long placementRetourneAuSouscripteur;
    private long sinistreTransmisAuSouscripteur;
    private long sinistreRetournesALaCedante;
    private long sinistreEnAttenteDeValidation;
    private long sinistreRetournesAuSouscripteur;
    private long sinistreEnAttenteDeReglement;
    private long sinistreRetourneAuValidateur;
    private long sinistreEnCoursDeReversement;
}