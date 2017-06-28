package com.example.android.android_browsesr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tommy on 2017/6/28.
 */

public class DownloadedFragment extends Fragment  {
    public DownloadedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.downloaded_fragment, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.downloaded_recycler_view);
        rv.setHasFixedSize(true);
        DownLoadedRecylerViewAdapter adapter = new DownLoadedRecylerViewAdapter(getContext());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }
}
