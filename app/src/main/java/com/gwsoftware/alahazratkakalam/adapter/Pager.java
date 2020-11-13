package com.gwsoftware.alahazratkakalam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gwsoftware.alahazratkakalam.fragments.DownloadedAudio;
import com.gwsoftware.alahazratkakalam.fragments.DownloadedBooks;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                DownloadedBooks tab1 = new DownloadedBooks();
                return tab1;
            case 1:
                DownloadedAudio tab2 = new DownloadedAudio();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}