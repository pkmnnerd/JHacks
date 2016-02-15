package com.example.brian.barcodescanner;

import android.app.ActionBar;
import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import layout.ProductFragment;
import layout.RecommendedFragment;
import layout.ReviewsFragment;
import layout.TweetsFragment;

public class InfoActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String upc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabAdapter tabAdapter = new TabAdapter((getSupportFragmentManager()));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(3);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.addTab(tabLayout.newTab().setText("Product"));
        //tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        //tabLayout.addTab(tabLayout.newTab().setText("Tweets"));
        //tabLayout.addTab(tabLayout.newTab().setText("Recommended"));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showButton();
                } else {
                    hideButton();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tabLayout.setTabsFromPagerAdapter(tabAdapter);
        showButton();
        Intent mIntent = getIntent();
        upc = mIntent.getStringExtra("UPC");


    }

    public void hideButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void showButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ProductFragment.pUrl));
                startActivity(i);
            }
        });
    }

    public String getUpc(){
        return upc;
    }

    class TabAdapter extends FragmentPagerAdapter {

        TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ProductFragment tab1 = new ProductFragment();
                    return tab1;
                case 1:
                    ReviewsFragment tab2 = new ReviewsFragment();
                    return tab2;
                case 2:
                    TweetsFragment tab3 = new TweetsFragment();
                    return tab3;
                case 3:
                    RecommendedFragment tab4 = new RecommendedFragment();
                    return tab4;
                default:
                    return null;
            }

        }
        @Override
        public CharSequence getPageTitle(int position) {
            String title = " ";
            switch (position) {
                case 0:
                    title = "Product";
                    break;
                case 1:
                    title = "Reviews";
                    break;
                case 2:
                    title = "Tweets";
                    break;
                case 3:
                    title = "Recommended";
                    break;
            }
            return title;
        }


            @Override
        public int getCount() {
            return 4;
        }
    }
}
