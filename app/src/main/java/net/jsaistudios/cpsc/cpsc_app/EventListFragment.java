package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventListFragment extends Fragment {
    View baseFragmentView;
    RecyclerView rv;
    private EventListAdapter eventCheckinAdapter;
    ArrayList<String> recyclerList = new ArrayList<>();
    public MainActivity activity;
    int count=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.event_list_frag, container, false);
        rv = baseFragmentView.findViewById(R.id.checkin_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        eventCheckinAdapter = new EventListAdapter(getContext(), activity, recyclerList);
        rv.setAdapter(eventCheckinAdapter);
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference("checkin");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerList.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    recyclerList.add(snap.getKey());
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