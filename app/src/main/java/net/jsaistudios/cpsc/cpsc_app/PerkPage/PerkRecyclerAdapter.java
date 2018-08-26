package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    final int dur = 500;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    PerkRecyclerAdapter(Context context, List<RecyclerModel> data, PageSpecificFunctions functions, ListViewModel vm, AppCompatActivity activity,
                        MainActivity ma) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.pageSpecificFunctions = functions;
        myActivity = activity;
        mainActivity = ma;
        viewModel = vm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.perk_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PerkRecyclerAdapter.ViewHolder holder, int position) {
        holder.fillViewModel(mData.get(position));
        if(holder.myDatabaseRef==null) {
            holder.enterEditStateNoAnimations();
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
        ImageView myImage, myEditButton;
        ImageView myEditImage;
        TextView myEditSave, myEditCancel;
        View fragRoot;
        ViewHolder me;
        EditText addressEditText, nameEditText, descriptionEditText;


        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_name);
            myLocationInfo = itemView.findViewById(R.id.card_description);
            myDeleteButton = itemView.findViewById(R.id.delete_card);
            myEditButton = itemView.findViewById(R.id.edit_button);
            fragRoot = itemView.getRootView();

            editLayout = itemView.findViewById(R.id.edit_layout_holder);
            defaultLayout = itemView.findViewById(R.id.default_layout);

            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myImage = itemView.findViewById(R.id.location_image);
            myEditImage = itemView.findViewById(R.id.location_image2);

            addressEditText = itemView.findViewById(R.id.edit_perk_address);
            nameEditText = itemView.findViewById(R.id.edit_card_name);
            descriptionEditText = itemView.findViewById(R.id.edit_card_info);

            myEditSave = itemView.findViewById(R.id.save_edit);
            myEditCancel = itemView.findViewById(R.id.cancel_edit);
            me = this;
            myEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterEditState();
                    PerkObject perkObject = (PerkObject)mData.get(getAdapterPosition()).getItemObject();
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
            this.myImage.setImageDrawable(this.itemView.getResources().getDrawable(R.drawable.fireston_img));
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
