package com.example.matte.hjelpeklasse;

import android.content.Context;
import android.widget.Toast;

//Bruker den klassen når vi vil vise brukeren noe på skjermen

public class Toaster {

    //DATAFELT
    Context context;
    public Toaster(Context context){
        this.context = context;
    }


    //Metode for kortvarig meldinger til brukeren
    public void makeShortToast( String msg) {
        Toast toast = Toast.makeText(this.context,msg,Toast.LENGTH_SHORT);
        toast.show();
    }

    //Metode for langvarig meldinger til brukeren
    public void makeLongToast(String msg) {
        Toast toast = Toast.makeText(this.context,msg,Toast.LENGTH_LONG);
        toast.show();
    }

}

