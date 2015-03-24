package com.gmail.nochtemirae.sms2mail_v1;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends ActionBarActivity {
    protected static SlidingMenu menu;
    protected static int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.sidemenu);
        menu.setBackgroundColor(0xFF333333);
        menu.setBehindWidthRes(R.dimen.slidingmenu_behind_width);

        ((ListView) findViewById(R.id.sidemenu)).setOnItemClickListener
                (new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        menuToggle();

                        if (currentPosition != position)
                            changeFragment(position);

                        currentPosition = position;
                    }
                });

        if (currentPosition != -1) {
            ((ListView) findViewById(R.id.sidemenu)).setItemChecked(currentPosition, true);
        }

        String[] items = {getString(R.string.ma_one), getString(R.string.ma_two)};
        ((ListView) findViewById(R.id.sidemenu)).setAdapter(
                new ArrayAdapter<Object>(
                        this,
                        R.layout.sidemenu_item,
                        R.id.sm_item,
                        items
                )
        );

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void changeFragment(int position) {
        switch (position) {
            case 0:
                showFragment(new SMSFragment());
                break;
            case 1:
                showFragment(new ContactsFragment());
                break;
        }
    }

    private void showFragment(Fragment currentFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, currentFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuToggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void menuToggle() {
        if (getSideMenu().isMenuShowing())
            menu.showContent();
        else
            menu.showMenu();
    }

    public static SlidingMenu getSideMenu() {
        return MainActivity.menu;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (menu.isMenuShowing()) {
                menu.toggle(true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
