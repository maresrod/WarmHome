package com.example.warmhome;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.warmhome.Habitaciones.PlanoCasa;
import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Button btnCancelar;
    private Button btnConfirmar;
    private boolean flag;
    private static final int SOLICITUD_PERMISO_CALL_PHONE = 0;


    //      ARRAYLIST Y RECYCLERVIEW LANDIND
    ArrayList<LugarLanding> listaLanding;
    RecyclerView recyclerLugaresLanding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        setTheme(R.style.DarkTheme);

        flag = isUserLogged();

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

//      TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                Intent i = new Intent(getApplicationContext(), ServicioTecnicoActivity.class);
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
                Intent i = new Intent(getApplicationContext(), PlanoCasa.class);
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
            Intent i = new Intent(MainActivity.this, PreferenciasActivity.class);
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
        listaLanding.add(new LugarLanding(R.drawable.planocasaprueba, "Plano Casa", null));
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
}