package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutocompleteFragment extends Fragment {
    View baseFragmentView;
    RecyclerView recyclerView;
    ArrayList<PerkObject> data = new ArrayList<>();
    EditText editText;
    String initialText = "";
    String clientId = "M5KFR5KHPA0VCRX1O4NDFJKZ3WACOK2JHA211FOG1YHZAGXZ", clientSecret="BYGXAZKZCSA0QLRJQZG5BFJFWZ3PQSO2MAXGWUMKFBSF2QX0";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.autocomplete_fragment_view, container, false);
        recyclerView = baseFragmentView.findViewById(R.id.autocomplete_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AutocompleteRecyclerAdapter autocompleteRecyclerAdapter = new AutocompleteRecyclerAdapter(data);
        recyclerView.setAdapter(autocompleteRecyclerAdapter);
        editText = baseFragmentView.findViewById(R.id.editText);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(""))
                    getSuggesList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setText(initialText);
        return baseFragmentView;
    }

    public void setInitialText(String text) {
        initialText = text;
    }

    private void getSuggesList(String query) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringBuilder url =new StringBuilder("https://api.foursquare.com/v2/venues/suggestcompletion?");
        url.append("client_id=");
        url.append(clientId);
        url.append("&client_secret=");
        url.append(clientSecret);
        url.append("&v=20180323");
        url.append("&limit=7");
        url.append("&ll=35.2826,-120.6600&query=");
        url.append(query);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resp = new JSONObject(response);
                            JSONArray venues = resp.getJSONObject("response").getJSONArray("minivenues");
                            for(int i=0; i<venues.length(); i++) {
                                try {
                                    PerkObject po = new PerkObject(venues.getJSONObject(i).getString("name"), venues.getJSONObject(i).getJSONObject("location").getString("address"));
                                    data.add(po);
                                } catch (Exception e) {

                                }
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.d("ee", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ee", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
