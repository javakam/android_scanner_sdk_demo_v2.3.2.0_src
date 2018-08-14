package com.richfit.zebra_ds36x8.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.FirmwareUpdateEvent;
import com.zebra.scannercontrol.IDcsSdkApiDelegate;

import java.util.List;

/**
 * Created by javakam on 2018/8/14.
 */
public class BaseFragment extends Fragment implements ScannerAppEngine, IDcsSdkApiDelegate {
    protected static final String ARG_POSITION = "position";

    @Override
    public void initializeDcsSdkWithAppSettings() {

    }

    @Override
    public void showMessageBox(String message) {

    }

    @Override
    public int showBackgroundNotification(String text) {
        return 0;
    }

    @Override
    public int dismissBackgroundNotifications() {
        return 0;
    }

    @Override
    public boolean isInBackgroundMode(Context context) {
        return false;
    }

    @Override
    public void addDevListDelegate(IScannerAppEngineDevListDelegate delegate) {

    }

    @Override
    public void addDevConnectionsDelegate(IScannerAppEngineDevConnectionsDelegate delegate) {

    }

    @Override
    public void addDevEventsDelegate(IScannerAppEngineDevEventsDelegate delegate) {

    }

    @Override
    public void removeDevListDelegate(IScannerAppEngineDevListDelegate delegate) {

    }

    @Override
    public void removeDevConnectiosDelegate(IScannerAppEngineDevConnectionsDelegate delegate) {

    }

    @Override
    public void removeDevEventsDelegate(IScannerAppEngineDevEventsDelegate delegate) {

    }

    @Override
    public List<DCSScannerInfo> getActualScannersList() {
        return null;
    }

    @Override
    public DCSScannerInfo getScannerInfoByIdx(int dev_index) {
        return null;
    }

    @Override
    public DCSScannerInfo getScannerByID(int scannerId) {
        return null;
    }

    @Override
    public void raiseDeviceNotificationsIfNeeded() {

    }

    @Override
    public void updateScannersList() {

    }

    @Override
    public DCSSDKDefs.DCSSDK_RESULT connect(int scannerId) {
        return null;
    }

    @Override
    public void disconnect(int scannerId) {

    }

    @Override
    public DCSSDKDefs.DCSSDK_RESULT setAutoReconnectOption(int scannerId, boolean enable) {
        return null;
    }

    @Override
    public void enableScannersDetection(boolean enable) {

    }

    @Override
    public void configureNotificationAvailable(boolean enable) {

    }

    @Override
    public void configureNotificationActive(boolean enable) {

    }

    @Override
    public void configureNotificationBarcode(boolean enable) {

    }

    @Override
    public void configureNotificationImage(boolean enable) {

    }

    @Override
    public void configureNotificationVideo(boolean enable) {

    }

    @Override
    public void configureOperationalMode(DCSSDKDefs.DCSSDK_MODE mode) {

    }

    @Override
    public boolean executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE opCode, String inXML, StringBuilder outXML, int scannerID) {
        return false;
    }

    @Override
    public void dcssdkEventScannerAppeared(DCSScannerInfo dcsScannerInfo) {

    }

    @Override
    public void dcssdkEventScannerDisappeared(int i) {

    }

    @Override
    public void dcssdkEventCommunicationSessionEstablished(DCSScannerInfo dcsScannerInfo) {

    }

    @Override
    public void dcssdkEventCommunicationSessionTerminated(int i) {

    }

    @Override
    public void dcssdkEventBarcode(byte[] bytes, int i, int i1) {

    }

    @Override
    public void dcssdkEventImage(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventVideo(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventFirmwareUpdate(FirmwareUpdateEvent firmwareUpdateEvent) {

    }

    @Override
    public void dcssdkEventAuxScannerAppeared(DCSScannerInfo dcsScannerInfo, DCSScannerInfo dcsScannerInfo1) {

    }
}
