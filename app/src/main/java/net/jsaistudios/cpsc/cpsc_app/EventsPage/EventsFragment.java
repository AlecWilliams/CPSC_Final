package net.jsaistudios.cpsc.cpsc_app.EventsPage;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;

import net.jsaistudios.cpsc.cpsc_app.Calendar.CaldroidSampleCustomFragment;
import net.jsaistudios.cpsc.cpsc_app.EventListViewController;
import net.jsaistudios.cpsc.cpsc_app.ListViewController;
import net.jsaistudios.cpsc.cpsc_app.ListViewModel;
import net.jsaistudios.cpsc.cpsc_app.MainActivity;
import net.jsaistudios.cpsc.cpsc_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ip on 8/21/18.
 */

public class EventsFragment extends Fragment {
    View baseFragmentView;
    Context context;
    ViewPager mPager;
    PagerAdapter mPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.events_fragment, container, false);
        mPager = (ViewPager) baseFragmentView.findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(MainActivity.getFragManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        TabLayout tabLayout = (TabLayout) baseFragmentView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);
        return baseFragmentView;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0) {
                ListViewModel listViewModel = new ListViewModel();
                new EventListViewController(new EventsFunctions(), context, listViewModel);
                return listViewModel;
            } else {
                CaldroidFragment caldroidFragment = new CaldroidFragment();
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                caldroidFragment.setArguments(args);

                HashMap hm = new HashMap();
                // Put elements to the map

                hm.put(new Date(), ContextCompat.getDrawable(getContext(), R.color.colorPrimary));
                if (caldroidFragment != null) {
                    caldroidFragment.setBackgroundDrawableForDates(hm);
                }

                return caldroidFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "List";
                case 1:
                    return "Calendar";
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
