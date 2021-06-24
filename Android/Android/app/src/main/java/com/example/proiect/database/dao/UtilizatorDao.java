package com.example.proiect.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiect.modele.Utilizator;

import java.util.List;

@Dao
public interface UtilizatorDao {
    @Query("select * from utilizatori") // SELECT
    List<Utilizator> getAll(); //ce intoarce

    @Insert
    long insert(Utilizator utilizator); //intoarce id-ul noii inregistrari SAU -1 dc apar probleme

    @Update
    int update(Utilizator utilizator); // intoarce nr randuri afectate (-1 probleme), in cazul acesta mereu 1 pt ca avem un update per entitate (furnizam id-ul inreg pe care vrem sa o modificam)

    @Delete
    int delete(Utilizator utilizator); // nr randuri sterse sau -1 dc apar probleme

    @Query("select * from utilizatori where nume_utilizator=:numeUtilizator and parola=:parola")
    Utilizator getUser(String numeUtilizator, String parola);

}
