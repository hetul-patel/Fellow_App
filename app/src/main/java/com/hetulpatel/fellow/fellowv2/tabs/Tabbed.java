package com.hetulpatel.fellow.fellowv2.tabs;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.TextView;

import com.hetulpatel.fellow.fellowv2.R;

import java.util.ArrayList;
import java.util.List;

public class Tabbed extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tabbed);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        //tabLayout.setupWithViewPager(mViewPager);


    }

    private void setupViewPager(ViewPager mViewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        RecyclerListFragment mon = new RecyclerListFragment();
        mon.setArguments(position(0));
        RecyclerListFragment tue = new RecyclerListFragment();
        tue.setArguments(position(1));
        RecyclerListFragment wed = new RecyclerListFragment();
        wed.setArguments(position(2));
        RecyclerListFragment thu = new RecyclerListFragment();
        thu.setArguments(position(3));
        RecyclerListFragment fri = new RecyclerListFragment();
        fri.setArguments(position(4));
        RecyclerListFragment sat = new RecyclerListFragment();
        sat.setArguments(position(5));

        viewPagerAdapter.addFragment(mon,  "MON");
        viewPagerAdapter.addFragment(tue, "TUE");
        viewPagerAdapter.addFragment(wed, "WED");
        viewPagerAdapter.addFragment(thu, "THU");
        viewPagerAdapter.addFragment(fri, "FRI");
        viewPagerAdapter.addFragment(sat, "SAT");

        mViewPager.setAdapter(viewPagerAdapter);
    }

    private Bundle position (int index){
        Bundle args = new Bundle();
        args.putInt("index", index);
        return args;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }



}
