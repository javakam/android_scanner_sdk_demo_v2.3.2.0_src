package com.richfit.zebra_ds36x8.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.richfit.zebra_ds36x8.Application;
import com.richfit.zebra_ds36x8.R;
import com.richfit.zebra_ds36x8.adapters.BarcodeListAdapter;
import com.richfit.zebra_ds36x8.base.BaseZebraScanActivity;
import com.richfit.zebra_ds36x8.helpers.Barcode;

/**
 * 扫描功能
 * <p>
 * Created by changbao on 2018-8-14.
 */
public class ScannersActivity extends BaseZebraScanActivity {
    private TextView barcodeCount;
    private BarcodeListAdapter barcodeAdapter;
    private ListView barcodesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanners);

        barcodeAdapter = new BarcodeListAdapter(this, barcodes);
        barcodesList = (ListView) this.findViewById(R.id.barcodesList);
        barcodesList.setAdapter(barcodeAdapter);
        Button btnClear = (Button) this.findViewById(R.id.btnClearList);

        if (barcodes == null || barcodes.size() <= 0) {
            btnClear.setEnabled(false);
        }
        if (barcodes.size() > 0) {
            btnClear.setEnabled(true);
        }
        barcodesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        updateBarcodeCount();
    }

    @Override
    public void scannerBarcodeEvent2(byte[] barcodeData, int barcodeType, int scannerID) {
        //        BarcodeFargment barcodeFargment = (BarcodeFargment) mAdapter.getRegisteredFragment(1);
//        if (barcodeFargment != null) {
//            barcodeFargment.showBarCode(barcodeData, barcodeType, scannerID);
        showBarCode(barcodeData, barcodeType, scannerID);
        barcodeCount = (TextView) findViewById(R.id.barcodesListCount);
        barcodeCount.setText("Barcodes Scanned: " + Integer.toString(++iBarcodeCount));
        if (iBarcodeCount > 0) {
            Button btnClear = (Button) findViewById(R.id.btnClearList);
            btnClear.setEnabled(true);
        }
//        if (!Application.isFirmwareUpdateInProgress) {
//           viewPager.setCurrentItem(BARCODE_TAB);
//        }
//        }
    }

    public void showBarCode(byte[] barcodeData, int barcodeType, int scannerID) {
        barcodes.add(new Barcode(barcodeData, barcodeType, scannerID));
        barcodeAdapter.add(new Barcode(barcodeData, barcodeType, scannerID));
        barcodeAdapter.notifyDataSetChanged();
        scrollListViewToBottom();
    }

    private void scrollListViewToBottom() {
        barcodesList.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                barcodesList.setSelection(barcodeAdapter.getCount() - 1);
            }
        });
    }

    public void clearList(View view) {
//        BarcodeFargment barcodeFargment = (BarcodeFargment) mAdapter.getRegisteredFragment(1);
//        if (barcodeFargment != null) {
//            barcodeFargment.clearList();
        barcodes.clear();
        barcodeAdapter.clear();
        barcodesList.setAdapter(barcodeAdapter);
        clearBarcodeData();

        barcodeCount = (TextView) findViewById(R.id.barcodesListCount);
        iBarcodeCount = 0;
        barcodeCount.setText("Barcodes Scanned: " + Integer.toString(iBarcodeCount));
        Button btnClear = (Button) findViewById(R.id.btnClearList);
        btnClear.setEnabled(false);
//        }
    }

    public void updateBarcodeCount() {
        if (Application.barcodeData.size() != iBarcodeCount) {
            barcodeCount = (TextView) findViewById(R.id.barcodesListCount);
            iBarcodeCount = Application.barcodeData.size();
            barcodeCount.setText("Barcodes Scanned: " + Integer.toString(iBarcodeCount));
            if (iBarcodeCount > 0) {
                Button btnClear = (Button) findViewById(R.id.btnClearList);
                btnClear.setEnabled(true);
            }
        }
    }
}