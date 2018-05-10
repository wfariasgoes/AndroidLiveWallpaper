package com.br.livewallpaper.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter{
    private Context context;
    private final List<Fragment> mfragmentlist =new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MyFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentlist.get(position);
    }


    public void addFrag(Fragment fragment,String title){
        mfragmentlist.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mfragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
