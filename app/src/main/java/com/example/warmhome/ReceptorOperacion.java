package com.example.warmhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceptorOperacion extends BroadcastReceiver {

    public static final String ACTION_RESP = "com.example.intentservice.intent.action.RESPUESTA_OPERACION";
    @Override
    public void onReceive(Context context, Intent intent) {
        Double res = intent.getDoubleExtra("resultado", 0.0);
        Log.d("Prueba",res.toString());
        //longitud.setText(res.toString());
    }
}