package net.jsaistudios.cpsc.cpsc_app;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.PerkPage.PerkFunctions;

public class MainActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        new ListViewController(new EventsFunctions(), R.id.test_holder, getSupportFragmentManager(),
                context);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                new ListViewController(new BoardFunctions(), R.id.test_holder, manager,
                        context);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                new ListViewController(new EventsFunctions(), R.id.test_holder, manager,
                        context);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();
                new ListViewController(new PerkFunctions(), R.id.test_holder, manager,
                        context);
            }
        });

    }
}
