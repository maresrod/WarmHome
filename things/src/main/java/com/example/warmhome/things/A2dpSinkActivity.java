package com.example.warmhome.things;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.bluetooth.BluetoothProfileManager;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*En esta actividad estableceremos el adaptador de Bluetooth en modo emparejamiento
* Mientras estamos en modo emparejamiento las solicitudes de emparejamiento se aceptan
* automáticamente.*/

public class A2dpSinkActivity extends Activity {
    private static final String TAG = "A2dpSinkActivity";

    private static final String ADAPTER_FRIENDLY_NAME = "WarmHome Music";
    private static final int DISCOVERABLE_TIMEOUT_MS = 1500;
    private static final int REQUEST_CODE_ENABLE_DISCOVERABLE = 1500;

    private static final String UTTERANCE_ID =
            "com.example.warmhome.things.UTTERANCE_ID";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothProfile mA2DPSinkProxy;

    private ButtonInputDriver mPairingButtonDriver;
    private ButtonInputDriver mDisconnectAllButtonDriver;

    private TextToSpeech mTtsEngine;

    /*Manejar una intención transmitida por el adaptador bluetooth cada vez que cambie su estado,
    * lo utilizamos para decir que el dispositivo está listo para funcionar*/

    private final BroadcastReceiver mAdapterStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int oldState = A2dpSinkHelper.getPreviousAdapterState(intent);
            int newState = A2dpSinkHelper.getCurrentAdapterState(intent);
            Log.d(TAG, "Adaptador Bluetooth cambia de" + oldState + " a " + newState);
            if (newState == BluetoothAdapter.STATE_ON) {
                Log.i(TAG, "Adaptador Bluetooth está preparado");
                initA2DPSink();
            }
        }
    };

    /*Maneja una intención que se transmite por el perfil del receptor Bluetooth A2DP cada vez que un dispositivo
     * se conecta o desconecta a él.
     * Los extras describen los estados de conexión antiguos y nuevos. Se pueden usar para indicar que
     * hay un dispositivo conectado.
     */

    private final BroadcastReceiver mSinkProfileStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(A2dpSinkHelper.ACTION_CONNECTION_STATE_CHANGED)) {
                int oldState = A2dpSinkHelper.getPreviousProfileState(intent);
                int newState = A2dpSinkHelper.getCurrentProfileState(intent);
                BluetoothDevice device = A2dpSinkHelper.getDevice(intent);
                Log.d(TAG, "Cambiando estado de conexión de" + oldState +
                        " a " + newState + " dispositivo " + device);
                if (device != null) {
                    String deviceName = Objects.toString(device.getName(), "un dispositivo");
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        speak("Conectado a " + deviceName);
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        speak("Desconectado de " + deviceName);
                    }
                }
            }
        }
    };

    /*Maneja una intención que se transmite por el perfil del receptor Bluetooth A2DP cada vez que un dispositivo
     * comienza o detiene la reproducción a través del sumidero A2DP.*/

    private final BroadcastReceiver mSinkProfilePlaybackChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(A2dpSinkHelper.ACTION_PLAYING_STATE_CHANGED)) {
                int oldState = A2dpSinkHelper.getPreviousProfileState(intent);
                int newState = A2dpSinkHelper.getCurrentProfileState(intent);
                BluetoothDevice device = A2dpSinkHelper.getDevice(intent);
                Log.d(TAG,  "El receptor Bluetooth A2DP cambia el estado de reproducción de " + oldState +
                        " a " + newState + " dispositivo " + device);
                if (device != null) {
                    if (newState == A2dpSinkHelper.STATE_PLAYING) {
                        Log.i(TAG, "Reproduciendo audio desde el dispositivo " + device.getAddress());
                    } else if (newState == A2dpSinkHelper.STATE_NOT_PLAYING) {
                        Log.i(TAG, "Reproducción parada desde  " + device.getAddress());
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.w(TAG, "\n" +
                    "Sin adaptador Bluetooth predeterminado. Es probable que el dispositivo no sea compatible con bluetooth.");
            return;
        }

        // Utilizamos Text-to-Speech para indicar el cambio de estado al usuario
        initTts();

        registerReceiver(mAdapterStateChangeReceiver, new IntentFilter(
                BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(mSinkProfileStateChangeReceiver, new IntentFilter(
                A2dpSinkHelper.ACTION_CONNECTION_STATE_CHANGED));
        registerReceiver(mSinkProfilePlaybackChangeReceiver, new IntentFilter(
                A2dpSinkHelper.ACTION_PLAYING_STATE_CHANGED));

        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "El adaptador Bluetooth ya está habilitado.");
            initA2DPSink();
        } else {
            Log.d(TAG, "Adaptador Bluetooth no habilitado. Habilitando");
            mBluetoothAdapter.enable();
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_P:
                //Habilitar el modo de emparejamiento (detectable)
                enableDiscoverable();
                return true;
            case KeyEvent.KEYCODE_D:
                // Desconecte todos los dispositivos conectados actualmente
                disconnectConnectedDevices();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        try {
            if (mPairingButtonDriver != null) mPairingButtonDriver.close();
        } catch (IOException e) {/*cerrar en silencio*/ }
        try {
            if (mDisconnectAllButtonDriver != null) mDisconnectAllButtonDriver.close();
        } catch (IOException e) { /* cerrar en silencio */}

        unregisterReceiver(mAdapterStateChangeReceiver);
        unregisterReceiver(mSinkProfileStateChangeReceiver);
        unregisterReceiver(mSinkProfilePlaybackChangeReceiver);

        if (mA2DPSinkProxy != null) {
            mBluetoothAdapter.closeProfileProxy(A2dpSinkHelper.A2DP_SINK_PROFILE,
                    mA2DPSinkProxy);
        }

        if (mTtsEngine != null) {
            mTtsEngine.stop();
            mTtsEngine.shutdown();
        }

        //Dejamos intencionalmente el adaptador Bluetooth habilitado, para que otras muestras puedan usarlo sin indicarlo

    }

    private void setupBTProfiles() {
        BluetoothProfileManager bluetoothProfileManager = BluetoothProfileManager.getInstance();
        List<Integer> enabledProfiles = bluetoothProfileManager.getEnabledProfiles();
        if (!enabledProfiles.contains(A2dpSinkHelper.A2DP_SINK_PROFILE)) {
            Log.d(TAG, "Habilitar el modo de sumidero A2dp.");
            List<Integer> toDisable = Arrays.asList(BluetoothProfile.A2DP);
            List<Integer> toEnable = Arrays.asList(
                    A2dpSinkHelper.A2DP_SINK_PROFILE,
                    A2dpSinkHelper.AVRCP_CONTROLLER_PROFILE);
            bluetoothProfileManager.enableAndDisableProfiles(toEnable, toDisable);
        } else {
            Log.d(TAG, "El perfil de sumidero A2dp está habilitado.");
        }
    }


     // Iniciamos la sincronización A2DP.

    private void initA2DPSink() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Log.e(TAG, "\n" +
                    "Adaptador Bluetooth no disponible o no habilitado.");
            return;
        }
        setupBTProfiles();
        Log.d(TAG, "\n" +
                "Configurar el nombre y el perfil del adaptador Bluetooth");
        mBluetoothAdapter.setName(ADAPTER_FRIENDLY_NAME);
        mBluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                mA2DPSinkProxy = proxy;
                enableDiscoverable();
            }
            @Override
            public void onServiceDisconnected(int profile) {
            }
        }, A2dpSinkHelper.A2DP_SINK_PROFILE);

        configureButton();
    }


    private void enableDiscoverable() {
        Log.d(TAG, "\n" +
                "Registrarse para el descubrimiento.");
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                DISCOVERABLE_TIMEOUT_MS);
        startActivityForResult(discoverableIntent, REQUEST_CODE_ENABLE_DISCOVERABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_DISCOVERABLE) {
            Log.d(TAG, "\n" +
                    "Habilitar recuperable devolver con resultado " + resultCode);

            //RESULT_CANCELED, número de milisegundos en el que el dispositivo permanece en modo reconocible.
            if (resultCode == RESULT_CANCELED) {
                Log.e(TAG, "\n" +
                        "Habilitar descubrimiento ha sido cancelado por el usuario. " +
                        "\n" +
                        "Esto nunca debería suceder en un dispositivo Android Things.");
                return;
            }
            Log.i(TAG, "Adaptador Bluetooth configurado correctamente en modo reconocible. " +
                    "\n" +
                    "Cualquier fuente A2DP puede encontrarlo con el nombre " + ADAPTER_FRIENDLY_NAME +
                    " y emparejar para el próximo " + DISCOVERABLE_TIMEOUT_MS + " ms. " +
                    "Intenta buscarlo en tu teléfono, por ejemplo.");

            speak("El receptor de audio Bluetooth es reconocible para " + DISCOVERABLE_TIMEOUT_MS +
                    "milisegundos. Busque un dispositivo llamado " + ADAPTER_FRIENDLY_NAME);

        }
    }

    private void disconnectConnectedDevices() {
        if (mA2DPSinkProxy == null || mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            return;
        }
        speak("Desconectando dispositivos");
        for (BluetoothDevice device: mA2DPSinkProxy.getConnectedDevices()) {
            Log.i(TAG, "Desconectando dispositivo " + device);
            A2dpSinkHelper.disconnect(mA2DPSinkProxy, device);
        }
    }

    private void configureButton() {
        try {
            mPairingButtonDriver = new ButtonInputDriver(BoardDefaults.getGPIOForPairing(),
                    Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_P);
            mPairingButtonDriver.register();
            mDisconnectAllButtonDriver = new ButtonInputDriver(
                    BoardDefaults.getGPIOForDisconnectAllBTDevices(),
                    Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_D);
            mDisconnectAllButtonDriver.register();
        } catch (IOException e) {
            Log.w(TAG, "No se pudieron registrar los controladores de botón GPIO. Usar eventos de teclado para disparar " +
                    "las funciones en su lugar", e);
        }
    }

    private void initTts() {
        mTtsEngine = new TextToSpeech(A2dpSinkActivity.this,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            mTtsEngine.setLanguage(Locale.US);
                        } else {
                            Log.w(TAG, "No podrías abrir TTS Engine (onInit status=" + status
                                    + "). Ignoring text to speech");
                            mTtsEngine = null;
                        }
                    }
                });
    }


    private void speak(String utterance) {
        Log.i(TAG, utterance);
        if (mTtsEngine != null) {
            mTtsEngine.speak(utterance, TextToSpeech.QUEUE_ADD, null, UTTERANCE_ID);
        }
    }
}

