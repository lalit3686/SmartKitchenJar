package com.app.kitchen.jar.screens;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.kitchen.jar.R;
import com.app.kitchen.jar.fragments.DayWiseChartFragment;
import com.app.kitchen.jar.fragments.LiveWeightFragment;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends BaseActivity {

    private Toolbar toolbarTabLayout;
    private TabLayout tabLayoutTabs;
    private ViewPager viewPagerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        initToolBar();
        initComponents();
        addListeners();
    }

    private void initToolBar(){
        toolbarTabLayout = (Toolbar) findViewById(R.id.toolbar_tab_layout);
        setSupportActionBar(toolbarTabLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    void initComponents() {
        viewPagerInfo = (ViewPager) findViewById(R.id.view_pager_info);
        setupViewPager(viewPagerInfo);

        tabLayoutTabs = (TabLayout) findViewById(R.id.tab_layout_tabs);
        tabLayoutTabs.setupWithViewPager(viewPagerInfo);
    }

    @Override
    void addListeners() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        LiveWeightFragment weightFragment = new LiveWeightFragment();
        weightFragment.setArguments(getIntent().getExtras());

        DayWiseChartFragment chartFragment = new DayWiseChartFragment();
        chartFragment.setArguments(getIntent().getExtras());

        adapter.addFragment(weightFragment, getString(R.string.title_live_weight));
        adapter.addFragment(chartFragment, getString(R.string.title_day_wise_chart));

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
