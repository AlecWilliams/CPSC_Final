package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EventCheckinAdapter extends RecyclerView.Adapter<EventCheckinAdapter.PlanetViewHolder> {

    ArrayList<String> data;
    private LayoutInflater mInflater;

    public EventCheckinAdapter(Context context, ArrayList<String> planetList) {
        this.mInflater = LayoutInflater.from(context);
        this.data = planetList;
    }

    @Override
    public EventCheckinAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= mInflater.inflate(R.layout.event_checkin_item,parent,false);
        return new PlanetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventCheckinAdapter.PlanetViewHolder holder, int position) {
        holder.text.setText(data.get(position));
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