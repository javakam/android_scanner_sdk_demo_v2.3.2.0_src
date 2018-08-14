package com.richfit.zebra_ds36x8.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.richfit.zebra_ds36x8.Application;
import com.richfit.zebra_ds36x8.helpers.Barcode;
import com.richfit.zebra_ds36x8.helpers.Constants;
import com.richfit.zebra_ds36x8.helpers.CustomProgressDialog;
import com.richfit.zebra_ds36x8.helpers.ScannerAppEngine;
import com.richfit.zebra_ds36x8.receivers.NotificationsReceiver;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.FirmwareUpdateEvent;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.zebra.scannercontrol.RMDAttributes.RMD_ATTR_VALUE_ACTION_HIGH_HIGH_LOW_LOW_BEEP;

public abstract class BaseZebraScanActivity extends BaseActivity implements ScannerAppEngine.IScannerAppEngineDevEventsDelegate,
        ScannerAppEngine.IScannerAppEngineDevConnectionsDelegate {
    protected ArrayList<Barcode> barcodes;
    protected static int picklistMode;
    protected static boolean pagerMotorAvailable;
    protected int scannerID;
    protected int scannerType;

    protected int iBarcodeCount;
    protected static MyAsyncTask cmdExecTask = null;

    protected static final int ENABLE_FIND_NEW_SCANNER = 1;
    protected List<Integer> ssaSupportedAttribs;

//   protected Button btnFindScanner = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ssaSupportedAttribs = new ArrayList<Integer>();
        scannerID = getIntent().getIntExtra(Constants.SCANNER_ID, -1);
        BaseActivity.lastConnectedScannerID = scannerID;
        String scannerName = getIntent().getStringExtra(Constants.SCANNER_NAME);
        String address = getIntent().getStringExtra(Constants.SCANNER_ADDRESS);
        scannerType = getIntent().getIntExtra(Constants.SCANNER_TYPE, -1);

        picklistMode = getIntent().getIntExtra(Constants.PICKLIST_MODE, 0);

        pagerMotorAvailable = getIntent().getBooleanExtra(Constants.PAGER_MOTOR_STATUS, false);

        Application.CurScannerId = scannerID;
        Application.CurScannerName = scannerName;
        Application.CurScannerAddress = address;

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
        if (nMgr != null) {
            nMgr.cancel(NotificationsReceiver.DEFAULT_NOTIFICATION_ID);
        }
        barcodes = getBarcodeData(getScannerID());
    }

    @Override
    public void onResume() {
        super.onResume();
        addDevEventsDelegate(this);
        addDevConnectionsDelegate(this);
        addMissedBarcodes();

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
        if (nMgr != null) {
            nMgr.cancel(NotificationsReceiver.DEFAULT_NOTIFICATION_ID);
        }
//        if (waitingForFWReboot) {
//            Intent intent = new Intent(this, UpdateFirmware.class);
//            intent.putExtra(Constants.SCANNER_ID, scannerID);
//            intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
//            intent.putExtra(Constants.FW_REBOOT, true);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            setWaitingForFWReboot(false);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeDevEventsDelegate(this);
        removeDevConnectiosDelegate(this);
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onBackPressed() {
        //TODO 2018年8月14日16:19:55
//        minimizeApp();
    }

    public boolean isPagerMotorAvailable() {
        return pagerMotorAvailable;
    }

    public void startFirmware(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_START_NEW_FIRMWARE, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void abortFirmware(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_ABORT_UPDATE_FIRMWARE, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void loadLedActions(View view) {
//        Intent intent = new Intent(this, LEDActivity.class);
//        intent.putExtra(Constants.SCANNER_ID, scannerID);
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
//        startActivity(intent);
    }

    public void loadBeeperActions(View view) {
//        Intent intent = new Intent(this, BeeperActionsActivity.class);
//        intent.putExtra(Constants.SCANNER_ID, scannerID);
//        intent.putExtra(Constants.BEEPER_VOLUME, getIntent().getIntExtra(Constants.BEEPER_VOLUME, 0));
//        startActivity(intent);
    }

    public void loadAssert(View view) {
//        Intent intent = new Intent(this, AssertActivity.class);
//        intent.putExtra(Constants.SCANNER_ID, scannerID);
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
//        startActivity(intent);
    }

    public void symbologiesClicked(View view) {
//        Intent intent = new Intent(this, SymbologiesActivity.class);
//        intent.putExtra(Constants.SCANNER_ID, scannerID);
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
//        startActivity(intent);
    }

    public void enableScanning(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_SCAN_ENABLE, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void disableScanning(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_SCAN_DISABLE, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void aimOn(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_AIM_ON, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void aimOff(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_AIM_OFF, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void vibrationFeedback(View view) {
//        Intent intent = new Intent(this, VibrationFeedback.class);
//        intent.putExtra(Constants.SCANNER_ID, scannerID);
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
//        startActivity(intent);

//        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
//        cmdExecTask = new MyAsyncTask(scannerID,DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_VIBRATION_FEEDBACK,null);
//        cmdExecTask.execute(new String[]{in_xml});
    }

    public void pullTrigger(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_PULL_TRIGGER, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void releaseTrigger(View view) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_RELEASE_TRIGGER, null);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public int getPickListMode() {
//        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>402</attrib_list></arg-xml></cmdArgs></inArgs>";
//        StringBuilder outXML = new StringBuilder();
//        cmdExecTask = new MyAsyncTask(scannerID,DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET,outXML);
//        cmdExecTask.execute(new String[]{in_xml});
        int attr_val = 0;
//        try {
//            cmdExecTask.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        if(outXML !=null) {
//            try {
//                XmlPullParser parser = Xml.newPullParser();
//
//                parser.setInput(new StringReader(outXML.toString()));
//                int event = parser.getEventType();
//                String text = null;
//                while (event != XmlPullParser.END_DOCUMENT) {
//                    String name = parser.getName();
//                    switch (event) {
//                        case XmlPullParser.START_TAG:
//                            break;
//                        case XmlPullParser.TEXT:
//                            text = parser.getText();
//                            break;
//
//                        case XmlPullParser.END_TAG:
//                           if (name.equals("value")) {
//                                attr_val = Integer.parseInt(text.trim());
//                           }
//                            break;
//                    }
//                    event = parser.next();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        }
        return picklistMode;
    }


    public int getScannerID() {
        return scannerID;
    }

    private void addMissedBarcodes() {
        if (Application.barcodeData.size() != iBarcodeCount) {
            for (int i = iBarcodeCount; i < Application.barcodeData.size(); i++) {
                scannerBarcodeEvent(Application.barcodeData.get(i).getBarcodeData(), Application.barcodeData.get(i).getBarcodeType(), Application.barcodeData.get(i).getFromScannerID());
            }
        }
    }

    //传给子Activity处理
    public abstract void scannerBarcodeEvent2(byte[] barcodeData, int barcodeType, int scannerID);

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {
        scannerBarcodeEvent2(barcodeData, barcodeType, scannerID);
    }

    @Override
    public void scannerFirmwareUpdateEvent(FirmwareUpdateEvent firmwareUpdateEvent) {

    }

    @Override
    public void scannerImageEvent(byte[] imageData) {

    }

    @Override
    public boolean scannerHasAppeared(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasDisappeared(int scannerID) {
        if (null != cmdExecTask) {
            cmdExecTask.cancel(true);
        }
        Application.barcodeData.clear();
        this.finish();
        return true;
    }

    @Override
    public boolean scannerHasConnected(int scannerID) {
        Application.barcodeData.clear();
//        pairNewScannerMenu.setTitle(R.string.menu_item_device_disconnect);
        return false;
    }

    @Override
    public boolean scannerHasDisconnected(int scannerID) {
        Application.barcodeData.clear();
//        pairNewScannerMenu.setTitle(R.string.menu_item_device_pair);
        this.finish();
        return true;
    }

    public void setPickListMode(int picklistInt) {
        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-xml><attrib_list><attribute><id>" + 402 + "</id><datatype>B</datatype><value>" + picklistInt + "</value></attribute></attrib_list></arg-xml></cmdArgs></inArgs>";
        StringBuilder outXML = new StringBuilder();
        cmdExecTask = new MyAsyncTask(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET, outXML);
        cmdExecTask.execute(new String[]{in_xml});
    }

    public void loadBatteryStatistics(View view) {
//        String in_xml = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
//        new AsyncTaskBatteryAvailable(scannerID, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL, this, BatteryStatistics.class).execute(new String[]{in_xml});
    }


    //需求：当多个手持机混杂在一起，找到刚刚蓝牙配对的那台设备
    public void findScanner(View view) {
//        btnFindScanner = (Button) findViewById(R.id.btn_find_scanner);
//        if (btnFindScanner != null) {
//            btnFindScanner.setEnabled(false);
//        }
//        new FindScannerTask(scannerID).execute();
    }

    protected class AsyncTaskBatteryAvailable extends AsyncTask<String, Integer, Boolean> {
        int scannerId;
        Context context;
        Class targetClass;
        private CustomProgressDialog progressDialog;
        DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode;

        public AsyncTaskBatteryAvailable(int scannerId, DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode, Context context, Class targetClass) {
            this.scannerId = scannerId;
            this.opcode = opcode;
            this.context = context;
            this.targetClass = targetClass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new CustomProgressDialog(BaseZebraScanActivity.this, "Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            boolean result = executeCommand(opcode, strings[0], sb, scannerId);
            if (opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
                if (result) {
                    try {
                        int i = 0;
                        XmlPullParser parser = Xml.newPullParser();

                        parser.setInput(new StringReader(sb.toString()));
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
                                    if (name.equals("attribute")) {
                                        if (text != null && text.trim().equals("30018")) {
                                            return true;
                                        }
                                    }
                                    break;
                            }
                            event = parser.next();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Intent intent = new Intent(context, targetClass);
            intent.putExtra(Constants.SCANNER_ID, scannerID);
            intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME));
            intent.putExtra(Constants.BATTERY_STATUS, b);
            startActivity(intent);
        }


    }

    protected class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
        private int scannerId;
        private StringBuilder outXML;
        private DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode;
        private CustomProgressDialog progressDialog;

        public MyAsyncTask(int scannerId, DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode, StringBuilder outXML) {
            this.scannerId = scannerId;
            this.opcode = opcode;
            this.outXML = outXML;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new CustomProgressDialog(BaseZebraScanActivity.this, "Execute Command...");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... strings) {
            return executeCommand(opcode, strings[0], outXML, scannerId);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (!b) {
                Toast.makeText(BaseZebraScanActivity.this, "Cannot perform the action", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected class FindScannerTask extends AsyncTask<String, Integer, Boolean> {
        private int scannerId;

        public FindScannerTask(int scannerId) {
            this.scannerId = scannerId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Boolean doInBackground(String... strings) {

            long t0 = System.currentTimeMillis();

            TurnOnLEDPattern();
            BeepScanner();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (System.currentTimeMillis() - t0 < 3000) {
                VibrateScanner();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                VibrateScanner();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BeepScanner();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                VibrateScanner();
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TurnOffLEDPattern();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            //Find Scanner Button
//            if (btnFindScanner != null) {
//                btnFindScanner.setEnabled(true);
//            }
        }

        private void TurnOnLEDPattern() {
            String inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
                    88 + "</arg-int></cmdArgs></inArgs>";
            StringBuilder outXML = new StringBuilder();
            executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID);
        }

        private void TurnOffLEDPattern() {
            String inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
                    90 + "</arg-int></cmdArgs></inArgs>";
            StringBuilder outXML = new StringBuilder();
            executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID);
        }

        private void VibrateScanner() {
            String inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs>";
            StringBuilder outXML = new StringBuilder();
            executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_VIBRATION_FEEDBACK, inXML, outXML, scannerID);
        }

        private void BeepScanner() {
            String inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
                    RMD_ATTR_VALUE_ACTION_HIGH_HIGH_LOW_LOW_BEEP + "</arg-int></cmdArgs></inArgs>";
            StringBuilder outXML = new StringBuilder();
            executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID);
        }

    }
}
