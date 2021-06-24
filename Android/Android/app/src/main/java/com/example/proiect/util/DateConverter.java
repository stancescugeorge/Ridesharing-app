package com.example.proiect.util;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE, Locale.US);

    @TypeConverter //fol pt 2 metode statice publice, adnotarea ii spune Room-ului ca are metode care primesc String si intoarce Date + invers
    public static Date fromString(String value) {
        try {
            //metoda parsa este folosita pentru conversia String to Date
            return formatter.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String fromDate(Date value) {
        if (value == null) {
            return null;
        }
        //metoda format este utilizata pentru conversia Date to String
        return formatter.format(value);
    }
}
