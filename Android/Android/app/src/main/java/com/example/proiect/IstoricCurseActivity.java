package com.example.proiect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.CursaService;
import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.IstoricCurseAdapter;
import com.example.proiect.modele.Utilizator;

import java.util.ArrayList;
import java.util.List;

public class IstoricCurseActivity extends AppCompatActivity {

    public static final String CURSA_KEY = "cursaKey";
    public static final int REQUEST_EDITARE_STERGERE_CURSA = 200;

    List<Cursa> listaCurse = new ArrayList<>();
    private Utilizator utilizatorCurent;
    private Intent intent;
    private CursaService cursaService;
    private int indexCursaSelectata = -1;

    List<Cursa> listaCurenta;
    private List<Cursa> listaCurseOferite = new ArrayList<>();
    private List<Cursa> listaCurseRezervate = new ArrayList<>();
    private IstoricCurseAdapter adapter;

    private ListView lvCurse;
    private Button btnOferite;
    private Button btnRezervate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizare_istoric_curse);

        intent=getIntent();
        if(intent.hasExtra(MainActivity.UTILIZATOR_KEY)) {
            utilizatorCurent = (Utilizator) intent.getSerializableExtra(MainActivity.UTILIZATOR_KEY);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.text_eroare,
                    Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        initComponents();
    }

    private void initComponents() {
        cursaService = new CursaService(IstoricCurseActivity.this);
        cursaService.getAll(getAllFromDbCallback(),utilizatorCurent.getId_utilizator());


        lvCurse = findViewById(R.id.lv_vizualizare_istoric_curse_curse);
        changeAdapter(listaCurse);

        btnRezervate=findViewById(R.id.btn_vizualizare_istoric_curse_rezervate);
        btnOferite=findViewById(R.id.btn_vizualizare_istoric_curse_oferite);

        btnOferite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaCurenta=listaCurseOferite;
                changeAdapter(listaCurseOferite);
                notifyAdapter();

                btnOferite.setTextColor(Color.BLUE);
                btnRezervate.setTextColor(Color.BLACK);
            }
        });

        btnRezervate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaCurenta=listaCurseRezervate;
                changeAdapter(listaCurseRezervate);
                notifyAdapter();

                btnOferite.setTextColor(Color.BLACK);
                btnRezervate.setTextColor(Color.BLUE);
            }
        });

        lvCurse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                indexCursaSelectata=position;
                Cursa cursaSelectata = listaCurenta.get(position);
                Intent intent=new Intent(getApplicationContext(),EditareStergereCursaActivity.class);
                intent.putExtra(CURSA_KEY,cursaSelectata);
                startActivityForResult(intent, REQUEST_EDITARE_STERGERE_CURSA);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_EDITARE_STERGERE_CURSA){
            if(resultCode==EditareStergereCursaActivity.RESULT_UPDATE && data!=null){
                Cursa cursaModificata = (Cursa) data.getSerializableExtra(CURSA_KEY);
                listaCurenta.set(indexCursaSelectata,cursaModificata);
                notifyAdapter();
            }
            else if(resultCode==EditareStergereCursaActivity.RESULT_DELETE){
                listaCurenta.remove(indexCursaSelectata);
                notifyAdapter();
            }
        }
    }

    private void changeAdapter(List<Cursa> lista) {
        adapter = new IstoricCurseAdapter(getApplicationContext(), R.layout.lv_istoric_curse_adapter,
                lista, getLayoutInflater());
        lvCurse.setAdapter(adapter);
    }

    private void notifyAdapter() {
        IstoricCurseAdapter adapter = (IstoricCurseAdapter) lvCurse.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Cursa>> getAllFromDbCallback(){
        return new Callback<List<Cursa>>() {
            @Override
            public void runResultOnUiThread(List<Cursa> result) {
                if(result!=null){
                    listaCurse.clear();
                    listaCurse.addAll(result);
                    listaCurenta=listaCurse;

                    for(Cursa cursa:listaCurse){
                        if(cursa.getSofer()==0)
                            listaCurseRezervate.add(cursa);
                        else listaCurseOferite.add(cursa);
                    }

                    notifyAdapter();
                }
            }
        };
    }


}