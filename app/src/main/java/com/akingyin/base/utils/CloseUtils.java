package com.akingyin.base.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @ Description:
 * @ Author king
 * @ Date 2017/2/28 11:56
 * @ Version V1.0
 */

public class CloseUtils {

  /**
   * 关闭IO
   *
   * @param closeables closeable
   */
  public static void closeIO(Closeable... closeables) {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 安静关闭IO
   *
   * @param closeables closeable
   */
  public static void closeIOQuietly(Closeable... closeables) {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException ignored) {
        }
      }
    }
  }
}
