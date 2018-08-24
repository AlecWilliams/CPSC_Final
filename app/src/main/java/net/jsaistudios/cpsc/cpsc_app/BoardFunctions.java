package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by ip on 8/18/18.
 */

public class BoardFunctions extends PageSpecificFunctions {
    public String getListFragTag() {
        return "boardPageListFragment";
    }

    public String getListDatabaseKey() {
        return "boardmembers";
    }

    public ItemObject getListItemObject(DataSnapshot child) {
        BoardObject object = new BoardObject();
        object.setName(child.child("name").getValue(String.class));
        object.setInfo(child.child("info").getValue(String.class));
        object.setBio(child.child("bio").getValue(String.class));
        try {
            object.setImage(Integer.valueOf(child.child("image").getValue(String.class)));
        } catch (Exception e) {
        }
        return object;
    }

    public ItemObject getExampleItemObject() {
        return new BoardObject("Board Fireston", "free pizza", R.drawable.fireston_img);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new BoardRecyclerAdapter(context, listViewModel.getModelView().getDataModelList());
    }
}
