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
import com.example.proiect.modele.IstoricCurseAdapter;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.util.DateConverter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditareStergereCursaActivity extends AppCompatActivity {

    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 202;

    private Cursa cursaSelectata=null;
    private Intent intent;

    private TextInputEditText tietPlecare;
    private TextInputEditText tietDestinatie;
    private TextInputEditText tietData;
    private TextInputEditText tietDurata;
    private TextInputEditText tietOra;
    private final Calendar calendar= Calendar.getInstance();

    private Button btnModifica;
    private Button btnSterge;

    private CursaService cursaService;
    private DateConverter dateConverter = new DateConverter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editare_stergere_cursa);

        intent=getIntent();
        if(intent.hasExtra(IstoricCurseActivity.CURSA_KEY)) {
            cursaSelectata = (Cursa) intent.getSerializableExtra(IstoricCurseActivity.CURSA_KEY);
        } else {
            finish();
            Toast.makeText(getApplicationContext(),
                    R.string.text_eroare,
                    Toast.LENGTH_LONG)
                    .show();
        }
        cursaService=new CursaService(EditareStergereCursaActivity.this);

        initComponents();
    }

    private void initComponents() {
        tietPlecare=findViewById(R.id.tiet_editare_stergere_cursa_plecare);
        tietDestinatie=findViewById(R.id.tiet_editare_stergere_cursa_destinatie);
        tietData=findViewById(R.id.tiet_editare_stergere_cursa_data);
        tietOra=findViewById(R.id.tiet_editare_stergere_cursa_ora);
        tietDurata=findViewById(R.id.tiet_editare_stergere_cursa_durata);

        tietPlecare.setText(cursaSelectata.getLocatiePlecare());
        tietDestinatie.setText(cursaSelectata.getLocatieDestinatie());
        tietData.setText(dateConverter.fromDate(cursaSelectata.getData()));
        tietOra.setText(cursaSelectata.getOra());
        tietDurata.setText(String.valueOf(cursaSelectata.getDurata()));

        afisareDatePicker();
        afisareTimePicker();

        btnModifica=findViewById(R.id.btn_editare_cursa);
        btnSterge=findViewById(R.id.btn_stergere_cursa);

        btnSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursaService.delete(deleteFromDbCallback(),cursaSelectata);
            }
        });
        btnModifica.setOnClickListener(editareCursa());

        findViewById(R.id.activitate_editareStergereCursa).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }

        });

    }

    private View.OnClickListener editareCursa() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                   cursaSelectata.setLocatiePlecare(String.valueOf(tietPlecare.getText()));
                   cursaSelectata.setLocatieDestinatie(String.valueOf(tietDestinatie.getText()));
                   cursaSelectata.setData(dateConverter.fromString(String.valueOf(tietData.getText())));
                   cursaSelectata.setOra(String.valueOf(tietOra.getText()));
                   cursaSelectata.setDurata(Integer.parseInt(String.valueOf(tietDurata.getText())));

                   cursaService.update(updateIntoDbCallback(),cursaSelectata);
                }
            }
        };
    }

    private Callback<Integer> deleteFromDbCallback(){
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result!=-1) {
                    Toast.makeText(getApplicationContext(),
                            R.string.text_succes_stergere_cursa,
                            Toast.LENGTH_LONG)
                            .show();
                    setResult(RESULT_DELETE, intent);
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

    private Callback<Cursa> updateIntoDbCallback() {
        return new Callback<Cursa>() {
            @Override
            public void runResultOnUiThread(Cursa result) {
                if(result!=null){
                    Toast.makeText(getApplicationContext(),
                            R.string.text_succes_update_cursa,
                            Toast.LENGTH_LONG)
                            .show();

                    intent.putExtra(IstoricCurseActivity.CURSA_KEY,cursaSelectata);
                    setResult(RESULT_UPDATE,intent);
                    finish();
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
                    DatePickerDialog datePickerDialog=new DatePickerDialog(EditareStergereCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog=new DatePickerDialog(EditareStergereCursaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    TimePickerDialog timePickerDialog=new TimePickerDialog(EditareStergereCursaActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog timePickerDialog=new TimePickerDialog(EditareStergereCursaActivity.this, new TimePickerDialog.OnTimeSetListener() {
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