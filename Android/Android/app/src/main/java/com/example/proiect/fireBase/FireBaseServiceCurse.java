package com.example.proiect.fireBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.proiect.R;
import com.example.proiect.asyncTask.Callback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FireBaseServiceCurse {

    public static final String ME_GA_CAR = "MeGaCar";
    public static final String FIREBASE_SERVICE = "FirebaseService";
    public static final String DATA_IS_NOT_AVAILABLE = "Data is not available!";
    private static FireBaseServiceCurse fireBaseServiceCurse;
    private DatabaseReference database;

    private FireBaseServiceCurse() {
        database= FirebaseDatabase.getInstance().getReference(ME_GA_CAR);
    }

    public static FireBaseServiceCurse getInstance(){
        if(fireBaseServiceCurse==null){
            synchronized (FireBaseServiceCurse.class){
                if(fireBaseServiceCurse==null){
                    fireBaseServiceCurse=new FireBaseServiceCurse();
                }
            }
        }
        return fireBaseServiceCurse;
    }

    public void upsert(NodFireBase nod){
        if(nod==null){
            return;
        }
        if(nod.getId()==null||nod.getId().trim().isEmpty()){
            String id=database.push().getKey();
            nod.setId(id);
        }
        database.child(nod.getId()).setValue(nod);

    }

    public void delete(NodFireBase nod){
        if(nod==null||nod.getId()==null||nod.getId().trim().isEmpty()){
            return;
        }
        database.child(nod.getId()).removeValue();
    }

    public void attachDataChangeEventListener(final Callback<List<NodFireBase>> callback,final NodFireBase cursaCautata) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NodFireBase> noduri = new ArrayList<>();
                //parcurgem lista de subnoduri din cel principal
                for (DataSnapshot data : snapshot.getChildren()) {
                    NodFireBase nod = data.getValue(NodFireBase.class);
                    if (nod != null && nod.getData().equals(cursaCautata.getData())&& nod.getLocatiePlecare().equals(cursaCautata.getLocatiePlecare())
                            && nod.getLocatieDestinatie().equals(cursaCautata.getLocatieDestinatie())) {
                        noduri.add(nod);
                    }
                }
                Collections.sort(noduri,new Comparator<NodFireBase>() {
                    @Override
                    public int compare(NodFireBase o1, NodFireBase o2) {
                        return o1.getOra().compareTo(o2.getOra());
                    }
                });
                //trimitem lista catre activitatea prin intermediul callback-ului
                callback.runResultOnUiThread(noduri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(FIREBASE_SERVICE, DATA_IS_NOT_AVAILABLE);
            }
        });
    }

}
