package com.example.proiect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.modele.Fondator;
import com.example.proiect.modele.Utilizator;
import com.example.proiect.network.HttpManager;
import com.example.proiect.util.FondatoriJsonParser;

import java.util.ArrayList;

public class ContinutPrincipalActivity extends AppCompatActivity {

    public static final int PROFILE_REQUEST_CODE = 100;
    private Utilizator utilizatorCurent=null;
    private Intent intent;

    public static final String URL_CETREBUIESAFAC_RO = "https://cetrebuiesafac.ro";
    private static final String URL_DESPRE = "https://jsonkeeper.com/b/Y8U4";

   // [{"nume":"Stancescu","prenume":"Georgian-Constantin","telefon":"0762555421","facultate":{"denumire":"Facultatea de Cibernetica, Statistica si Informatica Economica","specializare":"Informatica Economica","universitate":{"denumire":"Academia de Studii Economice Bucuresti","an fondare":1913,"adresa":"Piata Romana, nr. 6"}}},{"nume":"Tatu","prenume":"Marian","telefon":"0724211454","facultate":{"denumire":"Facultatea de Cibernetica, Statistica si Informatica Economica","specializare":"Informatica Economica","universitate":{"denumire":"Academia de Studii Economice Bucuresti","an fondare":1913,"adresa":"Piata Romana, nr. 6"}}}]

    private ImageButton btnProfil;
    private Button btnOfera;
    private Button btnCauta;
    private Button btnIstoric;
    private Button btnConditii;
    private TextView tvDespreNoi;

    private ArrayList<Fondator> fondatori = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continut_principal);

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

        getFondatoriFromHttp();
    }

    // preluare info fondatori de la URL folosind Thread
    private void getFondatoriFromHttp(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                final String result = new HttpManager(URL_DESPRE).process();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        fondatori.addAll(FondatoriJsonParser.fromJson(result));
                        tvDespreNoi.setEnabled(true);
                    }
                });
            }
        };
        thread.start();
    }

    private void initComponents() {
        btnProfil=findViewById(R.id.btn_continut_principal_profil);
        btnOfera=findViewById(R.id.btn_continut_principal_ofera);
        btnCauta=findViewById(R.id.btn_continut_principal_cauta);
        btnIstoric=findViewById(R.id.btn_continut_principal_istoric);
        btnConditii=findViewById(R.id.btn_continut_principal_conditii);
        tvDespreNoi=findViewById(R.id.tv_continut_principal_despreNoi);
        tvDespreNoi.setEnabled(false);

        btnConditii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(URL_CETREBUIESAFAC_RO));
                startActivity(browserIntent);
            }
        });

        btnOfera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),OferaCursaActivity.class);
                intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                startActivity(intent);
            }
        });

        btnCauta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CautaCursaActivity.class);
                intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                startActivity(intent);
            }
        });

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfilActivity.class);
                intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                startActivityForResult(intent, PROFILE_REQUEST_CODE);
            }
        });

        btnIstoric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), IstoricCurseActivity.class);
                intent.putExtra(MainActivity.UTILIZATOR_KEY,utilizatorCurent);
                startActivity(intent);
            }
        });

        tvDespreNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ContinutPrincipalActivity.this, R.style.temaAlertDialogPersonalizata);
                builder.setTitle(R.string.despre_noi_titlu);
                builder.setMessage(getString(R.string.despre_noi_text,
                        fondatori.get(1).getNume(),fondatori.get(1).getPrenume(),
                        fondatori.get(0).getNume(),fondatori.get(0).getPrenume(),
                        fondatori.get(0).getFacultate().getDenumire(), fondatori.get(0).getFacultate().getSpecializare(),
                        fondatori.get(0).getFacultate().getUniversitate().getDenumire(),fondatori.get(0).getFacultate().getUniversitate().getAnFondare(),
                        fondatori.get(0).getFacultate().getUniversitate().getAdresa(),
                        fondatori.get(0).getTelefon(),
                        fondatori.get(1).getTelefon(),
                        utilizatorCurent.getNumePrenume()));
                builder.setCancelable(true);
                builder.setIcon(R.drawable.info);
                builder.setNeutralButton(
                        R.string.text_ok_btn_dialog_despre_noi,
                        creareDialogInterfaceRating());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private DialogInterface.OnClickListener creareDialogInterfaceRating() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final Dialog rankDialog = new Dialog(ContinutPrincipalActivity.this, R.style.temaAlertDialogPersonalizata);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

                Button btnSalvare = (Button) rankDialog.findViewById(R.id.btn_rating_salveaza);
                btnSalvare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), R.string.text_multumire_recenzie, Toast.LENGTH_LONG).show();
                        rankDialog.dismiss();
                    }
                });

                Button btnRenuntare = (Button) rankDialog.findViewById(R.id.btn_rating_renunta);
                btnRenuntare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankDialog.dismiss();
                    }
                });
                rankDialog.show();
                dialog.cancel();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PROFILE_REQUEST_CODE){
            if(resultCode==ProfilActivity.RESULT_UPDATE && data!=null){
                utilizatorCurent = (Utilizator) data.getSerializableExtra(MainActivity.UTILIZATOR_KEY);
            }
            else if(resultCode==ProfilActivity.RESULT_DELETE || resultCode==ProfilActivity.RESULT_DISCONNECT){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}