package com.akingyin.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Description:
 * @ Author king
 * @ Date 2017/9/4 17:33
 * @ Version V1.0
 */

public final class AppUtils {

  private AppUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 判断App是否安装
   *
   * @param packageName 包名
   * @return {@code true}: 已安装<br>{@code false}: 未安装
   */
  public static boolean isInstallApp(final String packageName) {
    return !isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null;
  }

  /**
   * 安装App(支持7.0)
   *
   * @param filePath  文件路径
   * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
   *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
   */
  public static void installApp(final String filePath, final String authority) {
    installApp(new File(filePath), authority);
  }

  /**
   * 安装App（支持7.0）
   *
   * @param file      文件
   * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
   *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
   */
  public static void installApp(final File file, final String authority) {
    if (null == file) {
      return;
    }
    if (!FileUtils.isFileExist(file.getAbsolutePath())) {
      return;
    }
    Utils.getApp().startActivity(IntentUtils.getInstallAppIntent(file, authority));
  }

  /**
   * 安装App（支持6.0）
   *
   * @param activity    activity
   * @param filePath    文件路径
   * @param authority   7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
   *                    <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
   * @param requestCode 请求值
   */
  public static void installApp(final Activity activity, final String filePath, final String authority, final int requestCode) {
    installApp(activity, new File(filePath), authority, requestCode);
  }

  /**
   * 安装App(支持6.0)
   *
   * @param activity    activity
   * @param file        文件
   * @param authority   7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
   *                    <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
   * @param requestCode 请求值
   */
  public static void installApp(final Activity activity, final File file, final String authority,
      final int requestCode) {
    if (null == file || !file.exists()) {
      return;
    }
    activity.startActivityForResult(IntentUtils.getInstallAppIntent(file, authority), requestCode);
  }

  /**
   * 卸载App
   *
   * @param packageName 包名
   */
  public static void uninstallApp(final String packageName) {
    if (isSpace(packageName)) {
      return;
    }
    Utils.getApp().startActivity(IntentUtils.getUninstallAppIntent(packageName));
  }

  /**
   * 卸载App
   *
   * @param activity    activity
   * @param packageName 包名
   * @param requestCode 请求值
   */
  public static void uninstallApp(final Activity activity, final String packageName, final int requestCode) {
    if (isSpace(packageName)) {
      return;
    }
    activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode);
  }

  /**
   * 打开App
   *
   * @param packageName 包名
   */
  public static void launchApp(final String packageName) {
    if (isSpace(packageName)) {
      return;
    }
    Utils.getApp().startActivity(IntentUtils.getLaunchAppIntent(packageName));
  }

  /**
   * 打开App
   *
   * @param activity    activity
   * @param packageName 包名
   * @param requestCode 请求值
   */
  public static void launchApp(final Activity activity, final String packageName, final int requestCode) {
    if (isSpace(packageName)) {
      return;
    }
    activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode);
  }

  /**
   * 关闭App
   */
  public static void exitApp() {
    List<Activity> activityList = Utils.sActivityList;
    for (int i = activityList.size() - 1; i >= 0; --i) {
      activityList.get(i).finish();
      activityList.remove(i);
    }
    System.exit(0);
  }

  /**
   * 获取App具体设置
   */
  public static void getAppDetailsSettings() {
    getAppDetailsSettings(Utils.getApp().getPackageName());
  }

  /**
   * 获取App具体设置
   *
   * @param packageName 包名
   */
  public static void getAppDetailsSettings(final String packageName) {
    if (isSpace(packageName)) {
      return;
    }
    Utils.getApp().startActivity(IntentUtils.getAppDetailsSettingsIntent(packageName));
  }

  /**
   * 获取App图标
   *
   * @return App图标
   */
  public static Drawable getAppIcon() {
    return getAppIcon(Utils.getApp().getPackageName());
  }

  /**
   * 获取App图标
   *
   * @param packageName 包名
   * @return App图标
   */
  public static Drawable getAppIcon(final String packageName) {
    if (isSpace(packageName)) {
      return null;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.applicationInfo.loadIcon(pm);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取App路径
   *
   * @return App路径
   */
  public static String getAppPath() {
    return getAppPath(Utils.getApp().getPackageName());
  }

  /**
   * 获取App路径
   *
   * @param packageName 包名
   * @return App路径
   */
  public static String getAppPath(final String packageName) {
    if (isSpace(packageName)) {
      return null;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.applicationInfo.sourceDir;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取App版本号
   *
   * @return App版本号
   */
  public static String getAppVersionName() {
    return getAppVersionName(Utils.getApp().getPackageName());
  }

  /**
   * 获取App版本号
   *
   * @param packageName 包名
   * @return App版本号
   */
  public static String getAppVersionName(final String packageName) {
    if (isSpace(packageName)) {
      return null;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取App版本码
   *
   * @return App版本码
   */
  public static int getAppVersionCode() {
    return getAppVersionCode(Utils.getApp().getPackageName());
  }

  /**
   * 获取App版本码
   *
   * @param packageName 包名
   * @return App版本码
   */
  public static int getAppVersionCode(final String packageName) {
    if (isSpace(packageName)) {
      return -1;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? -1 : pi.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * 判断App是否是系统应用
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isSystemApp() {
    return isSystemApp(Utils.getApp().getPackageName());
  }

  /**
   * 判断App是否是系统应用
   *
   * @param packageName 包名
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isSystemApp(final String packageName) {
    if (isSpace(packageName)) {
      return false;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
      return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 判断App是否是Debug版本
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isAppDebug() {
    return isAppDebug(Utils.getApp().getPackageName());
  }

  /**
   * 判断App是否是Debug版本
   *
   * @param packageName 包名
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isAppDebug(final String packageName) {
    if (isSpace(packageName)) {
      return false;
    }
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
      return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 判断App是否处于前台
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isAppForeground() {
    ActivityManager manager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
    if (info == null || info.size() == 0) {
      return false;
    }
    for (ActivityManager.RunningAppProcessInfo aInfo : info) {
      if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
        return aInfo.processName.equals(Utils.getApp().getPackageName());
      }
    }
    return false;
  }

  /**
   * 封装App信息的Bean类
   */
  public static class AppInfo {

    private String name;
    private Drawable icon;
    private String packageName;
    private String packagePath;
    private String versionName;
    private int versionCode;
    private boolean isSystem;

    public Drawable getIcon() {
      return icon;
    }

    public void setIcon(final Drawable icon) {
      this.icon = icon;
    }

    public boolean isSystem() {
      return isSystem;
    }

    public void setSystem(final boolean isSystem) {
      this.isSystem = isSystem;
    }

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public String getPackageName() {
      return packageName;
    }

    public void setPackageName(final String packageName) {
      this.packageName = packageName;
    }

    public String getPackagePath() {
      return packagePath;
    }

    public void setPackagePath(final String packagePath) {
      this.packagePath = packagePath;
    }

    public int getVersionCode() {
      return versionCode;
    }

    public void setVersionCode(final int versionCode) {
      this.versionCode = versionCode;
    }

    public String getVersionName() {
      return versionName;
    }

    public void setVersionName(final String versionName) {
      this.versionName = versionName;
    }

    /**
     * @param name        名称
     * @param icon        图标
     * @param packageName 包名
     * @param packagePath 包路径
     * @param versionName 版本号
     * @param versionCode 版本码
     * @param isSystem    是否系统应用
     */
    public AppInfo(String packageName, String name, Drawable icon, String packagePath,
        String versionName, int versionCode, boolean isSystem) {
      this.setName(name);
      this.setIcon(icon);
      this.setPackageName(packageName);
      this.setPackagePath(packagePath);
      this.setVersionName(versionName);
      this.setVersionCode(versionCode);
      this.setSystem(isSystem);
    }

    @Override public String toString() {
      return "pkg name: "
          + getPackageName()
          + "\napp name: "
          + getName()
          + "\napp path: "
          + getPackagePath()
          + "\napp v name: "
          + getVersionName()
          + "\napp v code: "
          + getVersionCode()
          + "\nis system: "
          + isSystem();
    }
  }

  /**
   * 获取App信息
   * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）</p>
   *
   * @return 当前应用的AppInfo
   */
  public static AppInfo getAppInfo() {
    return getAppInfo(Utils.getApp().getPackageName());
  }

  /**
   * 获取App信息
   * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）</p>
   *
   * @param packageName 包名
   * @return 当前应用的AppInfo
   */
  public static AppInfo getAppInfo(final String packageName) {
    try {
      PackageManager pm = Utils.getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return getBean(pm, pi);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 得到AppInfo的Bean
   *
   * @param pm 包的管理
   * @param pi 包的信息
   * @return AppInfo类
   */
  private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
    if (pm == null || pi == null) {
      return null;
    }
    ApplicationInfo ai = pi.applicationInfo;
    String packageName = pi.packageName;
    String name = ai.loadLabel(pm).toString();
    Drawable icon = ai.loadIcon(pm);
    String packagePath = ai.sourceDir;
    String versionName = pi.versionName;
    int versionCode = pi.versionCode;
    boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
    return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
  }

  /**
   * 获取所有已安装App信息
   * <p>{@link #getBean(PackageManager, PackageInfo)}（名称，图标，包名，包路径，版本号，版本Code，是否系统应用）</p>
   * <p>依赖上面的getBean方法</p>
   *
   * @return 所有已安装的AppInfo列表
   */
  public static List<AppInfo> getAppsInfo() {
    List<AppInfo> list = new ArrayList<>();
    PackageManager pm = Utils.getApp().getPackageManager();
    // 获取系统中安装的所有软件信息
    List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
    for (PackageInfo pi : installedPackages) {
      AppInfo ai = getBean(pm, pi);
      if (ai == null) {
        continue;
      }
      list.add(ai);
    }
    return list;
  }

  /**
   * 清除App所有数据
   *
   * @param dirPaths 目录路径
   * @return {@code true}: 成功<br>{@code false}: 失败
   */
  public static boolean cleanAppData(final String... dirPaths) {
    File[] dirs = new File[dirPaths.length];
    int i = 0;
    for (String dirPath : dirPaths) {
      dirs[i++] = new File(dirPath);
    }
    return cleanAppData(dirs);
  }

  /**
   * 清除App所有数据
   *
   * @param dirs 目录
   * @return {@code true}: 成功<br>{@code false}: 失败
   */
  public static boolean cleanAppData(final File... dirs) {
    boolean isSuccess = CleanUtils.cleanInternalCache();
    isSuccess &= CleanUtils.cleanInternalDbs();
    isSuccess &= CleanUtils.cleanInternalSP();
    isSuccess &= CleanUtils.cleanInternalFiles();
    isSuccess &= CleanUtils.cleanExternalCache();
    for (File dir : dirs) {
      isSuccess &= CleanUtils.cleanCustomCache(dir);
    }
    return isSuccess;
  }

  private static boolean isSpace(final String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @SuppressLint("MissingPermission")
  public static String getImei(Context context) {
    try {
      TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

      return telephonyManager.getDeviceId();

    }catch (Exception e){
      e.printStackTrace();

    }
     return  null;
  }
}
