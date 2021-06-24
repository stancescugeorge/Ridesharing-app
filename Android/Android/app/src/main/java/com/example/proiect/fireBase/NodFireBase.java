package com.example.proiect.fireBase;

import java.io.Serializable;
import java.util.Date;

public class NodFireBase implements Serializable {
    private String id;
    private String numeSofer;
    private String locatiePlecare;
    private String locatieDestinatie;
    private String ora;
    private String data;
    private String durata;
    private String pret;
    private String telefon;

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public NodFireBase(String id, String numeSofer, String locatiePlecare, String locatieDestinatie, String ora, String data, String pret,String telefon,String durata) {
        this.id = id;
        this.numeSofer = numeSofer;
        this.locatiePlecare = locatiePlecare;
        this.locatieDestinatie = locatieDestinatie;
        this.ora = ora;
        this.data = data;
        this.pret = pret;
        this.durata=durata;
        this.telefon=telefon;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NodFireBase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeSofer() {
        return numeSofer;
    }

    public void setNumeSofer(String numeSofer) {
        this.numeSofer = numeSofer;
    }

    public String getLocatiePlecare() {
        return locatiePlecare;
    }

    public void setLocatiePlecare(String locatiePlecare) {
        this.locatiePlecare = locatiePlecare;
    }

    public String getLocatieDestinatie() {
        return locatieDestinatie;
    }

    public void setLocatieDestinatie(String locatieDestinatie) {
        this.locatieDestinatie = locatieDestinatie;
    }



    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }
}
