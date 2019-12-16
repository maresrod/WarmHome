package com.example.warmhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorLanding extends RecyclerView.Adapter<AdaptadorLanding.ViewHolderLanding>{


    protected View.OnClickListener onClickListener;
    ArrayList<LugarLanding> listaLanding;

    public AdaptadorLanding(ArrayList<LugarLanding> listaLanding){
        this.listaLanding = listaLanding;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolderLanding onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflamos la vista desde el xml
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementos_landing, null, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolderLanding(v);
    }
    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolderLanding holder, int posicion) {
        holder.nombre.setText(listaLanding.get(posicion).getNombre());
        holder.notificacion.setText(listaLanding.get(posicion).getNotificacion());
        holder.imagenFondo.setImageResource(listaLanding.get(posicion).getImagenFondo());
    }



    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return listaLanding.size();
    }

    public class ViewHolderLanding extends RecyclerView.ViewHolder{

        ImageView imagenFondo;
        TextView nombre, notificacion;

        public ViewHolderLanding(View itemView){
            super(itemView);
            imagenFondo = (ImageView) itemView.findViewById(R.id.fondo);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            notificacion = (TextView) itemView.findViewById(R.id.notificaciones);
        }
    }
}