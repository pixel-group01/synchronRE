package com.pixel.synchronre.sychronremodule.service.interfac;

public interface IserviceAffaire {
    Float calculateRestARepartir(Long affId);

    Float calculateDejaRepartir(Long affId);

    Float calculateTauxDejaRepartir(Long affId);
    Float calculateRestTauxARepartir(Long affId);
}
