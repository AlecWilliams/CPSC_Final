package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 8/16/2018.
 */

public class PerkViewController extends Fragment {
    private PerkModelView perkModelView;
    private View frag;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;
    FirebaseDatabase db;
    List<RecyclerPerkModel> recyclerPerkModelList = new ArrayList<>();
    public enum PageType {
        PERK, TRIPS, ANOUNCEMENTS, BOARD;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        perkModelView = new PerkModelView(context);
        perkModelView.setFragView(inflater.inflate(R.layout.list_page, container, false));
        perkModelView.setContext(context);

        ViewModel vm = new ViewModel(perkModelView);


        frag = perkModelView.getFragView();

       // final MessageRecyclerViewAdapter adapter = new MessageRecyclerViewAdapter(context,recyclerPerkModelList);


        //perkModelView.getRecyclerView().setAdapter(adapter);

        DatabaseReference myRef;

        FirebaseApp.initializeApp(context);

        //recyclerPerkModelList.add(new RecyclerPerkModel());
        RecyclerPerkModel pm = new RecyclerPerkModel();

        pm.setName("Testame");
        pm.setInfo("Fake info");
        pm.setImage(R.drawable.fireston_img);
        perkModelView.getRecyclerPerkModelList().add(pm);

        perkModelView.getAdapter().notifyDataSetChanged();

       /** db = FirebaseDatabase.getInstance();
        myRef = db.getReference("perks");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);


                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                recyclerPerkModelList.clear();
                int i=0;
                while(iterable.iterator().hasNext()){
                    RecyclerPerkModel pm = new RecyclerPerkModel();
                    DataSnapshot child = iterable.iterator().next();
                    pm.setPerkDatabaseNode(child);
                    pm.setName(child.child("name").getValue(String.class));
                    pm.setInfo(child.child("info").getValue(String.class));
                    if(i==0){
                        pm.setImage(R.drawable.fireston_img);
                    } else if(i==1){
                        pm.setImage(R.drawable.bulls);
                    } else if(i==2) {
                        pm.setImage(R.drawable.fireston_img);
                    } else if(i==4) {
                        pm.setImage(R.drawable.slobrew);
                    } else {
                        pm.setImage(R.drawable.fireston_img);
                    }
                    recyclerPerkModelList.add(pm);
                    i++;

                }

                //Notes=================================
                //Make fragment, put in frame layout.
                //Edit button - remove / add
                //input field
                //Add navigation to location
                //


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
**/
        return frag;
    }

/**
    public void setupPerk(){
        //Add Button Stuff
        final ImageView editButtom = findViewById(R.id.edit_button);
        editButtom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                //Fragment stuff


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                PerkAddFragment enterNewPerkFragment = new PerkAddFragment();
                fragmentTransaction.add(R.id.main_frame, enterNewPerkFragment);
                fragmentTransaction.commit();



            }
        });

    }
 **/
}
