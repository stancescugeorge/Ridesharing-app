package com.example.proiect.modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Fondator implements Parcelable {
    private String nume;
    private String prenume;
    private String telefon;
    private Facultate facultate;

    public Fondator(String nume, String prenume, String telefon, Facultate facultate) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.facultate = facultate;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Facultate getFacultate() {
        return facultate;
    }

    public void setFacultate(Facultate facultate) {
        this.facultate = facultate;
    }

    @Override
    public String toString() {
        return "Fondator{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", telefon=" + telefon +
                ", facultate=" + facultate +
                '}';
    }

    protected Fondator(Parcel in) {
        nume = in.readString();
        prenume = in.readString();
        telefon = in.readString();
        facultate = in.readParcelable(Facultate.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeString(prenume);
        dest.writeString(telefon);
        dest.writeParcelable(facultate, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Fondator> CREATOR = new Creator<Fondator>() {
        @Override
        public Fondator createFromParcel(Parcel in) {
            return new Fondator(in);
        }

        @Override
        public Fondator[] newArray(int size) {
            return new Fondator[size];
        }
    };
}
