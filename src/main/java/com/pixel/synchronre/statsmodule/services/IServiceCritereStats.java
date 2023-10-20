package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;

public interface IServiceCritereStats
{
    CritereStat initCriteres();
    CritereStat initCriteres(CritereStat critereStat);
}
