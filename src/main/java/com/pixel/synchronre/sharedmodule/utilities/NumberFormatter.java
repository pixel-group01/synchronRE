package com.pixel.synchronre.sharedmodule.utilities;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberFormatter
{
    private static DecimalFormat getFormater()
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Set white space as grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

        return decimalFormat;
    }

    public static String format(BigDecimal montant)
    {
        return getFormater().format(montant);
    }
}
