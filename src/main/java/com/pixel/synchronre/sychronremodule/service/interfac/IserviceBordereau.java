package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.entities.Bordereau;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;

public interface IserviceBordereau
{
    Bordereau createNoteCession(Long plaId);
    Bordereau createNoteDebit(Long affId);
    void updateDetailsBordereaux(Repartition updatedPlacement);
    void deleteBordereau(Long bordId);

    void deleteDetailBordereau(Long debId);
}
