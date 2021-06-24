package com.example.proiect.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiect.modele.Cursa;

import java.util.List;

@Dao
public interface CursaDao {
    @Query("select * from curse where id_utilizator=:idUtilizator order by data desc,ora") // SELECT
    List<Cursa> getAll(long idUtilizator); //ce intoarce

    @Insert
    long insert(Cursa cursa); //intoarce id-ul noii inregistrari SAU -1 dc apar probleme

    @Update
    int update(Cursa cursa); // intoarce nr randuri afectate (-1 probleme), in cazul acesta mereu 1 pt ca avem un update per entitate (furnizam id-ul inreg pe care vrem sa o modificam)

    @Delete
    int delete(Cursa cursa); // nr randuri sterse sau -1 dc apar probleme
}
