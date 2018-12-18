package com.akingyin.base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 *  数据转换工具类
 * @ Description:
 * @ Author king
 * @ Date 2017/2/28 10:52
 * @ Version V1.0
 */

public class ConvertUtils {

  private static final char[] hexDigits =
      { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  /**
   * KB与Byte的倍数
   */
  public static final int KB = 1024;
  /**
   * MB与Byte的倍数
   */
  public static final int MB = 1048576;
  /**
   * GB与Byte的倍数
   */
  public static final int GB = 1073741824;
  private ConvertUtils() {
    throw new UnsupportedOperationException(
        "Should not create instance of Util class. Please use as static..");
  }

  /**
   * short array to byte array.
   *
   * @param sData
   *     the s data
   * @return the byte []
   */
  public static byte[] short2byte(short[] sData) {
    int shortArrsize = sData.length;
    byte[] bytes = new byte[shortArrsize * 2];
    for (int i = 0; i < shortArrsize; i++) {
      bytes[i * 2] = (byte) (sData[i] & 0x00FF);
      bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
      sData[i] = 0;
    }
    return bytes;
  }

  /**
   * 短整型与字节的转换
   */
  public  static byte[] shortToByte(short number) {
    int temp = number;
    byte[] b = new byte[2];
    for (int i = 0; i < b.length; i++) {
      // 将最低位保存在最低位
      b[i] = new Integer(temp & 0xff).byteValue();
      // 向右移8位
      temp = temp >> 8;
    }
    return b;
  }



  public  static byte[] encodeBytes(byte[] source, char split) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
    for (byte b : source) {
      if (b < 0) {
        b += 256;
      }
      bos.write(split);
      char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
      char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
      bos.write(hex1);
      bos.write(hex2);
    }
    return bos.toByteArray();
  }


  /**
   * byteArr转hexString
   * <p>例如：</p>
   * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
   *
   * @param src 字节数组
   * @return 16进制大写字符串
   */
  public static String bytesToHexString(byte[] src){
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString().toUpperCase();
  }

  /**
   * 倒序排列
   * @param src
   * @return
   */
  public  static  String bytes2HexStrReverse(byte[]  src){
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = src.length-1; i >=0; i--) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString().toUpperCase();
  }

  /**
   * hexString转byteArr
   * <p>例如：</p>
   * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
   *
   * @param hexString 十六进制字符串
   * @return 字节数组
   */
  public static byte[] hexString2Bytes(String hexString) {
    if (StringUtils.isEmpty(hexString)) {
      return null;
    }
    int len = hexString.length();
    if (len % 2 != 0) {
      hexString = "0" + hexString;
      len = len + 1;
    }
    char[] hexBytes = hexString.toUpperCase().toCharArray();
    byte[] ret = new byte[len >> 1];
    for (int i = 0; i < len; i += 2) {
      ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
    }
    return ret;
  }


  /**
   * hexChar转int
   *
   * @param hexChar hex单个字节
   * @return 0..15
   */
  private static int hex2Dec(char hexChar) {
    if (hexChar >= '0' && hexChar <= '9') {
      return hexChar - '0';
    } else if (hexChar >= 'A' && hexChar <= 'F') {
      return hexChar - 'A' + 10;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * inputStream转outputStream
   *
   * @param is 输入流
   * @return outputStream子类
   */
  public static ByteArrayOutputStream input2OutputStream(InputStream is) {
    if (is == null) {
      return null;
    }
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] b = new byte[KB];
      int len;
      while ((len = is.read(b, 0,KB)) != -1) {
        os.write(b, 0, len);
      }
      return os;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      CloseUtils.closeIO(is);
    }
  }


  /**
   * 字节数转合适内存大小
   * <p>保留3位小数</p>
   *
   * @param byteNum 字节数
   * @return 合适内存大小
   */

  public static String LongToFitMemorySize(long byteNum) {
    if (byteNum < 0) {
      return "shouldn't be less than zero!";
    } else if (byteNum < KB) {
      return String.format(Locale.getDefault(),"%.1fB", byteNum*1F );
    } else if (byteNum < MB) {
      return String.format(Locale.getDefault(),"%.1fKB", byteNum / KB*1F );
    } else if (byteNum < GB) {
      return String.format(Locale.getDefault(),"%.1fMB", byteNum / MB*1F );
    } else {
      return String.format(Locale.getDefault(),"%.1fGB", byteNum / GB + 0.0005);
    }
  }

  /**
   * Dp to pixels
   *
   * @param context
   *     the context
   * @param dp
   *     the dp
   * @return the int
   */
  public static int dpToPixels(Activity context, int dp) {
    DisplayMetrics metrics = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    return (int) ((dp * metrics.density) + 0.5);
  }

  /**
   * Pixels to dp int.
   *
   * @param context
   *     the context
   * @param px
   *     the px
   * @return the int
   */
  public static int pixelsToDp(Activity context, int px) {
    DisplayMetrics metrics = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    return (int) ((px / metrics.density) + 0.5);
  }

  /**
   * Sp to px
   *
   * @param context
   *     the context
   * @param sp
   *     the sp
   * @return the int
   */
  public static int spToPx(Context context, float sp) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (sp * fontScale + 0.5f);
  }

  /**
   * Px to sp
   *
   * @param context
   *     the context
   * @param px
   *     the px
   * @return the int
   */
  public static int pxToSp(Context context, float px) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (px / fontScale + 0.5f);
  }


}
