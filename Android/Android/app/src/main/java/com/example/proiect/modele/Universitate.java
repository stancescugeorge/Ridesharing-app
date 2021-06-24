package com.example.proiect.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Universitate implements Parcelable {
    private String denumire;
    private int anFondare;
    private String adresa;

    public Universitate(String denumire, int anFondare, String adresa) {
        this.denumire = denumire;
        this.anFondare = anFondare;
        this.adresa = adresa;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getAnFondare() {
        return anFondare;
    }

    public void setAnFondare(int anFondare) {
        this.anFondare = anFondare;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Universitate{" +
                "denumire='" + denumire + '\'' +
                ", anFondare=" + anFondare +
                ", adresa='" + adresa + '\'' +
                '}';
    }

    protected Universitate(Parcel in) {
        denumire = in.readString();
        anFondare = in.readInt();
        adresa = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(denumire);
        dest.writeInt(anFondare);
        dest.writeString(adresa);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Universitate> CREATOR = new Creator<Universitate>() {
        @Override
        public Universitate createFromParcel(Parcel in) {
            return new Universitate(in);
        }

        @Override
        public Universitate[] newArray(int size) {
            return new Universitate[size];
        }
    };
}
