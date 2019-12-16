package com.example.warmhome.Habitaciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.warmhome.ParametrosCardview.Iluminacion;
import com.example.warmhome.ParametrosCardview.Presencia;
import com.example.warmhome.ParametrosCardview.Temperatura;
import com.example.warmhome.ParametrosCardview.Ventana;
import com.example.warmhome.R;

// TAB SALON
public class Salon extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_salon, container, true);

        CardView tarjetaT = view.findViewById(R.id.TemperaturaS);

        tarjetaT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Temperatura.class);
                startActivity(i);

            }
        });

        //Tarjeta Presencia
        CardView tarjetaP = view.findViewById(R.id.PresenciaS);

        tarjetaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Presencia.class);
                startActivity(i);

            }
        });

        //Tarjeta Iluminacion
        CardView tarjetaI = view.findViewById(R.id.LucesS);

        tarjetaI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Iluminacion.class);
                startActivity(i);

            }
        });

        //Tarjeta Ventana
        CardView tarjetaV = view.findViewById(R.id.VentanasS);

        tarjetaV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEEEEEEEEE","CLICKASTE");
                Intent i = new Intent(getContext(), Ventana.class);
                startActivity(i);

            }
        });
        return inflater.inflate(R.layout.fragment_salon, container, false);
    }
}
