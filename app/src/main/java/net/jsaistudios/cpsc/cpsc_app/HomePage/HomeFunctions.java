package net.jsaistudios.cpsc.cpsc_app.HomePage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.ItemObject;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.MainActivity;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.R;

import java.util.Collections;

public class HomeFunctions extends PageSpecificFunctions {
    private AppCompatActivity myActivity;
    private MainActivity mainActivity;
    public HomeFunctions(AppCompatActivity activity, MainActivity ma) {
        myActivity = activity;
        mainActivity = ma;
    }
    public String getListFragTag() {
        return "homeageListFragment";
    }

    public String getListDatabaseKey() {
        return "notifications";
    }

    public ItemObject getListItemObject(DataSnapshot child) {
        HomeObject perkObject = new HomeObject();
        perkObject.setName(child.child("name").getValue(String.class));
        perkObject.setBody(child.child("body").getValue(String.class));
        perkObject.setDate(child.child("date").getValue(String.class));
        return perkObject;
    }

    public ItemObject getAddItemObject() {
        return new HomeObject("", "", "");
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        return new HomeRecyclerAdapter(context, listViewModel.getModelView().getDataModelList(), this, listViewModel, myActivity, mainActivity);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
