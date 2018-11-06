package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Alec on 8/16/2018.
 */

public class ListViewModel extends android.support.v4.app.Fragment {
    private View baseFragmentView;
    private PageSpecificFunctions pageSpecificFunctions;
    private RecyclerView recyclerView;
    private ListModelView modelView = new ListModelView();
    private Observer creationObserver;
    public int scrollTo = -1;

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

        if(scrollTo!=-1)
            scrollRecycler(scrollTo);

        return baseFragmentView;
    }

    public void scrollRecycler(int pos) {
        if(recyclerView!=null) {
            recyclerView.scrollToPosition(pos);
            scrollTo = -1;
        }
    }
    void setupDataList() {
        final ListeningList.ListObserver listWatcher = new ListeningList.ListObserver() {
            @Override
            public void notifyItemChanged(int i) {
                getRecyclerAdapter().notifyDataSetChanged();
            }

            @Override
            public void notifyDataSetChanged() {
                getRecyclerAdapter().notifyDataSetChanged();
            }
        };
        modelView.listObserver = new Observer2<ListeningList<RecyclerModel>>() {
            @Override
            public void update(ListeningList<RecyclerModel> inp) {
                inp.setListObserver(listWatcher);
            }
        };
        modelView.getDataModelList().setListObserver(listWatcher);
    }

    public void addCard() {
        RecyclerModel pm = new RecyclerModel();
        pm.setItemObject(getPageSpecificFunctions().getAddItemObject());
        getModelView().getDataModelList().add(pm);
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

    public PageSpecificFunctions getPageSpecificFunctions() {
        return pageSpecificFunctions;
    }

    public void setPageSpecificFunctions(PageSpecificFunctions pageSpecificFunctions) {
        this.pageSpecificFunctions = pageSpecificFunctions;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
