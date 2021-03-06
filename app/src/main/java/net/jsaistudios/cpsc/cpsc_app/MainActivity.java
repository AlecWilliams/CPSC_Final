package net.jsaistudios.cpsc.cpsc_app;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkFunctions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private FloatingActionButton fab;
    private static FragmentManager fragmentManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        fragmentManager = getSupportFragmentManager();
        mPager = (ViewPager) findViewById(R.id.main_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter();
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        createBottomMenu();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewModel fragment = (ListViewModel)mPagerAdapter.getRegisteredFragment(mPager.getCurrentItem());
                fragment.addCard();
            }
        });
    }

    public static FragmentManager getFragManager() {
        return fragmentManager;
    }

    void createBottomMenu() {
        AHBottomNavigation bottomNavigationBar = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        bottomNavigationBar.addItem(new AHBottomNavigationItem("Events", R.drawable.event));
        bottomNavigationBar.addItem(new AHBottomNavigationItem("Perks", R.drawable.tag));
        bottomNavigationBar.addItem(new AHBottomNavigationItem("Board", R.drawable.board));
        bottomNavigationBar.manageFloatingActionButtonBehavior(fab);
        bottomNavigationBar.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomNavigationBar.setInactiveColor(ContextCompat.getColor(this, R.color.gray_1));
        bottomNavigationBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                mPager.setCurrentItem(position);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ScreenSlidePagerAdapter() {
            super(getFragManager());
        }

        @Override
        public Fragment getItem(int position) {
            return PageController.getInstance().createPageFragment(position, context);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return PageController.PageType.size;
        }
    }
}
