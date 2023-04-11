package com.pixel.synchronre.sychronremodule.service.interfac;

public interface IserviceAffaire {
    float calculateRestARepartir(Long affId);

    float calculateDejaRepartir(Long affId);

    float calculateTauxDejaRepartir(Long affId);
    float calculateRestTauxARepartir(Long affId);
}
