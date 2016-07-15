package com.preid.visualreddit;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class RedditListingPresenter {
    private static final String BASE_URL = "https://www.reddit.com/r/SUBREDDIT/ORDER/.json?raw_json=1&limit=30&after=";

    private String order = "hot";
    private String subreddit = "aww";
    private String after;
    private List<RecyclerViewAdapter> observers;
    private Context mContext;
    private boolean loading = false;

    public RedditListingPresenter(Context context) {
        mContext = context;
        observers = new ArrayList<>();
    }

    public boolean isLoading() {
        return loading;
    }

    public void registerObserver(RecyclerViewAdapter adapter) {
        observers.add(adapter);
    }

    private String buildUrlString() {
        return BASE_URL
                .replace("ORDER", order)
                .replace("SUBREDDIT", subreddit);
    }

    public void sort(String order) {
        this.order = order;
        refreshListing();
    }

    public void scrollListing() {
        String url = buildUrlString();

        if (after != null) {
            url += after;
        }

        load(url);
    }

    public void refreshListing() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).clear();
        }

        load(buildUrlString());
    }

    private void load(String url) {
        loading = true;

        Ion.with(mContext)
                .load(url)
                .as(RedditResponse.class)
                .setCallback(new FutureCallback<RedditResponse>() {
                    @Override
                    public void onCompleted(Exception e, RedditResponse result) {

                        after = result.getAfter();

                        List<RedditResponse.Child> children = result.getChildren();
                        List<RedditResponse.RedditPostData> posts = new ArrayList<>();

                        for (int i = 0; i < children.size(); i++) {
                            RedditResponse.RedditPostData data = children.get(i).getData();

                            if (LinkHandler.canHandle(data.getUrl())) {
                                posts.add(data);
                            }
                        }

                        for (int i = 0; i < observers.size(); i++) {
                            observers.get(i).add(posts);
                        }
                        loading = false;
                    }
                });
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getOrder() {
        return order;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
        refreshListing();
    }
}
