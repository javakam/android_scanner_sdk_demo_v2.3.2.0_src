package com.richfit.zebra_ds36x8;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.richfit.zebra_ds36x8.base.BaseFragment;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;


/**
 * 可用设备列表
 * <p>
 * Created by changbao on 2018-8-14.
 */
public class DeviceListFragment extends BaseFragment implements ScannerAppEngine.IScannerAppEngineDevListDelegate {

    public DeviceListFragment() {
        // Required empty public constructor
    }

    public static DeviceListFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, pos);
        DeviceListFragment fragment = new DeviceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.scanners, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_add: {
                configureNotificationAvailable(true);
                /*
                 * RHBJ36 03.03.2016
                 * Start discovering available bluetooth devices.
                 */
//                Application.sdkHandler.dcssdkStartScanForAvailableDevices();
//                Application.sdkHandler.dcssdkStartScanForTopologyChanges();
                return true;
            }
            case android.R.id.home:
//                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean scannersListHasBeenUpdated() {
        return false;
    }
}
