package net.jsaistudios.cpsc.cpsc_app;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Alec on 8/16/2018.
 */

public class ViewModel{
    private RecyclerView recyclerView;

    public PerkModelView getPerkModelView() {
        return perkModelView;
    }

    public void setPerkModelView(PerkModelView perkModelView) {
        this.perkModelView = perkModelView;
    }

    private PerkModelView perkModelView;

    public ViewModel(PerkModelView pmv){
        perkModelView = pmv;
        recyclerView = (RecyclerView) perkModelView.getFragView().findViewById(R.id.message_recycler_view);

        perkModelView.setRecyclerView(recyclerView);
        recyclerView.setAdapter(perkModelView.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(perkModelView.getContext()));

    }
}
