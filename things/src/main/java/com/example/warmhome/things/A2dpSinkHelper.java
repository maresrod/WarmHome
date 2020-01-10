package com.example.warmhome.things;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;


 /*Métodos auxiliares y constantes relacionados con el perfil A2DP_SINK.
 *La mayoría de las constantes definidas aquí se copian de las clases en el
 *{android.bluetooth} paquete, ya que están ocultos de la API pública de Android y no pueden
 *ser utilizado directamente.
 */

public final class A2dpSinkHelper {

    private static final String TAG = "A2DPSinkHelper";


     //Número de perfil para el perfil A2DP_SINK

    public static final int A2DP_SINK_PROFILE = 11;


     //Número de perfil para el perfil AVRCP_CONTROLLER.

    public static final int AVRCP_CONTROLLER_PROFILE = 12;


    public static final String ACTION_CONNECTION_STATE_CHANGED =
            "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED";

    //Transmitir el cambio de estado de reproducción del A2DP del perfil
    public static final String ACTION_PLAYING_STATE_CHANGED =
            "android.bluetooth.a2dp-sink.profile.action.PLAYING_STATE_CHANGED";

    //El dispositivo A2DP está transmitiendo música, este prodía ser un estado.
    public static final int STATE_PLAYING   =  10;

    //EL dispositivo no transmite música, este prodía ser otro estado.
    public static final int STATE_NOT_PLAYING   =  11;

    public static int getPreviousAdapterState(Intent intent) {
        return intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);
    }

    public static int getCurrentAdapterState(Intent intent) {
        return intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
    }

    public static int getPreviousProfileState(Intent intent) {
        return intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, -1);
    }

    public static int getCurrentProfileState(Intent intent) {
        return intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
    }

    public static BluetoothDevice getDevice(Intent intent) {
        return intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    }

    /*Proporciona una forma de llamar al método de desconexión en la clase BluetoothA2dpSink que está
     * actualmente oculto de la API pública.
    */

    public static boolean disconnect(BluetoothProfile profile, BluetoothDevice device) {
        try {
            Method m = profile.getClass().getMethod("desconectar", BluetoothDevice.class);
            m.invoke(profile, device);
            return true;
        } catch (NoSuchMethodException e) {
            Log.w(TAG, "No hay método de desconexión en el " + profile.getClass().getName() +
                    "clase, ignorando la solicitud.");
            return false;
        } catch (InvocationTargetException | IllegalAccessException e) {
            Log.w(TAG, "No se pudo ejecutar el metodo 'desconectar' en el perfil " +
                    profile.getClass().getName() + ", ignorando la solicitud.", e);
            return false;
        }
    }

}

