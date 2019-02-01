package codeit.com.codeit.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import codeit.com.codeit.Fragment.info_profile_fragment;
import codeit.com.codeit.Fragment.post_profile_fragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{

    public ViewPagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                info_profile_fragment infoProfileFragment=new info_profile_fragment();
                return infoProfileFragment;

            case 1:
                post_profile_fragment postProfileFragment=new post_profile_fragment();
                return postProfileFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {

        switch (position)
        {
            case 0:
                return "Info";

            case 1:
                return "Posts";

            default:
                return null;
        }
    }
}
