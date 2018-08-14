package com.richfit.zebra_ds36x8.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.richfit.zebra_ds36x8.R;
import com.richfit.zebra_ds36x8.base.BaseActivity;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.zebra.scannercontrol.FirmwareUpdateEvent;

public class ZebraScanActivity extends BaseActivity implements  ScannerAppEngine.IScannerAppEngineDevEventsDelegate, ScannerAppEngine.IScannerAppEngineDevConnectionsDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zebra_scan);
    }

    @Override
    public boolean scannerHasAppeared(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasDisappeared(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasConnected(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasDisconnected(int scannerID) {
        return false;
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerFirmwareUpdateEvent(FirmwareUpdateEvent firmwareUpdateEvent) {

    }

    @Override
    public void scannerImageEvent(byte[] imageData) {

    }
}
