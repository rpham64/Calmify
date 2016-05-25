package com.rpham64.android.calmify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rudolf on 5/24/2016.
 */
public class CalmifyFragment extends Fragment {

    public static CalmifyFragment newInstance() {

        Bundle args = new Bundle();

        CalmifyFragment fragment = new CalmifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calmify, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.calmify_recycler_view);

        return view;
    }
}
