package com.example.warmhome;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.SeekBar;

public class PreferenciasFragment extends PreferenceFragment {


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

    }
}
