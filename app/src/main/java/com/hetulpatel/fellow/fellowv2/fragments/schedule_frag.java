package com.hetulpatel.fellow.fellowv2.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.tabs.RecyclerListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link schedule_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class schedule_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public static int current_day = 0;


    public schedule_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment schedule_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static schedule_frag newInstance(String param1, String param2) {
        schedule_frag fragment = new schedule_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tabbed, container, false);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("CURRENT",""+position);
                current_day = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());


        viewPagerAdapter.addFragment(new RecyclerListFragment_mon(),  "MON");
        viewPagerAdapter.addFragment(new RecyclerListFragment_tue(), "TUE");
        viewPagerAdapter.addFragment(new RecyclerListFragment_wed(), "WED");
        viewPagerAdapter.addFragment(new RecyclerListFragment_thu(), "THU");
        viewPagerAdapter.addFragment(new RecyclerListFragment_fri(), "FRI");
        viewPagerAdapter.addFragment(new RecyclerListFragment_sat(), "SAT");

        mViewPager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

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
