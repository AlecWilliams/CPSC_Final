package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    LinearLayout listCheckIn;
    ArrayList<String> recyclerList = new ArrayList<>();
    int count=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.event_checkin_frag, container, false);
        listCheckIn = baseFragmentView.findViewById(R.id.checkin_rv);
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("checkin");
        recyclerList.add("hi");
        recyclerList.add("he");
        recyclerList.add("hwhji");
        recyclerList.add("iojoi");
        for(String s: recyclerList) {
            createEventCheckin(s);
        }


        myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                            }
                                        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return baseFragmentView;
    }

    private CheckinItemFrag createEventCheckin(String name) {
        count++;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        CheckinItemFrag fragment = new CheckinItemFrag();
        fragment.name = name;
        fragmentTransaction.add(R.id.checkin_rv, fragment, "Check In Item" + count);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

}
