package net.jsaistudios.cpsc.cpsc_app.Dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckInEventCreationDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout lila1=  new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        lila1.setPadding(30,0,30,0);
        final EditText input = new EditText(getContext());
        input.setHint("Event Name");
        lila1.addView(input);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add a new check-in event")
                .setPositiveButton("Check In", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        db.getReference("checkin").child(input.getText().toString()).push().setValue(":placeholder:");
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
