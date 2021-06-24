package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.UtilizatorService;
import com.example.proiect.modele.Utilizator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    public static final int RESULT_UPDATE = 101;
    public static final int RESULT_DELETE = 102;
    public static final int RESULT_DISCONNECT = 103;
    private Utilizator utilizatorCurent=null;
    private UtilizatorService utilizatorService;
    private Intent intent;

    private List<Utilizator> listaUtilizatori=new ArrayList<>();

    private ImageView btnProfil;
    private TextInputEditText tietNumeUtilizator;
    private TextInputEditText tietParola;
    private TextInputEditText tietConfirmaParola;
    private TextInputEditText tietNumePrenume;
    private TextInputEditText tietTelefon;
    private Button btnActualizareCont;
    private Button btnStergeCont;
    private Button btnDeconectare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        intent=getIntent();
        if(intent.hasExtra(MainActivity.UTILIZATOR_KEY)) {
            utilizatorCurent = (Utilizator) intent.getSerializableExtra(MainActivity.UTILIZATOR_KEY);
        } else {
            finish();
            Toast.makeText(getApplicationContext(),
                    R.string.text_eroare,
                    Toast.LENGTH_LONG)
                    .show();
        }

        utilizatorService = new UtilizatorService(ProfilActivity.this);
        utilizatorService.getAll(getAllUsersFromDbCallback());

        initComponents();
    }

    private void initComponents() {
        btnProfil=findViewById(R.id.btn_profil_profil);
        tietNumeUtilizator = findViewById(R.id.profil_tiet_nume_utilizator);
        tietParola = findViewById(R.id.profil_tiet_parola);
        tietConfirmaParola = findViewById(R.id.profil_tiet_confirma_parola);
        tietNumePrenume=findViewById(R.id.profil_tiet_nume_prenume);
        tietTelefon=findViewById(R.id.profil_tiet_telefon);

        tietNumeUtilizator.setText(utilizatorCurent.getNumeUtilizator());
        tietNumePrenume.setText(utilizatorCurent.getNumePrenume());
        tietParola.setText(utilizatorCurent.getParola());
        tietConfirmaParola.setText(utilizatorCurent.getParola());
        tietTelefon.setText(utilizatorCurent.getTelefon());

        btnActualizareCont=findViewById(R.id.profil_btn_actualizare_cont);
        btnStergeCont=findViewById(R.id.profil_btn_stergere);
        btnDeconectare=findViewById(R.id.profil_btn_deconectare);

        findViewById(R.id.activitate_profil).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;}

        });

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDeconectare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        R.string.text_succes_deconectare,
                        Toast.LENGTH_LONG)
                        .show();
                setResult(RESULT_DISCONNECT,intent);
                finish();
            }
        });

        btnStergeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilizatorService.delete(deleteUserFromDbCallback(),utilizatorCurent);
            }
        });

        btnActualizareCont.setOnClickListener(actualizareCont());
    }

    private View.OnClickListener actualizareCont() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    if(existaNumeUtilizator()){
                        Toast.makeText(getApplicationContext(),
                                R.string.text_exista_nume_utilizator,
                                Toast.LENGTH_LONG)
                                .show();
                    } else {
                        utilizatorCurent.setNumeUtilizator(tietNumeUtilizator.getText().toString());
                        utilizatorCurent.setNumePrenume(tietNumePrenume.getText().toString());
                        utilizatorCurent.setParola(tietParola.getText().toString());
                        utilizatorCurent.setTelefon(tietTelefon.getText().toString());

                        utilizatorService.update(updateUserIntoDbCallback(),utilizatorCurent);

                        SharedPreferences preferences = getSharedPreferences(MainActivity.ME_GA_CAR,MODE_PRIVATE);

                        Boolean remember=preferences.getBoolean(MainActivity.REMEMBER,false);
                        if(remember){
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(MainActivity.NUME_UTILIZATOR, utilizatorCurent.getNumeUtilizator());
                            editor.putString(MainActivity.PAROLA, utilizatorCurent.getParola());
                            editor.apply();
                        }

                        intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                        setResult(RESULT_UPDATE,intent);
                        finish();
                    }
                }
            }
        };
    }

    private boolean existaNumeUtilizator(){
        boolean utilizatorExistent = false;
        if(!utilizatorCurent.getNumeUtilizator().equals(String.valueOf(tietNumeUtilizator.getText())))
        {
            for(Utilizator u: listaUtilizatori){
                if(u.getNumeUtilizator().equals(tietNumeUtilizator.getText().toString())){
                    utilizatorExistent=true;
                    break;
                }
            }
        }
        return utilizatorExistent;
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

    private Callback<Utilizator> updateUserIntoDbCallback(){
        return new Callback<Utilizator>() {
            @Override
            public void runResultOnUiThread(Utilizator result) {
                Toast.makeText(getApplicationContext(),
                        R.string.text_succes_actualizare_utilizator,
                        Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    private Callback<Integer> deleteUserFromDbCallback(){
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result!=-1){
                    utilizatorCurent=null;
                    SharedPreferences preferences=getSharedPreferences(MainActivity.ME_GA_CAR,MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(MainActivity.REMEMBER, false);
                    editor.apply();

                    Toast.makeText(getApplicationContext(),
                            R.string.text_succes_stergere_cont,
                            Toast.LENGTH_LONG)
                            .show();

                    setResult(RESULT_DELETE,intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.text_eroare,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
    }
}