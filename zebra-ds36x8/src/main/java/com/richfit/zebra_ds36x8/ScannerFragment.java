package com.richfit.zebra_ds36x8;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.richfit.zebra_ds36x8.base.BaseFragment;


/**
 * 扫描功能
 * <p>
 * Created by changbao on 2018-8-14.
 */
public class ScannerFragment extends BaseFragment {
    public ScannerFragment() {
        // Required empty public constructor
    }

    public static ScannerFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, pos);
        ScannerFragment fragment = new ScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }
}
