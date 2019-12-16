package com.example.warmhome.ParametrosCardview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.warmhome.R;
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



public class Iluminacion extends Activity implements MqttCallback {

    MqttClient client;
    String cambio = "ON";
    TextView textoLuz;
    ImageView imagen;
    public Button Rojo;
    public Button Verde;
    public Button Azul;
    public Button Amarillo;
    public Button Morado;
    public Button Cian;
    public Button Blanco;
    public Button on;
    public Button off;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> datos = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iluminacion);


        textoLuz=findViewById(R.id.textoilum);
        imagen = findViewById(R.id.imagenilum);

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

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MqttMessage message = new MqttMessage(cambio.getBytes());
                    Log.i(TAG, "Publicando mensaje: " + message);
                    message.setQos(qos);
                    message.setRetained(false);
                    client.publish(topicRoot + "cmnd/POWER", message);
                    if(cambio.equals("ON")){
                        datos.put("luzEncendida", true);
                        db.collection("Baño").document("luz").set(datos);
                        cambio = "OFF";
                    }else {
                        datos.put("luzEncendida", false);
                        db.collection("Baño").document("luz").set(datos);
                        cambio = "ON";
                    }
                } catch (MqttException e) {
                    Log.e(TAG, "Error al publicar.", e);
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.9));

        ImageView bSalir = findViewById(R.id.cruz);
        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();

            }

        });

        Rojo = findViewById(R.id.Rojo);
        Verde = findViewById(R.id.Verde);
        Azul = findViewById(R.id.button5);
        Amarillo =findViewById(R.id.button6);
        Morado = findViewById(R.id.button7);
        Cian = findViewById(R.id.button8);
        Blanco = findViewById(R.id.button9);

        Rojo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Log.i("asdd","Vaaaaa");

                datos.put("color", "rojo");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
                Log.i("asdd","Funcionaaaa");
            }
        });

        Verde.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datos.put("color", "verde");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
            }
        });
        Azul.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datos.put("color", "azul");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
            }
        });
        Amarillo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datos.put("color", "amarillo");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
            }
        });
        Morado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                datos.put("color", "morado");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
            }
        });
        Cian.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datos.put("color", "cian");
                db.collection("Baño").document("luz").set(datos);
            }
        });
        Blanco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datos.put("color", "blanco");
                datos.put("encender", true);
                db.collection("Baño").document("luz").set(datos);
            }
        });

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
            @SuppressLint("WrongConstant")
            @Override public void run() {
                if (payload.equals("ON")) {
                    textoLuz.setText("ON");


                } else if (payload.equals("OFF")) {
                    textoLuz.setText("OFF");
                }


            }
        });

    }
}