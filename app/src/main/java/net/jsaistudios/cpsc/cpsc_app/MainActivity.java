package net.jsaistudios.cpsc.cpsc_app;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkFunctions;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private PageSpecificFunctions[] functions = {new EventsFunctions(), new PerkFunctions(), new BoardFunctions()};
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        manager = getSupportFragmentManager();

        manager.popBackStack();
        ListViewController first = new ListViewController(functions[0], R.id.test_holder, manager,
                context);

        createBottomMenu();
    }

    void createBottomMenu() {
        AHBottomNavigation bottomNavigationBar = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        bottomNavigationBar.addItem(new AHBottomNavigationItem("Events", R.drawable.event));
        bottomNavigationBar.addItem(new AHBottomNavigationItem("Perks", R.drawable.tag));
        bottomNavigationBar.addItem(new AHBottomNavigationItem("Board", R.drawable.board));
        bottomNavigationBar.manageFloatingActionButtonBehavior(fab);
        bottomNavigationBar.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomNavigationBar.setInactiveColor(ContextCompat.getColor(this, R.color.gray_1));
        bottomNavigationBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                    manager.popBackStack();
                    new ListViewController(functions[position], R.id.test_holder, manager,
                            context);

                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
