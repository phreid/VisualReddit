package com.preid.visualreddit;

import android.widget.Adapter;

public interface ListingPresenter {
    void scrollListing();

    void refreshListing();

    boolean isLoading();

    void registerObserver(Adapter adapter);

    void sort(String order);
}
