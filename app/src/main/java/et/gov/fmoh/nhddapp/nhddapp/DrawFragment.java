package et.gov.fmoh.nhddapp.nhddapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import et.gov.fmoh.nhddapp.nhddapp.views.fragments.Tab1Fragment;
import et.gov.fmoh.nhddapp.nhddapp.views.fragments.Tab2Fragment;

public class DrawFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     view =inflater.inflate(R.layout.tab_main,container, false);

     viewPager = view.findViewById(R.id.viewoager);
     viewPager.setAdapter(new sliderAdapter(getChildFragmentManager()));
     tabLayout = view.findViewById(R.id.tabs);
     tabLayout.post(new Runnable() {
         @Override
         public void run() {
             tabLayout.setupWithViewPager(viewPager);
         }
     });

     return view;
    }

    private class sliderAdapter extends FragmentPagerAdapter{

        final String tabs[]={"NCoD", "HMIS Indicators"};

        sliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
            return new Tab1Fragment();
            else
                return new Tab2Fragment();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}