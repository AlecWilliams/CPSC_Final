package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class EventPersonCheckinAdapteer extends RecyclerView.Adapter<EventPersonCheckinAdapteer.PlanetViewHolder> {

    ArrayList<String> data;
    private LayoutInflater mInflater;
    public String eventChosen;

    public EventPersonCheckinAdapteer(Context context, ArrayList<String> planetList) {
        this.mInflater = LayoutInflater.from(context);
        this.data = planetList;
    }

    @Override
    public EventPersonCheckinAdapteer.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= mInflater.inflate(R.layout.event_checkin_person_item,parent,false);
        return new PlanetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventPersonCheckinAdapteer.PlanetViewHolder holder, final int position) {
        holder.text.setText(data.get(position));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    eventChosen = holder.text.getText().toString();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder{

        protected RadioButton text;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.radiobutton);
        }
    }
}