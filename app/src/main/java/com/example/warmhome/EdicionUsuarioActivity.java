package com.example.warmhome;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import static com.example.warmhome.UsuarioFragment.telefono;

public class EdicionUsuarioActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_fragment_usuario);

        Bundle extras = getIntent().getExtras();
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = (TextView) findViewById(R.id.nombre);
        TextView email = (TextView) findViewById(R.id.email);
        TextView proveedores = (TextView) findViewById(R.id.proveedores);
        TextView telefono = (TextView) findViewById(R.id.telefono);
        TextView iden = (TextView) findViewById(R.id.iden);
        proveedores.setText(usuario.getProviderData().toString());
        nombre.setText(usuario.getDisplayName());
        email.setText(usuario.getEmail());
        iden.setText(usuario.getUid());
        telefono.setText(usuario.getPhoneNumber());

    }

    public void cancelar (View view){
        finish();
    }
    public void guardar (View view){
        finish();
    }
}