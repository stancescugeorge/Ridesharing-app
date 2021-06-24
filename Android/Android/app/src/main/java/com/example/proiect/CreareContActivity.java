package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.UtilizatorService;
import com.example.proiect.modele.Utilizator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CreareContActivity extends AppCompatActivity {

    private TextInputEditText tietNumeUtilizator;
    private TextInputEditText tietParola;
    private TextInputEditText tietConfirmaParola;
    private TextInputEditText tietNumePrenume;
    private TextInputEditText tietTelefon;
    private Button btnCreareCont;
    private Button btnRevenire;

    private List<Utilizator> listaUtilizatori=new ArrayList<>();
    private UtilizatorService utilizatorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creare_cont);

        initComponents();

        findViewById(R.id.activitate_creare).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;}

        });
    }

    private void initComponents() {
        utilizatorService=new UtilizatorService(getApplicationContext());
        utilizatorService.getAll(getAllUsersFromDbCallback());

        tietNumeUtilizator = findViewById(R.id.creareCont_tiet_nume_utilizator);
        tietParola = findViewById(R.id.creareCont_tiet_parola);
        tietConfirmaParola = findViewById(R.id.creareCont_tiet_confirma_parola);
        tietNumePrenume=findViewById(R.id.creareCont_tiet_nume_prenume);
        tietTelefon=findViewById(R.id.creareCont_tiet_telefon);
        btnCreareCont=findViewById(R.id.btn_creare_cont_nou);
        btnRevenire=findViewById(R.id.creareCont_btn_revenire);

        btnCreareCont.setOnClickListener(creareUtilizator());
        btnRevenire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener creareUtilizator() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    boolean unic=true;
                    for(Utilizator u: listaUtilizatori){
                        if(u.getNumeUtilizator().equals(tietNumeUtilizator.getText().toString())){
                            unic=false;
                            break;
                        }
                    }

                    if(unic) {
                        Utilizator utilizator = new Utilizator(tietNumeUtilizator.getText().toString(),
                                tietParola.getText().toString(),
                                tietNumePrenume.getText().toString(),
                                tietTelefon.getText().toString());

                        utilizatorService.insert(insertUserIntoDbCallback(),utilizator);

                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                R.string.text_exista_nume_utilizator,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        };
    }


    private boolean validate(){
        if (tietNumeUtilizator.getText() == null || tietNumeUtilizator.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_nume_utilizator,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietParola.getText() == null || tietParola.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_parola,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietConfirmaParola.getText() == null || !tietConfirmaParola.getText().toString().equals(tietParola.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_confirmaParola,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietNumePrenume.getText() == null || tietNumePrenume.getText().toString().trim().length() < 5) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_numePrenume,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietTelefon.getText() == null || tietTelefon.getText().toString().trim().length()!=10) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_telefon,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }

    private Callback<List<Utilizator>> getAllUsersFromDbCallback(){
        return new Callback<List<Utilizator>>() {
            @Override
            public void runResultOnUiThread(List<Utilizator> result) {
                if(result!=null){
                    listaUtilizatori.clear();
                    listaUtilizatori.addAll(result);
                }
            }
        };
    }

    private Callback<Utilizator> insertUserIntoDbCallback(){
        return new Callback<Utilizator>() {
            @Override
            public void runResultOnUiThread(Utilizator result) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.text_creareCont_succes,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        };
    }
}