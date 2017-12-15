package com.tigertian.bluetoothprinter.printer;

import java.util.List;

public interface PrintDataMaker {
    List<byte[]> getPrintData(int type);
}
