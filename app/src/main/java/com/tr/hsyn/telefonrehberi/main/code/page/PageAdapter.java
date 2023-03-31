package com.tr.hsyn.telefonrehberi.main.code.page;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


@SuppressWarnings("deprecation")
public class PageAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments;
    private final String[]   titles;

    public PageAdapter(final FragmentManager fm, final Fragment[] fragments) {

        this(fm, fragments, null);
    }

    public PageAdapter(final FragmentManager fm, final Fragment[] fragments, String[] titles) {

        super(fm);
        this.fragments = fragments;
        this.titles    = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragments[position];
    }

    @Override
    public int getCount() {

        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles == null ? "" : titles[position];
    }
}
