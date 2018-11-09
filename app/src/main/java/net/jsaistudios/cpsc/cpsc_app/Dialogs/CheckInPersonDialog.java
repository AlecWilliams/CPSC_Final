package net.jsaistudios.cpsc.cpsc_app.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.jsaistudios.cpsc.cpsc_app.EventListAdapter;
import net.jsaistudios.cpsc.cpsc_app.EventPersonCheckinAdapteer;

import java.util.ArrayList;

public class CheckInPersonDialog extends DialogFragment {
    AlertDialog.Builder builder;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout lila1=  new LinearLayout(getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        lila1.setPadding(30,0,30,0);
        final String myName = getArguments().getString("personName");
        builder = new AlertDialog.Builder(getActivity());

        RecyclerView rv = new RecyclerView(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> list = getArguments().getStringArrayList("nameList");
        final EventPersonCheckinAdapteer eventListAdapter = new EventPersonCheckinAdapteer(getContext(), list);
        rv.setAdapter(eventListAdapter);
        lila1.addView(rv);
        builder.setMessage("Select an event to check " + myName + " into.")
                .setPositiveButton("Full Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = db.getReference("checkin").child(eventListAdapter.eventChosen).push();
                        myRef.setValue(myName);
                        dialog.dismiss();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        builder.setView(lila1);
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
