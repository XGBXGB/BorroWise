package com.example.shayanetan.borrowise.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shayanetan.borrowise.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise.R;
import com.example.shayanetan.borrowise.Views.SlidingTabLayout;


public class TransactionFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;
    private static String TITLE_TAB1 = "BORROWED";
    private static String TITLE_TAB2 = "LENT";

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_transaction, container, false);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager = (ViewPager) layout.findViewById(R.id.view_pager);
        viewPagerAdapter.addFragment(new TransactionBorrowedFragment(), TITLE_TAB1);
        viewPagerAdapter.addFragment(new TransactionLentFragment(), TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) layout.findViewById(R.id.sliding_tab);
        // True if Tabs are same Width
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        return layout;
    }


}
