package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ip on 8/18/18.
 */

public class ListViewController {
    ListViewModel listViewModel;
    Context context;
    private PageSpecificFunctions pageSpecificFunctions;

    public ListViewController(PageSpecificFunctions funcs, Context c, ListViewModel lm) {
        listViewModel = lm;
        this.context = c;
        listViewModel.setCreationObserver(new Observer() {
            @Override
            public void update() {
                listViewModel.setRecyclerAdapter(pageSpecificFunctions.getRecyclerAdapter(context, listViewModel));
            }
        });
        pageSpecificFunctions = funcs;
        listViewModel.getModelView().getDataModelList().clear();
        getDatabaseList();
    }

    private void getDatabaseList() {
        FirebaseApp.initializeApp(context);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(pageSpecificFunctions.getListDatabaseKey());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                List<RecyclerModel> responseList = new ArrayList<>();
                while(iterable.iterator().hasNext()){
                    RecyclerModel pm = new RecyclerModel();
                    DataSnapshot child = iterable.iterator().next();
                    pm.setDatabaseNodeReference(child);

                    pm.setItemObject(pageSpecificFunctions.getListItemObject(child));

                    responseList.add(pm);
                }
                listViewModel.getModelView().getDataModelList().addAll(responseList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private ListViewModel createListFragment(int holder, FragmentManager manager, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        listViewModel = new ListViewModel();
        listViewModel.setCreationObserver(new Observer() {
            @Override
            public void update() {
                listViewModel.setRecyclerAdapter(pageSpecificFunctions.getRecyclerAdapter(context, listViewModel));
            }
        });
        fragmentTransaction.add(holder, listViewModel, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        return listViewModel;
    }

    public ListViewModel getListViewModel() {
        return listViewModel;
    }

    public void setListViewModel(ListViewModel listViewModel) {
        this.listViewModel = listViewModel;
    }
}
