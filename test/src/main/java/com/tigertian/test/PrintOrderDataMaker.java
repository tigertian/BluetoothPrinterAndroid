package com.tigertian.test;

import android.content.Context;

import com.tigertian.bluetoothprinter.printer.PrintDataMaker;
import com.tigertian.bluetoothprinter.printer.PrinterWriter;
import com.tigertian.bluetoothprinter.printer.PrinterWriter58mm;
import com.tigertian.bluetoothprinter.printer.PrinterWriter80mm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author tianlu
 */
public class PrintOrderDataMaker implements PrintDataMaker {


    private String qr;
    private int width;
    private int height;
    Context btService;

    public PrintOrderDataMaker(Context btService, String qr, int width, int height) {
        this.qr = qr;
        this.width = width;
        this.height = height;
        this.btService = btService;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();

        try {
            PrinterWriter printer;
            printer = (type == PrinterWriter58mm.TYPE_58) ? new PrinterWriter58mm(height) : new PrinterWriter80mm(height);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            data.add(printer.getDataAndReset());

            ArrayList<byte[]> image1 = printer.getImageByte(btService.getResources(), R.drawable.code);
            data.addAll(image1);

            printer.setAlignLeft();
            printer.printLine();
            printer.printLineFeed();
            printer.setFontSize(0);

            printer.print("TM-1234567890" + "          " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.print("This is test");
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();
            printer.print("From: " +"XXXX"+ "      " +"Tel: " +"XXXXXXXXX");
            printer.printLineFeed();
            printer.print("Addr: " + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            printer.printLineFeed();
            printer.printLineFeed();
            printer.print("To: " +"XXXX"+ "      " +"Tel: " +"XXXXXXXXXXX");
            printer.printLineFeed();
            printer.print("Addr: " + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.print("Memo：" + "Nothing");
            printer.printLineFeed();
            printer.feedPaperCutPartial();

            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


}
