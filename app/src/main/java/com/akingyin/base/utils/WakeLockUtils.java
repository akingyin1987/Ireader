package com.akingyin.base.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 *  屏幕长亮控制工具
 *  另外WakeLock的设置是 Activiy 级别的，不是针对整个Application应用的。
 *  可以在activity的onResume方法里面操作WakeLock,  在onPause方法里面释放。
 *  要进行电源的操作需要在AndroidManifest.xml中声明该应用有设置电源管理的权限。
 *  <uses-permission android:name="android.permission.WAKE_LOCK" />
 *  你可能还需要
 *  <uses-permission android:name="android.permission.DEVICE_POWER" />
 * @ Description:
 * @ Author king
 * @ Date 2017/2/28 10:46
 * @ Version V1.0
 */

public class WakeLockUtils {

  /**
   * The Wake lock.
   */
  private static PowerManager.WakeLock wakeLock;

  private WakeLockUtils() {
    throw new UnsupportedOperationException(
        "Should not create instance of Util class. Please use as static..");
  }

  /**
   * Hold wake lock.
   *
   * @param context
   *     the context
   */
  public static void holdWakeLock(Context context) {
    PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
    wakeLock.acquire();
  }

  /**
   * Release wake lock.
   */
  public static void releaseWakeLock() {
    if (wakeLock != null && wakeLock.isHeld()) {
      wakeLock.release();
    }
  }
}
