package net.jsaistudios.cpsc.cpsc_app.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerkEditDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout lila1=  new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        lila1.setPadding(30,0,30,0);
        final EditText input = new EditText(getContext());
        input.setHint("Name");
        input.setText(getArguments().getString("name"));
        final EditText input1 = new EditText(getContext());
        input1.setHint("Description");
        input1.setText(getArguments().getString("info"));
        final EditText input2 = new EditText(getContext());
        input2.setHint("Url");
        input2.setText(getArguments().getString("url"));
        lila1.addView(input);
        lila1.addView(input1);
        lila1.addView(input2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Edit perk info")
                .setPositiveButton("Full Send!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference myDatabaseRef = db.getReference("perks").child(getArguments().getString("dbKey"));

                        myDatabaseRef.child("name").setValue(input.getText().toString());
                        myDatabaseRef.child("info").setValue(input1.getText().toString());
                        myDatabaseRef.child("url").setValue(input2.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        builder.setView(lila1);
        return builder.create();
    }
}
