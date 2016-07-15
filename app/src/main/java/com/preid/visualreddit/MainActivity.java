package com.preid.visualreddit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS_NAME = "prefs";
    public static final String LIST_NAME = "subList";
    
    private ViewPager mViewPager;
    private RedditListingPresenter mRedditListingPresenter;
    private ActionBar mActionBar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Set<String> mySubredditSet;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mViewPager = (ViewPager) findViewById(R.id.listing_view_pager);
        FragmentStatePagerAdapter adapter = new ListingPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_grid);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list);

        mRedditListingPresenter = new RedditListingPresenter(this);
        mRedditListingPresenter.refreshListing();

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mRedditListingPresenter.getSubreddit());
        mActionBar.setSubtitle(mRedditListingPresenter.getOrder());

        mSharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mySubredditSet = mSharedPrefs.getStringSet(LIST_NAME, null);
        
        if (mySubredditSet == null) {
            mySubredditSet = new HashSet<>();
            mySubredditSet.add("earthporn");
            mySubredditSet.add("aww");

            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putStringSet(LIST_NAME, mySubredditSet);
            editor.apply();
        }
        
        setMySubredditList();
    }

    private void setMySubredditList() {
        Menu menu = mNavigationView.getMenu();
        int order = 0;

        List<String> mySubredditList = new ArrayList<>();
        mySubredditList.addAll(mySubredditSet);
        Collections.sort(mySubredditList);

        for (String subreddit : mySubredditList) {
            menu.add(R.id.subreddit_list_group, Menu.NONE, order, subreddit);
            order++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_refresh) {
            mRedditListingPresenter.refreshListing();
            return true;
        }

        if (id == R.id.action_sort) {
            View sortItemView = findViewById(R.id.action_sort);
            showSortMenu(sortItemView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.menu_sort);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String order = String.valueOf(item.getTitle());
                mRedditListingPresenter.sort(order);
                mActionBar.setSubtitle(mRedditListingPresenter.getOrder());

                return true;
            }
        });

        popup.show();
    }

    public RedditListingPresenter getListingPresenter() {
        return mRedditListingPresenter;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_edit_subreddits) {
            mDrawerLayout.closeDrawers();

            Intent intent = new Intent(this, MySubredditActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.nav_open_sub) {
            mDrawerLayout.closeDrawers();

            showNewSubDialog();

            return true;
        }

        if (item.getGroupId() == R.id.subreddit_list_group) {
            String subreddit = String.valueOf(item.getTitle());

            setCurrentSubreddit(subreddit);
            mDrawerLayout.closeDrawers();
            return true;
        }

        return false;
    }

    private void setCurrentSubreddit(String subreddit) {
        mRedditListingPresenter.setSubreddit(subreddit);
        mRedditListingPresenter.sort("hot");
        mActionBar.setTitle(mRedditListingPresenter.getSubreddit());
        mActionBar.setSubtitle(mRedditListingPresenter.getOrder());
    }

    private void showNewSubDialog() {
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);

        Dialog dialog = new AlertDialog.Builder(this)
                .setView(editText)
                .setTitle("Open new subreddit")
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subreddit = String.valueOf(editText.getText());
                        setCurrentSubreddit(subreddit);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        dialog.show();
    }

    private class ListingPagerAdapter extends FragmentStatePagerAdapter {
        private String[] titles = new String[] {"Grid", "List"};

        public ListingPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TileListingFragment();
                case 1:
                    return new ListListingFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
