package com.external.lifoto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.external.lifoto.adapter.MainPagerAdapter;
import com.external.lifoto.fragment.SortFragment;
import com.external.lifoto.content.TabList;
import com.external.lifoto.design.widget.FitToolbar;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class MainActivity extends AppCompatActivity {

    private FitToolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager pager;

    private MainPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (FitToolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.pager);

        initPager();
    }

    private void initPager() {
        String[] tabs = TabList.tabs;
        ArrayList<SortFragment> fragments = new ArrayList<>();
        for (String tab : tabs) {
            SortFragment fragment = new SortFragment();
            fragment.setTitle(tab);
            fragments.add(fragment);
        }
        pagerAdapter = new MainPagerAdapter(getFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
}
