package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.Dialogs.EventCreationDialog;
import net.jsaistudios.cpsc.cpsc_app.Dialogs.NotificationCreationDialog;
import net.jsaistudios.cpsc.cpsc_app.Dialogs.PerkCreationDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private FloatingActionButton fab;
    private  MainActivity mainActivity;
    private AppCompatActivity activity;
    private View topBar;
    private static FragmentManager fragmentManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        activity = this;
        mainActivity = this;
        topBar = findViewById(R.id.top_bar);
        View checkInButton = findViewById(R.id.checkin_button);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCheckIn();
                if(fab!=null) {
                    fab.setVisibility(View.GONE);
                }
            }
        });

        View logooutButton = findViewById(R.id.logout_button);
        logooutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getFragManager().popBackStack();
                createLogin();
            }
        });
        start();

    }
    private void start() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            createApp();
        } else {
            createLogin();
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
    private Login createLogin() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        final View loginHolder =findViewById(R.id.login_holder);
        Login fragment = new Login();
        loginHolder.setVisibility(View.VISIBLE);
        fragment.loginListener = new Login.LoginListener() {
            @Override
            public void loggedIn(boolean isAdmin, boolean isNew) {
                createApp();
                loginHolder.setVisibility(View.GONE);
                hideSoftKeyboard(mainActivity);
            }
        };
        fragment.activity = activity;
        fragment.context = this;
        fragmentTransaction.add(R.id.login_holder, fragment, "Login Frag");
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    private CheckInFragment createCheckIn() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CheckInFragment fragment = new CheckInFragment();
        fragment.activity = this;
        fragment.closeObserver = getCloseObserver();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.checkin_holder, fragment, "CheckIn Frag");
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }
    private Observer closeObserver;
    private Observer getCloseObserver() {
        if(closeObserver==null) {
            closeObserver = new Observer() {
                @Override
                public void update() {
                    if (fab != null) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            };
        }
        return closeObserver;
    }

    private void handleNewPage(int position, boolean admin) {
        if(position==3 || !admin) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
            if (position == 2) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PerkCreationDialog().show(getFragmentManager(), "Make Perk");

                    }
                });
            } else if (position == 0) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new NotificationCreationDialog().show(getFragmentManager(), "Make Notif");
                    }
                });
            } else if(position==1) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();

                        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, final int y, final int m, final int d) {
                                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        Bundle args = new Bundle();
                                        args.putString("date", String.format("%02d", y)+"/"+String.format("%02d", m)+"/"+String.format("%02d", d));
                                        args.putString("time", String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute));
                                        EventCreationDialog eventCreationDialog = new EventCreationDialog();
                                        eventCreationDialog.setArguments(args);
                                        eventCreationDialog.show(getFragmentManager(), "Make Event");
                                    }
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });
            }
        }
    }
    private boolean admin = false;
    private int position = 0;

    private void createApp() {
        fragmentManager = getSupportFragmentManager();
        mPager = (ViewPager) findViewById(R.id.main_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter();
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        handleNewPage(position, admin);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position=pos;
                handleNewPage(position, admin);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            DatabaseReference userAdminCheck = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("clearance");
            userAdminCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue().toString().equals("admin")) {
                            admin=true;
                            handleNewPage(position, admin);
                            if(mPager.getCurrentItem()!=0) {
                                topBar.setVisibility(View.GONE);
                            } else {
                                topBar.setVisibility(View.VISIBLE);
                            }

                        } else {
                            admin=false;
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        createBottomMenu();
    }


    public static FragmentManager getFragManager() {
        return fragmentManager;
    }

    void createBottomMenu() {
        AHBottomNavigation bottomNavigationBar = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        if(bottomNavigationBar.getItemsCount()==0) {
            bottomNavigationBar.addItem(new AHBottomNavigationItem("Home", R.drawable.home));
            bottomNavigationBar.addItem(new AHBottomNavigationItem("Events", R.drawable.event));
            bottomNavigationBar.addItem(new AHBottomNavigationItem("Perks", R.drawable.tag));
            bottomNavigationBar.addItem(new AHBottomNavigationItem("Board", R.drawable.board));
        }
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

    public AutocompleteFragment createAutocompleteFrag(String tag, String initText) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AutocompleteFragment fragment = new AutocompleteFragment();
        fragment.setInitialText(initText);
        fragmentTransaction.add(R.id.autocomplete_frag_holder, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
        Fragment fragmentA = getFragManager().findFragmentByTag("CheckIn Frag");
        if (fragmentA != null) {
            getCloseObserver().update();
        }
        super.onBackPressed();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ScreenSlidePagerAdapter() {
            super(getFragManager());
        }

        @Override
        public Fragment getItem(int position) {
            return PageController.getInstance().createPageFragment(position, context, activity, mainActivity);
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
