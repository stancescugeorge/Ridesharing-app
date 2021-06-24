package com.example.proiect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.CursaService;
import com.example.proiect.fireBase.FireBaseServiceCurse;
import com.example.proiect.fireBase.NodAdapter;
import com.example.proiect.fireBase.NodFireBase;
import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.util.DateConverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListaCurseCautateActivity extends AppCompatActivity {
    private static Boolean accesat=true;
    private Utilizator utilizatorCurent = null;
    private Intent intent;

    private ListView lvCurse;
    private Button btnRezerva;
    private Button btnAnuleaza;
    private List<NodFireBase> curse = new ArrayList<>();
    private FireBaseServiceCurse fireBaseServiceCurse;

    private NodFireBase cursaCautata = null;
    private NodFireBase cursaSelectata = null;
    private DateConverter dateConverter = new DateConverter();
    private CursaService cursaService;

    private int save=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_curse_cautate);
        accesat=true;

        cursaService=new CursaService(ListaCurseCautateActivity.this);

        intent = getIntent();
        if (intent.hasExtra(MainActivity.UTILIZATOR_KEY) && intent.hasExtra(CautaCursaActivity.CURSA_CAUTATA_KEY)) {
            utilizatorCurent = (Utilizator) intent.getSerializableExtra(MainActivity.UTILIZATOR_KEY);
            cursaCautata = (NodFireBase) intent.getSerializableExtra(CautaCursaActivity.CURSA_CAUTATA_KEY);
        } else {
            finish();
            Toast.makeText(getApplicationContext(),
                    R.string.text_eroare,
                    Toast.LENGTH_LONG)
                    .show();
        }
        initComponents();


    }

    private void initComponents() {
        lvCurse = findViewById(R.id.lv_lista_curse_cautate);
        btnAnuleaza = findViewById(R.id.btn_lista_curse_cautate_anuleaza);
        btnRezerva = findViewById(R.id.btn_lista_curse_cautate_rezervare);

        btnAnuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accesat=false;finish();
            }
        });

        fireBaseServiceCurse = FireBaseServiceCurse.getInstance();
        fireBaseServiceCurse.attachDataChangeEventListener(dataChangeCallback(),cursaCautata);

        final NodAdapter adapter = new NodAdapter(getApplicationContext(), R.layout.lv_curse_adapter, curse, getLayoutInflater());
        lvCurse.setAdapter(adapter);

        lvCurse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(save!=-1){
                    View viewAnterior = parent.getChildAt(save);
                    viewAnterior.setBackground(getDrawable(R.drawable.back_sim_stroke));
                }
                save=position;
                view.setBackground(getDrawable(R.drawable.back_sim_stroke_selected));
            }
        });

        btnRezerva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save==-1){
                    Toast.makeText(getApplicationContext(),
                            R.string.text_selectati_cursa,
                            Toast.LENGTH_LONG)
                            .show();
                } else{
                    cursaSelectata=curse.get(save);
                    if(cursaSelectata.getNumeSofer().equals(utilizatorCurent.getNumePrenume()) ||
                            cursaSelectata.getTelefon().equals(utilizatorCurent.getTelefon())) {
                        Toast.makeText(getApplicationContext(),
                                R.string.text_rezervare_propria_cursa,
                                Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Cursa cursa = new Cursa(utilizatorCurent.getId_utilizator(),
                                cursaSelectata.getLocatiePlecare(),cursaSelectata.getLocatieDestinatie(),
                                dateConverter.fromString(cursaSelectata.getData()),cursaSelectata.getOra(),
                                Integer.valueOf(cursaSelectata.getDurata()),0);

                        cursaService.insert(insertIntoDbCallback(),cursa);
                        fireBaseServiceCurse.delete(cursaSelectata);
                    }
                }
            }
        });
    }

    public void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvCurse.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<NodFireBase>> dataChangeCallback() {
        return new Callback<List<NodFireBase>>() {
            @Override
            public void runResultOnUiThread(List<NodFireBase> result) {
                //primim raspunsul de la attachDataChangeEventListener
                //declansat de fiecare data cand se produc modificari asupra bazei de date
                if (result != null ) {
                    curse.clear();
                    curse.addAll(result);
                    notifyInternalAdapter();
                }
                if(result.size()==0 && accesat){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.text_indisponibilitate_cursa, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    toast.show();
                }
            }
        };
    }

    private Callback<Cursa> insertIntoDbCallback() {
        return new Callback<Cursa>() {
            @Override
            public void runResultOnUiThread(Cursa result) {
                if(result!=null){
                    Toast.makeText(getApplicationContext(),
                            R.string.text_cursa_rezervata_succes,
                            Toast.LENGTH_LONG)
                            .show();

                    finish();
                }
            }
        };
    }

}