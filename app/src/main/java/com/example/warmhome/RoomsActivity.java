package com.example.warmhome;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import com.example.warmhome.Habitaciones.Baño;
import com.example.warmhome.Habitaciones.Cocina;
import com.example.warmhome.Habitaciones.Habitacion;
import com.example.warmhome.Habitaciones.PlanoCasa;
import com.example.warmhome.Habitaciones.Salon;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


// CLASE DONDE SE VISUALIZARÁN LOS TABS

public class RoomsActivity extends FragmentActivity {
    private FragmentTabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Resources res = getResources();

        tabHost =  findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("",
               res.getDrawable(R.drawable.bath) ),
                Baño.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("",
                res.getDrawable(R.drawable.bed) ),
                Habitacion.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("",
                res.getDrawable(R.drawable.fridge) ),
                Cocina.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("",
                res.getDrawable(R.drawable.couch) ),
                Salon.class, null);


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
    }
}