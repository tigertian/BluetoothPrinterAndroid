package com.tigertian.test;

import android.app.IntentService;
import android.content.Intent;

import com.tigertian.bluetoothprinter.printer.BtPrinter;
import com.tigertian.bluetoothprinter.printer.PrinterWriter;
import com.tigertian.bluetoothprinter.printer.PrinterWriter80mm;

import java.util.ArrayList;


/**
 * @author tianlu
 */
public class BtTestService extends IntentService {


    public static final String ACTION_PRINT_TEST = "action_print_test";
    public static final String PRINT_EXTRA = "print_extra";

    public BtTestService() {
        super("BtTestService");
    }

    public BtTestService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(ACTION_PRINT_TEST)) {
            printTest();
        }

    }

    private void printTest() {
        PrintOrderDataMaker printOrderDataMaker = new PrintOrderDataMaker(this, "", PrinterWriter80mm.TYPE_80, PrinterWriter.HEIGHT_PARTING_DEFAULT);
        ArrayList<byte[]> printData = (ArrayList<byte[]>) printOrderDataMaker.getPrintData(PrinterWriter80mm.TYPE_80);
        BtPrinter.getBtPrinter(BondHelper.getDefaultBluethoothDeviceAddress(getApplicationContext())).add(printData);
    }

}