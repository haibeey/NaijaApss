package com.haibeey.android.naijaapps.mainpage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {
    private MobileApps mobileApps;
    private DesktopApps desktopApps;
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
        mobileApps=MobileApps.newInstance();
        desktopApps=DesktopApps.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mobileApps;
            case 1:
                return desktopApps;
        }
        return null;
    }

    public DesktopApps getDesktopApps() {
        return desktopApps;
    }

    public MobileApps getMobileApps() {
        return mobileApps;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 1:
                return "DesktopApps";
            case 0:
                return "MobileApps";
            default:
                return "MobileApps";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
