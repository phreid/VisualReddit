package com.preid.visualreddit;

import java.util.ArrayList;
import java.util.List;

public class ContentPresenter {
    private ContentZoomActivity mActivityParent;

    public ContentPresenter(ContentZoomActivity parent) {
        mActivityParent = parent;
        mActivityParent.setContentPresenter(this);
    }

    public void refreshContent(String url) {
        List<String> urlList = new ArrayList<>();
        urlList.add(url);
        mActivityParent.onContentLoaded(urlList);
    }

    public void onDestroy() {
        mActivityParent = null;
    }

    public ContentZoomActivity getActivityParent() {
        return mActivityParent;
    }
}
