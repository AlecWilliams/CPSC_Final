package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFragment;
import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.HomePage.HomeFunctions;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkFunctions;

/**
 * Created by ip on 8/21/18.
 */

public class PageController {
    private static PageController instance=null;

    public enum PageType {
        HOME, EVENTS, PERKS, BOARD, MESSAGING;
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
            case HOME:
                new ListViewController<HomeFunctions>(new HomeFunctions(activity, mainActivity), context, listViewModel);
                return listViewModel;
            case EVENTS:
                new EventListViewController(new EventsFunctions(), context, listViewModel);
                return listViewModel;
            case PERKS:
                new ListViewController<PerkFunctions>(new PerkFunctions(activity, mainActivity), context, listViewModel);
                return listViewModel;
            case BOARD:
                new ListViewController<BoardFunctions>(new BoardFunctions(), context, listViewModel);
                return listViewModel;
            case MESSAGING:
                return new MessagingFragment();
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
