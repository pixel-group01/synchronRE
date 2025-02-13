package com.pixel.synchronre.reportmodule.scriptlets;

import com.pixel.synchronre.sharedmodule.utilities.NumberFormatter;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

import java.math.BigDecimal;

public class NumberFormatterScriptlet extends JRDefaultScriptlet
{
    public String format(BigDecimal montant)
    {
        return NumberFormatter.format(montant);
    }
}
