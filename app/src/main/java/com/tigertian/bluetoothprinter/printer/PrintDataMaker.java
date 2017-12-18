package com.tigertian.bluetoothprinter.printer;

import java.util.List;

/**
 * @author tianlu
 */
public interface PrintDataMaker {
    List<byte[]> getPrintData(int type);
}
