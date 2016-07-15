package com.preid.visualreddit;


import android.support.v7.widget.LinearLayoutManager;

public class ListListingFragment extends ListingFragment {


    @Override
    protected int setRecyclerViewId() {
        return R.id.list_recycler_view;
    }

    @Override
    protected LinearLayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_list;
    }

    @Override
    protected RecyclerViewAdapter createRecyclerViewAdapter() {
        return new RecyclerViewAdapter(getActivity(), RecyclerViewAdapter.VIEW_LIST);
    }
}
