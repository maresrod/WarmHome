package com.example.warmhome.ParametrosCardview;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.warmhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import static com.firebase.ui.auth.AuthUI.TAG;


public class Temperatura extends Activity {
    private TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperatura);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("coleccion").document("documento").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Firebase", "Error al leer", e);
                } else if (snapshot == null || !snapshot.exists()) {
                    Log.e("Firebase", "Error: documento no encontrado ");
                } else {
                    Log.e("Firestore", "datos:" + snapshot.getData());
                }
            }
        });
    }*/
        ImageView bSalir = findViewById(R.id.cruz);
        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();

            }

        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        text = findViewById(R.id.text);
        DocumentReference docRef = db.collection("Habitación").document("datos");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String a=document.get("Temperatura").toString();
                       Log.e("AAAAAA",document.getId());
                        text.setText(""+a);
                    } else {
                        Log.d("dd", "No such document");
                    }
                } else {
                    Log.d("ss", "get failed with ", task.getException());
                }
            }
        });
    }
    public void salir(View view){
        finish();
    }

}
