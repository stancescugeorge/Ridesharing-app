package com.example.proiect.modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "utilizatori")
public class Utilizator implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_utilizator")
    private long id_utilizator;

    @ColumnInfo(name = "nume_utilizator")
    private String numeUtilizator;
    @ColumnInfo(name = "parola")
    private String parola;
    @ColumnInfo(name = "nume_prenume")
    private String numePrenume;
    @ColumnInfo(name = "telefon")
    private String telefon;

    public Utilizator(long id_utilizator, String numeUtilizator, String parola, String numePrenume, String telefon) {
        this.id_utilizator = id_utilizator;
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
        this.numePrenume = numePrenume;
        this.telefon = telefon;
    }

    @Ignore
    public Utilizator(String numeUtilizator, String parola, String numePrenume, String telefon) {
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
        this.numePrenume = numePrenume;
        this.telefon = telefon;
    }

    public long getId_utilizator() {
        return id_utilizator;
    }

    public void setId_utilizator(long id_utilizator) {
        this.id_utilizator = id_utilizator;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getNumePrenume() {
        return numePrenume;
    }

    public void setNumePrenume(String numePrenume) {
        this.numePrenume = numePrenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "idUtilizator='" + id_utilizator + '\'' +
                "numeUtilizator='" + numeUtilizator + '\'' +
                ", parola='" + parola + '\'' +
                ", numePrenume='" + numePrenume + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}
