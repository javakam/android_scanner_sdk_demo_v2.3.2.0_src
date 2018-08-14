package com.richfit.zebra_ds36x8.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.zebra_ds36x8.Application;
import com.richfit.zebra_ds36x8.R;
import com.richfit.zebra_ds36x8.base.BaseActivity;
import com.richfit.zebra_ds36x8.helpers.Constants;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.richfit.zebra_ds36x8.utils.L;
import com.zebra.scannercontrol.BarCodeView;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by javakam on 2018/8/14.
 */
public class HomeActivity extends BaseActivity implements ScannerAppEngine.IScannerAppEngineDevConnectionsDelegate {
    private FrameLayout llBarcode;
    //    private FrameLayout mFrameLayout;
    private static final int PERMISSIONS_ACCESS_COARSE_LOCATION = 10;
    static boolean firstRun = true;
    Dialog dialogBTAddress;
    static String btAddress;
    static String userEnteredBluetoothAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mFrameLayout = findViewById(R.id.content);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            initialize();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDevConnectionsDelegate(this);

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        TextView txtBarcodeType = (TextView) findViewById(R.id.scan_to_connect_barcode_type);
        TextView txtScannerConfiguration = (TextView) findViewById(R.id.scan_to_connect_scanner_config);
        String sourceString = "";
        txtBarcodeType.setText(Html.fromHtml(sourceString));
        txtScannerConfiguration.setText("");
        boolean dntShowMessage = settings.getBoolean(Constants.PREF_DONT_SHOW_INSTRUCTIONS, false);
        int barcode = settings.getInt(Constants.PREF_PAIRING_BARCODE_TYPE, 0);
        boolean setDefaults = settings.getBoolean(Constants.PREF_PAIRING_BARCODE_CONFIG, true);
        int protocolInt = settings.getInt(Constants.PREF_COMMUNICATION_PROTOCOL_TYPE, 0);
        String strProtocol = "SSI over Classic Bluetooth";
        llBarcode = (FrameLayout) findViewById(R.id.scan_to_connect_barcode);
        DCSSDKDefs.DCSSDK_BT_PROTOCOL protocol = DCSSDKDefs.DCSSDK_BT_PROTOCOL.LEGACY_B;
        DCSSDKDefs.DCSSDK_BT_SCANNER_CONFIG config = DCSSDKDefs.DCSSDK_BT_SCANNER_CONFIG.KEEP_CURRENT;
        if (barcode == 0) {
            txtBarcodeType.setText("");
            txtScannerConfiguration.setText("");
            sourceString = "STC Barcode ";
            txtBarcodeType.setText(Html.fromHtml(sourceString));
            switch (protocolInt) {
                case 0:
                    protocol = DCSSDKDefs.DCSSDK_BT_PROTOCOL.SSI_BT_CRADLE_HOST;//SSI over Classic Bluetooth
                    strProtocol = "SSI over Classic Bluetooth";
                    break;
                case 1:
                    protocol = DCSSDKDefs.DCSSDK_BT_PROTOCOL.SSI_BT_LE;//SSI over Bluetooth LE
                    strProtocol = "Bluetooth LE";
                    break;
                default:
                    protocol = DCSSDKDefs.DCSSDK_BT_PROTOCOL.SSI_BT_CRADLE_HOST;//SSI over Classic Bluetooth
                    break;
            }
            if (setDefaults) {
                config = DCSSDKDefs.DCSSDK_BT_SCANNER_CONFIG.SET_FACTORY_DEFAULTS;
                txtScannerConfiguration.setText(Html.fromHtml("<i> Set Factory Defaults, Com Protocol = " + strProtocol + "</i>"));
            } else {
                txtScannerConfiguration.setText(Html.fromHtml("<i> Keep Current Settings, Com Protocol = " + strProtocol + "</i>"));
            }
        } else {
            sourceString = "Legacy Pairing ";
            txtBarcodeType.setText(Html.fromHtml(sourceString));
            txtScannerConfiguration.setText("");
        }
        selectedProtocol = protocol;
        selectedConfig = config;
        generatePairingBarcode();
        if (dialogBTAddress == null && firstRun && !dntShowMessage) {
            //
            firstRun = false;
        }

        if ((barcode == 0)
                && (setDefaults == true)
                && (protocolInt == 0)) {
            txtScannerConfiguration.setText("");
            txtBarcodeType.setText("");
        } else {
            if (barcode == 0) {
                txtBarcodeType.setText(Html.fromHtml(sourceString));
            } else {
                txtBarcodeType.setText(Html.fromHtml(sourceString));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeDevConnectiosDelegate(this);
    }

    @Override
    protected void onDestroy() {
        // TODO: https://jiraemv.zebra.com/browse/SSDK-5961
        // Application.sdkHandler.dcssdkClose();
        super.onDestroy();
    }

    private void initialize() {
        initializeDcsSdk();
        llBarcode = (FrameLayout) findViewById(R.id.scan_to_connect_barcode);
        addDevConnectionsDelegate(this);
        setTitle("Pair New Scanner");
        broadcastSCAisListening();
    }

    private void broadcastSCAisListening() {
        Intent intent = new Intent();
        intent.setAction("com.zebra.scannercontrol.LISTENING_STARTED");
        sendBroadcast(intent);
    }

    private void updateBarcodeView(LinearLayout.LayoutParams layoutParams, BarCodeView barCodeView) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int orientation = this.getResources().getConfiguration().orientation;
        int x = width * 9 / 10;
        int y = x / 3;
        if (getDeviceScreenSize() > 6) { // TODO: Check 6 is ok or not
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                x = width / 2;
                y = x / 3;
            } else {
                x = width * 2 / 3;
                y = x / 3;
            }
        }
        barCodeView.setSize(x, y);
        llBarcode.addView(barCodeView, layoutParams);
    }

    private double getDeviceScreenSize() {
        double screenInches = 0;
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        int mWidthPixels;
        int mHeightPixels;

        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(mWidthPixels / dm.xdpi, 2);
            double y = Math.pow(mHeightPixels / dm.ydpi, 2);
            screenInches = Math.sqrt(x + y);
        } catch (Exception ignored) {
        }
        return screenInches;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    initialize();
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void initializeDcsSdk() {
        Application.sdkHandler.dcssdkEnableAvailableScannersDetection(true);
        Application.sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL);
        Application.sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_SNAPI);
        Application.sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_LE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        Fragment fragment = null;
        if (id == R.id.action_shut_scanner) {
//            fragment = ScannerFragment.newInstance(0);
//            startActivity(new Intent(this, ScannersActivity.class));
            disconnect(Application.currentConnectedScannerID);
            Application.barcodeData.clear();
            Application.CurScannerId = Application.SCANNER_ID_NONE;
            finish();
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_device_list) {
//            fragment = DeviceListFragment.newInstance(0);
            startActivity(new Intent(this, DeviceListActivity.class));
            return true;
        }
//        if (fragment != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.content, fragment, ScannerFragment.class.getName());
//            transaction.commit();
//        }
        return super.onOptionsItemSelected(item);
    }

    private void toZebraScan(int opt) {
        Class<?> clz = ZebraScanActivity.class;
        switch (opt) {
            case 0:

                break;
            case 1:

                break;

        }
        Intent intent = new Intent(this, clz);
        Bundle bundle = new Bundle();
        intent.putExtra("", bundle);
        startActivity(intent);
    }

    private void generatePairingBarcode() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        BarCodeView barCodeView = Application.sdkHandler.dcssdkGetPairingBarcode(selectedProtocol, selectedConfig);
        if (barCodeView != null) {
            updateBarcodeView(layoutParams, barCodeView);
        } else {
            // SDK was not able to determine Bluetooth MAC. So call the dcssdkGetPairingBarcode with BT Address.

            btAddress = getDeviceBTAddress(settings);
            if (btAddress.equals("")) {
                llBarcode.removeAllViews();
            } else {
                Application.sdkHandler.dcssdkSetBTAddress(btAddress);
                barCodeView = Application.sdkHandler.dcssdkGetPairingBarcode(selectedProtocol, selectedConfig, btAddress);
                if (barCodeView != null) {
                    updateBarcodeView(layoutParams, barCodeView);
                }
            }
        }
    }

    private String getDeviceBTAddress(SharedPreferences settings) {
        String bluetoothMAC = settings.getString(Constants.PREF_BT_ADDRESS, "");
        if (bluetoothMAC.equals("")) {
            if (dialogBTAddress == null) {
                dialogBTAddress = new Dialog(HomeActivity.this);
                dialogBTAddress.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogBTAddress.setContentView(R.layout.dialog_get_bt_address);

                final TextView cancelContinueButton = (TextView) dialogBTAddress.findViewById(R.id.cancel_continue);
                final TextView abtPhoneButton = (TextView) dialogBTAddress.findViewById(R.id.abt_phone);
                abtPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent statusSettings = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
                        startActivity(statusSettings);
                    }

                });
                cancelContinueButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(View view) {
                        if (cancelContinueButton.getText().equals(getResources().getString(R.string.cancel))) {
                            finish();
                        } else {
                            SharedPreferences.Editor settingsEditor = getSharedPreferences(Constants.PREFS_NAME, 0).edit();
                            settingsEditor.putString(Constants.PREF_BT_ADDRESS, userEnteredBluetoothAddress).commit();// Commit is required here. So suppressing warning.
                            if (dialogBTAddress != null) {
                                dialogBTAddress.dismiss();
                                dialogBTAddress = null;
                            }
                            startHomeActivityAgain();
                        }
                    }
                });

                final EditText editTextBluetoothAddress = (EditText) dialogBTAddress.findViewById(R.id.text_bt_address);
                editTextBluetoothAddress.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        userEnteredBluetoothAddress = s.toString();
                        if (isValidBTAddress(userEnteredBluetoothAddress)) {
                            Drawable dr = getResources().getDrawable(R.drawable.tick);
                            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                            editTextBluetoothAddress.setCompoundDrawables(null, null, dr, null);
                            cancelContinueButton.setText(getResources().getString(R.string.continue_txt));
                        } else {
                            editTextBluetoothAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            cancelContinueButton.setText(getResources().getString(R.string.cancel));
                        }
                    }
                });

                final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipBoard != null) {
                    clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

                        @SuppressLint("ApplySharedPref")
                        @Override
                        public void onPrimaryClipChanged() {
                            ClipData clipData = clipBoard.getPrimaryClip();
                            ClipData.Item item = clipData.getItemAt(0);
                            String bluetoothAddress = item.getText().toString();
                            if (isValidBTAddress(bluetoothAddress)) {
                                SharedPreferences.Editor settingsEditor = getSharedPreferences(Constants.PREFS_NAME, 0).edit();
                                settingsEditor.putString(Constants.PREF_BT_ADDRESS, bluetoothAddress).commit();// Commit is required here. So suppressing warning.
                                clipBoard.removePrimaryClipChangedListener(this);
                                if (dialogBTAddress != null) {
                                    dialogBTAddress.dismiss();
                                    dialogBTAddress = null;
                                }
                                startHomeActivityAgain();
                            }
                        }
                    });
                }
                dialogBTAddress.setCancelable(false);
                dialogBTAddress.setCanceledOnTouchOutside(false);
                dialogBTAddress.show();
                Window window = dialogBTAddress.getWindow();
                if (window != null) {
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                bluetoothMAC = settings.getString(Constants.PREF_BT_ADDRESS, "");
            } else {
                dialogBTAddress.show();
            }
        }
        return bluetoothMAC;
    }

    private void startHomeActivityAgain() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
    }

    public boolean isValidBTAddress(String text) {
        return text != null && text.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        reloadBarcode();
    }

    private void reloadBarcode() {
        generatePairingBarcode();
    }

    @Override
    public boolean scannerHasAppeared(int scannerID) {
        L.d("Home scannerHasAppeared...");
        return false;
    }

    @Override
    public boolean scannerHasDisappeared(int scannerID) {
        L.d("Home scannerHasDisappeared scannerID : " + scannerID);
        return false;
    }

    @Override
    public boolean scannerHasConnected(int scannerID) {
        L.d("Home scannerHasConnected scannerID : "+scannerID);
//        SsaSetSymbologyActivity.resetScanSpeedAnalyticSettings();
        ArrayList<DCSScannerInfo> activeScanners = new ArrayList<DCSScannerInfo>();
        Application.sdkHandler.dcssdkGetActiveScannersList(activeScanners);
        Intent intent = new Intent(this, ScannersActivity.class);
//        pairNewScannerMenu.setTitle(R.string.menu_item_device_disconnect);

        for (DCSScannerInfo scannerInfo : Application.mScannerInfoList) {
            if (scannerInfo.getScannerID() == scannerID) {
                intent.putExtra(Constants.SCANNER_NAME, scannerInfo.getScannerName());
                intent.putExtra(Constants.SCANNER_TYPE, scannerInfo.getConnectionType().value);
                intent.putExtra(Constants.SCANNER_ADDRESS, scannerInfo.getScannerHWSerialNumber());
                intent.putExtra(Constants.SCANNER_ID, scannerInfo.getScannerID());
                intent.putExtra(Constants.AUTO_RECONNECTION, scannerInfo.isAutoCommunicationSessionReestablishment());
                intent.putExtra(Constants.CONNECTED, true);
                intent.putExtra(Constants.PICKLIST_MODE, getPickListMode(scannerID));

                if (scannerInfo.getScannerModel() != null && scannerInfo.getScannerModel().startsWith("PL3300")) { // remove this condition when CS4070 get the capability
                    intent.putExtra(Constants.PAGER_MOTOR_STATUS, true);
                } else {
                    intent.putExtra(Constants.PAGER_MOTOR_STATUS, isPagerMotorAvailable(scannerID));
                }
                intent.putExtra(Constants.BEEPER_VOLUME, getBeeperVolume(scannerID));
                Application.isAnyScannerConnected = true;
                Application.currentConnectedScannerID = scannerID;
                Application.currentConnectedScanner = scannerInfo;
                Application.lastConnectedScanner = Application.currentConnectedScanner;
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    private int getBeeperVolume(int scannerID) {
        int beeperVolume = 0;
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>140</attrib_list></arg-xml></cmdArgs></inArgs>";
        StringBuilder outXML = new StringBuilder();
        executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET, in_xml, outXML, scannerID);

        try {
            XmlPullParser parser = Xml.newPullParser();

            parser.setInput(new StringReader(outXML.toString()));
            int event = parser.getEventType();
            String text = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("value")) {
                            beeperVolume = Integer.parseInt(text != null ? text.trim() : null);
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (beeperVolume == 0) {
            return 100;
        } else if (beeperVolume == 1) {
            return 50;
        } else {
            return 0;
        }
    }

    private boolean isPagerMotorAvailable(int scannerID) {
        boolean isFound = false;
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>613</attrib_list></arg-xml></cmdArgs></inArgs>";
        StringBuilder outXML = new StringBuilder();
        executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET, in_xml, outXML, scannerID);
        if (outXML.toString().contains("<id>613</id>")) {
            isFound = true;
        }
        return isFound;
    }


    private int getPickListMode(int scannerID) {
        int attrVal = 0;
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>402</attrib_list></arg-xml></cmdArgs></inArgs>";
        StringBuilder outXML = new StringBuilder();
        executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET, in_xml, outXML, scannerID);

        try {
            XmlPullParser parser = Xml.newPullParser();

            parser.setInput(new StringReader(outXML.toString()));
            int event = parser.getEventType();
            String text = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("value")) {
                            attrVal = Integer.parseInt(text != null ? text.trim() : null);
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return attrVal;
    }

    @Override
    public boolean scannerHasDisconnected(int scannerID) {
//        pairNewScannerMenu.setTitle(R.string.menu_item_device_pair);
        Application.isAnyScannerConnected = false;
        Application.currentConnectedScannerID = -1;
        Application.lastConnectedScanner = Application.currentConnectedScanner;
        Application.currentConnectedScanner = null;
        return false;
    }
}

