package com.br.livewallpaper.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.br.livewallpaper.view.fragment.CategoryFragment;
import com.br.livewallpaper.view.fragment.DailyPopularFragment;
import com.br.livewallpaper.view.fragment.RecentsFragment;

public class PrincipalAdapter extends FragmentPagerAdapter {

    private Context context;
    public PrincipalAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return CategoryFragment.getInstance();
        else if (position == 1)
            return DailyPopularFragment.getInstance();
        else if (position == 2)
            return RecentsFragment.getInstance(context);
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Categoria";
            case 1:
                return "Diário";
            case 2:
                return "Recente";
        }
        return "";
    }
}
