package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by ip on 8/18/18.
 */

public class PerkFunctions extends PageSpecificFunctions {
    public String getListFragTag() {
        return "perkPageListFragment";
    }

    public String getListDatabaseKey() {
        return "perks";
    }

    public ItemObject getListItemObject(DataSnapshot child) {
        PerkObject perkObject = new PerkObject();
        perkObject.setName(child.child("name").getValue(String.class));
        perkObject.setInfo(child.child("info").getValue(String.class));
        try {
            perkObject.setImage(Integer.valueOf(child.child("image").getValue(String.class)));
        } catch (Exception e) {
        }
        return perkObject;
    }

    @Override
    RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new PerkRecyclerAdapter(context, listViewModel.getModelView().getDataModelList());
    }

}
