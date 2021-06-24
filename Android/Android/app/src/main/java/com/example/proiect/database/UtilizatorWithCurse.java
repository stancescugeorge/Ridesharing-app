package com.example.proiect.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.Utilizator;

import java.util.List;

// clasa folosita pentru one to many in room
public class UtilizatorWithCurse {
    @Embedded
    public Utilizator utilizator;
    @Relation(
            parentColumn = "id_utilizator",
            entityColumn = "id_cursa"
    )
    public List<Cursa> listaCurse;

    public UtilizatorWithCurse(Utilizator utilizator, List<Cursa> listaCurse) {
        this.utilizator = utilizator;
        this.listaCurse = listaCurse;
    }
}
