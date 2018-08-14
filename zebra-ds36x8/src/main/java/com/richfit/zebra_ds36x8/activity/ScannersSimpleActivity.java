package com.richfit.zebra_ds36x8.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.richfit.zebra_ds36x8.Application;
import com.richfit.zebra_ds36x8.R;
import com.richfit.zebra_ds36x8.base.BaseZebraScanActivity;
import com.richfit.zebra_ds36x8.helpers.Barcode;
import com.richfit.zebra_ds36x8.utils.RichEditText;

/**
 * 扫描功能 - 只有一个 RichEditText
 * <p>
 * Created by changbao on 2018-8-14.
 */
public class ScannersSimpleActivity extends BaseZebraScanActivity {
    TextView barcodeCount;
    RichEditText edtScanResult;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanners);
        barcodeCount = (TextView) findViewById(R.id.barcodesListCount);
        edtScanResult = (RichEditText) findViewById(R.id.edt_scan_result);
        btnClear = (Button) this.findViewById(R.id.btnClearList);

        if (barcodes == null || barcodes.size() <= 0) {
            btnClear.setEnabled(false);
        }
        if (barcodes.size() > 0) {
            btnClear.setEnabled(true);
        }
        updateBarcodeCount();
    }

    @Override
    public void scannerBarcodeEvent2(byte[] barcodeData, int barcodeType, int scannerID) {
        barcodes.add(new Barcode(barcodeData, barcodeType, scannerID));
        barcodeCount.setText("Barcodes Scanned: " + Integer.toString(++iBarcodeCount));
        if (iBarcodeCount > 0) {
            btnClear.setEnabled(true);
        }
//        if (!Application.isFirmwareUpdateInProgress) {
//           viewPager.setCurrentItem(BARCODE_TAB);
//        }
    }

    public void clearData(View view) {
        barcodes.clear();
        clearBarcodeData();

        iBarcodeCount = 0;
        barcodeCount.setText("Barcodes Scanned: " + Integer.toString(iBarcodeCount));
        btnClear.setEnabled(false);
    }

    public void updateBarcodeCount() {
        if (Application.barcodeData.size() != iBarcodeCount) {
            iBarcodeCount = Application.barcodeData.size();
            barcodeCount.setText("Barcodes Scanned: " + Integer.toString(iBarcodeCount));
            if (iBarcodeCount > 0) {
                btnClear.setEnabled(true);
            }
        }
    }
}