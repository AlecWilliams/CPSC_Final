package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsObject;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkObject;

/**
 * Created by ip on 8/18/18.
 */

public abstract class PageSpecificFunctions {
    public abstract String getListFragTag();
    public abstract String getListDatabaseKey();
    public abstract ItemObject getListItemObject(DataSnapshot child);
    public abstract RecyclerView.Adapter  getRecyclerAdapter(Context context, ListViewModel listViewModel);
    public abstract ItemObject getExampleItemObject();
}
