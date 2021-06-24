package com.example.proiect.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Facultate implements Parcelable {
    private String denumire;
    private String specializare;
    private Universitate universitate;

    public Facultate(String denumire, String specializare, Universitate universitate) {
        this.denumire = denumire;
        this.specializare = specializare;
        this.universitate = universitate;
    }

    public static final Creator<Facultate> CREATOR = new Creator<Facultate>() {
        @Override
        public Facultate createFromParcel(Parcel in) {
            return new Facultate(in);
        }

        @Override
        public Facultate[] newArray(int size) {
            return new Facultate[size];
        }
    };

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }

    public Universitate getUniversitate() {
        return universitate;
    }

    public void setUniversitate(Universitate universitate) {
        this.universitate = universitate;
    }

    @Override
    public String toString() {
        return "Facultate{" +
                "denumire='" + denumire + '\'' +
                ", specializare='" + specializare + '\'' +
                ", universitate=" + universitate +
                '}';
    }

    protected Facultate(Parcel in) {
        denumire = in.readString();
        specializare = in.readString();
        universitate = in.readParcelable(Universitate.class.getClassLoader());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(denumire);
        dest.writeString(specializare);
        dest.writeParcelable(universitate, flags);
    }
}
