package com.richfit.zebra_ds36x8.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.richfit.zebra_ds36x8.Application;
import com.richfit.zebra_ds36x8.helpers.Constants;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.FirmwareUpdateEvent;
import com.zebra.scannercontrol.IDcsSdkApiDelegate;

import java.util.ArrayList;
import java.util.List;

import static com.zebra.scannercontrol.DCSSDKDefs.DCSSDK_CONN_TYPES.DCSSDK_CONNTYPE_BT_NORMAL;

/**
 * Created by javakam on 2018/8/14.
 */
public class BaseActivityDelegate implements ScannerAppEngine, IDcsSdkApiDelegate {
    private Activity mActivity;
    private static ArrayList<IScannerAppEngineDevConnectionsDelegate> mDevConnDelegates = new ArrayList<IScannerAppEngineDevConnectionsDelegate>();
    private static ArrayList<IScannerAppEngineDevEventsDelegate> mDevEventsDelegates = new ArrayList<IScannerAppEngineDevEventsDelegate>();

    public BaseActivityDelegate(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void initializeDcsSdkWithAppSettings() {
        // Restore preferences
        SharedPreferences settings =mActivity. getSharedPreferences(Constants.PREFS_NAME, 0);

        Application.MOT_SETTING_OPMODE = settings.getInt(Constants.PREF_OPMODE, DCSSDK_CONNTYPE_BT_NORMAL.value);

        Application.MOT_SETTING_SCANNER_DETECTION = settings.getBoolean(Constants.PREF_SCANNER_DETECTION, true);
        Application.MOT_SETTING_EVENT_IMAGE = settings.getBoolean(Constants.PREF_EVENT_IMAGE, true);
        Application.MOT_SETTING_EVENT_VIDEO = settings.getBoolean(Constants.PREF_EVENT_VIDEO, false);

        Application.MOT_SETTING_EVENT_ACTIVE = settings.getBoolean(Constants.PREF_EVENT_ACTIVE, true);
        Application.MOT_SETTING_EVENT_AVAILABLE = settings.getBoolean(Constants.PREF_EVENT_AVAILABLE, true);
        Application.MOT_SETTING_EVENT_BARCODE = settings.getBoolean(Constants.PREF_EVENT_BARCODE, true);

        Application.MOT_SETTING_NOTIFICATION_AVAILABLE = settings.getBoolean(Constants.PREF_NOTIFY_AVAILABLE, false);
        Application.MOT_SETTING_NOTIFICATION_ACTIVE = settings.getBoolean(Constants.PREF_NOTIFY_ACTIVE, false);
        Application.MOT_SETTING_NOTIFICATION_BARCODE = settings.getBoolean(Constants.PREF_NOTIFY_BARCODE, false);

        Application.MOT_SETTING_NOTIFICATION_IMAGE = settings.getBoolean(Constants.PREF_NOTIFY_IMAGE, false);
        Application.MOT_SETTING_NOTIFICATION_VIDEO = settings.getBoolean(Constants.PREF_NOTIFY_VIDEO, false);

        int notifications_mask = 0;
        if (Application.MOT_SETTING_EVENT_AVAILABLE) {
            notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value);
        }
        if (Application.MOT_SETTING_EVENT_ACTIVE) {
            notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value | DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value);
        }
        if (Application.MOT_SETTING_EVENT_BARCODE) {
            notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value);
        }
        if (Application.MOT_SETTING_EVENT_IMAGE) {
            notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_IMAGE.value);
        }
        if (Application.MOT_SETTING_EVENT_VIDEO) {
            notifications_mask |= (DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_VIDEO.value);
        }
        Application.sdkHandler.dcssdkSubsribeForEvents(notifications_mask);
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
        if (Application.mDevListDelegates == null) {
            Application.mDevListDelegates = new ArrayList<IScannerAppEngineDevListDelegate>();
        }
        Application.mDevListDelegates.add(delegate);
    }

    @Override
    public void addDevConnectionsDelegate(IScannerAppEngineDevConnectionsDelegate delegate) {
        if (mDevConnDelegates == null) {
            mDevConnDelegates = new ArrayList<IScannerAppEngineDevConnectionsDelegate>();
        }
        mDevConnDelegates.add(delegate);
    }

    @Override
    public void addDevEventsDelegate(IScannerAppEngineDevEventsDelegate delegate) {
        if (mDevEventsDelegates == null) {
            mDevEventsDelegates = new ArrayList<IScannerAppEngineDevEventsDelegate>();
        }
        mDevEventsDelegates.add(delegate);
    }

    @Override
    public void removeDevListDelegate(IScannerAppEngineDevListDelegate delegate) {
        if (Application.mDevListDelegates != null) {
            Application.mDevListDelegates.remove(delegate);
        }
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
