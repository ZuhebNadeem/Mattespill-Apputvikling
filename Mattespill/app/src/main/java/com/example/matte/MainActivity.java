package com.example.matte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Tre knappene på hovedsiden, henter de fra XML
    Button spillBtn, preferanserBtn,statistikkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hentLandskode();
        layoutAndView();
        importXML();
        spillValgPreferanser();
        btnListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        spillValgPreferanser();
        knappTekst();
        hentLandskode(); //Bytter språk
    }



    //    METODER



    //KNAPPER OG LISTENER
    private void btnListener(){
        //Hva som skal skje når man trykker på StartSpill - knappen
        spillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                åpneSpill();
            }
        });

        //Hva som skal skje når man trykker på preferanse - knappen
        preferanserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                åpnePreferanser();
            }
        });

        //Hva som skal skje når man trykker på statistikk - knappen
        statistikkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                åpneStatistikk();
            }
        });
    }

    //Metoder for å føre brukerene til forksjellige aktiviteter
    private void åpneSpill() {
        Intent intent = new Intent(MainActivity.this, Spill.class);
        startActivity(intent);
    }

    private void åpnePreferanser() {
        Intent intent = new Intent(MainActivity.this, SetSpillPreferanser.class);
        startActivity(intent);
    }

    private void åpneStatistikk() {
        Intent intent = new Intent(MainActivity.this, Statistikk.class);
        startActivity(intent);
    }




    //LAYOUT
    private void layoutAndView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    //Import XML
    private void importXML(){
        //Tre knappene på hovedsiden, henter de fra XML
        spillBtn = (Button) findViewById(R.id.startBtn);
        preferanserBtn = (Button) findViewById(R.id.preferanseBtn);
        statistikkBtn = (Button) findViewById(R.id.statistikkBtn);
    }


    //knapp - tekst
    protected void spillValgPreferanser() {
        spillBtn.setText(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("ValgtAktivitet", "5")
                + " " + this.getString(R.string.spill));
    }
    protected void knappTekst() {
        preferanserBtn.setText(this.getString(R.string.velg_preferanser));
        statistikkBtn.setText(this.getString(R.string.se_statistikk));
    }


    //SPRÅK
    private void settLand(String landskode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration cf = res.getConfiguration();
        cf.setLocale(new Locale(landskode));
        res.updateConfiguration(cf,dm);
    }

    private void hentLandskode(){
        String landskode = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("ValgtLanguage", "no");
        settLand(landskode);
    }

}