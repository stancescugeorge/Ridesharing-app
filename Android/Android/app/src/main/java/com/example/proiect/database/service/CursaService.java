package com.example.proiect.database.service;

import android.content.Context;

import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.DatabaseManager;
import com.example.proiect.database.dao.CursaDao;
import com.example.proiect.modele.Cursa;

import java.util.List;
import java.util.concurrent.Callable;

public class CursaService {

    private final CursaDao cursaDao;
    private final AsyncTaskRunner taskRunner;

    public CursaService(Context context) {
        cursaDao = DatabaseManager.getInstance(context).getCursaDao();
        taskRunner=new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Cursa>> callback, final long idUtilizator){
        Callable<List<Cursa>> callable = new Callable<List<Cursa>>() {
            @Override
            public List<Cursa> call() throws Exception {
                return cursaDao.getAll(idUtilizator);
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<Cursa> callback, final Cursa cursa){
        Callable<Cursa> callable=new Callable<Cursa>() {
            @Override
            public Cursa call() throws Exception {
                if(cursa==null){
                    return null;
                }
                long id=cursaDao.insert(cursa);
                if(id==-1){
                    return null;
                }
                cursa.setId_cursa(id);
                return cursa;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<Cursa> callback, final Cursa cursa) {
        Callable<Cursa> callable = new Callable<Cursa>() {
            @Override
            public Cursa call() throws Exception {
                if (cursa == null) {
                    return null;
                }
                int count = cursaDao.update(cursa);
                if (count != 1) {
                    return null;
                }
                return cursa;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }


    public void delete(Callback<Integer> callback, final Cursa cursa){
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if(cursa==null){
                    return null;
                }
                return cursaDao.delete(cursa);
            }
        };
        taskRunner.executeAsync(callable,callback);
    }
}
