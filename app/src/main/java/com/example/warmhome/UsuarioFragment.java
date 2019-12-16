package com.example.warmhome;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFragment extends Fragment {

    String TAG ="MARCE";
    @Override
    public View onCreateView(LayoutInflater inflador,
                             ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario,
                contenedor, false);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = (TextView) vista.findViewById(R.id.nombre);
        TextView email = (TextView) vista.findViewById(R.id.email);
        TextView proveedores = (TextView) vista.findViewById(R.id.proveedores);
        TextView telefono = (TextView) vista.findViewById(R.id.telefono);
        TextView iden = (TextView) vista.findViewById(R.id.iden);

        proveedores.setText(usuario.getProviderId());
        nombre.setText(usuario.getDisplayName());
        email.setText(usuario.getEmail());
        iden.setText(usuario.getUid());
        telefono.setText(usuario.getPhoneNumber());

        Button btnConvertir = vista.findViewById(R.id.btnConvertir);

        if(!usuario.isAnonymous()){
            btnConvertir.setVisibility(View.GONE);
        }else{
            btnConvertir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAuth mAuth;

                    mAuth = FirebaseAuth.getInstance();


                    String eml = "bbbb@gmail.com";
                    String password = "12345678";

                    AuthCredential credential = EmailAuthProvider.getCredential(eml, password);

                    mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(getContext(),"Auntenticado correctamente", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }


        //RECOGE IMAGEN DEL PROVEEDOR

        // Inicialización Volley (Hacer solo una vez en Singleton o Applicaction)
        RequestQueue colaPeticiones = Volley.newRequestQueue(getActivity()
                .getApplicationContext());
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(10);

                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }

                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                });
// Foto de usuario
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView fotoUsuario = (NetworkImageView)
                    vista.findViewById(R.id.imagen);
            fotoUsuario.setImageUrl(urlImagen.toString(), lectorImagenes);
        }
        return vista;
    }
}
