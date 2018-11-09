package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventCheckInFragment extends Fragment {
    View baseFragmentView;
    RecyclerView rv;
    private EventCheckinAdapter eventCheckinAdapter;
    TextView clearButton, closeButton;
    ArrayList<String> recyclerList = new ArrayList<>();
    public Activity activity;
    int count=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.event_checkin_frag, container, false);
        rv = baseFragmentView.findViewById(R.id.checkin_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        eventCheckinAdapter = new EventCheckinAdapter(getContext(), recyclerList);
        rv.setAdapter(eventCheckinAdapter);
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference("checkin");
        clearButton = baseFragmentView.findViewById(R.id.clear_button);
        closeButton = baseFragmentView.findViewById(R.id.close_button);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(null);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            recyclerList.clear();
                                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                                recyclerList.add(snap.getValue(String.class));
                                            }
                                            eventCheckinAdapter.notifyDataSetChanged();
                                        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return baseFragmentView;
    }

}
