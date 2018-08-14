package com.richfit.zebra_ds36x8;

import android.os.Handler;

import com.richfit.zebra_ds36x8.helpers.Barcode;
import com.richfit.zebra_ds36x8.helpers.Foreground;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.SDKHandler;

import java.util.ArrayList;

/**
 * Created by javakam on 2018/8/14.
 */
public class Application extends android.app.Application {
    //Instance of SDK Handler
    public static SDKHandler sdkHandler;

    //Handler to handle bluetooth events
    public static Handler globalMsgHandler;

    //Settings for notifications
    public static int MOT_SETTING_OPMODE ;
    public static boolean MOT_SETTING_SCANNER_DETECTION;
    public static boolean MOT_SETTING_EVENT_ACTIVE;
    public static boolean MOT_SETTING_EVENT_AVAILABLE;
    public static boolean MOT_SETTING_EVENT_BARCODE;
    public static boolean MOT_SETTING_EVENT_IMAGE;
    public static boolean MOT_SETTING_EVENT_VIDEO;

    public static boolean MOT_SETTING_NOTIFICATION_ACTIVE;
    public static boolean MOT_SETTING_NOTIFICATION_AVAILABLE;
    public static boolean MOT_SETTING_NOTIFICATION_BARCODE;
    public static boolean MOT_SETTING_NOTIFICATION_IMAGE;
    public static boolean MOT_SETTING_NOTIFICATION_VIDEO;

    public static int SCANNER_ID_NONE=  -1;
    public static String CurScannerName="";
    public static String CurScannerAddress="";
    public static int CurScannerId=SCANNER_ID_NONE;
    public static boolean CurAutoReconnectionState=true;
    public static boolean isAnyScannerConnected = false; //True, if currently connected to any scanner
    public static int currentConnectedScannerID = -1; //Track scannerId of currently connected Scanner
//    public static boolean isFirmwareUpdateInProgress = false;
    //Scanners (both available and active)
    public static ArrayList<DCSScannerInfo> mScannerInfoList = new ArrayList<DCSScannerInfo>();
    public static ArrayList<ScannerAppEngine.IScannerAppEngineDevListDelegate> mDevListDelegates = new ArrayList<ScannerAppEngine.IScannerAppEngineDevListDelegate>();
    //Barcode data
    public static ArrayList<Barcode> barcodeData = new ArrayList<Barcode>();
    public static DCSScannerInfo currentConnectedScanner;
    public static DCSScannerInfo lastConnectedScanner;
    public static int minScreenWidth = 360;

    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.init(this);
        sdkHandler = new SDKHandler(this);
    }
}
