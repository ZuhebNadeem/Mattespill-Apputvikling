package com.example.matte.hjelpeklasse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import com.example.matte.R;


public class Dialog extends DialogFragment {

    //DATAFELT
    private DialogClickListener dialog;

    //INTERFACE
    public interface DialogClickListener {
        public void onPositiveClick();
        public void onNegativeClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            dialog = (DialogClickListener) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Interfacet er ikke implementert i klassen som kalles");
        }
    }


    //Også lager vi kode for å lage selve dialogboksen når tilbakeknappen i spill trykkes
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.dialogtekst).setPositiveButton
                (R.string.ja, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int whichButton) {
                                dialog.onPositiveClick();
                            }
                        } //Lager avbryt knappen og sier hva som skal skje når den blir trykket på. I dette tilfellet kjører vi en metode
                ).setNegativeButton(R.string.nei, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.onNegativeClick();
                    }
                }
        ).create();
    }

}