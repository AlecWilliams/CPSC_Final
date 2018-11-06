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
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
        object.setPlace(child.child("place").getValue(String.class));
        object.setDate(child.child("date").getValue(String.class));
        object.setId(child.child("fbId").getValue(String.class));
        return object;
    }

    public ItemObject getAddItemObject() {
        return new EventsObject("Events Fireston", "free pizza", R.drawable.fireston_img);
    }

    public Date makeDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter(Context context, ListViewModel listViewModel) {
        List mData = listViewModel.getModelView().getDataModelList();
        Comparator stringDateComparator = new Comparator<RecyclerModel>() {
            @Override
            public int compare(RecyclerModel recyclerModel, RecyclerModel t1) {
                return makeDate(((EventsObject)recyclerModel.getItemObject()).getDate()).compareTo(makeDate(((EventsObject)t1.getItemObject()).getDate()));
            }
        };
        Collections.sort(mData, stringDateComparator);
        for(int i =0; i< listViewModel.getModelView().getDataModelList().size(); i++) {
            if(makeDate(((EventsObject)listViewModel.getModelView().getDataModelList().get(i).getItemObject()).getDate()).getTime() - new Date().getTime()>0) {
                listViewModel.scrollTo=i;
                listViewModel.scrollRecycler(i);
            } else {
                ((EventsObject) listViewModel.getModelView().getDataModelList().get(i).getItemObject()).setOld(true);
            }
        }
        return new EventsRecyclerAdapter(context, mData);
    }

}
