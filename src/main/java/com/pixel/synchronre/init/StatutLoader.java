package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class StatutLoader implements Loader
{
    private final StatutRepository staRepo;
    @Override
    public void load() {
        Statut s1 = new Statut("SAI", "Saisie", "Saisie", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s2 = new Statut("TRA", "Transmis(e)", "Transmis", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s3 = new Statut("RET", "Retourné(e)", "Retourné(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s4 = new Statut("VAL", "Validé(e)", "Validé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s5 = new Statut("ARC", "Archivé(e)", "Archivé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s6 = new Statut("SUP", "Supprimé(e)", "supprimé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());

        Statut s7 = new Statut("ACT", "Actif", "Actif", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());
        Statut s8 = new Statut("SUPP", "Supprimée", "Supprimé", TypeStatut.PERSISTANCE, LocalDateTime.now(), LocalDateTime.now());


        Statut s9 = new Statut("APLA", "Attente de placement", "Affaire en attente de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
        Statut S10 = new Statut("CPLA", "En cours de placement", "Affaire en cours de placement", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());
        Statut S11 = new Statut("CREP", "En cours de repartition", "Affaire en cours de repartition", TypeStatut.AFFAIRE, LocalDateTime.now(), LocalDateTime.now());

        Statut s12 = new Statut("AVAL", "En attente de validation", "En attente de validation", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut S13 = new Statut("VAL", "Validé(e)", "Validé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut S14 = new Statut("ACONF", "En attente de confirmation", "En attente de confirmation", TypeStatut.PLACEMENT, LocalDateTime.now(), LocalDateTime.now());
        Statut s15 = new Statut("REFUSE", "Refusé(e)", "Refusé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut S16 = new Statut("ANNULE", "Annulé(e)", "Annulé(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut S17 = new Statut("MOD", "Modifié(e)", "Modifié(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut staMail = new Statut("MAIL", "Mail", "Mail envoyé", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s18 = new Statut("ACCEPTE", "Accepté(e)", "Accepté(e)", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());

        //CPAI-CREV

        Statut s19 = new Statut("SAI-CRT", "Saisie courtier", "Saisie(e) par le courtier", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s20 = new Statut("APAI", "En attente de paiement", "En attente de paiement", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut s21 = new Statut("CPAI", "En cours de paiement", "En cours de paiement", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());

        Statut crev = new Statut("CREV", "En cours de reversement", "En cours de reversement", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        Statut cpai_crev = new Statut("CPAI-CREV", "En cours de paiement et de reversement", "En cours de paiement et de reversement", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());

        Statut s22 = new Statut("SOLD", "Règlement soldé", "Règlement soldé", TypeStatut.PARTAGE, LocalDateTime.now(), LocalDateTime.now());
        staRepo.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8,s9,S10,S11, s12,S13,S14, s15,S16,S17, s18, s19, s20, s21, s22, staMail, crev, cpai_crev ));

    }
}
