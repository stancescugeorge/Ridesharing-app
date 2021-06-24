package com.example.proiect.util;

import com.example.proiect.modele.Facultate;
import com.example.proiect.modele.Fondator;
import com.example.proiect.modele.Universitate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FondatoriJsonParser {
    private static DateConverter dateConverter = new DateConverter();

    public static final String TELEFON = "telefon";
    public static final String NUME = "nume";
    public static final String PRENUME = "prenume";
    public static final String FACULTATE = "facultate";
    public static final String DENUMIRE = "denumire";
    public static final String SPECIALIZARE = "specializare";
    public static final String UNIVERSITATE = "universitate";
    public static final String AN = "an fondare";
    public static final String ADRESA = "adresa";

    public static List<Fondator> fromJson(String json) {
        if(json==null||json.isEmpty()){
            return new ArrayList<>();
        }

        try {
            JSONArray array = new JSONArray(json);
            return readFondatori(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(); //dc apare vreo eroare intorc totusi o lista goala
    }

    private static List<Fondator> readFondatori(JSONArray array) throws JSONException {
        List<Fondator> fondatori = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Fondator fondator = readFondator(array.getJSONObject(i));
            fondatori.add(fondator);
        }
        return fondatori;
    }

    private static Fondator readFondator(JSONObject object) throws JSONException {
        String nume = object.getString(NUME);
        String prenume = object.getString(PRENUME);
        String telefon = object.getString(TELEFON);
        Facultate facultate = readFacultate(object.getJSONObject(FACULTATE));

        return new Fondator(nume,prenume,telefon,facultate);
    }

    private static Facultate readFacultate(JSONObject object) throws JSONException {
        String denumire=object.getString(DENUMIRE);
        String specializare = object.getString(SPECIALIZARE);
        Universitate universitate = readUniversitate(object.getJSONObject(UNIVERSITATE)); // preluare obiect de tip Simptomatologie din interiorul obiectului Pacient

        return new Facultate(denumire,specializare,universitate);
    }

    private static Universitate readUniversitate(JSONObject object) throws JSONException {
        String denumire=object.getString(DENUMIRE);
        Integer anFondare = object.getInt(AN);
        String adresa = object.getString(ADRESA);

        return new Universitate(denumire,anFondare,adresa);
    }
}
