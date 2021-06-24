package com.example.proiect.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proiect.database.dao.CursaDao;
import com.example.proiect.database.dao.UtilizatorDao;
import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.util.DateConverter;

@Database(entities = {Utilizator.class, Cursa.class}, exportSchema = false,version = 1)
@TypeConverters({DateConverter.class})
public abstract class DatabaseManager extends RoomDatabase {
    private static final String MEGACAR_DB = "megacar_db";

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context,
                            DatabaseManager.class,
                            MEGACAR_DB)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return databaseManager;
    }

     //conexiune pt a face DML-uri
    public abstract CursaDao getCursaDao();
    public abstract UtilizatorDao getUtilizatorDao();

}
