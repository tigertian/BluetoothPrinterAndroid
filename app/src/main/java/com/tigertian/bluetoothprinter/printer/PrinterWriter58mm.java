package com.tigertian.bluetoothprinter.printer;

import java.io.IOException;

/**
 * 58mm paper printer
 *
 * @author tianlu
 */
public class PrinterWriter58mm extends PrinterWriter {

    public static final int TYPE_58 = 58;
    public int width = 380;

    public PrinterWriter58mm() throws IOException {
    }

    public PrinterWriter58mm(int parting) throws IOException {
        super(parting);
    }

    public PrinterWriter58mm(int parting, int width) throws IOException {
        super(parting);
        this.width = width;
    }

    @Override
    protected int getLineWidth() {
        return 16;
    }

    @Override
    protected int getLineStringWidth(int textSize) {
        switch (textSize) {
            default:
            case 0:
                return 31;
            case 1:
                return 15;
        }
    }

    @Override
    protected int getDrawableMaxWidth() {
        return width;
    }
}
