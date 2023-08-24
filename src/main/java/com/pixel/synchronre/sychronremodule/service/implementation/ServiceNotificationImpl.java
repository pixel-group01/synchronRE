package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.NotificationRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

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
        return !hasAuthority ? 0 : notifRepo.countAffaires(Collections.singletonList("APLA"), null);
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
        return !hasAuthority ? 0 : notifRepo.countAffaires(Collections.singletonList("APAI"), null);
    }

    @Override
    public long countPlacementsEnAttenteDeValidation()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("VAL-PLA", "RET-PLA");
        return !hasAuthority ? 0 : notifRepo.countPlacements(Collections.singletonList("AVAL"), null);
    }

    @Override
    public long countPlacementRetourneAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("ACPT-PLA", "ANL-PLA", "REFU-PLA","TRANS-PLA", "DLT-PLA");
        return !hasAuthority ? 0 : notifRepo.countPlacements(Collections.singletonList("RET"), null);
    }

    @Override
    public long countSinistreTransmisAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("TRANS-SIN-VAL");
        return !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("TRA"), null);
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
        return !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("AVAL"), null);
    }

    @Override
    public long countSinistreRetournesAuSouscripteur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("TRANS-SIN-VAL", "RET-SIN-CED");
        return !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("RET-VAL"), null);
    }

    @Override
    public long countSinistreEnAttenteDeReglement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PAI-SIN");
        return !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("APAI"), null);
    }

    @Override
    public long countSinistreRetourneAuValidateur()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("VAL-SIN", "RET-SIN-SOUS");
        return !hasAuthority ? 0 : notifRepo.countSinistres(Collections.singletonList("RET-COMPTA"), null);
    }

    @Override
    public long countSinistreEnCoursDeReglement()
    {
        boolean hasAuthority = jwtService.hasAnyAuthority("CRT-PAI-SIN", "CRT-REV-SIN", "UPD-PAI-SIN", "UPD-REV-SIN");
        return !hasAuthority ? 0 : notifRepo.countSinistres(Arrays.asList("CPAI", "CPAI-CREV", "CREV"), null);
    }

    @Override
    public NotificationResp getNotifications()
    {
        NotificationResp notification = new NotificationResp();
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
        notification.setSinistreEnCoursDeReversement(this.countSinistreEnCoursDeReglement());
      return notification;
    }
}
