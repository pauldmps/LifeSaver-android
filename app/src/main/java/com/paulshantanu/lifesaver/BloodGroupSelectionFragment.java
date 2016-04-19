package com.paulshantanu.lifesaver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Shantanu Paul on 19-04-2016.
 */
public class BloodGroupSelectionFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String bloodGroups[] = {"A+","B+","AB+","O+","A-","B-","AB-","O-"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select your blood group")
                .setItems(bloodGroups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
