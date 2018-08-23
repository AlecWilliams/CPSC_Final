package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.BoardObject;
import net.jsaistudios.cpsc.cpsc_app.ItemObject;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.R;

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

    public ItemObject getExampleItemObject() {
        return new PerkObject("Perks Fireston", "free pizza", R.drawable.fireston_img);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new PerkRecyclerAdapter(context, listViewModel.getModelView().getDataModelList());
    }

}
