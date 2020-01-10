package com.example.warmhome;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.warmhome.Habitaciones.PlanoCasa;
import com.example.warmhome.ParametrosCardview.Presencia;
import com.example.warmhome.ParametrosCardview.Temperatura;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static com.example.warmhome.comun.Imagen.registrarImagen;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AIListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Button btnCancelar;
    private Button btnConfirmar;
    private boolean flag;
    private static final int SOLICITUD_PERMISO_CALL_PHONE = 0;
    private static final int SOLICITUD_PERMISO_RECORD_AUDIO = 200;
    private AIService mAIService;
    private TextToSpeech mTextToSpeech;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private PendingIntent pendingIntent;
    private PendingIntent pendingIntent2;

    //      ARRAYLIST Y RECYCLERVIEW LANDIND
    ArrayList<LugarLanding> listaLanding;
    RecyclerView recyclerLugaresLanding;



    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        flag = isUserLogged();

        NavigationView navigation = findViewById(R.id.nav_view);
        View hView = navigation.getHeaderView(0);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        if (flag) {
            TextView name = hView.findViewById(R.id.menunombre);
            name.setText(usuario.getDisplayName());
            TextView email = hView.findViewById(R.id.menucorreo);
            email.setText(usuario.getEmail());
        } else {
            TextView name = hView.findViewById(R.id.menunombre);
            name.setText("Anónimo");
            TextView email = hView.findViewById(R.id.menucorreo);
            email.setText("");
        }

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


//      ------------ RECYCLERVIEW LANDING ------------
        listaLanding = new ArrayList<>();
        recyclerLugaresLanding = (RecyclerView) findViewById(R.id.RecyclerId);
        recyclerLugaresLanding.setLayoutManager(new LinearLayoutManager(this));

        llenaLugares();

        AdaptadorLanding adapter = new AdaptadorLanding(listaLanding);
        recyclerLugaresLanding.setAdapter(adapter);


        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerLugaresLanding.getChildAdapterPosition(v);
                mostrar(pos); // se podría hacer otra clase java más
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

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
                            double d = Double.parseDouble(a);
                            Log.i("leer", String.valueOf(d));
                            if (d >= 22) {
                                setPendingIntent();
                                createNotificationChannel();
                                createNotification();
                            }

                        }
                    }
                }
        );

        db.collection("salon").document("datos").addSnapshotListener(
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
                            Log.i("mostrar dato", a);
                            double d = Double.parseDouble(a);
                            Log.i("leer", String.valueOf(d));
                            if (d >= 28) {
                                setPendingIntent();
                                createNotificationChannel();
                                createNotification();
                            }
                        }
                    }
                }
        );

        db.collection("Baño").document("presencia").addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firebase", "Error al leer", e);
                        } else if (snapshot == null || !snapshot.exists()) {
                            Log.e("Firebase", "Error: documento no encontrado ");
                        } else {
                            //String a = Objects.requireNonNull(snapshot.get("presencia")).toString();
                            boolean d = snapshot.getBoolean("presencia");
                            String a = String.valueOf(d);
                            Log.i("Firebase", a);
                            if (d == true) {
                                setPendingIntent2();
                                createNotificationChannel();
                                createNotification2();
                            }

                        }
                    }
                }
        );


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer1) {
            Intent i = new Intent(MainActivity.this, AcercaDe.class);
            startActivity(i);
        } else if (id == R.id.drawer2) {
            Intent i = new Intent(MainActivity.this, UsuarioActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer3) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);

        } else if (id == R.id.drawer4) {
            Intent i = new Intent(MainActivity.this, ServicioTecnicoActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer5) {
            Intent i = new Intent(MainActivity.this, Buscador.class);
            startActivity(i);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void mostrar(int pos) {
        Intent i = new Intent(this, RoomsActivity.class);
        i.putExtra("pos", pos);
        this.startActivity(i);
    }

    private void llenaLugares() {
        listaLanding.add(new LugarLanding(R.drawable.banyoprueba, "Baño", "1 per."));
        listaLanding.add(new LugarLanding(R.drawable.habitacionprueba, "Habitacion", "1 per."));
        listaLanding.add(new LugarLanding(R.drawable.cocinaprueba, "Cocina", "1 per."));
        listaLanding.add(new LugarLanding(R.drawable.salonprueba, "Salon", "3 per."));
    }
//    ------------------------------------------------

    //    OPCIONES MENÚ TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.removeItem(R.id.info_usuario);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        final AIConfiguration config = new AIConfiguration("42f6c346f87542418a59be43010c751f ",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        mAIService = AIService.getService(this, config);
        mAIService.setListener(this);

        if (id == R.id.micro){
            validar();
            configAsistente();
            mAIService.startListening();
            Toast.makeText(this, "Assitanta activada", Toast.LENGTH_SHORT).show();
        }

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesion) {
            AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);

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
            Intent i = new Intent(MainActivity.this, Graficas.class);
            startActivity(i);
        }

        if(id== R.id.preferencias){
            lanzarPreferencias(null);
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    public void confirmarCierreSesion(){
        AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                MainActivity.this.finish();
            }
        });
    }
    public static boolean isUserLogged() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return true;
        }
        return false;
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


    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        mTextToSpeech.speak(result.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onError(AIError error) {
        Log.d("tag", error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    public void configAsistente (){
        final AIConfiguration config = new AIConfiguration("42f6c346f87542418a59be43010c751f ",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);
        mAIService = AIService.getService(this, config);
        mAIService.setListener(this);
    }
    public void validar(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            solicitarPermiso();
        }
    }
    public void solicitarPermiso(){
        solicitarPermiso(Manifest.permission.RECORD_AUDIO, "Sin el perimso no se puede realizar esta accion.",
                SOLICITUD_PERMISO_RECORD_AUDIO, this);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_wamhomeicon);
        builder.setTicker("Nueva notificacion");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("La temperatura del baño rara");
        builder.setContentText("La temperatura de la habitacion esta fuera de lo normal");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
    public void createNotification2(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_wamhomeicon);
        builder.setTicker("Nueva notificacion");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Presencia");
        builder.setContentText("Se ha detectado alguien en la casa");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }


    private void setPendingIntent(){
        Intent intent = new Intent (this, Temperatura.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setPendingIntent2(){
        Intent intent = new Intent (this, Presencia.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent2 = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}