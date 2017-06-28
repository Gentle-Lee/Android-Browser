package com.example.android.android_browsesr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tommy on 2017/6/27.
 */

public class HistoryFragment extends Fragment implements HistoryRecylerViewAdapter.TextViewListener{

    public HistoryFragment() {
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
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.historyRecyclerView);
        rv.setHasFixedSize(true);
        HistoryRecylerViewAdapter adapter = new HistoryRecylerViewAdapter(getContext(),HistoryFragment.this);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    @Override
    public void clickListen(String urlString) {
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("urlString",urlString);
        HistoryFragment.this.startActivity(intent);
    }
}
