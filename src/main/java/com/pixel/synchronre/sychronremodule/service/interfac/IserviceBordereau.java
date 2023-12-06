package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;

public interface IserviceBordereau
{
    Bordereau createNoteCession(Long plaId);
    Bordereau createNoteDebit(Long affId);
}
