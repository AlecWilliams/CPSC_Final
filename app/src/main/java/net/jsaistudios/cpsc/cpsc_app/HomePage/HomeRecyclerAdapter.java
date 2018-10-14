package net.jsaistudios.cpsc.cpsc_app.HomePage;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.MainActivity;
import net.jsaistudios.cpsc.cpsc_app.PageSpecificFunctions;
import net.jsaistudios.cpsc.cpsc_app.R;
import net.jsaistudios.cpsc.cpsc_app.RecyclerModel;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private PageSpecificFunctions pageSpecificFunctions;
    private List<RecyclerModel> mData;
    private LayoutInflater mInflater;
    private AppCompatActivity myActivity;
    private MainActivity mainActivity;
    private ListViewModel viewModel;
    final int dur = 500;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    HomeRecyclerAdapter(Context context, List<RecyclerModel> data, PageSpecificFunctions functions, ListViewModel vm, AppCompatActivity activity,
                        MainActivity ma) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.pageSpecificFunctions = functions;
        myActivity = activity;
        mainActivity = ma;
        viewModel = vm;
    }

    @Override
    public HomeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerAdapter.ViewHolder holder, int position) {
        holder.fillViewModel(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView;
        TextView myLocationInfo;


        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_name);
            myLocationInfo = itemView.findViewById(R.id.card_description);

        }

        private void fillViewModel(RecyclerModel model) {
            HomeObject perkObject = (HomeObject) model.getItemObject();
            this.myLocationInfo.setText(perkObject.getInfo());
            this.myTextView.setText(model.getItemObject().getName());
        }
    }
}
