package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alec on 8/16/2018.
 */

public class ListViewModel extends android.support.v4.app.Fragment {
    private View baseFragmentView;
    private RecyclerView recyclerView;
    private ListModelView modelView = new ListModelView();
    private Observer creationObserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getContext();
        baseFragmentView = inflater.inflate(R.layout.list_page, container, false);
        recyclerView = baseFragmentView.findViewById(R.id.message_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //Here ListViewController will set RecyclerViewAdapter
        if(creationObserver!=null) {
            creationObserver.update();
        }

        setupDataList();

        return baseFragmentView;
    }
    void setupDataList() {
        modelView.getDataModelList().setListObserver(new ListeningList.ListObserver() {
            @Override
            public void notifyItemChanged(int i) {
                getRecyclerAdapter().notifyItemChanged(i);
            }

            @Override
            public void notifyDataSetChanged() {
                getRecyclerAdapter().notifyDataSetChanged();
            }
        });
    }

    public ListModelView getModelView() {
        return modelView;
    }

    public void setModelView(ListModelView modelView) {
        this.modelView = modelView;
    }

    public RecyclerView.Adapter getRecyclerAdapter() {
        return recyclerView.getAdapter();
    }

    public void setRecyclerAdapter(RecyclerView.Adapter recyclerAdapter) {
        recyclerView.setAdapter(recyclerAdapter);
    }

    public Observer getCreationObserver() {
        return creationObserver;
    }

    public void setCreationObserver(Observer creationObserver) {
        this.creationObserver = creationObserver;
    }
}
