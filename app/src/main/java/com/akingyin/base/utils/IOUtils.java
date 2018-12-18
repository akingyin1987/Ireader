package com.akingyin.base.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @ Description:
 *
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/10/14 10:55
 * @ Version V1.0
 */

public class IOUtils {

  private IOUtils() {
    throw new AssertionError();
  }


  /**
   * Close closable object and wrap {@link IOException} with {@link RuntimeException}
   * @param closeable closeable object
   */
  public static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        throw new RuntimeException("IOException occurred. ", e);
      }
    }
  }

  /**
   * Close closable and hide possible {@link IOException}
   * @param closeable closeable object
   */
  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        // Ignored
      }
    }
  }

}
