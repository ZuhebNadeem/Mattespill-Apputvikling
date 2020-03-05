package com.example.matte;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;


public class SetSpillPreferanser extends Activity  {

    //DATAFELT
    SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hentLandskode();
        prefListener();

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }



    //PREFERANSER
    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferanser);
        }
    }

    //METODER

    //Antall spill
    protected void preferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String valgt = sharedPreferences.getString("antallSpill","");
        getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .edit().putString("ValgtAktivitet",valgt).apply();


    }

    //Språk
    protected void language(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String valgt = sharedPreferences.getString("language","");
        getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .edit().putString("ValgtLanguage",valgt).apply();

    }

    //LISTENER
    private void prefListener(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("language")){
                    language();
                    recreate();
                }
                if (s.equals("antallSpill")){
                    preferences();

                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);
    }


    //SPRÅK
    public void settLand(String landskode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration cf = res.getConfiguration();
        cf.setLocale(new Locale(landskode));
        res.updateConfiguration(cf,dm);
    }

    public void hentLandskode(){
        String landskode = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("ValgtLanguage", "no");
        settLand(landskode);
    }


}