package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.AutocompleteFragment;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.MainActivity;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.R;
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Alec on 5/18/2018.
 */

public class PerkRecyclerAdapter extends RecyclerView.Adapter<PerkRecyclerAdapter.ViewHolder> {
    private PageSpecificFunctions pageSpecificFunctions;
    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    private AppCompatActivity myActivity;
    private MainActivity mainActivity;
    private ListViewModel viewModel;
    private Context context;
    final int dur = 500;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    PerkRecyclerAdapter(Context context, List<RecyclerModel> data, PageSpecificFunctions functions, ListViewModel vm, AppCompatActivity activity,
                        MainActivity ma) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.pageSpecificFunctions = functions;
        myActivity = activity;
        this.context = context;
        mainActivity = ma;
        viewModel = vm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.perk_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PerkRecyclerAdapter.ViewHolder holder, final int position) {
        holder.fillViewModel(mData.get(position));
        if(holder.myDatabaseRef==null) {
            holder.enterEditStateNoAnimations();
        }
        holder.googleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:35.2827,-120.6596?q=" + mData.get(position).getItemObject().getName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        String site = ((PerkObject) mData.get(position).getItemObject()).getUrl();
        if(site==null || site.equals("")) {
            holder.urlButton.setVisibility(View.INVISIBLE);
        } else {
            holder.urlButton.setVisibility(View.VISIBLE);
            holder.urlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String prefix = "https://";
                        String site = ((PerkObject) mData.get(position).getItemObject()).getUrl();
                        if (!site.substring(0, prefix.length()).equals(prefix)) {
                            site = prefix + site;
                        }
                        if (!site.equals("")) {
                            Uri uri = Uri.parse(site); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView;
        TextView myLocationInfo;
        ImageView myDeleteButton;
        DatabaseReference myDatabaseRef;
        LinearLayout editLayout;
        RelativeLayout rLayout, defaultLayout;
        ImageView myEditButton;
        ImageView myEditImage;
        TextView googleLogo, urlButton;
        TextView myEditSave, myEditCancel;
        View fragRoot;
        ViewHolder me;
        EditText addressEditText, nameEditText, descriptionEditText;
        SwipeLayout swipeLayout;


        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_name);
            googleLogo = itemView.findViewById(R.id.googlelogo);
            urlButton = itemView.findViewById(R.id.website);
            myLocationInfo = itemView.findViewById(R.id.card_description);
            myDeleteButton = itemView.findViewById(R.id.delete_card);
            myEditButton = itemView.findViewById(R.id.edit_button);
            fragRoot = itemView.getRootView();

            editLayout = itemView.findViewById(R.id.edit_layout_holder);
            defaultLayout = itemView.findViewById(R.id.default_layout);

            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myEditImage = itemView.findViewById(R.id.location_image2);

            addressEditText = itemView.findViewById(R.id.edit_perk_address);
            nameEditText = itemView.findViewById(R.id.edit_card_name);
            descriptionEditText = itemView.findViewById(R.id.edit_card_info);
            swipeLayout = itemView.findViewById(R.id.swipeToEdit);

            myEditSave = itemView.findViewById(R.id.save_edit);
            myEditCancel = itemView.findViewById(R.id.cancel_edit);
            me = this;


            //If user is not admin, dont allow swipe
            //Check if ifAdmin == true
            swipeLayout.setSwipeEnabled(false);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userID = user.getUid();
                DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("clearance");
                userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.getValue().toString().equals("admin")) {
                                swipeLayout.setSwipeEnabled(true);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            myEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterEditState();
                    final PerkObject perkObject = (PerkObject)mData.get(getAdapterPosition()).getItemObject();
                    nameEditText.setText(perkObject.getName());
                    descriptionEditText.setText(perkObject.getInfo());
                }
            });
            myEditCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myDatabaseRef==null) {
                        int pos=getAdapterPosition();
                        viewModel.getModelView().getDataModelList().remove(pos);
                        notifyDataSetChanged();
                    } else {
                        enterDefaultState();
                    }
                }
            });
            myEditSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myDatabaseRef==null) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = db.getReference(pageSpecificFunctions.getListDatabaseKey());
                        myDatabaseRef = myRef.push();
                    }
                    myDatabaseRef.child("name").setValue(nameEditText.getText().toString());
                    myDatabaseRef.child("info").setValue(descriptionEditText.getText().toString());
                    enterDefaultState();
                }
            });
            myDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(myDatabaseRef != null){
                        myDatabaseRef.removeValue();
                    }
                    int pos=getAdapterPosition();
                    mData.remove(pos);
                }
            });
            nameEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    viewModel.getRecyclerView().setVisibility(View.INVISIBLE);
                    mainActivity.createAutocompleteFrag("autoFrag", nameEditText.getText().toString());
                }
            });
        }

        private void fillViewModel(RecyclerModel model) {
            PerkObject perkObject = (PerkObject) model.getItemObject();
            this.myLocationInfo.setText(perkObject.getInfo());
            this.myTextView.setText(model.getItemObject().getName());
            this.myDatabaseRef = model.getDatabaseNodeReference();
        }

        protected void enterEditStateNoAnimations() {
            myDeleteButton.setVisibility(View.GONE);
            myEditButton.setVisibility(View.GONE);
            defaultLayout.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
            editLayout.setAlpha(0);
            editLayout.animate().alpha(1).setDuration(dur);
        }
        protected void enterEditState() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                myDeleteButton.animate().alpha(0).setDuration(dur);
                myEditButton.animate().alpha(0).setDuration(dur);
                defaultLayout.setAlpha(1);
                defaultLayout.animate().alpha(0).setDuration(dur).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        editLayout.setAlpha(0);
                        editLayout.setVisibility(View.VISIBLE);
                        editLayout.animate().alpha(1).setDuration(dur);
                        defaultLayout.setVisibility(View.GONE);
                    }
                });
            }
        }

        private void enterDefaultState() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                editLayout.animate().alpha(0).setDuration(dur).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        editLayout.setVisibility(View.GONE);
                        defaultLayout.setVisibility(View.VISIBLE);
                        defaultLayout.setAlpha(1);
                        myEditButton.animate().alpha(1).setDuration(dur);
                        myDeleteButton.animate().alpha(1).setDuration(dur);
                    }
                });
            }
        }
    }

}
