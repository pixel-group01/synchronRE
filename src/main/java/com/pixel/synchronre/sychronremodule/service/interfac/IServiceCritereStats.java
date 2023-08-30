package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;

public interface IServiceCritereStats
{
    CritereStat initCriteres();
    CritereStat initCriteres(CritereStat critereStat);
}
