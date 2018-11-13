package net.jsaistudios.cpsc.cpsc_app.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;

import java.util.Calendar;

public class BasicAlertDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString("text"))
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    public static void create(String s, android.app.FragmentManager manager) {
        Bundle args = new Bundle();
        args.putString("text", s);
        BasicAlertDialog eventCreationDialog = new BasicAlertDialog();
        eventCreationDialog.setArguments(args);
        eventCreationDialog.show(manager, "Make Event");
    }
}
