package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.BoardObject;
import net.jsaistudios.cpsc.cpsc_app.ItemObject;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.MainActivity;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.R;
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.util.Comparator;

/**
 * Created by ip on 8/18/18.
 */

public class PerkFunctions extends PageSpecificFunctions {
    private AppCompatActivity myActivity;
    private MainActivity mainActivity;
    public PerkFunctions(AppCompatActivity activity, MainActivity ma) {
        myActivity = activity;
        mainActivity = ma;
    }
    public String getListFragTag() {
        return "perkPageListFragment";
    }

    public String getListDatabaseKey() {
        return "perks";
    }

    public ItemObject getListItemObject(DataSnapshot child) {
        PerkObject perkObject = new PerkObject();
        try {
            perkObject.setName(child.child("name").getValue(String.class));
            perkObject.setInfo(child.child("info").getValue(String.class));
            perkObject.setUrl(child.child("url").getValue(String.class));
            Log.d("hioshioj", perkObject.getUrl());
            perkObject.setImage(Integer.valueOf(child.child("image").getValue(String.class)));
        } catch (Exception e) {
        }
        return perkObject;
    }

    public ItemObject getAddItemObject() {
        return new PerkObject("", "", R.drawable.fireston_img);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new PerkRecyclerAdapter(context, listViewModel.getModelView().getDataModelList(), this, listViewModel, myActivity, mainActivity);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public void sortList(ListViewModel listViewModel) {
        listViewModel.getModelView().getSortedDataModeLList(new Comparator<RecyclerModel>() {
            @Override
            public int compare(RecyclerModel recyclerModel, RecyclerModel t1) {
                try {
                    PerkObject object = (PerkObject) recyclerModel.getItemObject();
                    PerkObject object2 = (PerkObject) t1.getItemObject();
                    return object.getName().compareTo(object2.getName());
                } catch (Exception e) {

                }
                return 0;
            }
        });
    }
}
