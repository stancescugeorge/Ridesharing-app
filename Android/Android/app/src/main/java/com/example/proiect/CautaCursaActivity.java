package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.proiect.fireBase.NodFireBase;
import com.example.proiect.modele.Cursa;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.util.DateConverter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CautaCursaActivity extends AppCompatActivity {

    public static final String CURSA_CAUTATA_KEY = "cursaCautataKey";
    private Utilizator utilizatorCurent=null;
    private Intent intent;

    private final Calendar calendar= Calendar.getInstance();

    private TextInputEditText tietPlecare;
    private TextInputEditText tietDestinatie;
    private TextInputEditText tietData;
    private Button btnCauta;
    private Button btnRevenire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauta_cursa);
        initComponent();

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

    private void initComponent() {
        tietPlecare=findViewById(R.id.tiet_cauta_cursa_plecare);
        tietDestinatie=findViewById(R.id.tiet_cauta_cursa_destinatie);
        tietData=findViewById(R.id.tiet_cauta_cursa_data);
        btnCauta=findViewById(R.id.btn_cauta_cursa_cautare);
        btnRevenire=findViewById(R.id.btn_cauta_cursa_revinere);

        afisareDatePicker();

        findViewById(R.id.activitate_cautaCursa).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }

        });


        btnRevenire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCauta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Intent intent=new Intent(getApplicationContext(), ListaCurseCautateActivity.class);
                    intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                    NodFireBase cursaCautata = new NodFireBase(null,null,tietPlecare.getText().toString(),
                            tietDestinatie.getText().toString(),null,
                            tietData.getText().toString(),null,
                            null,null);
                    intent.putExtra(CURSA_CAUTATA_KEY,cursaCautata);
                    startActivity(intent);
                }
            }
        });
    }

    private void afisareDatePicker() {
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        tietData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog datePickerDialog=new DatePickerDialog(CautaCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog=new DatePickerDialog(CautaCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        return true;
    }
}