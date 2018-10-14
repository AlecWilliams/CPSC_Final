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
            fragRoot = itemView.getRootView();

            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myImage = itemView.findViewById(R.id.location_image);

        }
    }
}
