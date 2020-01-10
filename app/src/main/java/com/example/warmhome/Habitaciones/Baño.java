package com.example.warmhome.Habitaciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.warmhome.ParametrosCardview.Iluminacion;
import com.example.warmhome.ParametrosCardview.Presencia;
import com.example.warmhome.ParametrosCardview.Temperatura;
import com.example.warmhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

//TAB BAÑO
public class Baño extends Fragment {
    private TextView text;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_banyo, container, false);


        text = view.findViewById(R.id.textTempB);
        db.collection("Baño").document("datos").addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firebase", "Error al leer", e);
                        } else if (snapshot == null || !snapshot.exists()) {
                            Log.e("Firebase", "Error: documento no encontrado ");
                        } else {
                            String a = Objects.requireNonNull(snapshot.get("Temperatura")).toString();
                            Log.e("AAAAAA", snapshot.getId());

                            text.setText(""+a+"ºC");



                        }
                    }
                }
        );

        //Tarjeta Temperatura
        CardView tarjetaT = view.findViewById(R.id.TemperaturaB);

        tarjetaT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Temperatura.class);
                startActivity(i);

            }
        });

        //Tarjeta Presencia
        CardView tarjetaP = view.findViewById(R.id.PresenciaB);

        tarjetaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Presencia.class);
                startActivity(i);

            }
        });

        //Tarjeta Iluminacion
        CardView tarjetaI = view.findViewById(R.id.LucesB);

        tarjetaI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Iluminacion.class);
                startActivity(i);

            }
        });


       return view;
    }
}
