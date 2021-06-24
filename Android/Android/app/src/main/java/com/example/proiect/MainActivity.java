package com.example.proiect;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.UtilizatorService;
import com.example.proiect.modele.Utilizator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UtilizatorService utilizatorService;
    public static final String UTILIZATOR_KEY = "utilizatorKey";
    public Utilizator utilizatorCurent=null;

    public static final String ME_GA_CAR = "MeGaCar";
    public static final String REMEMBER = "remember";
    public static final String NUME_UTILIZATOR = "nume_utilizator";
    public static final String PAROLA = "parola";

    private TextInputEditText tietNume;
    private TextInputEditText tietParola;
    private Button btnCreare;
    private Button btnIntrare;
    private CheckBox cbRemember;
    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utilizatorService=new UtilizatorService(getApplicationContext());

        initComponents();
        incarcaSharedPreferences();
    }

    private void incarcaSharedPreferences() {
        Boolean remember=preferences.getBoolean(REMEMBER,false);

        if(remember){
            tietParola.setText(preferences.getString(PAROLA,""));
            tietNume.setText(preferences.getString(NUME_UTILIZATOR,""));
        }
        cbRemember.setChecked(remember);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initComponents() {

        ImageView view = findViewById(R.id.logo_megacar);
        Path path = new Path();
        path.arcTo(-10f, 0f, 100f, 100f, 185f, -360f, true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(10000);
        animator.start();

        btnCreare = findViewById(R.id.btn_creare);
        btnIntrare = findViewById(R.id.btn_intrare);
        tietNume=findViewById(R.id.main_tiet_nume_utilizator);
        tietParola=findViewById(R.id.main_tiet_parola);
        cbRemember = findViewById(R.id.cb_remember_me);
        preferences = getSharedPreferences(ME_GA_CAR, MODE_PRIVATE);

        btnIntrare.setOnClickListener(logareInAplicatie());

        btnCreare.setOnClickListener(creareCont());

        findViewById(R.id.actvitate_conectare).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }

        });

    }

    private View.OnClickListener creareCont() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreareContActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener logareInAplicatie() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //validare nume utilizator + parola, preluare utilizator curent din baza de date, logare
                    utilizatorService.getUser(getUserFromDbCallback(), String.valueOf(tietNume.getText()), String.valueOf(tietParola.getText()));
                }
            }
        };
    }

    private boolean validate(){
        if (tietNume.getText() == null || tietNume.getText().toString().trim().length() < 3) {
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
        return true;
    }

    private Callback<Utilizator> getUserFromDbCallback(){
        return new Callback<Utilizator>() {
            @Override
            public void runResultOnUiThread(Utilizator result) {
                if (result != null) {
                    utilizatorCurent = new Utilizator(result.getId_utilizator(),result.getNumeUtilizator(),
                            result.getParola(),result.getNumePrenume(),result.getTelefon());

                        Boolean remember = cbRemember.isChecked();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(REMEMBER, remember);
                        editor.putString(NUME_UTILIZATOR, utilizatorCurent.getNumeUtilizator());
                        editor.putString(PAROLA, utilizatorCurent.getParola());
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), ContinutPrincipalActivity.class);
                        //transmitere utilizatorCurent catre activitatea continutPrincipal
                        intent.putExtra(UTILIZATOR_KEY, utilizatorCurent);
                        startActivity(intent);
                        finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.main_text_nume_parola_incorecte,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
    }


}