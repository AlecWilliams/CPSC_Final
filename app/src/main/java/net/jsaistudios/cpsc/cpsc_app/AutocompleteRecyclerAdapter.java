package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkObject;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteRecyclerAdapter extends RecyclerView.Adapter<AutocompleteRecyclerAdapter.MyViewHolder> {
    List<PerkObject> data;
    public AutocompleteRecyclerAdapter(List<PerkObject> list) {
        data = list;
    }
    @Override
    public AutocompleteRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.autocomplete_item, parent, false);
        return new AutocompleteRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AutocompleteRecyclerAdapter.MyViewHolder holder, int position) {
        holder.nameTextView.setText(data.get(position).getName());
        holder.addressTextView.setText(data.get(position).getAddress());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public View mainView;
        public MyViewHolder(View v) {
            super(v);
            mainView = v;
            nameTextView = v.findViewById(R.id.location_name);
            addressTextView = v.findViewById(R.id.location_address);
        }
    }
}
