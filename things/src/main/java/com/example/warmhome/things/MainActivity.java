package com.example.warmhome.things;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {
    private static final int INTERVALO_LED = 2000;
    private Handler handler = new Handler();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> datos = new HashMap<>();


    ArduinoUart uart;

    private static final String TAG = "gsghdj";

    private static final String LED_PIN = "BCM13"; // Puerto GPIO del LED
    private static final String LED_PIN_2 = "BCM19"; // Puerto GPIO del LED
    private static final String LED_PIN_3 = "BCM26"; // Puerto GPIO del LED


    private Gpio ledGpio;
    private Gpio ledGpio2;
    private Gpio ledGpio3;
    String luz;
    String encender;

    PeripheralManager manager = PeripheralManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Lista de UART disponibles: " + ArduinoUart.disponibles());
        uart = new ArduinoUart("UART0", 115200);
        Log.d(TAG, "Mandado a Arduino: H");
        uart.escribir("H");
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        handler.post(runnable);


        try {
            ledGpio = manager.openGpio(LED_PIN); // 1. Crea conexión GPIO
            ledGpio2 = manager.openGpio(LED_PIN_2);
            ledGpio3 = manager.openGpio(LED_PIN_3);
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW); // 2. Se indica que es de salida
            ledGpio2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            ledGpio3.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);



           /* docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            luz = document.get("color").toString();
                            Log.e("AAAAAA", document.getId());
                            cambiarLuz();

                        } else {
                            Log.d("dd", "No such document");
                        }
                    } else {
                        Log.d("ss", "get failed with ", task.getException());
                    }
                }
            });*/

            DocumentReference docRef = db.collection("Baño").document("luz");
            docRef.addSnapshotListener(
                    new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("Firebase", "Error al leer", e);
                            } else if (snapshot == null || !snapshot.exists()) {
                                Log.e("Firebase", "Error: documento no encontrado ");
                            } else {
                                encender = snapshot.get("encender").toString();
                                luz = snapshot.get("color").toString();

                                Log.e("AAAAAA", snapshot.getId());
                                Log.e("EEE", luz);
                                cambiarLuz();
                                Log.i("aasd", "funciona");
                                ;
                            }
                        }
                    });

        } catch (IOException e) {
            Log.e(TAG, "Error en PeripheralIO API", e);
        }

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //Log.d(TAG, "Distancia");
            // uart.escribir("Distancia");
            //uart.escribir("Segundos");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.w(TAG, "Error en sleep()", e);
            }
            String s = uart.leer();

            Log.d(TAG, "Recibido de Arduino: " + s);   //{"Hora":10,"Minuto":13,"Segundo":13,"Distancia":0,"temperatura":21.7,"Movimiento":false}

            String a[] = s.split("\\n");
            for (int i = 0; i < a.length; i++) {
                Log.d(TAG, "yeeeeeeeee " + a[i] );
            }

            Log.i("minutos", a[1]);
            Log.i("segundos", a[2]);
            Log.i("distancia", a[3]);
            Log.i("temperatura", a[4]);
            Log.i("humedad", a[5]);
            Log.i("temperatura1", a[6]);
            Log.i("humedad1", a[7]);


            datos.put("Humedad", a[5]);
            datos.put("Minutos", a[1]);
            datos.put("Humedad1", a[7]);
            datos.put("Segundos", a[2]);
            datos.put("Temperatura", a[4]);
            datos.put("Distancia", a[3]);
            datos.put("Temperatura1", a[6]);


            db.collection("Habitación").document("datos").set(datos);


            /*db.collection("datos2")
                    .add(date)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });*/

            //datos.put("Temp", s);
            //db.collection("datos").document("datos").set(datos);
            //String a[] = s.split(" ");
            /*JsonElement medidas = new JsonParser().parse(s);
            double distancia = medidas.getAsJsonObject().get("Distancia").getAsDouble();
            double hora = medidas.getAsJsonObject().get("Hora").getAsDouble();
            double minutos = medidas.getAsJsonObject().get("Minuto").getAsDouble();
            double segundos = medidas.getAsJsonObject().get("Segundo").getAsDouble();
            boolean movimiento = medidas.getAsJsonObject().get("Movimiento").getAsBoolean();
            float temperatura = medidas.getAsJsonObject().get("temperatura").getAsFloat();

             */
            //Datos datos1 = new Datos(a[1], a[3], a[5], a[7], a[9], a[11]);//paso los datos a la clase datos

            //db.collection("datos").document("datos1").set(s);
            //db.document("datos").set(datos);
          /*  db.document("datos").set(datos);;

            JSONObject medidas = jsonParser.parse(s).getAsJsonObject();

            //Map<String, Object> altura = new HashMap<>();

            double altura = medidas.getAsJsonObject().get("Altura").getAsDouble();*/


            handler.postDelayed(runnable, INTERVALO_LED);

        }

    };


    private void cambiarLuz() {
        boolean a = true;
        boolean b = false;


        try {

            if (encender.equals(false)) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                if (luz.equals("rojo")) {
                    ledGpio.setValue(a);// 4. Cambiamos valor LED
                    ledGpio2.setValue(b);
                    ledGpio3.setValue(b);   // rojo

                }
                if (luz.equals("verde")) {
                    ledGpio.setValue(b);
                    ledGpio2.setValue(a);
                    ledGpio3.setValue(b);   // verde
                }
                if (luz.equals("azul")) {
                    ledGpio.setValue(b);
                    ledGpio2.setValue(b);
                    ledGpio3.setValue(a);   // azul
                }
                if (luz.equals("blanco")) {
                    ledGpio.setValue(a);
                    ledGpio2.setValue(a);
                    ledGpio3.setValue(a); // blanco
                }
                if (luz.equals("amarillo")) {
                    ledGpio.setValue(a);
                    ledGpio2.setValue(a);
                    ledGpio3.setValue(b); // amarillo
                }
                if (luz.equals("cian")) {
                    ledGpio.setValue(b);
                    ledGpio2.setValue(a);
                    ledGpio3.setValue(a); // cian
                }
                if (luz.equals("magenta")) {
                    ledGpio.setValue(b);
                    ledGpio2.setValue(b);
                    ledGpio3.setValue(b); // magenta
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error en PeripheralIO API", e);
        }

    }

    ;





   /* private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String s = uart.leer();
            Log.d(TAG, "Recibido de Arduino: " + s);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> datos = new HashMap<>();
            Date d = Calendar.getInstance().getTime();
            datos.put("Temp", s);
            datos.put("Hora", d.toString());
            db.document("datos").set(datos);
            db.collection("coleccion").document("datos").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("Firebase", "Error al leer", e);
                    } else if (snapshot == null || !snapshot.exists()) {
                        Log.e("Firebase", "Error: documento no encontrado ");
                    } else {
                        Log.e("Firestore", "datos:" + snapshot.getData());
                    }
                }
            });
            handler.postDelayed(runnable,INTERVALO_LED);
        }
    };*/

}
