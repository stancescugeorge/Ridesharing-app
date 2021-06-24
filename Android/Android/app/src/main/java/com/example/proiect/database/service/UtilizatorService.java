package com.example.proiect.database.service;

import android.content.Context;

import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.DatabaseManager;
import com.example.proiect.database.dao.UtilizatorDao;
import com.example.proiect.modele.Utilizator;

import java.util.List;
import java.util.concurrent.Callable;

public class UtilizatorService {
    private final UtilizatorDao utilizatorDao;
    private final AsyncTaskRunner taskRunner;

    public UtilizatorService(Context context) {
        utilizatorDao = DatabaseManager.getInstance(context).getUtilizatorDao();
        taskRunner=new AsyncTaskRunner();
    }

    public void getUser(Callback<Utilizator> callback, final String numeUtilizator, final String parola){
        Callable<Utilizator> callable = new Callable<Utilizator>() {
            @Override
            public Utilizator call() throws Exception {
                return utilizatorDao.getUser(numeUtilizator,parola);
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void getAll(Callback<List<Utilizator>> callback){
        Callable<List<Utilizator>> callable = new Callable<List<Utilizator>>() {
            @Override
            public List<Utilizator> call() throws Exception {
                return utilizatorDao.getAll();
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<Utilizator> callback, final Utilizator utilizator){
        Callable<Utilizator> callable=new Callable<Utilizator>() {
            @Override
            public Utilizator call() throws Exception {
                if(utilizator==null){
                    return null;
                }
                long id= utilizatorDao.insert(utilizator);
                if(id==-1){
                    return null;
                }
                utilizator.setId_utilizator(id);
                return utilizator;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<Utilizator> callback, final Utilizator utilizator){
        Callable<Utilizator> callable = new Callable<Utilizator>() {
            @Override
            public Utilizator call() throws Exception {
                if(utilizator==null){
                    return null;
                }
                int count=utilizatorDao.update(utilizator);
                if(count!=1){
                    return null;
                }
                return utilizator;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void delete(Callback<Integer> callback, final Utilizator utilizator){
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if(utilizator==null){
                    return null;
                }
                return utilizatorDao.delete(utilizator);
            }
        };
        taskRunner.executeAsync(callable,callback);
    }
}
