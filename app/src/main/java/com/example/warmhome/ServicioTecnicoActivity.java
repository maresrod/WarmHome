package com.example.warmhome;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.warmhome.Habitaciones.PlanoCasa;
import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class ServicioTecnicoActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicio_tecnico);

        //      TOOLBAR

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


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
                Intent i = new Intent(getApplicationContext(), PlanoCasa.class);
                startActivity(i);
                menuBotones.collapse();
            }
        });

    }


    // OPCIONES MENU TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.removeItem(R.id.servicio_tecnico);
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
            AlertDialog.Builder  builder = new AlertDialog.Builder(ServicioTecnicoActivity.this);

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
        return super.onOptionsItemSelected(item);
    }

    public void confirmarCierreSesion(){
        AuthUI.getInstance().signOut(ServicioTecnicoActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(ServicioTecnicoActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                ServicioTecnicoActivity.this.finish();
            }
        });
    }

    // INTENCIONES PÁGINA WEB Y LLAMDA(CAMBIAR POR ACTION_CALL)
    public void verPgWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.ecomputer.es/servicio-tecnico/"));
        startActivity(intent);
    }

    public void lanzarLlamada (View v) {
        if(ActivityCompat.checkSelfPermission(ServicioTecnicoActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "962849300")));
        }else{
            ActivityCompat.requestPermissions(ServicioTecnicoActivity.this, new String[]{ Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            lanzarLlamada(null);
        }
    }

    public void lanzarDireccion(View view) {
        String labelLocation = "Dirección del técnico";
        String uri = "geo:<" + "38.99643"+ ">,<" + "-0.165367"+ ">?q=<" + "38.99643"+ ">,<" + "-0.165367"+ ">(" + labelLocation + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }




    public void mandarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "asunto");
        intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jtomas@upv.es"});
        startActivity(intent);
    }



}
