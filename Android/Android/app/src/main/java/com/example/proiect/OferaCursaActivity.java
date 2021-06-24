package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.service.CursaService;
import com.example.proiect.fireBase.FireBaseServiceCurse;
import com.example.proiect.fireBase.NodFireBase;
import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.util.DateConverter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class OferaCursaActivity extends AppCompatActivity {

    private CursaService cursaService;

    private Utilizator utilizatorCurent=null;
    private Intent intent;

    private TextInputEditText tietPlecare;
    private TextInputEditText tietDestinatie;
    private TextInputEditText tietData;
    private TextInputEditText tietDurata;
    private TextInputEditText tietOra;
    private TextInputEditText tietPret;
    private final Calendar calendar= Calendar.getInstance();

    private Button btnAdaugare;
    private Button btnRevenire;

    private DateConverter dateConverter = new DateConverter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofera_cursa);
        initComponents();

        cursaService=new CursaService(OferaCursaActivity.this);

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
    }

    private void initComponents() {
        tietPlecare=findViewById(R.id.tiet_ofera_cursa_plecare);
        tietDestinatie=findViewById(R.id.tiet_ofera_cursa_destinatie);
        tietData=findViewById(R.id.tiet_ofera_cursa_data);
        tietDurata=findViewById(R.id.tiet_ofera_cursa_durata);
        tietOra=findViewById(R.id.tiet_ofera_cursa_ora);
        tietPret=findViewById(R.id.tiet_ofera_cursa_pret);

        afisareDatePicker();
        afisareTimePicker();

        btnAdaugare=findViewById(R.id.btn_ofera_cursa_adauga);
        btnRevenire=findViewById(R.id.btn_ofera_cursa_revenire);

        btnRevenire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAdaugare.setOnClickListener(adaugaCursaNoua());

        findViewById(R.id.actvitate_oferaCursa).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }

        });

    }

    private View.OnClickListener adaugaCursaNoua() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String locPlecare= String.valueOf(tietPlecare.getText());
                    String locDestinatie= String.valueOf(tietDestinatie.getText());
                    String data= String.valueOf(tietData.getText());
                    String ora= String.valueOf(tietOra.getText());
                    String durata= String.valueOf(tietDurata.getText());
                    String pret= String.valueOf(tietPret.getText());

                    Cursa cursa = new Cursa(utilizatorCurent.getId_utilizator(),
                            locPlecare, locDestinatie,
                            dateConverter.fromString(data), ora,
                            Integer.parseInt(durata), 1);

                    cursaService.insert(insertIntoDbCallback(),cursa);

                    // Firebase
                    FireBaseServiceCurse fireBaseServiceCurse=FireBaseServiceCurse.getInstance();

                    NodFireBase nod=new NodFireBase(null, utilizatorCurent.getNumePrenume(),
                            locPlecare, locDestinatie,
                            ora, data, pret,
                            utilizatorCurent.getTelefon(), durata);

                    fireBaseServiceCurse.upsert(nod);
                    finish();
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
                            R.string.text_cursa_adaugata_succes,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
    }

    private boolean validate(){
        if (tietPlecare.getText() == null || tietPlecare.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_plecare,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietDestinatie.getText() == null || tietDestinatie.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_destinatie,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (tietDestinatie.getText().toString().trim().equals(tietPlecare.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    R.string.text_destinatie_diferita_plecare,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if(tietData.getText() == null || tietData.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_data,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if(tietOra.getText() == null || tietOra.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_ora,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if(tietDurata.getText() == null || tietDurata.getText().toString().length()==0 || Integer.parseInt(tietDurata.getText().toString().trim())<=0){
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_durata,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if(tietPret.getText() == null || tietPret.getText().toString().length()==0 || Integer.parseInt(tietPret.getText().toString().trim())<0){
            Toast.makeText(getApplicationContext(),
                    R.string.text_validare_pret,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }

    private void afisareDatePicker() {
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        tietData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog datePickerDialog=new DatePickerDialog(OferaCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month=month+1;
                            String data=day+"/"+month+"/"+year;
                            tietData.setText(String.valueOf(data));
                        }
                    },year,month,day);
                    datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                    datePickerDialog.show();}
            }
        });
        tietData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(OferaCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String data=day+"/"+month+"/"+year;
                        tietData.setText(String.valueOf(data));
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void afisareTimePicker() {
        tietOra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    TimePickerDialog timePickerDialog=new TimePickerDialog(OferaCursaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String timp=String.valueOf(hourOfDay);
                            if(hourOfDay<10)
                                timp="0"+timp;

                            if(minute<10)
                                timp+=":0"+minute;
                            else
                                timp+=":"+minute;
                            tietOra.setText(timp);
                        }
                    },24,0,true);
                    timePickerDialog.show();
                }
            }
        });

        tietOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(OferaCursaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timp=String.valueOf(hourOfDay);
                        if(hourOfDay<10)
                            timp="0"+timp;

                        if(minute<10)
                            timp+=":0"+minute;
                        else
                            timp+=":"+minute;
                        tietOra.setText(timp);
                    }
                },24,0,true);
                timePickerDialog.show();
            }
        });
    }
}