package com.upgrad.internshipapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    private String[] tabTitles = new String[]{"Home", "Hot", "Offline"};
    String tag;
    public MyAdapter(Context context, FragmentManager fm, int totalTabs,String tag) {
        super(fm);
        myContext = context;
        this.tag=tag;
        this.totalTabs = totalTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return QuestionsFragment.newInstance(tag,"activity");
            case 1:
                return QuestionsFragment.newInstance(tag,"hot");
            case 2:
                return QuestionsFragmentOffline.newInstance(tag,"offline");
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}