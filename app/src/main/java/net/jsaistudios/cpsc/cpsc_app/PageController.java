package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFragment;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkFunctions;

/**
 * Created by ip on 8/21/18.
 */

public class PageController {
    private static PageController instance=null;

    public enum PageType {
        EVENTS, PERKS, BOARD;
        private final int value;
        PageType() {
            this.value = ordinal();
        }
        public static final int size = PageType.values().length;
    }

    public Fragment createPageFragment(int type, Context context, AppCompatActivity activity, MainActivity mainActivity) {
        PageType enumType = PageType.values()[type];
        ListViewModel listViewModel = new ListViewModel();
        switch (enumType) {
            case EVENTS:
                EventsFragment fragment = new EventsFragment();
                fragment.setContext(context);
                return fragment;
            case PERKS:
                new ListViewController(new PerkFunctions(activity, mainActivity), context, listViewModel);
                return listViewModel;
            case BOARD:
                new ListViewController(new BoardFunctions(), context, listViewModel);
                return listViewModel;
        }
        return null;
    }

    public static PageController getInstance() {
        if(instance==null) {
            instance = new PageController();
        }
        return instance;
    }
}
