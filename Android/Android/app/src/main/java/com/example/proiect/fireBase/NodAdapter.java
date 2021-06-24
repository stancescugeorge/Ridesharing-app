package com.example.proiect.fireBase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.proiect.R;

import java.util.List;

public class NodAdapter extends ArrayAdapter<NodFireBase> {
    private Context context;
    private List<NodFireBase> noduri;
    private LayoutInflater inflater;
    private int resource;

    public NodAdapter(@NonNull Context context, int resource, @NonNull List<NodFireBase> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.noduri = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        NodFireBase nod=noduri.get(position);
        if(nod!=null){
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_destinatie),nod.getLocatieDestinatie());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_plecare),nod.getLocatiePlecare());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_ora),nod.getOra());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_pret),nod.getPret());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_durata),nod.getDurata());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_nume),nod.getNumeSofer());
            populateTextViewContent((TextView) view.findViewById(R.id.tv_adapter_cursa_telefon),nod.getTelefon());
        }

        return view;
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText("-");
        }
    }

}
