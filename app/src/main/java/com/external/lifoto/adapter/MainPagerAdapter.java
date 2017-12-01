package com.external.lifoto.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.external.lifoto.fragment.SortFragment;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<SortFragment> fragments;

    public MainPagerAdapter(FragmentManager manager, ArrayList<SortFragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();//页卡标题
    }
}