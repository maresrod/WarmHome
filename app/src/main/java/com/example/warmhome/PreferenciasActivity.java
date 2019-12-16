package com.example.warmhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class PreferenciasActivity extends Activity {

      //  CheckBox musica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
/*
        musica = (CheckBox) findViewById(R.id.musica);

        musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bandera = ((CheckBox)v).isChecked();
                if(bandera){
                        startService(new Intent(PreferenciasActivity.this,
                                ServicioMusica.class));
                }else{
                    stopService(new Intent(PreferenciasActivity.this,
                            ServicioMusica.class));
                }
            }
        });
 */
    }
}
