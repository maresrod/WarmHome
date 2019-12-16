package com.example.warmhome.Habitaciones;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.warmhome.AcercaDe;
import com.example.warmhome.LoginActivity;
import com.example.warmhome.MainActivity;
import com.example.warmhome.Mapa;
import com.example.warmhome.ParametrosCardview.Temperatura;
import com.example.warmhome.PreferenciasActivity;
import com.example.warmhome.R;
import com.example.warmhome.ServicioLocalizacion;
import com.example.warmhome.ServicioTecnicoActivity;
import com.example.warmhome.UsuarioActivity;
import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//TAB PLANO CASA
public class PlanoCasa extends AppCompatActivity {


    private TextView tempB;
    private TextView tempS;
    private TextView tempH;
    private TextView tempC;

    public static TextView longitud;
    private TextView latitud;
    // Creamos esta constante para el permiso de localización
    private static final int SOLICITUD_PERMISO_FINE_LOCATION = 0;

        @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planocasa);

        //      TOOLBAR

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//      MENU FLOTANTE
        final FloatingActionsMenu menuBotones = (FloatingActionsMenu) findViewById(R.id.grupofab);

        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);
        final FloatingActionButton fab4 = findViewById(R.id.fab4);
        final FloatingActionButton fab5 = findViewById(R.id.fab5);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AcercaDe.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UsuarioActivity.class);
                startActivity(i);
                menuBotones.collapse();

                menuBotones.collapse();
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                menuBotones.collapse();
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PreferenciasActivity.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });
            fab5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    menuBotones.collapse();
                }
            });



/*
            // COGE LA TEMPERATURA DEL BAÑO
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            tempB = findViewById(R.id.tempBañPlano);
            DocumentReference docRef = db.collection("Habitación").document("datos");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String a=document.get("Temperatura").toString();
                            Log.e("AAAAAA",document.getId());
                            tempB.setText(""+a);
                        } else {
                            Log.d("dd", "No such document");
                        }
                    } else {
                        Log.d("ss", "get failed with ", task.getException());
                    }
                }
            });


            // COGE LA TEMPERATURA DEL SALÓN
            tempS = findViewById(R.id.tempSalPlano);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String a=document.get("Temperatura").toString();
                            Log.e("AAAAAA",document.getId());
                            tempS.setText(""+a);
                        } else {
                            Log.d("dd", "No such document");
                        }
                    } else {
                        Log.d("ss", "get failed with ", task.getException());
                    }
                }
            });

            // COGE LA TEMPERATURA DEL HABITACION
            tempH = findViewById(R.id.tempHabPlano);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String a=document.get("Temperatura").toString();
                            Log.e("AAAAAA",document.getId());
                            tempH.setText(""+a);
                        } else {
                            Log.d("dd", "No such document");
                        }
                    } else {
                        Log.d("ss", "get failed with ", task.getException());
                    }
                }
            });

            // COGE LA TEMPERATURA DEL COCINA
            tempC = findViewById(R.id.tempCocPlano);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String a=document.get("Temperatura").toString();
                            Log.e("AAAAAA",document.getId());
                            tempC.setText(""+a);
                        } else {
                            Log.d("dd", "No such document");
                        }
                    } else {
                        Log.d("ss", "get failed with ", task.getException());
                    }
                }
            });

*/
            //VARIABLES
            longitud = findViewById(R.id.Longi);
            latitud = findViewById(R.id.Lati);

            // Hacemos esto para recibir el valor de la longitud
            IntentFilter filtro = new IntentFilter(ReceptorOperacion.ACTION_RESP);
            filtro.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(new ReceptorOperacion(), filtro);
        }

    //Mostrar temperatura en el plano
    public void lanzarTempPlano(View v) {
        Intent i = new Intent(this, Temperatura.class);
        startActivity(i);
    }

// OPCIONES MENU TOOLBAR
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.removeItem(R.id.servicio_tecnico);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesion) {
            new AlertDialog.Builder(this)
                    .setTitle("Cierre de Sesión")
                    .setMessage("¿Estas seguro que quieres cerrar tu sesión?")
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            confirmarCierreSesion();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

        public void confirmarCierreSesion () {
        AuthUI.getInstance().signOut(PlanoCasa.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(PlanoCasa.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                PlanoCasa.this.finish();
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // PERMISOS
    //------------------------------------------------------------------------------------------------------------------------------------------
    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad) .setTitle("Solicitud de permiso").setMessage(justificacion).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
                }
            }) .show();
        }else{
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_FINE_LOCATION) {
            if (grantResults.length== 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                localizacion(null);
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la " + "acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void localizacion(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Al pulsar el botón 2, se lanzará un servicio en primer plano
            startService(new Intent(PlanoCasa.this, ServicioLocalizacion.class));
            //getLocation();
        }else{
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso"+ "localización fina no se puede ejecutar.", SOLICITUD_PERMISO_FINE_LOCATION, this);
        }
    }


    public class ReceptorOperacion extends BroadcastReceiver {
        public static final String ACTION_RESP = "org.example.laumega1.examentipo1.intent.action.RESPUESTA_OPERACION";
        @Override
        public void onReceive(Context context, Intent intent) {
            Double longi = intent.getDoubleExtra("longitud", 0.0);
            longitud.setText(longi.toString());
            Double lati = intent.getDoubleExtra("latitud", 0.0);
            latitud.setText(lati.toString());
        }
    }

    public void lanzarMapa(View v) {
        Intent i = new Intent(this, Mapa.class);
        startActivity(i);
    }

}