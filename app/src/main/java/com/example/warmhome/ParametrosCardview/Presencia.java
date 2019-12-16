package com.example.warmhome.ParametrosCardview;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.warmhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

import static com.example.warmhome.comun.Mqtt.broker;
import static com.example.warmhome.comun.Mqtt.qos;
import static com.example.warmhome.comun.Mqtt.topicRoot;
import static com.example.warmhome.comun.Mqtt.clientId;
import static com.example.warmhome.comun.Mqtt.TAG;


public class Presencia extends Activity implements MqttCallback {

    MqttClient client;
    String cambio = "ON";
    TextView textoPresencia;
    int contador =0 ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> datos = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presencia);


        textoPresencia = findViewById(R.id.textopresencia);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        ImageView bSalir = findViewById(R.id.cruz);
        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();

            }

        });

        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic", "App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);

        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar.", e);
        }
        try {
            Log.i(TAG, "Suscrito a " + topicRoot+"#");
            client.subscribe(topicRoot+ "#", qos);
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }




    }
    public void salir(View view){
        finish();
    }

    @Override public void connectionLost(Throwable cause) {
        Log.d(TAG, "Conexión perdida");
    }
    @Override public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "Entrega completa");
    }



    @Override public void messageArrived(final String topic, MqttMessage message)
            throws Exception {
        final String payload = new String(message.getPayload());
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if (topic.equals("Grupo6xD/proyecto/presencia")) {
                    textoPresencia.setText(payload);

                    if(payload.equals("hay alguien en la habitacion")){
                        try {
                            MqttMessage message = new MqttMessage("ON".getBytes());
                            Log.i(TAG, "Publicando mensaje: " + message);
                            message.setQos(qos);
                            message.setRetained(false);
                            client.publish(topicRoot + "cmnd/POWER", message);

                        } catch (MqttException e) {
                            Log.e(TAG, "Error al publicar.", e);
                        }
                        datos.put("presencia", true);
                        db.collection("Baño").document("presencia").set(datos);
                    }

                    if(payload.equals("habitacion vacia")){
                        contador++;
                        if(contador == 2){
                            try {
                                MqttMessage message = new MqttMessage("OFF".getBytes());
                                Log.i(TAG, "Publicando mensaje: " + message);
                                message.setQos(qos);
                                message.setRetained(false);
                                client.publish(topicRoot + "cmnd/POWER", message);

                            } catch (MqttException e) {
                                Log.e(TAG, "Error al publicar.", e);
                            }
                            datos.put("presencia", false);
                            db.collection("Baño").document("presencia").set(datos);
                            contador = 0;
                        }
                    }
                }


            }
        });

    }
}