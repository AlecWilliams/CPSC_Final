package net.jsaistudios.cpsc.cpsc_app.PerkPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import net.jsaistudios.cpsc.cpsc_app.R;
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.util.List;

/**
 * Created by Alec on 5/18/2018.
 */

public class PerkRecyclerAdapter extends RecyclerView.Adapter<PerkRecyclerAdapter.ViewHolder> {

    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    private ImageButton deleteButton;

    PerkRecyclerAdapter(Context context, List<RecyclerModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_fragment_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PerkRecyclerAdapter.ViewHolder holder, int position) {
        String animal = mData.get(position).getItemObject().getName();

        PerkObject perkObject = (PerkObject) mData.get(position).getItemObject();
        holder.myLocationInfo.setText(perkObject.getInfo());
        holder.myTextView.setText(animal);
        holder.myDatabaseRef = mData.get(position).getDatabaseNodeReference();
        holder.myImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.fireston_img));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView;
        TextView myLocationInfo;
        ImageView myDeleteButton;
        DataSnapshot myDatabaseRef;
        EditText editName, editInfo;
        LinearLayout editLayout;
        RelativeLayout rLayout;
        ImageView myImage, myEditButton;
        ImageView myEditImage;
        TextView myEditSave, myEditCancel;
        View fragRoot;


        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_name);
            myLocationInfo = itemView.findViewById(R.id.card_description);
            myDeleteButton = itemView.findViewById(R.id.delete_card);
            myEditButton = itemView.findViewById(R.id.edit_button);
            fragRoot = itemView.getRootView();

            editName = itemView.findViewById(R.id.edit_card_name);
            editInfo = itemView.findViewById(R.id.edit_card_info);
            editLayout = itemView.findViewById(R.id.edit_layout_holder);

            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myImage = itemView.findViewById(R.id.location_image);
            myEditImage = itemView.findViewById(R.id.location_image2);

            myEditSave = itemView.findViewById(R.id.save_edit);
            myEditCancel = itemView.findViewById(R.id.cancel_edit);

            myEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int dur = 500;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        rLayout.animate().alpha(0).setDuration(dur).withEndAction(new Runnable() {
                            @SuppressLint("NewApi")
                            @Override
                            public void run() {
                                myImage.setVisibility(View.GONE);
                                editLayout.setAlpha(0);
                                myEditImage.setAlpha(0);
                                editLayout.animate().alpha(1).setDuration(dur);
                                editLayout.setVisibility(View.VISIBLE);
                                myEditImage.setVisibility(View.VISIBLE);
                                myEditImage.animate().alpha(1).setDuration(dur);
                                rLayout.setVisibility(View.GONE);

                            }
                        });
                        myImage.animate().alpha(0).setDuration(dur);
                        editName.setText(myTextView.getText());
                        editInfo.setText(myLocationInfo.getText());
                        myEditButton.setVisibility(View.GONE);
                        fragRoot.animate().scaleY(1);
                        rLayout.setVisibility(View.VISIBLE);
                        editLayout.setVisibility(View.VISIBLE);
                        editLayout.setVisibility(View.GONE);
                    }
                }
            });
            myEditCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editLayout.setVisibility(View.GONE);
                    myImage.setVisibility(View.VISIBLE);
                    rLayout.setVisibility(View.VISIBLE);
                    myImage.setAlpha(1);
                    rLayout.setAlpha(1);
                    myEditButton.setVisibility(View.VISIBLE);
                }
            });
            myEditSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDatabaseRef.getRef().child("name").setValue(editName.getText().toString());
                    myDatabaseRef.getRef().child("info").setValue(editInfo.getText().toString());
                    editLayout.setVisibility(View.GONE);
                    myEditImage.setVisibility(View.GONE);
                    myImage.setVisibility(View.VISIBLE);
                    rLayout.setVisibility(View.VISIBLE);
                }
            });
            myDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if(myDatabaseRef != null){
                        myDatabaseRef.getRef().removeValue();
                    }
                }
            });
        }
    }

}
