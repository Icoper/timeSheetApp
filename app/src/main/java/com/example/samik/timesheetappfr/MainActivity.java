package com.example.samik.timesheetappfr;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.samik.timesheetappfr.fragment.FragmentExport;

public class MainActivity extends AppCompatActivity {
    // Fragments install
    private FragmentTransaction transaction;
    private FragmentExport fragmentExport;
    private Toolbar toolbar;
    private Menu menu;
    private int status = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentExport = new FragmentExport();
        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer,fragmentExport);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        this.menu = menu;

        //Change colour for selected icon
        if (Build.VERSION.SDK_INT >= 21) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem mItem = this.menu.getItem(i);
                Drawable icon = mItem.getIcon();
                if (this.status == i)
                    icon.setTint(getResources().getColor(R.color.colorActiveIcon));
                else
                    icon.setTint(getResources().getColor(R.color.colorNoActiveIcon));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_export:
                this.setContentView(R.layout.screen_email);
                toolbar = (Toolbar) this.findViewById(R.id.toolBar_MainActivity);
                toolbar.setTitleTextColor(Color.WHITE);
                toolbar.setSubtitleTextColor(Color.WHITE);
                this.setSupportActionBar(toolbar);
                Toast.makeText(this, "Export", Toast.LENGTH_SHORT).show();

                // Запускаем FragmentExport
                fragmentExport = new FragmentExport();
                transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragmentContainer,fragmentExport);
                transaction.commit();
                return true;
            case R.id.action_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
