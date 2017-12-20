package com.tigertian.bluetoothprinter.printer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public abstract class PrinterWriter {

    public static final int HEIGHT_PARTING_DEFAULT = 255;
    private static final String CHARSET = "GB2312";
    private ByteArrayOutputStream bos;
    private int heightParting;

    public PrinterWriter() throws IOException {
        this(HEIGHT_PARTING_DEFAULT);
    }

    public PrinterWriter(int parting) throws IOException {
        if (parting <= 0 || parting > HEIGHT_PARTING_DEFAULT)
            heightParting = HEIGHT_PARTING_DEFAULT;
        else
            heightParting = parting;
        initAndReset();
    }

    /**
     * initialize and RESET
     *
     * @throws IOException
     */
    public void initAndReset() throws IOException {
        bos = new ByteArrayOutputStream();
        write(PrinterUtils.initPrinter());
    }

    /**
     * get the data that needs to be printed and RESET the stream
     *
     * @return
     * @throws IOException 异常
     */
    public byte[] getDataAndReset() throws IOException {
        byte[] data;
        bos.flush();
        data = bos.toByteArray();
        bos.reset();
        return data;
    }

    /**
     * Get the data that needs to be printed and close the stream
     *
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public byte[] getDataAndClose() throws IOException {
        byte[] data;
        bos.flush();
        data = bos.toByteArray();
        bos.close();
        bos = null;
        return data;
    }

    /**
     * Write the data to the stream
     *
     * @param data
     * @throws IOException
     */
    public void write(byte[] data) throws IOException {
        if (bos == null)
            initAndReset();
        bos.write(data);
    }


    public void setAlignCenter() throws IOException {
        write(PrinterUtils.alignCenter());
    }

    public void setAlignLeft() throws IOException {
        write(PrinterUtils.alignLeft());
    }

    public void setAlignRight() throws IOException {
        write(PrinterUtils.alignRight());
    }

    /**
     * make text BOLD
     *
     * @throws IOException
     */
    public void setEmphasizedOn() throws IOException {
        write(PrinterUtils.emphasizedOn());
    }

    public void setEmphasizedOff() throws IOException {
        write(PrinterUtils.emphasizedOff());
    }


    public void setFontSize(int size) throws IOException {
        write(PrinterUtils.fontSizeSetBig(size));
    }

    /**
     * Set the line height between 0 and 255
     *
     * @param height
     * @throws IOException
     */
    public void setLineHeight(int height) throws IOException {
        if (height >= 0 && height <= 255)
            write(PrinterUtils.printLineHeight((byte) height));
    }

    /**
     * write the text to PRINT stream
     *
     * @param string
     * @throws IOException
     */
    public void print(String string) throws IOException {
        print(string, CHARSET);
    }

    /**
     * write the text to PRINT stream
     *
     * @param string
     * @param charsetName
     * @throws IOException
     */
    public void print(String string, String charsetName) throws IOException {
        if (string == null)
            return;
        write(string.getBytes(charsetName));
    }

    /**
     * PRINT the dotted line
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
        int length = getLineWidth();
        String line = "";
        while (length > 0) {
            line += "- ";
            length--;
        }
        print(line);
    }

    /**
     * the line width
     *
     * @return
     */
    protected abstract int getLineWidth();

    /**
     * the width of line of strings
     *
     * @param textSize
     * @return
     */
    protected abstract int getLineStringWidth(int textSize);

    private int getStringWidth(String str) {
        int width = 0;
        for (char c : str.toCharArray()) {
            width += isChinese(c) ? 2 : 1;
        }
        return width;
    }

    /**
     * Convert the image res to bytes
     *
     * @param res Resources
     * @param id  res id
     * @return data bytes
     */
    public ArrayList<byte[]> getImageByte(Resources res, int id) {
        int maxWidth = getDrawableMaxWidth();
        Bitmap image = scalingBitmap(res, id, maxWidth);
        if (image == null)
            return null;
        ArrayList<byte[]> data = PrinterUtils.decodeBitmapToDataList(image, heightParting);
        image.recycle();
        return data;
    }

    /**
     * get the max width of the image
     *
     * @return
     */
    protected abstract int getDrawableMaxWidth();

    /**
     * scale image
     *
     * @param res
     * @param id       res id
     * @param maxWidth
     * @return the scaled image
     */
    private Bitmap scalingBitmap(Resources res, int id, int maxWidth) {
        if (res == null)
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        options.inJustDecodeBounds = false;
        // 粗略缩放
        if (maxWidth > 0 && options.outWidth > maxWidth) {
            // 超过限定宽
            double ratio = options.outWidth / (double) maxWidth;
            int sampleSize = (int) Math.floor(ratio);
            if (sampleSize > 1) {
                options.inSampleSize = sampleSize;
            }
        }
        try {
            Bitmap image = BitmapFactory.decodeResource(res, id, options);
            final int width = image.getWidth();
            final int height = image.getHeight();
            // 精确缩放
            if (maxWidth <= 0 || width <= maxWidth) {
                return image;
            }
            final float scale = maxWidth / (float) width;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizeImage = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
            image.recycle();
            return resizeImage;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public ArrayList<byte[]> getImageByte(Drawable drawable) {
        int maxWidth = getDrawableMaxWidth();
        Bitmap image = scalingDrawable(drawable, maxWidth);
        if (image == null)
            return null;
        ArrayList<byte[]> data = PrinterUtils.decodeBitmapToDataList(image, heightParting);
        image.recycle();
        return data;
    }

    private Bitmap scalingDrawable(Drawable drawable, int maxWidth) {
        if (drawable == null || drawable.getIntrinsicWidth() == 0
                || drawable.getIntrinsicHeight() == 0)
            return null;
        final int width = drawable.getIntrinsicWidth();
        final int height = drawable.getIntrinsicHeight();
        try {
            Bitmap image = Bitmap.createBitmap(width, height,
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(image);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            // 精确缩放
            if (maxWidth <= 0 || width <= maxWidth) {
                return image;
            }
            final float scale = maxWidth / (float) width;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizeImage = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
            image.recycle();
            return resizeImage;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    public ArrayList<byte[]> getImageByte(Bitmap image) {
        int maxWidth = getDrawableMaxWidth();
        Bitmap scalingImage = scalingBitmap(image, maxWidth);
        if (scalingImage == null)
            return null;
        ArrayList<byte[]> data = PrinterUtils.decodeBitmapToDataList(image, heightParting);
        image.recycle();
        return data;
    }

    private Bitmap scalingBitmap(Bitmap image, int maxWidth) {
        if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0)
            return null;
        try {
            final int width = image.getWidth();
            final int height = image.getHeight();
            // 精确缩放
            float scale = 1;
            if (maxWidth <= 0 || width <= maxWidth) {
                scale = maxWidth / (float) width;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            return Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public ArrayList<byte[]> getImageByte(String filePath) {
        Bitmap image;
        try {
            int width;
            int height;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            width = options.outWidth;
            height = options.outHeight;
            if (width <= 0 || height <= 0)
                return null;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError | Exception e) {
            return null;
        }
        return getImageByte(image);
    }

    public void printLineFeed() throws IOException {
        write(PrinterUtils.printLineFeed());
    }

    public void feedPaperCut() throws IOException {
        write(PrinterUtils.feedPaperCut());
    }

    public void feedPaperCutPartial() throws IOException {
        write(PrinterUtils.feedPaperCutPartial());
    }

    /**
     * set the height parting of image. The max is 255 px
     *
     * @param parting
     */
    public void setHeightParting(int parting) {
        if (parting <= 0 || parting > HEIGHT_PARTING_DEFAULT)
            return;
        heightParting = parting;
    }

    public int getHeightParting() {
        return heightParting;
    }

    /**
     * Check if it is chinese
     * GENERAL_PUNCTUATION=“
     * CJK_SYMBOLS_AND_PUNCTUATION=。
     * HALFWIDTH_AND_FULLWIDTH_FORMS=，
     *
     * @param c char
     * @return result
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
}
