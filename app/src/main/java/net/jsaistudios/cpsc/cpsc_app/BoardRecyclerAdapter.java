package net.jsaistudios.cpsc.cpsc_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkObject;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by ip on 8/18/18.
 */

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {
    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    //private ImageButton deleteButton;
    private Context context;
    BoardRecyclerAdapter(Context context, List<RecyclerModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_fragment_view_copy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardRecyclerAdapter.ViewHolder holder, int position) {
        String animal = mData.get(position).getItemObject().getName();

        BoardObject object = (BoardObject) mData.get(position).getItemObject();

        holder.myLocationInfo.setText(object.getInfo());
        holder.myTextView.setText(animal);
        holder.myBoardBio.setText(object.getBio());
        holder.myDatabaseRef = mData.get(position).getDatabaseNodeReference();
        holder.myImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.kurtis_barth));
        holder.myEditImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.kurtis_barth));



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView;
        TextView myLocationInfo;
        TextView myBoardBio;
        //ImageView myDeleteButton;
        DatabaseReference myDatabaseRef;
        EditText editName, editInfo;
        LinearLayout editLayout;
        ImageView myDeleteButton;
        RelativeLayout rLayout, defaultLayout;
        ImageView myImage;// myEditButton;
        ImageView myEditImage;
        ImageView myMessageButton;
        ImageView myEditButton;
        TextView myEditSave, myEditCancel;
        View fragRoot;
        EditText nameEditText, descriptionEditText, bioEditText;
        SwipeLayout swipeLayout;
        final int dur = 500;



        ViewHolder(final View itemView) {
            super(itemView);

            nameEditText = itemView.findViewById(R.id.edit_card_name);
            descriptionEditText = itemView.findViewById(R.id.edit_card_info);

            myTextView = itemView.findViewById(R.id.card_name);
            myLocationInfo = itemView.findViewById(R.id.card_description);
            myBoardBio = itemView.findViewById(R.id.card_bio);

            myDeleteButton = itemView.findViewById(R.id.delete_card);
            myEditButton = itemView.findViewById(R.id.edit_button2);
            bioEditText = itemView.findViewById(R.id.edit_perk_address);
            myMessageButton= itemView.findViewById(R.id.message_button);

            fragRoot = itemView.getRootView();

            editName = itemView.findViewById(R.id.edit_card_name);
            editInfo = itemView.findViewById(R.id.edit_card_info);

            defaultLayout = itemView.findViewById(R.id.default_layout);
            editLayout = itemView.findViewById(R.id.edit_layout_holder);

            rLayout = itemView.findViewById(R.id.cardInfoHolder);
            myImage = itemView.findViewById(R.id.location_image);
            myEditImage = itemView.findViewById(R.id.location_image2);

            myEditSave = itemView.findViewById(R.id.save_edit);
            myEditCancel = itemView.findViewById(R.id.cancel_edit);

            swipeLayout = itemView.findViewById(R.id.swipeToEdit);

            swipeLayout.setSwipeEnabled(false);


            //If user is not admin, dont allow swipe
            //Check if ifAdmin == true

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userID = user.getUid();
                DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("isAdmin");
                userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if ((Boolean.valueOf(dataSnapshot.getValue().toString()))) {
                                //Disable swipe layout
                                swipeLayout.setSwipeEnabled(true);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            myMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int dur = 500;
                        String phoneNumber;
                    phoneNumber = "7863091616";
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:" + phoneNumber));
                    context.startActivity(sendIntent);



                }
            });

            myEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    enterEditState();
                    BoardObject boardObject = (BoardObject)mData.get(getAdapterPosition()).getItemObject();
                    nameEditText.setText(boardObject.getName());
                    bioEditText.setText(boardObject.getBio());
                    descriptionEditText.setText(boardObject.getInfo());
                }
            });
            myEditCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(myDatabaseRef==null) {
                        int pos=getAdapterPosition();
                        //modelView.getDataModelList().remove(pos);
                        notifyDataSetChanged();
                    } else {
                        enterDefaultState();
                    }
                    /**editLayout.setVisibility(View.GONE);
                    myImage.setVisibility(View.VISIBLE);
                    rLayout.setVisibility(View.VISIBLE);
                    myImage.setAlpha(1);
                    rLayout.setAlpha(1);
                    myMessageButton.setVisibility(View.VISIBLE);
                     **/
                }
            });
            myEditSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(myDatabaseRef==null) {
                        int pos=getAdapterPosition();
                        //modelView.getDataModelList().remove(pos);
                        notifyDataSetChanged();
                    } else {
                        enterDefaultState();
                    }

                    myDatabaseRef.getRef().child("name").setValue(editName.getText().toString());
                    myDatabaseRef.getRef().child("info").setValue(editInfo.getText().toString());

                 /**   editLayout.setVisibility(View.GONE);
                    myEditImage.setVisibility(View.GONE);
                    myImage.setVisibility(View.VISIBLE);
                    rLayout.setVisibility(View.VISIBLE);
                     **/
                }
            });

           /** myDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if(myDatabaseRef != null){
                        myDatabaseRef.getRef().removeValue();
                    }
                }
            });
            **/
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
