package com.fma.fmapos.helper;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyHelper
{
    public static final Locale LOCALE = new Locale("id", "ID");
    private static final DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(LOCALE);
    private static final DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();


    public static String format(double paramDouble)
    {
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        return numberFormat.format(paramDouble);
    }

    public static String format(long paramLong)
    {
        return numberFormat.format(paramLong);
    }


    public static Double revert(String paramString)
            throws ParseException
    {
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        return Double.valueOf(numberFormat.parse(paramString).doubleValue());
    }
}