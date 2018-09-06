package net.jsaistudios.cpsc.cpsc_app.EventsPage;

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

import com.daimajia.swipe.SwipeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.R;
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.util.List;

/**
 * Created by ip on 8/18/18.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    private ImageButton deleteButton;

    EventsRecyclerAdapter(Context context, List<RecyclerModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsRecyclerAdapter.ViewHolder holder, int position) {
        String animal = mData.get(position).getItemObject().getName();

        EventsObject eventsObject = (EventsObject) mData.get(position).getItemObject();

        holder.myLocationInfo.setText(eventsObject.getInfo());
        holder.myTextView.setText(animal);
        holder.myDatabaseRef = mData.get(position).getDatabaseNodeReference();
        holder.myImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.firestone));
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
        EditText editName, editInfo;
        LinearLayout editLayout;
        RelativeLayout rLayout, defaultLayout;
        ImageView myImage, myEditButton;
        ImageView myEditImage;
        TextView myEditSave, myEditCancel;
        View fragRoot;
        SwipeLayout swipeLayout;


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
            defaultLayout = itemView.findViewById(R.id.default_layout);
            swipeLayout = itemView.findViewById(R.id.swipeToEdit);

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
                        defaultLayout.setAlpha(1);
                        myDeleteButton.animate().alpha(0);
                        defaultLayout.animate().alpha(0).setDuration(dur).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                editLayout.setVisibility(View.VISIBLE);
                                editLayout.animate().alpha(1).setDuration(dur);
                                defaultLayout.setVisibility(View.GONE);
                            }
                        });
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
