package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPerkViewController(R.id.test_holder,"PERK_FRAGMENT");
    }

    private PerkViewController createPerkViewController(int holder, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        PerkViewController fragment = new PerkViewController();

        fragment.setContext(this);
        fragmentTransaction.add(holder, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }
}
