package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.PlanetViewHolder> {

    ArrayList<String> data;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    public String eventChosen;

    public EventListAdapter(Context context, MainActivity activity, ArrayList<String> planetList) {
        this.mInflater = LayoutInflater.from(context);
        mainActivity = activity;
        this.data = planetList;
    }

    @Override
    public EventListAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v=mInflater.inflate(R.layout.event_checkin_item,parent,false);

        return new PlanetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventListAdapter.PlanetViewHolder holder, final int position) {
        holder.text.setText(data.get(position));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEventCheckin(holder.text.getText().toString());
            }
        });
    }
    private EventCheckInFragment createEventCheckin(String event) {
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        EventCheckInFragment fragment = new EventCheckInFragment();
        fragment.activity = mainActivity;
        Bundle b = new Bundle();
        b.putString("event", event);
        fragment.setArguments(b);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.event_check_in_holder, fragment, "EventCheckin Frag");
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.textView);
        }
    }
}