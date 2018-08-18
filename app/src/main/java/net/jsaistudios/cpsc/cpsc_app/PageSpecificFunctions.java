package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by ip on 8/18/18.
 */

public abstract class PageSpecificFunctions {
    abstract String getListFragTag();
    abstract String getListDatabaseKey();
    abstract ItemObject getListItemObject(DataSnapshot child);
    abstract RecyclerView.Adapter  getRecyclerAdapter(Context context, ListViewModel listViewModel);
}
