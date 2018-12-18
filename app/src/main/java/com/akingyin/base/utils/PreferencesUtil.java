/*
 *
 *   Copyright (c) 2016 [akingyin@163.com]
 *
 *   Licensed under the Apache License, Version 2.0 (the "License”);
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.akingyin.base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;


/**
 * @ Description:
 *
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/5/25 18:22
 * @ Version V1.0
 */
public class PreferencesUtil {

  private   static   String    DefString="";
  private   static   float    DefFloat = 0;
  private   static   int      DefInt =0;
  private   static   long     DefLong=0;
  private   static   boolean  DefBoolean = false;



  private static  String   TAG = PreferencesUtil.class.getName();

  private static String defaultName = PreferencesUtil.class.getCanonicalName();

  private static SharedPreferences getPreferences(String name) {
    return KissTools.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
  }


  public static String getDefaultName() {
    return defaultName;
  }

  public static void setDefaultName(String name) {
    defaultName = name;
  }


  public static boolean get(String key, boolean defValue) {
    return get(defaultName, key, defValue);
  }
  public static boolean getBoolean(String key) {
    return get(defaultName, key, DefBoolean);
  }
  public static int get(String key, int defValue) {
    return get(defaultName, key, defValue);
  }

  public static int getInt(String key) {
    return get(defaultName, key, DefInt);
  }

  public static float get(String key, float defValue) {
    return get(defaultName, key, defValue);
  }
  public static float getFloat(String key) {
    return get(defaultName, key, DefFloat);
  }
  public static long get(String key, long defValue) {
    return get(defaultName, key, defValue);
  }

  public static String getString(String key) {
    return get(defaultName, key, DefString);
  }

  public static String get(String key, String defValue) {
    return get(defaultName, key, defValue);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static Set<String> get(String key, Set<String> defValue) {
    return get(defaultName, key, defValue);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  public static <C extends Serializable> C get(String key, C defValue) {
    return get(defaultName, key, defValue);
  }

  public static boolean get(String name, String key, boolean defValue) {
    return getPreferences(name).getBoolean(key, defValue);
  }

  public static int get(String name, String key, int defValue) {
    return getPreferences(name).getInt(key, defValue);
  }

  public static float get(String name, String key, float defValue) {
    return getPreferences(name).getFloat(key, defValue);
  }

  public static long get(String name, String key, long defValue) {
    return getPreferences(name).getLong(key, defValue);
  }

  public static String get(String name, String key, String defValue) {
    return getPreferences(name).getString(key, defValue);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static Set<String> get(String name, String key, Set<String> defValue) {
    return getPreferences(name).getStringSet(key, defValue);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  public static <C extends Serializable> C get(String name, String key, C defValue) {
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    C result = defValue;

    String value = getPreferences(name).getString(key, null);
    if (value != null) {
      try {
        byte[] decoded = Base64.decode(value.getBytes(), Base64.DEFAULT);
        bais = new ByteArrayInputStream(decoded);
        ois = new ObjectInputStream(bais);
        result = (C) ois.readObject();

      } catch (Exception e) {
        Log.e(TAG,e.getMessage(),e);
      } finally {
        if (ois != null) {
          try {
            ois.close();
          } catch (IOException e) {
            Log.e(TAG,e.getMessage(),e);
          }
        }
        if (bais != null) {
          try {
            bais.close();
          } catch (IOException e) {
            Log.e(TAG,e.getMessage(),e);
          }
        }
      }
    }

    return result;
  }


  public static void put(String key, boolean value) {
    put(defaultName, key, value);
  }

  public static void put(String key, int value) {
    put(defaultName, key, value);
  }

  public static void put(String key, float value) {
    put(defaultName, key, value);
  }

  public static void put(String key, long value) {
    put(defaultName, key, value);
  }

  public static void put(String key, String value) {
    put(defaultName, key, value);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static void put(String key, Set<String> value) {
    put(defaultName, key, value);
  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  public static <C extends Serializable> void put(String key, C value) {
    put(defaultName, key, value);
  }

  public static void put(String name, String key, boolean value) {

      getPreferences(name).edit().putBoolean(key, value).apply();

  }

  public static void put(String name, String key, int value) {

      getPreferences(name).edit().putInt(key, value).apply();

  }

  public static void put(String name, String key, float value) {

      getPreferences(name).edit().putFloat(key, value).apply();

  }

  public static void put(String name, String key, long value) {

      getPreferences(name).edit().putLong(key, value).apply();

  }

  public static void put(String name, String key, String value) {

      getPreferences(name).edit().putString(key, value).apply();

  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static void put(String name, String key, Set<String> value) {

      getPreferences(name).edit().putStringSet(key, value).apply();

  }

  @TargetApi(Build.VERSION_CODES.FROYO)
  public static <C extends Serializable> void put(String name, String key, C value) {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream oos = null;

    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      oos.writeObject(value);
      byte[] encoded = Base64.encode(baos.toByteArray(), Base64.DEFAULT);

        getPreferences(name).edit().putString(key, new String(encoded)).apply();


    } catch (IOException e) {
      Log.e(TAG,e.getMessage(),e);
      throw new RuntimeException(e);

    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (IOException e) {
          Log.e(TAG,e.getMessage(),e);
        }
      }
      if (baos != null) {
        try {
          baos.close();
        } catch (IOException e) {
          Log.e(TAG,e.getMessage(),e);
        }
      }
    }
  }


  public static void remove(String key) {
    remove(defaultName, key);
  }

  public static void remove(String name, String key) {
      getPreferences(name).edit().remove(key).apply();
  }


  public static void clear() {
    clear(defaultName);
  }

  public static void clear(String name) {

      getPreferences(name).edit().clear().apply();

  }
}
