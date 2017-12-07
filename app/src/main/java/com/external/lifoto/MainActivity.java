package com.external.lifoto;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.external.lifoto.adapter.MainPagerAdapter;
import com.external.lifoto.content.TabList;
import com.external.lifoto.design.widget.FitToolbar;
import com.external.lifoto.fragment.SortFragment;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FitToolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager pager;
    private TextView title;

    private MainPagerAdapter pagerAdapter;
    private ArrayList<SortFragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        toolbar = (FitToolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.pager);
        title = (TextView) findViewById(R.id.title);

        init();
        initPager();
    }

    private void init() {
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ordinary.ttf"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        Toast.makeText(MainActivity.this,
                                "sub item 01", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void initPager() {
        String[] tabs = TabList.tabs;
        fragments = new ArrayList<>();
        for (String tab : tabs) {
            SortFragment fragment = new SortFragment();
            fragment.setTitle(tab);
            fragments.add(fragment);
        }
        pagerAdapter = new MainPagerAdapter(getFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action:
                fragments.get(pager.getCurrentItem()).goToTop();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
}
