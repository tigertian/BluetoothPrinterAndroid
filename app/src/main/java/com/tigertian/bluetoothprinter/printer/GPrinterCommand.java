package com.tigertian.bluetoothprinter.printer;

/**
 * Printer commands
 */
public class GPrinterCommand {

    /**
     * align left
     */
    public static final byte[] left = new byte[]{0x1b, 0x61, 0x00};
    /**
     * align center
     */
    public static final byte[] center = new byte[]{0x1b, 0x61, 0x01};
    /**
     * align right
     */
    public static final byte[] right = new byte[]{0x1b, 0x61, 0x02};
    /**
     * bold text
     */
    public static final byte[] bold = new byte[]{0x1b, 0x45, 0x01};
    /**
     * cancel bold text
     */
    public static final byte[] bold_cancel = new byte[]{0x1b, 0x45, 0x00};
    /**
     * normal size
     */
    public static final byte[] text_normal_size = new byte[]{0x1d, 0x21, 0x00};
    /**
     * double height text
     */
    public static final byte[] text_big_height = new byte[]{0x1b, 0x21, 0x10};
    /**
     * double size
     */
    public static final byte[] text_big_size = new byte[]{0x1d, 0x21, 0x11};
    /**
     * reset the printer
     */
    public static final byte[] reset = new byte[]{0x1b, 0x40};
    /**
     * print the text and line feed
     */
    public static final byte[] print = new byte[]{0x0a};
    /**
     * under line the text
     */
    public static final byte[] under_line = new byte[]{0x1b, 0x2d, 2};
    /**
     * cancel the underline
     */
    public static final byte[] under_line_cancel = new byte[]{0x1b, 0x2d, 0};

    /**
     * Walk paper
     *
     * @param n line count
     * @return command
     */
    public static byte[] walkPaper(byte n) {
        return new byte[]{0x1b, 0x64, n};
    }

    /**
     * Move to x and y
     *
     * @param x
     * @param y
     * @return command
     */
    public static byte[] move(byte x, byte y) {
        return new byte[]{0x1d, 0x50, x, y};
    }

}
