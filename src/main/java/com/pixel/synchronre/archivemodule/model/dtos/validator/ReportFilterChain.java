package com.pixel.synchronre.archivemodule.model.dtos.validator;

import com.pixel.synchronre.reportmodule.config.ReportPointer;
import com.pixel.synchronre.sharedmodule.groups.WindowResolver;

public class ReportFilterChain
{
    public void chain()
    {
        System.setProperty(ReportPointer.xmlFileResolver, String.valueOf(WindowResolver.resolve()));
    }
}