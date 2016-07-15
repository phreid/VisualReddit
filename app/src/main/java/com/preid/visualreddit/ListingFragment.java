package com.preid.visualreddit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ListingFragment extends Fragment {

    protected RecyclerViewAdapter mRecyclerViewAdapter;
    protected LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutResourceId(), container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(setRecyclerViewId());

        mLayoutManager = createLayoutManager();
        recyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter = createRecyclerViewAdapter();
        recyclerView.setAdapter(mRecyclerViewAdapter);

        final RedditListingPresenter presenter = ((MainActivity) getActivity()).getListingPresenter();
        presenter.registerObserver(mRecyclerViewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();

                if (dy > 0
                    && ! presenter.isLoading()
                    && lastVisibleItem == totalItemCount - 1) {

                    presenter.scrollListing();
                }

            }
        });

        return view;
    }

    protected abstract int setRecyclerViewId();

    protected abstract LinearLayoutManager createLayoutManager();

    protected abstract int setLayoutResourceId();

    protected abstract RecyclerViewAdapter createRecyclerViewAdapter();
}
