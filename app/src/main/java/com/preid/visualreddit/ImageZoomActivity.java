package com.preid.visualreddit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageZoomActivity extends ContentZoomActivity {
    private ContentPresenter mContentPresenter;
    private ImageViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");

        TextView zoomTextView = (TextView) findViewById(R.id.zoom_image_text);
        zoomTextView.setText(title);

        ViewPager viewPager = (ViewPager) findViewById(R.id.zoom_view_pager);
        mAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        viewPager.setPageMargin(100);
        viewPager.setAdapter(mAdapter);

        LinkHandler.handle(this, url);
    }

    @Override
    void onContentLoaded(List<String> urlList) {
        for (int i = 0; i < urlList.size(); i++) {
            mAdapter.add(urlList.get(i));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentPresenter.onDestroy();
    }

    @Override
    void setContentPresenter(ContentPresenter presenter) {
        mContentPresenter = presenter;
    }

    private class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> urlList;

        public ImageViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            urlList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment imageZoomFragment = new ImageZoomFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", urlList.get(position));
            imageZoomFragment.setArguments(bundle);

            return imageZoomFragment;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        public void add(String url) {
            urlList.add(url);
        }
    }
}
