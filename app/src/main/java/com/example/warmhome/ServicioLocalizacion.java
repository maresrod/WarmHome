package com.example.warmhome;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.warmhome.Habitaciones.PlanoCasa;

import java.util.List;

public class ServicioLocalizacion extends Service {
    // Notificación necesaria para lanzar un serivio en primer plano
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    // Handler para hacer un delay
    private Handler h = new Handler();
    private int delay = 5000; // en milisegundos
    // Creamos lo necesario para obtener la localización
    private LocationManager locationManager; // Proporciona acceso al servicio de localización del sistema
    private double longitud;
    private double latitud;

    @Override public void onCreate() {
        Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        // Creamos una notificiación que nos servirá para iniciar el servicio
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel( CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(ServicioLocalizacion.this, CANAL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Título").setContentText("Texto de la notificación.");
        //notificationManager.notify(NOTIFICACION_ID, notificacion.build());
        startForeground(NOTIFICACION_ID,notificacion.build());
        //------------------------------------------------------------------------------------------------------------
        Toast.makeText(this,"Servicio arrancado "+ idArranque, Toast.LENGTH_SHORT).show();
        // Hacemos esto para que se ejecute cada 5 segunos, que es lo que nos pide el ejercicio 5
        h.postDelayed(new Runnable(){
            public void run(){
                getLocation();
                Intent i = new Intent();
                i.setAction(PlanoCasa.ReceptorOperacion.ACTION_RESP);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.putExtra("longitud", longitud);
                i.putExtra("latitud", latitud);
                sendBroadcast(i);
                h.postDelayed(this, delay);
            }
        }, delay);
        return START_STICKY;
    }

    @Override public void onDestroy() {
        notificationManager.cancel(NOTIFICACION_ID);
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
    }

    @Override public IBinder onBind(Intent intencion) {
        return null;
    }

    public void getLocation() {
        String location_context = Context.LOCATION_SERVICE;
        // Se asigna la clase LocationManager el servicio a nivel de sistema a partir del nombre
        locationManager = (LocationManager) this.getApplicationContext().getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
                public void onLocationChanged(Location location) {}

                public void onProviderDisabled(String provider) {}

                public void onProviderEnabled(String provider) {}

                public void onStatusChanged(String provider, int status, Bundle extras) {}
            });
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d("Location",String.valueOf(location.getLatitude()));
                latitud = location.getLatitude();
                Log.d("Location",String.valueOf(location.getLongitude()));
                longitud = location.getLongitude();
                /*Log.d("Location",String.valueOf(location.getAltitude()));
                Log.d("Location",String.valueOf(location.getAccuracy()));*/
            }
        }
    }
}
