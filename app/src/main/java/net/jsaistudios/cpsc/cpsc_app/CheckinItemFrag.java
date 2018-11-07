package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheckinItemFrag extends Fragment {
    View baseFragmentView;
    public String name;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.event_checkin_item, container, false);
        ((TextView)baseFragmentView.findViewById(R.id.textView)).setText(name);
        return baseFragmentView;
    }
}
