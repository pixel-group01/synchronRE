package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.NotificationRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationBody;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationsResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationUnitaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor
public class ServiceNotificationImpl implements IServiceNotification
{
    private final NotificationRepository notifRepo;
    private final IJwtService jwtService;

    @Override
    public long countTotalNotifications() {
        long totalNotifications = countAffairesEnAttenteDePlacement() +
                countAffairesRetourneesALaCedante() +
                countAffairesEnAttenteDeReglement() +
                countPlacementsEnAttenteDeValidation() +
                countPlacementRetourneAuSouscripteur() +
                countSinistreTransmisAuSouscripteur() +
                countSinistreRetournesALaCedante() +
                countSinistreEnAttenteDeValidation() +
                countSinistreRetournesAuSouscripteur()+
                countSinistreEnAttenteDeReglement() +
                countSinistreRetourneAuValidateur() +
                countSinistreEnCoursDeReglement();
        return totalNotifications;
    }

    @Override
    public long countAffairesEnAttenteDePlacement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PLA", "RET-FAC-CED");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority? 0 : notifRepo.countAffaires(Collections.singletonList("APLA"), null);
    }

    @Override
    public long countAffairesRetourneesALaCedante()
    {
        boolean isCourtier = jwtService.UserIsCourtier();
        Long cedId = jwtService.getConnectedUserCedId();
        return isCourtier ? 0 : notifRepo.countAffaires(Collections.singletonList("RET"), cedId);
    }

    @Override
    public long countAffairesEnAttenteDeReglement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PAI-FAC", "CRT-REV-FAC");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countAffaires(Collections.singletonList("APAI"), null);
    }

    @Override
    public long countPlacementsEnAttenteDeValidation()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("VAL-PLA", "RET-PLA");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countPlacements(Collections.singletonList("AVAL"), null);
    }

    @Override
    public long countPlacementRetourneAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("ACPT-PLA", "ANL-PLA", "REFU-PLA","TRANS-PLA", "DLT-PLA");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countPlacements(Collections.singletonList("RET"), null);
    }

    @Override
    public long countSinistreTransmisAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("TRANS-SIN-VAL");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("TRA"), null);
    }

    @Override
    public long countSinistreRetournesALaCedante()
    {
        boolean isCourtier = jwtService.UserIsCourtier();
        Long cedId = jwtService.getConnectedUserCedId();
        return isCourtier ? 0 : notifRepo.countSinistres(Collections.singletonList("RET"), cedId);
    }

    @Override
    public long countSinistreEnAttenteDeValidation()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("VAL-SIN", "RET-SIN-SOUS");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("AVAL"), null);
    }

    @Override
    public long countSinistreRetournesAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("TRANS-SIN-VAL", "RET-SIN-CED");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("RET-VAL"), null);
    }

    @Override
    public long countSinistreEnAttenteDeReglement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PAI-SIN");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("APAI"), null);
    }

    @Override
    public long countSinistreRetourneAuValidateur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("VAL-SIN", "RET-SIN-SOUS");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("RET-COMPTA"), null);
    }

    @Override
    public long countSinistreEnCoursDeReglement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PAI-SIN", "CRT-REV-SIN", "UPD-PAI-SIN", "UPD-REV-SIN");
        boolean isCourtier = jwtService.UserIsCourtier();
        return !isCourtier || !hasAuthority ? 0 : notifRepo.countSinistres(Arrays.asList("CPAI", "CPAI-CREV", "CREV"), null);
    }

    @Override
    public NotificationsResp getNotifications()
    {
        NotificationsResp notification = new NotificationsResp();
        notification.setTotalNotifications(this.countTotalNotifications());
        notification.setAffairesEnAttenteDePlacement(this.countAffairesEnAttenteDePlacement());
        notification.setAffairesRetourneesALaCedante(this.countAffairesRetourneesALaCedante());
        notification.setAffairesEnAttenteDeReglement(this.countAffairesEnAttenteDeReglement());
        notification.setPlacementsEnAttenteDeValidation(this.countPlacementsEnAttenteDeValidation());
        notification.setPlacementRetourneAuSouscripteur(this.countPlacementRetourneAuSouscripteur());
        notification.setSinistreRetournesAuSouscripteur(this.countSinistreTransmisAuSouscripteur());
        notification.setSinistreRetournesALaCedante(this.countSinistreRetournesALaCedante());
        notification.setSinistreEnAttenteDeValidation(this.countSinistreEnAttenteDeValidation());
        notification.setSinistreRetournesAuSouscripteur(this.countSinistreRetournesAuSouscripteur());
        notification.setSinistreEnAttenteDeReglement(this.countSinistreEnAttenteDeReglement());
        notification.setSinistreRetourneAuValidateur(this.countSinistreRetourneAuValidateur());
        notification.setSinistreEnCoursDeReglement(this.countSinistreEnCoursDeReglement());
      return notification;
    }

    @Override
    public NotificationBody getNotificationUnitaires()
    {
        List<NotificationUnitaire> notificationUnitaires =new ArrayList<>();
        //notificationUnitaires.add(new NotificationUnitaire("total", this.countTotalNotifications(), true, true,Collections.emptyList()));
        notificationUnitaires.add(new NotificationUnitaire("Affaire(s) en attente de placement", this.countAffairesEnAttenteDePlacement(), true, false, Arrays.asList("TYF_DEV", "RET-FAC-CED"), Arrays.asList("TYF_DEV", "TYF_SOUS")));
        notificationUnitaires.add(new NotificationUnitaire("Affaire(s) retournées par le souscripteur", this.countAffairesRetourneesALaCedante(), false, true, Collections.emptyList(), Arrays.asList("TYF_DEV", "TYF_SAI_CED")));
        notificationUnitaires.add(new NotificationUnitaire("Affaire(s) en attente de règlement", this.countAffairesEnAttenteDeReglement(), true, false, Arrays.asList("CRT-PAI-FAC", "CRT-REV-FAC"), Arrays.asList("TYF_DEV", "TYF_COMPTA")));
        notificationUnitaires.add(new NotificationUnitaire("Placement(s) en attente de validation", this.countPlacementsEnAttenteDeValidation(), true, false, Arrays.asList("VAL-PLA", "RET-PLA"), Arrays.asList("TYF_DEV", "TYF_VAL")));
        notificationUnitaires.add(new NotificationUnitaire("Placement(s) retournés par le validateur", this.countPlacementRetourneAuSouscripteur(), true, false, Arrays.asList("ACPT-PLA", "ANL-PLA", "REFU-PLA","TRANS-PLA", "DLT-PLA"), Arrays.asList("TYF_DEV", "TYF_SOUS")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) transmis par une cedante", this.countSinistreTransmisAuSouscripteur(), true, false, Arrays.asList("TRANS-SIN-VAL"), Arrays.asList("TYF_DEV", "TYF_SOUS")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) retournés par le souscripteur", this.countSinistreRetournesALaCedante(), false, true, Collections.emptyList(), Arrays.asList("TYF_DEV", "TYF_SAI_CED")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) retournés par le validateur", this.countSinistreRetournesAuSouscripteur(), true, false, Arrays.asList("TRANS-SIN-VAL", "RET-SIN-CED"), Arrays.asList("TYF_DEV", "TYF_SOUS")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) en attente de validation", this.countSinistreEnAttenteDeValidation(), true, false, Arrays.asList("VAL-SIN", "RET-SIN-SOUS"), Arrays.asList("TYF_DEV", "TYF_VAL")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) en attente de règlement", this.countSinistreEnAttenteDeReglement(), true, false, Arrays.asList("CRT-PAI-SIN"), Arrays.asList("TYF_DEV", "TYF_COMPTA")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) retourné par le comptable", this.countSinistreRetourneAuValidateur(), true, false, Arrays.asList("VAL-SIN", "RET-SIN-SOUS"), Arrays.asList("TYF_DEV", "TYF_VAL")));
        notificationUnitaires.add(new NotificationUnitaire("Sinistre(s) en cours de règlement", this.countSinistreEnCoursDeReglement(), true, false, Arrays.asList("CRT-PAI-SIN", "CRT-REV-SIN", "UPD-PAI-SIN", "UPD-REV-SIN"), Arrays.asList("TYF_DEV", "TYF_COMPTA")));
        NotificationBody body = new NotificationBody(this.countTotalNotifications(), notificationUnitaires);
        return body;
    }
}