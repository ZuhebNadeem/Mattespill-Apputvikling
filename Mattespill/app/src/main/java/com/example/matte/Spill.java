package com.example.matte;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.matte.hjelpeklasse.Dialog;
import com.example.matte.hjelpeklasse.Toaster;

import java.util.ArrayList;
import java.util.Locale;


public class Spill extends AppCompatActivity implements Dialog.DialogClickListener {

    //DATAFELT
    final int TOTAL_ANTALL = 25;  // 25 spørsmål
    final int ANIMATION_TIME = 1000; // 1 Sekund
    int antall; //Oppdateres med preferanser
    int antSvarteSpm, poeng, feil = 0;
    ArrayList<String> regnestykkerUnik= new ArrayList<>();
    ArrayList<Integer> fasitUnik = new ArrayList<>();
    ArrayList<Integer> brukteRegnestykker = new ArrayList<>();
    String[] regnestykker;
    int[] fasit;

    //Hjelpe - Klasser
    final Toaster toast = new Toaster(Spill.this);
    final DialogFragment dialog = new Dialog();

    //XML
    TextView svarTxt, spmTxt, antallRiktigTxt, antallFeilTxt, rundeFerdigTxt;
    Button svarBtn, knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6,
            knapp7, knapp8, knapp9, slettBtn, spillIgjenBtn,hjemBtn;
    ImageView riktigAnimasjon,feilAnimasjon;
    LinearLayout linearLayout, rundeFerdigLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hentLandskode();
        layoutAndView();
        importXML();
        prefKontroll();
        listenerKnapp();

        //Henter oppgaver fra Arrays.xml
        regnestykker = getResources().getStringArray(R.array.regneStykker);
        fasit = getResources().getIntArray(R.array.fasit);

        restoreInstance(savedInstanceState);     //Tilstandsbevaring

        //KontrollSjekk
        if(!(antSvarteSpm >= antall)){
            gjeldendeSpm();
        }else{
            rundeFerdig();
        }

    }



    //      METODER


    //SPILL LOGIKK



    // Unike spørsmål uten duplikat
    private void randomSpill(){
        int i = 0;
        while(i < antall){
            int random = (int) (Math.random()*TOTAL_ANTALL);
            if(!brukteRegnestykker.contains(random)){
                brukteRegnestykker.add(random);
                regnestykkerUnik.add(regnestykker[random]);
                fasitUnik.add(fasit[random]);
                i++;
            }
        }
    }

    private void nesteSpm() {
        svarTxt.setText("");
        antSvarteSpm++;

        //NÅR DU ER FERDIG
        if (antSvarteSpm == antall) {
            rundeFerdig();
        } else {
            gjeldendeSpm();
        }
    }

    private void gjeldendeSpm() {
        spmTxt.setText(regnestykkerUnik.get(antSvarteSpm) + " =");

    }

    //Gameover
    private void rundeFerdig() {

        linearLayout.setVisibility(View.GONE);
        rundeFerdigLayout.setVisibility(View.VISIBLE);
        rundeFerdigTxt.setVisibility(View.VISIBLE);
        svarBtn.setEnabled(false);
        svarTxt.setHint("");
        hjemBtn.setVisibility(View.VISIBLE);
        statistik();

        if((brukteRegnestykker.size()+antall) >= TOTAL_ANTALL){
            spillIgjenBtn.setVisibility(View.GONE);
            rundeFerdigTxt.setText(R.string.runde_avslutt);

        }else {
            rundeFerdigTxt.setText(R.string.runde_spill_avslutt);
            spillIgjenBtn.setVisibility(View.VISIBLE);
        }
    }

    //Oppdater Statistikk
    private void statistik(){
        SharedPreferences sharedPreference = getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        int totalPoeng;
        int totalFeil;

        totalPoeng = sharedPreference.getInt("totalPoeng", 0);
        totalFeil = sharedPreference.getInt("totalFeil", 0);

        totalPoeng += poeng;
        totalFeil += feil;

        //Lager totalPoeng
        sharedPreference
                .edit()
                .putInt("totalPoeng", totalPoeng)
                .apply();

        //Lager TotalFeil
        sharedPreference
                .edit()
                .putInt("totalFeil", totalFeil)
                .apply();
    }


    //Henter antall valgte spill fra preferanser - dersom ikke valgt, så 5 spm
    private void prefKontroll() {
        //Antall valgte spm fra preferanser
        SharedPreferences sharedPreference = getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        String prefValg = sharedPreference.getString("ValgtAktivitet","5");
        antall = Integer.parseInt(prefValg);

    }

    //Avslutt spill - når runden er ferdig
    private void hjem() {
        finish();
    }

    //Spill igjen - når runden er ferdig
    private void spillIgjen() {
        //Henter nye spørsmål
        randomSpill();

        //nullstiller score
        antSvarteSpm = 0;
        poeng = 0;
        feil = 0;

        //Skriver ut spørsmål til skjerm
        gjeldendeSpm();


        //Gjør klar GUI
        linearLayout.setVisibility(View.VISIBLE);
        svarBtn.setEnabled(true);
        svarTxt.setEnabled(true);
        svarTxt.setHint(R.string.skriv_svaret_her);
        antallRiktigTxt.setText("0");
        antallFeilTxt.setText("0");

        //Skjuler knapper
        rundeFerdigLayout.setVisibility(View.GONE);
        rundeFerdigTxt.setVisibility(View.GONE);
        spillIgjenBtn.setVisibility(View.GONE);
        hjemBtn.setVisibility(View.GONE);

    }


    //ANIMATION
    private void animation(ImageView image){
        final ImageView tmpImage = image;
        tmpImage.setVisibility(View.VISIBLE);
        final Drawable d = linearLayout.getBackground();
        linearLayout.setBackground(null);

        tmpImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                tmpImage.setVisibility(View.GONE);
                linearLayout.setBackground(d);
            }
        }, ANIMATION_TIME);  //ET SEKUND
    }

    //LISTENER
    public void listenerKnapp() {
        knapp1.setOnClickListener(new Clicked());
        knapp2.setOnClickListener(new Clicked());
        knapp3.setOnClickListener(new Clicked());
        knapp4.setOnClickListener(new Clicked());
        knapp5.setOnClickListener(new Clicked());
        knapp6.setOnClickListener(new Clicked());
        knapp7.setOnClickListener(new Clicked());
        knapp8.setOnClickListener(new Clicked());
        knapp9.setOnClickListener(new Clicked());
        knapp0.setOnClickListener(new Clicked());
        slettBtn.setOnClickListener(new Clicked());
        svarBtn.setOnClickListener(new Clicked());
        spillIgjenBtn.setOnClickListener(new Clicked());
        hjemBtn.setOnClickListener(new Clicked());
    }

    //Keylistener - Tastatur
    public class Clicked implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.number_1:
                    svarTxt.append("1");
                    break;
                case R.id.number_2:
                    svarTxt.append("2");
                    break;
                case R.id.number_3:
                    svarTxt.append("3");
                    break;
                case R.id.number_4:
                    svarTxt.append("4");
                    break;
                case R.id.number_5:
                    svarTxt.append("5");
                    break;
                case R.id.number_6:
                    svarTxt.append("6");
                    break;
                case R.id.number_7:
                    svarTxt.append("7");
                    break;
                case R.id.number_8:
                    svarTxt.append("8");
                    break;
                case R.id.number_9:
                    svarTxt.append("9");
                    break;
                case R.id.number_0:
                    svarTxt.append("0");
                    break;
                case R.id.backspace:
                    svarTxt.setText("");
                    break;
                case R.id.spilligjenBtn:
                    spillIgjen();
                    break;
                case R.id.hjemBtn:
                    hjem();
                    break;
                case R.id.svarBtn:
                    if (svarTxt.getText().toString().matches("")) {
                        toast.makeShortToast("Fyll inn svaret!");
                    } else if (svarTxt.getText().toString().matches(String.valueOf(fasitUnik.get(antSvarteSpm)))) {
                        animation(riktigAnimasjon);
                        antallRiktigTxt.setText(String.valueOf(++poeng));
                        nesteSpm();
                    } else {
                        animation(feilAnimasjon);
                        antallFeilTxt.setText(String.valueOf(++feil));
                        nesteSpm();
                    }
                    break;
            }
        }
    }

    //Tilbake knapp - on click - dialog
    @Override
    public void onBackPressed() {
        dialog.show(getSupportFragmentManager(),"Dialogknappene");
    }

    //DIALOG KLASSENS INTERFACE
    @Override
    public void onPositiveClick() {
        finish();
    }
    @Override
    public void onNegativeClick() {
        return;
    }



    //LAYOUT
    //Nav - Layout
    private void layoutAndView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spill);

    }

    //Import xml
    private void importXML(){
        //Import from xml
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        rundeFerdigLayout = (LinearLayout) findViewById(R.id.rundeFerdigLayout);
        rundeFerdigTxt = (TextView) findViewById(R.id.rundeFerdig);
        spmTxt = (TextView) findViewById(R.id.spmTxt);
        svarTxt = (TextView) findViewById(R.id.svarTxt);
        svarBtn = (Button) findViewById(R.id.svarBtn);
        antallRiktigTxt = (TextView) findViewById(R.id.antallRiktig);
        antallFeilTxt = (TextView) findViewById(R.id.antallFeil);
        knapp0 = (Button) findViewById(R.id.number_0);
        knapp1 = (Button) findViewById(R.id.number_1);
        knapp2 = (Button) findViewById(R.id.number_2);
        knapp3 = (Button) findViewById(R.id.number_3);
        knapp4 = (Button) findViewById(R.id.number_4);
        knapp5 = (Button) findViewById(R.id.number_5);
        knapp6 = (Button) findViewById(R.id.number_6);
        knapp7 = (Button) findViewById(R.id.number_7);
        knapp8 = (Button) findViewById(R.id.number_8);
        knapp9 = (Button) findViewById(R.id.number_9);
        slettBtn = (Button) findViewById(R.id.backspace);
        spillIgjenBtn = (Button) findViewById(R.id.spilligjenBtn);
        hjemBtn = (Button) findViewById(R.id.hjemBtn);
        riktigAnimasjon = (ImageView) findViewById(R.id.riktigAnimasjon);
        feilAnimasjon = (ImageView) findViewById(R.id.feilAnimasjon);
        svarTxt.setInputType(InputType.TYPE_NULL); //default keyboard - off


    }



    //TILSTANDSBEVARING
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("ant_svarte_spm",antSvarteSpm);
        outState.putStringArrayList("regnestykker_unik", regnestykkerUnik);
        outState.putIntegerArrayList("fasit_unik",fasitUnik);
        outState.putInt("poeng", poeng);
        outState.putInt("feil", feil);
        super.onSaveInstanceState(outState);
    }

    private void restoreInstance(Bundle savedInstanceState){
        //Tilstandsbevaring
        if(savedInstanceState != null){
            antSvarteSpm = savedInstanceState.getInt("ant_svarte_spm");
            regnestykkerUnik = savedInstanceState.getStringArrayList("regnestykker_unik");
            fasitUnik = savedInstanceState.getIntegerArrayList("fasit_unik");
            poeng = savedInstanceState.getInt("poeng");
            feil = savedInstanceState.getInt("feil");
            antallRiktigTxt.setText(String.valueOf(poeng));
            antallFeilTxt.setText(String.valueOf(feil));
        }else{
            randomSpill();
        }

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

