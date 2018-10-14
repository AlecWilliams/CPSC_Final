package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListViewController extends ListViewController {
    String fbPageKey = "EAAfROW4QQZCABAEQhXphI8SJqhZAxuwD8Q18CzNiESFQU3MmB01qGhluCzNDWkxZAA48nAshoPHOfu9T4VhnODEJLwLaC3UZAin3IQCoAZBZA2UBAPkIAtoG2vaJMCd09T9faB6zoRvBUmFaTjkIOHp8B84msTtwGA0jZAmBlVqvIIPNDvoPC79xpps8wi41RA3qTyZC9ZCocggZDZD";
    public EventListViewController(PageSpecificFunctions funcs, Context c, ListViewModel lm) {
        listViewModel = lm;
        pageSpecificFunctions = funcs;
        lm.setPageSpecificFunctions(pageSpecificFunctions);
        this.context = c;
        listViewModel.setCreationObserver(new Observer() {
            @Override
            public void update() {
                listViewModel.setRecyclerAdapter(pageSpecificFunctions.getRecyclerAdapter(context, listViewModel));
            }
        });
        getDatabaseList();
    }
    private void getDatabaseList() {
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url ="https://graph.facebook.com/v3.1/382506115285435/events";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response FB", response);
                        try {
                            JSONObject events = new JSONObject(response);
                            JSONArray eList = events.getJSONArray("data");
                            List<RecyclerModel> responseList = new ArrayList<>();
                            for (int i=0; i<eList.length(); i++) {
                                RecyclerModel pm = new RecyclerModel();
                                EventsObject object = new EventsObject();
                                object.setName(eList.getJSONObject(i).getString("name"));
                                object.setInfo(eList.getJSONObject(i).getString("description"));
                                pm.setItemObject(object);
                                responseList.add(pm);
                            }
                            listViewModel.getModelView().getDataModelList().clear();
                            listViewModel.getModelView().getDataModelList().addAll(responseList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + fbPageKey);
                return params;
            }
        };
        queue.add(getRequest);
    }
}
