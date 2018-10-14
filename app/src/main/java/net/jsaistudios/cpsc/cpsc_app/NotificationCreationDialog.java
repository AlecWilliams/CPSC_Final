package net.jsaistudios.cpsc.cpsc_app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationCreationDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout lila1=  new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(getContext());
        input.setHint("Title (Optional)");
        final EditText input1 = new EditText(getContext());
        input1.setHint("Body");
        lila1.addView(input);
        lila1.addView(input1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Send Push Notification")
                .setPositiveButton("Full Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference myDatabaseRef = db.getReference("notifications").push();

                        myDatabaseRef.child("name").setValue(input.getText().toString());
                        myDatabaseRef.child("body").setValue(input1.getText().toString());
                        DateFormat dateFormat = new SimpleDateFormat("MMM d 'at' hh:mm a", Locale.US);
                        myDatabaseRef.child("date").setValue(dateFormat.format(Calendar.getInstance().getTime()).replace("AM", "am").replace("PM","pm"));
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
