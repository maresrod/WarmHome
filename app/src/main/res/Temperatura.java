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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.model.value.NullValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.TAG;


public class Temperatura extends Activity {
    private TextView text;
    public TextView max;
    public TextView min;
    public TextView media;
    public EditText t_max;
    public EditText t_min;
    public Button subir;
    double t1;
    double t2;
    double t3;
    double t4;
    double t5;
    double med = 0;
    double maxim = 0;
    double minim = 100;
    String a;
    String b;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> datos = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperatura);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));
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

        text = findViewById(R.id.text);
        max = findViewById(R.id.textView14);
        min = findViewById(R.id.textView15);
        media = findViewById(R.id.textView16);
        t_max = findViewById(R.id.editText6);
        t_min = findViewById(R.id.editText7);
        subir = findViewById(R.id.button4);


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
                            text.setText("" + a + " ºC");
                            Log.e("Firestore", "datos:" + snapshot.getData());
                            t1 = Double.parseDouble(a);
//
                            Log.i("mostrar dato", a);
                            Log.i("mim", String.valueOf(t1));


                        }
                    }
                }
        );
        db.collection("Baño").document("datos2").addSnapshotListener(
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

                            Log.e("Firestore", "datos:" + snapshot.getData());
                            t2 = Double.parseDouble(a);

                            Log.i("mostrar dato", a);

                            Log.i("mim", String.valueOf(t2));


                        }
                    }
                }
        );
        db.collection("Baño").document("datos3").addSnapshotListener(
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

                            Log.e("Firestore", "datos:" + snapshot.getData());
                            t3 = Double.parseDouble(a);

                            Log.i("mostrar dato", a);

                            Log.i("mim", String.valueOf(t3));


                        }
                    }
                }
        );
        db.collection("Baño").document("datos4").addSnapshotListener(
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

                            Log.e("Firestore", "datos:" + snapshot.getData());
                            t4 = Double.parseDouble(a);

                            Log.i("mostrar dato", a);

                            Log.i("mim", String.valueOf(t4));

                        }
                    }
                }
        );
        db.collection("Baño").document("datos5").addSnapshotListener(
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

                            Log.e("Firestore", "datos:" + snapshot.getData());
                            t5 = Double.parseDouble(a);

                            Log.i("mostrar dato", a);

                            Log.i("mim", String.valueOf(t5));
                            Log.i("mim", String.valueOf(t4));
                            Log.i("mim", String.valueOf(t3));
                            Log.i("mim", String.valueOf(t2));
                            Log.i("mim", String.valueOf(t1));
                            med = (t1 + t2 + t3 + t4 + t5) / 5;
                            media.setText(String.valueOf(med) + " ºC");
                            Log.i("media", String.valueOf(med));


                            if (t1 < minim) {
                                minim = t1;
                                Log.i("mim1", String.valueOf(minim));

                            }
                            if (t2 < minim) {
                                minim = t2;
                                Log.i("mim2", String.valueOf(minim));

                            }
                            if (t3 < minim) {
                                minim = t3;
                                Log.i("mim3", String.valueOf(minim));

                            }
                            if (t4 < minim) {
                                minim = t4;
                                Log.i("mim4", String.valueOf(minim));

                            }
                            if (t5 < minim) {
                                minim = t5;
                                Log.i("mim5", String.valueOf(minim));
                            }

                            if (t1 > maxim) {
                                maxim = t1;
                                Log.i("max1", String.valueOf(maxim));

                            }
                            if (t2 > maxim) {
                                maxim = t2;
                                Log.i("max2", String.valueOf(maxim));

                            }
                            if (t3 > maxim) {
                                maxim = t3;

                            }
                            if (t4 > maxim) {
                                maxim = t4;

                            }
                            if (t5 > maxim) {
                                maxim = t5;
                                Log.i("max5", String.valueOf(maxim));
                            }

                            Log.i("max", String.valueOf(maxim));
                            Log.i("mim", String.valueOf(minim));
                            max.setText(String.valueOf(maxim) + " ºC");
                            min.setText(String.valueOf(minim) + " ºC");

                            /// calcular();
                            //calcular_media();
                        }
                    }
                }
        );

        subir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                subir();
            }
        });

        Log.i("media", String.valueOf(med));
        Log.i("max", String.valueOf(maxim));
        Log.i("mim", String.valueOf(minim));


    }

    public void salir(View view) {
        finish();
    }

    public void subir() {
        Log.i("subir", "Vaaaaa");
        a = t_max.getText().toString();
        Log.i("subir", a);
        b = t_min.getText().toString();
        Log.i("subir", b);
        //  if (!a.equals("") && !b.equals("")) {
        datos.put("maxin", a);
        datos.put("minim", b);

        db.collection("Baño").document("temp").set(datos);
        db.collection("salon").document("temp").set(datos);
        Log.i("subir", "Funcionaaaa");

    }


}
