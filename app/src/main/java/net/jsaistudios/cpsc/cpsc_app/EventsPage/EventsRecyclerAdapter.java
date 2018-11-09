package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ip on 8/18/18.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    private ImageButton deleteButton;
    private Context context;

    EventsRecyclerAdapter(Context context, List<RecyclerModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context= context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_item_view, parent, false);
        return new ViewHolder(view);
    }
    public String formattedDate(Date date) {
        String strDateFormat = "MMM dd',' yyyy h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date).replace("PM", "pm").replace("AM", "am");
    }
    @Override
    public void onBindViewHolder(EventsRecyclerAdapter.ViewHolder holder, int position) {
        final EventsObject eventsObject = (EventsObject) mData.get(position).getItemObject();
        holder.myLocationInfo.setText(eventsObject.getInfo());
        holder.myTextView.setText(eventsObject.getName());
        holder.myDatabaseRef = mData.get(position).getDatabaseNodeReference();
        holder.myImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.firestone));
        holder.dateAndPlace.setText(eventsObject.getPlace() + " • " + formattedDate(eventsObject.getDate()));
        if(((EventsObject) mData.get(position).getItemObject()).getId().equals("CPSCCustom")) {
            holder.moreInfo.setVisibility(View.GONE);
        } else {
            holder.moreInfo.setVisibility(View.VISIBLE);
        }
        if(eventsObject.getDate().before(new Date())) {
            holder.myRoot.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray_4));
        } else {
            holder.myRoot.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView;
        TextView myLocationInfo, dateAndPlace, moreInfo;
        ImageView myDeleteButton;
        DatabaseReference myDatabaseRef;
        EditText editName, editInfo;
        LinearLayout editLayout;
        RelativeLayout rLayout, defaultLayout;
        ImageView myImage, myEditButton;
        ImageView myEditImage;
        TextView myEditSave, myEditCancel;
        View fragRoot, myRoot;
        SwipeLayout swipeLayout;


        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_name);
            myLocationInfo = itemView.findViewById(R.id.card_description);
            dateAndPlace = itemView.findViewById(R.id.dateandplace);
            fragRoot = itemView.getRootView();
            myRoot = itemView.findViewById(R.id.event_root);
            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myImage = itemView.findViewById(R.id.location_image);
            moreInfo = itemView.findViewById(R.id.more_info);
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://www.facebook.com/events/" + ((EventsObject)mData.get(getAdapterPosition()).getItemObject()).getId());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }
    }
}
