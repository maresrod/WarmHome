package com.example.warmhome;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warmhome.Habitaciones.PlanoCasa;
import com.example.warmhome.comun.Imagen;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static com.example.warmhome.comun.Imagen.registrarImagen;

public class CamaraPortal extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_CALL_PHONE = 0;
    private StorageReference storageRef;
    RecyclerView recyclerView;
    private AdaptadorImagenes adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_portal);

        //      TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //      MENU FLOTANTE
        final FloatingActionsMenu menuBotones = (FloatingActionsMenu) findViewById(R.id.grupofab);
        menuBotones.setScaleX(0);
        menuBotones.setScaleY(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            menuBotones.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(500)
            ;
        }
        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);
        final FloatingActionButton fab4 = findViewById(R.id.fab4);
        final FloatingActionButton fab5 = findViewById(R.id.fab5);
        final FloatingActionButton fab6 = findViewById(R.id.fab6);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InstruccionesActivity.class);
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
                Intent i = new Intent(getApplicationContext(), ServicioTecnicoActivity.class);
                startActivity(i);

                menuBotones.collapse();
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BluetoothActivity.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PlanoCasa.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });
        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CamaraPortal.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });

        //////////////////////////////////////////
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        Query query = FirebaseFirestore.getInstance()
                .collection("imagenes")
                .orderBy("tiempo", Query.Direction.DESCENDING)
                .limit(50);
        FirestoreRecyclerOptions<Imagen> opciones = new FirestoreRecyclerOptions
                .Builder<Imagen>().setQuery(query, Imagen.class).build();

        adaptador = new AdaptadorImagenes(this, opciones);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storageRef = FirebaseStorage.getInstance().getReference();
 //bajarFichero();
    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                String nombreFichero = UUID.randomUUID().toString();

                subirFichero(data.getData(), "imagenes/" + nombreFichero);
            }
        }
    }

    private void subirFichero(Uri fichero, String referencia) {
        final StorageReference ref = storageRef.child(referencia);
        UploadTask uploadTask = ref.putFile(fichero);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull
                                          Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.e("Almacenamiento", "URL: " + downloadUri.toString());
                    registrarImagen("Subida por Móvil", downloadUri.toString());
                } else {
                    Log.e("Almacenamiento", "ERROR: subiendo fichero");
                }
            }
        });
    }

    private void bajarFichero() {
        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String path = localFile.getAbsolutePath();
        Log.d("Almacenamiento", "creando fichero: " + path);
        StorageReference ficheroRef = storageRef.child("imagenes/image");
        ficheroRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("Almacenamiento", "Fichero bajado");
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Almacenamiento", "ERROR: bajando fichero");
            }
        });
    }

    private void delete() {
        StorageReference referenciaImagen = storageRef.child("imagenes/image");
        referenciaImagen.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) { //Error al subir el fichero
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptador.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }

    //    OPCIONES MENÚ TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camara, menu);
        //menu.removeItem(R.id.info_usuario);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesion) {
            AlertDialog.Builder  builder = new AlertDialog.Builder(CamaraPortal.this);

            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_personalizado, null);

            builder.setView(view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            TextView txt = view.findViewById(R.id.text_dialog);

            Button btnCancelar = view.findViewById(R.id.cancelar);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button btnConfirmar = view.findViewById(R.id.confirmar);
            btnConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmarCierreSesion();
                }
            });

        }
        if(id== R.id.llamar){
            LLamar();
        }

        if(id==R.id.graficas){
            Intent i = new Intent(CamaraPortal.this, Graficas.class);
            startActivity(i);
        }


        if(id== R.id.preferencias){
            lanzarPreferencias(null);
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    public void confirmarCierreSesion(){
        AuthUI.getInstance().signOut(CamaraPortal.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(CamaraPortal.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                CamaraPortal.this.finish();
            }
        });
    }

    public void LLamar(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:962849300"));
            startActivity(intent);
        } else {
            solicitarPermiso(Manifest.permission.CALL_PHONE, "Sin el perimso no se puede realizar esta llamada.",
                    SOLICITUD_PERMISO_CALL_PHONE, this);
        }
    }
    public static void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)) {
            new androidx.appcompat.app.AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }


    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_CALL_PHONE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LLamar();
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la llamada", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
