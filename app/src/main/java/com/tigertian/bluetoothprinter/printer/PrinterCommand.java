package com.tigertian.bluetoothprinter.printer;

/**
 * Printer commands
 * @author tianlu
 */
public class PrinterCommand {

    /**
     * align LEFT
     */
    public static final byte[] LEFT = new byte[]{0x1b, 0x61, 0x00};
    /**
     * align CENTER
     */
    public static final byte[] CENTER = new byte[]{0x1b, 0x61, 0x01};
    /**
     * align RIGHT
     */
    public static final byte[] RIGHT = new byte[]{0x1b, 0x61, 0x02};
    /**
     * BOLD text
     */
    public static final byte[] BOLD = new byte[]{0x1b, 0x45, 0x01};
    /**
     * cancel BOLD text
     */
    public static final byte[] BOLD_CANCEL = new byte[]{0x1b, 0x45, 0x00};
    /**
     * normal size
     */
    public static final byte[] TEXT_NORMAL_SIZE = new byte[]{0x1d, 0x21, 0x00};
    /**
     * double height text
     */
    public static final byte[] TEXT_BIG_HEIGHT = new byte[]{0x1b, 0x21, 0x10};
    /**
     * double size
     */
    public static final byte[] TEXT_BIG_SIZE = new byte[]{0x1d, 0x21, 0x11};
    /**
     * RESET the printer
     */
    public static final byte[] RESET = new byte[]{0x1b, 0x40};
    /**
     * PRINT the text and line feed
     */
    public static final byte[] PRINT = new byte[]{0x0a};
    /**
     * under line the text
     */
    public static final byte[] UNDER_LINE = new byte[]{0x1b, 0x2d, 2};
    /**
     * cancel the underline
     */
    public static final byte[] UNDER_LINE_CANCEL = new byte[]{0x1b, 0x2d, 0};

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
