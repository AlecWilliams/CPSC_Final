package net.jsaistudios.cpsc.cpsc_app.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventCreationDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentActivity myContext;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout lila1=  new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(getContext());
        input.setHint("Name");

        lila1.addView(input);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add Event")
                .setPositiveButton("Full Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference myDatabaseRef = db.getReference("notifications").push();

                        myDatabaseRef.child("name").setValue(input.getText().toString());
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
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
