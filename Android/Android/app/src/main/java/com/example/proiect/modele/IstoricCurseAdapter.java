package com.example.proiect.modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiect.R;
import com.example.proiect.util.DateConverter;

import java.util.List;

public class IstoricCurseAdapter extends ArrayAdapter<Cursa> {
    private DateConverter dateConverter = new DateConverter();

    private Context context;
    private int resource;
    private List<Cursa> listaCurse;
    private LayoutInflater inflater;

    public IstoricCurseAdapter(@NonNull Context context, int resource, @NonNull List<Cursa> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listaCurse = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Cursa cursa = listaCurse.get(position);
        if (cursa != null) {
            TextView textView=view.findViewById(R.id.tv_adapter_istoric_data);
            textView.setText(dateConverter.fromDate(cursa.getData()));

            textView=view.findViewById(R.id.tv_adapter_istoric_plecare);
            textView.setText(cursa.getLocatiePlecare());

            textView=view.findViewById(R.id.tv_adapter_istoric_destinatie);
            textView.setText(cursa.getLocatieDestinatie());

            textView=view.findViewById(R.id.tv_adapter_istoric_ora);
            textView.setText(cursa.getOra());

            textView=view.findViewById(R.id.tv_adapter_istoric_durata);
            textView.setText(String.valueOf(cursa.getDurata()));
        }
        return view;
    }
}
