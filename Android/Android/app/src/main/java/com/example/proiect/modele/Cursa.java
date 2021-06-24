package com.example.proiect.modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "curse")
public class Cursa implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cursa")
    private long id_cursa;

    // un utilizator are mai multe curse
    @ForeignKey
            (entity = Utilizator.class,
            parentColumns = "id_utilizator",
            childColumns = "id_fkutilizator",
            onDelete = CASCADE)
    @ColumnInfo(name = "id_utilizator")
    private long id_fkutilizator;

    @ColumnInfo(name = "locatiePlecare")
    private String locatiePlecare;
    @ColumnInfo(name = "locatieDestinatie")
    private String locatieDestinatie;
    @ColumnInfo(name = "data")
    private Date data;
    @ColumnInfo(name = "ora")
    private String ora;
    @ColumnInfo(name = "durata")
    private int durata;
    @ColumnInfo(name = "sofer")
    private int sofer; // 0 - utilizatorul = pasager, 1 - sofer

    public Cursa(long id_cursa, long id_fkutilizator, String locatiePlecare, String locatieDestinatie, Date data, String ora, Integer durata, int sofer) {
        this.id_cursa = id_cursa;
        this.id_fkutilizator = id_fkutilizator;
        this.locatiePlecare = locatiePlecare;
        this.locatieDestinatie = locatieDestinatie;
        this.data = data;
        this.ora=ora;
        this.durata = durata;
        this.sofer = sofer;
    }

    @Ignore
    public Cursa(long id_fkutilizator, String locatiePlecare, String locatieDestinatie, Date data, String ora, int durata, int sofer) {
        this.id_fkutilizator = id_fkutilizator;
        this.locatiePlecare = locatiePlecare;
        this.locatieDestinatie = locatieDestinatie;
        this.data = data;
        this.ora=ora;
        this.durata = durata;
        this.sofer = sofer;
    }

    public long getId_cursa() {
        return id_cursa;
    }

    public void setId_cursa(long id_cursa) {
        this.id_cursa = id_cursa;
    }

    public long getId_fkutilizator() {
        return id_fkutilizator;
    }

    public void setId_fkutilizator(long id_fkutilizator) {
        this.id_fkutilizator = id_fkutilizator;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public int getSofer() {
        return sofer;
    }

    public void setSofer(int sofer) {
        this.sofer = sofer;
    }

    @Override
    public String toString() {
        return "Cursa{" +
                "id_cursa='"+ id_cursa + '\'' +
                "id_fkutilizator='"+ id_fkutilizator + '\'' +
                "locatiePlecare='" + locatiePlecare + '\'' +
                ", locatieDestinatie='" + locatieDestinatie + '\'' +
                ", data=" + data +
                ", durata=" + durata +
                ", sofer=" + sofer +
                '}';
    }
}
