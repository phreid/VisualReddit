package com.preid.visualreddit;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

public abstract class ContentZoomActivity extends AppCompatActivity {

    abstract void onContentLoaded(List<String> urlList);

    abstract void setContentPresenter(ContentPresenter presenter);
}
