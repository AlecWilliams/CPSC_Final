package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkObject;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkRecyclerAdapter;
import net.jsaistudios.cpsc.cpsc_app.R;

/**
 * Created by ip on 8/18/18.
 */

public class EventsFunctions extends PageSpecificFunctions {
    public String getListFragTag() {
        return "eventsPageListFragment";
    }

    public String getListDatabaseKey() {
        return "events";
    }

    public ItemObject getListItemObject(DataSnapshot child) {
        EventsObject object = new EventsObject();
        object.setName(child.child("name").getValue(String.class));
        object.setInfo(child.child("info").getValue(String.class));
        try {
            object.setImage(Integer.valueOf(child.child("image").getValue(String.class)));
        } catch (Exception e) {
        }
        return object;
    }

    public ItemObject getExampleItemObject() {
        return new EventsObject("Events Fireston", "free pizza", R.drawable.fireston_img);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new EventsRecyclerAdapter(context, listViewModel.getModelView().getDataModelList());
    }

}
