package com.akingyin.base.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import java.text.DecimalFormat;

/**
 * 位置相关工具
 * @ Description:
 * @ author king
 * @ Date 2017/8/11 12:43
 * @ Version V1.0
 */

public class LocationUtils {

  public static double pi = 3.1415926535897932384626;
  public static double a = 6378245.0;
  public static double ee = 0.00669342162296594323;



  /**
   * 判断Gps是否可用
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isGpsEnabled(Context context) {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }


  /**
   * 判断定位是否可用
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isLocationEnabled(Context context) {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  /**
   * 打开Gps设置界面
   */
  public static void openGpsSettings(Context context) {
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }


  /**
   * GPS坐标 转换成 角度
   * 例如 113.202222 转换成 113°12′8″
   *
   * @param location
   * @return
   */
  public static String gpsToDegree(double location) {
    double degree = Math.floor(location);
    double minuteTemp = (location - degree) * 60;
    double minute = Math.floor(minuteTemp);

    String second = new DecimalFormat("#.##").format((minuteTemp - minute) * 60);
    return (int) degree + "°" + (int) minute + "′" + second + "″";
  }

  /**
   * 国际 GPS84 坐标系
   * 转换成
   * [国测局坐标系] 火星坐标系 (GCJ-02)
   * <p>
   * World Geodetic System ==> Mars Geodetic System
   *
   * @param lon 经度
   * @param lat 纬度
   * @return GPS实体类
   */
  public static Gps GPS84ToGCJ02(double lat, double lon) {
    if (outOfChina(lat, lon)) {
      return null;
    }
    double dLat = transformLat(lon - 105.0, lat - 35.0);
    double dLon = transformLon(lon - 105.0, lat - 35.0);
    double radLat = lat / 180.0 * pi;
    double magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    double sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    double mgLat = lat + dLat;
    double mgLon = lon + dLon;
    return new Gps(mgLat, mgLon);
  }

  /**
   * [国测局坐标系] 火星坐标系 (GCJ-02)
   * 转换成
   * 国际 GPS84 坐标系
   *
   * @param lon 火星经度
   * @param lat 火星纬度
   */
  public static Gps GCJ02ToGPS84(double lat, double lon) {
    Gps gps = transform(lat, lon);
    double lontitude = lon * 2 - gps.getWgLon();
    double latitude = lat * 2 - gps.getWgLat();
    return new Gps(latitude, lontitude);
  }

  /**
   * 火星坐标系 (GCJ-02)
   * 转换成
   * 百度坐标系 (BD-09)
   *
   * @param gg_lon 经度
   * @param gg_lat 纬度
   */
  public static Gps GCJ02ToBD09(double gg_lat, double gg_lon) {
    double x = gg_lon, y = gg_lat;
    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
    double bd_lon = z * Math.cos(theta) + 0.0065;
    double bd_lat = z * Math.sin(theta) + 0.006;
    return new Gps(bd_lat, bd_lon);
  }

  /**
   * 百度坐标系 (BD-09)
   * 转换成
   * 火星坐标系 (GCJ-02)
   *
   * @param bd_lon 百度*经度
   * @param bd_lat 百度*纬度
   * @return GPS实体类
   */
  public static Gps BD09ToGCJ02(double bd_lat, double bd_lon) {
    double x = bd_lon - 0.0065, y = bd_lat - 0.006;
    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
    double gg_lon = z * Math.cos(theta);
    double gg_lat = z * Math.sin(theta);
    return new Gps(gg_lat, gg_lon);
  }

  /**
   * 百度坐标系 (BD-09)
   * 转换成
   * 国际 GPS84 坐标系
   *
   * @param bd_lon 百度*经度
   * @param bd_lat 百度*纬度
   * @return GPS实体类
   */
  public static Gps BD09ToGPS84(double bd_lat, double bd_lon) {
    Gps gcj02 = BD09ToGCJ02(bd_lat, bd_lon);
    Gps map84 = GCJ02ToGPS84(gcj02.getWgLat(),
        gcj02.getWgLon());
    return map84;

  }

  /**
   * 不在中国范围内
   *
   * @param lon 经度
   * @param lat 纬度
   * @return boolean值
   */
  public static boolean outOfChina(double lat, double lon) {
    if (lon < 72.004 || lon > 137.8347) {
      return true;
    }
    return lat < 0.8293 || lat > 55.8271;
  }

  /**
   * 转化算法
   *
   * @param lon
   * @param lat
   * @return
   */
  public static Gps transform(double lat, double lon) {
    if (outOfChina(lat, lon)) {
      return new Gps(lat, lon);
    }
    double dLat = transformLat(lon - 105.0, lat - 35.0);
    double dLon = transformLon(lon - 105.0, lat - 35.0);
    double radLat = lat / 180.0 * pi;
    double magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    double sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    double mgLat = lat + dLat;
    double mgLon = lon + dLon;
    return new Gps(mgLat, mgLon);
  }

  /**
   * 纬度转化算法
   *
   * @param x
   * @param y
   * @return
   */
  public static double transformLat(double x, double y) {
    double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
        + 0.2 * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
    return ret;
  }

  /**
   * 经度转化算法
   *
   * @param x
   * @param y
   * @return
   */
  public static double transformLon(double x, double y) {
    double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
        * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
        * pi)) * 2.0 / 3.0;
    return ret;
  }


  public  static class   Gps{

    private    double  wgLat;

    private    double  wgLon;

    public double getWgLat() {
      return wgLat;
    }

    public void setWgLat(double wgLat) {
      this.wgLat = wgLat;
    }

    public double getWgLon() {
      return wgLon;
    }

    public void setWgLon(double wgLon) {
      this.wgLon = wgLon;
    }

    public Gps(double wgLat, double wgLon) {
      this.wgLat = wgLat;
      this.wgLon = wgLon;
    }
  }
}
