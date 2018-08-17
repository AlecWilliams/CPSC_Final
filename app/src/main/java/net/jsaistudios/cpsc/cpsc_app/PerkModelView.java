package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/16/2018.
 */

public class PerkModelView {

    private RecyclerView recyclerView;
    public View fragView;
    private int fragID;

    public MessageRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MessageRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    private MessageRecyclerViewAdapter adapter;

    public List<RecyclerPerkModel> getRecyclerPerkModelList() {
        return recyclerPerkModelList;
    }

    public void setRecyclerPerkModelList(List<RecyclerPerkModel> recyclerPerkModelList) {
        this.recyclerPerkModelList = recyclerPerkModelList;
    }

    private List<RecyclerPerkModel> recyclerPerkModelList = new ArrayList<>();


    public PerkModelView(Context c){
        context = c;
        adapter = new MessageRecyclerViewAdapter(context,recyclerPerkModelList);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context;
    ArrayList<String> data = new ArrayList<>();
    MessageRecyclerViewAdapter  messageRecyclerViewAdapter;

    public int getFragID() {
        return fragID;
    }

    public void setFragID(int fragID) {
        this.fragID = fragID;
    }

    public View getFragView() {
        return fragView;
    }

    public void setFragView(View fragView) {
        this.fragView = fragView;
    }


    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


}
