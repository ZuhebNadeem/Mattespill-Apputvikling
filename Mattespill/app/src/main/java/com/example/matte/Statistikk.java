package com.example.matte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.matte.hjelpeklasse.Dialog;

import java.util.Locale;

public class Statistikk extends AppCompatActivity implements Dialog.DialogClickListener  {

    //DATAFELT
    private TextView riktigStatTxt, feilStatTxt;
    private Button slettStatBtn;
    final DialogFragment dialog = new Dialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hentLandskode();
        layoutAndView();
        importXML();
        oppdaterStat();
    }



    //METODE

    private void oppdaterStat(){
        SharedPreferences sharedPreference = getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        int totalPoeng = sharedPreference.getInt("totalPoeng", 0);
        int totalFeil = sharedPreference.getInt("totalFeil", 0);

        riktigStatTxt.setText(String.valueOf(totalPoeng));
        feilStatTxt.setText(String.valueOf(totalFeil));
    }


    //DIALOG
    private void dialogClick(){
        dialog.show(getSupportFragmentManager(),"Dialogknappene");
    }

    @Override
    public void onPositiveClick() {
        SharedPreferences sharedPreference = getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        //Nullstiller totalPoeng
        sharedPreference
                .edit()
                .putInt("totalPoeng", 0)
                .apply();

        //Nullstiller TotalFeil
        sharedPreference
                .edit()
                .putInt("totalFeil", 0)
                .apply();

        riktigStatTxt.setText("0");
        feilStatTxt.setText("0");
    }

    @Override
    public void onNegativeClick() {
        return;
    }



    //LAYOUT
    private void layoutAndView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistikk);

    }

    private void importXML(){
        riktigStatTxt = (TextView) findViewById(R.id.riktigStatTxt);
        feilStatTxt = (TextView) findViewById(R.id.feilStatTxt);
        slettStatBtn = (Button) findViewById(R.id.slettStatBtn);

        //Listener til tøm statistikk
        slettStatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick();
            }
        });
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
