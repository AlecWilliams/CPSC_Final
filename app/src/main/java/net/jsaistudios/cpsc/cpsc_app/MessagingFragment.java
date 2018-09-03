package net.jsaistudios.cpsc.cpsc_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessagingFragment extends Fragment {
    private View baseFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.messaging_fragment, container, false);
        return baseFragmentView;
    }
}
