package com.preid.visualreddit;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

public class TileListingFragment extends ListingFragment {

    @Override
    protected int setRecyclerViewId() {
        return R.id.tile_recycler_view;
    }

    @Override
    protected LinearLayoutManager createLayoutManager() {
        return new GridLayoutManager(getActivity(), 3);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tile;
    }

    @Override
    protected RecyclerViewAdapter createRecyclerViewAdapter() {
        return new RecyclerViewAdapter(getActivity(), RecyclerViewAdapter.VIEW_GRID);
    }
}
