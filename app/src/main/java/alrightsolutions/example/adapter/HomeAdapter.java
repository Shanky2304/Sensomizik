package alrightsolutions.example.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import alrightsolutions.example.HomeFragment;
import alrightsolutions.example.PlaylistFragment;

/**
 * Created by Shanky23 on 12/24/2016.
 */

public class HomeAdapter extends FragmentPagerAdapter{

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return PlaylistFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Playlists";
        }
        return null;
    }
}
