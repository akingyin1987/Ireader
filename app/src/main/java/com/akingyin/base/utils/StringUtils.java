package com.akingyin.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ Description:
 *
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/10/14 10:50
 * @ Version V1.0
 */

public class StringUtils {

  public   static   String   DEFAULT_EMPTY="";

  public   static DecimalFormat  df = new DecimalFormat("0");

  private StringUtils() {
    throw new AssertionError();
  }

  /**
   * is null or its length is 0 or it is made by space
   *
   * <pre>
   * isBlank(null) = true;
   * isBlank(&quot;&quot;) = true;
   * isBlank(&quot;  &quot;) = true;
   * isBlank(&quot;a&quot;) = false;
   * isBlank(&quot;a &quot;) = false;
   * isBlank(&quot; a&quot;) = false;
   * isBlank(&quot;a b&quot;) = false;
   * </pre>
   *
   * @param str
   * @return if string is null or its size is 0 or it is made by space, return true, else return false.
   */
  public static boolean isBlank(String str) {
    return (str == null || str.trim().length() == 0);
  }

  /**
   * is null or its length is 0
   *
   * <pre>
   * isEmpty(null) = true;
   * isEmpty(&quot;&quot;) = true;
   * isEmpty(&quot;  &quot;) = false;
   * </pre>
   *
   * @param str
   * @return if string is null or its size is 0, return true, else return false.
   */
  public static boolean isEmpty(CharSequence str) {
    return (str == null || str.length() == 0);
  }

  /**
   *
   * @param str
   * @return
   */
  public  static  String    isEmptyOrNull(String   str){
    if(TextUtils.isEmpty(str)){
      return  DEFAULT_EMPTY;
    }
    return str;
  }

  public  static  String   IntegerToStr(Integer   integer){
    if(null == integer){
      return  DEFAULT_EMPTY;
    }
    return String.valueOf(integer);
  }


  public  static  String  DoubleToStr(Double   d){
    if(null == d){
      return  DEFAULT_EMPTY;
    }
    return df.format(d);
  }


  /**
   * compare two string
   *
   * @param actual
   * @param expected
   * @return
   * @see
   */
  public static boolean isEquals(String actual, String expected) {

    return  TextUtils.equals(actual,expected);
  }

  /**
   * get length of CharSequence
   *
   * <pre>
   * length(null) = 0;
   * length(\"\") = 0;
   * length(\"abc\") = 3;
   * </pre>
   *
   * @param str
   * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
   */
  public static int length(CharSequence str) {
    return str == null ? 0 : str.length();
  }

  /**
   * null Object to empty string
   *
   * <pre>
   * nullStrToEmpty(null) = &quot;&quot;;
   * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
   * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
   * </pre>
   *
   * @param str
   * @return
   */
  public static String nullStrToEmpty(Object str) {
    return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
  }

  /**
   * capitalize first letter
   *
   * <pre>
   * capitalizeFirstLetter(null)     =   null;
   * capitalizeFirstLetter("")       =   "";
   * capitalizeFirstLetter("2ab")    =   "2ab"
   * capitalizeFirstLetter("a")      =   "A"
   * capitalizeFirstLetter("ab")     =   "Ab"
   * capitalizeFirstLetter("Abc")    =   "Abc"
   * </pre>
   *
   * @param str
   * @return
   */
  public static String capitalizeFirstLetter(String str) {
    if (isEmpty(str)) {
      return str;
    }

    char c = str.charAt(0);
    return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
        .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
  }

  /**
   * encoded in utf-8
   *
   * <pre>
   * utf8Encode(null)        =   null
   * utf8Encode("")          =   "";
   * utf8Encode("aa")        =   "aa";
   * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
   * </pre>
   *
   * @param str
   * @return
   * @throws  if an error occurs
   */
  public static String utf8Encode(String str) {
    if (!isEmpty(str) && str.getBytes().length != str.length()) {
      try {
        return URLEncoder.encode(str, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
      }
    }
    return str;
  }

  /**
   * encoded in utf-8, if exception, return defultReturn
   *
   * @param str
   * @param defultReturn
   * @return
   */
  public static String utf8Encode(String str, String defultReturn) {
    if (!isEmpty(str) && str.getBytes().length != str.length()) {
      try {
        return URLEncoder.encode(str, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        return defultReturn;
      }
    }
    return str;
  }

  /**
   * get innerHtml from href
   *
   * <pre>
   * getHrefInnerHtml(null)                                  = ""
   * getHrefInnerHtml("")                                    = ""
   * getHrefInnerHtml("mp3")                                 = "mp3";
   * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
   * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
   * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
   * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
   * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
   * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
   * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
   * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
   * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
   * </pre>
   *
   * @param href
   * @return <ul>
   *         <li>if href is null, return ""</li>
   *         <li>if not match regx, return source</li>
   *         <li>return the last string that match regx</li>
   *         </ul>
   */
  public static String getHrefInnerHtml(String href) {
    if (isEmpty(href)) {
      return "";
    }

    String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
    Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
    Matcher hrefMatcher = hrefPattern.matcher(href);
    if (hrefMatcher.matches()) {
      return hrefMatcher.group(1);
    }
    return href;
  }

  /**
   * process special char in html
   *
   * <pre>
   * htmlEscapeCharsToString(null) = null;
   * htmlEscapeCharsToString("") = "";
   * htmlEscapeCharsToString("mp3") = "mp3";
   * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
   * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
   * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
   * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
   * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
   * </pre>
   *
   * @param source
   * @return
   */
  public static String htmlEscapeCharsToString(String source) {
    return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
        .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
  }

  /**
   * transform half width char to full width char
   *
   * <pre>
   * fullWidthToHalfWidth(null) = null;
   * fullWidthToHalfWidth("") = "";
   * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
   * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
   * </pre>
   *
   * @param s
   * @return
   */
  public static String fullWidthToHalfWidth(String s) {
    if (isEmpty(s)) {
      return s;
    }

    char[] source = s.toCharArray();
    for (int i = 0; i < source.length; i++) {
      if (source[i] == 12288) {
        source[i] = ' ';
        // } else if (source[i] == 12290) {
        // source[i] = '.';
      } else if (source[i] >= 65281 && source[i] <= 65374) {
        source[i] = (char)(source[i] - 65248);
      } else {
        source[i] = source[i];
      }
    }
    return new String(source);
  }

  /**
   * transform full width char to half width char
   *
   * <pre>
   * halfWidthToFullWidth(null) = null;
   * halfWidthToFullWidth("") = "";
   * halfWidthToFullWidth(" ") = new String(new char[] {12288});
   * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
   * </pre>
   *
   * @param s
   * @return
   */
  public static String halfWidthToFullWidth(String s) {
    if (isEmpty(s)) {
      return s;
    }

    char[] source = s.toCharArray();
    for (int i = 0; i < source.length; i++) {
      if (source[i] == ' ') {
        source[i] = (char)12288;
        // } else if (source[i] == '.') {
        // source[i] = (char)12290;
      } else if (source[i] >= 33 && source[i] <= 126) {
        source[i] = (char)(source[i] + 65248);
      } else {
        source[i] = source[i];
      }
    }
    return new String(source);
  }

  /**
   * 转化为半角字符
   *
   * @param s 待转字符串
   * @return 半角字符串
   */
  public static String toDBC(String s) {
    if (isEmpty(s)) {
      return s;
    }
    char[] chars = s.toCharArray();
    for (int i = 0, len = chars.length; i < len; i++) {
      if (chars[i] == 12288) {
        chars[i] = ' ';
      } else if (65281 <= chars[i] && chars[i] <= 65374) {
        chars[i] = (char) (chars[i] - 65248);
      } else {
        chars[i] = chars[i];
      }
    }
    return new String(chars);
  }

  /**
   * 转化为全角字符
   *
   * @param s 待转字符串
   * @return 全角字符串
   */
  public static String toSBC(String s) {
    if (isEmpty(s)) {
      return s;
    }
    char[] chars = s.toCharArray();
    for (int i = 0, len = chars.length; i < len; i++) {
      if (chars[i] == ' ') {
        chars[i] = (char) 12288;
      } else if (33 <= chars[i] && chars[i] <= 126) {
        chars[i] = (char) (chars[i] + 65248);
      } else {
        chars[i] = chars[i];
      }
    }
    return new String(chars);
  }

  public   static   String    getUUID(){
    return UUID.randomUUID().toString().replace("-","");
  }

  /**
   * @see #join(Object[] array, String sep, String prefix)
   * @param array 需要连接的对象数组
   * @param sep 元素连接之间的分隔符
   * @return 连接好的新字符串
   */
  public static String join(Object[] array, String sep) {
    return join(array, sep, null);
  }

  /**
   * @see #join(Object[] array, String sep, String prefix)
   * @param list 需要连接的对象数组
   * @param sep 元素连接之间的分隔符
   * @return 连接好的新字符串
   */
  public static String join(Collection<?> list, String sep) {
    return join(list, sep, null);
  }

  /**
   * @see #join(Object[] array, String sep, String prefix)
   * @param list 需要连接的对象数组
   * @param sep 元素连接之间的分隔符
   * @param prefix 前缀字符串
   * @return 连接好的新字符串
   */
  public static String join(Collection<?> list, String sep, String prefix) {
    Object[] array = list == null ? null : list.toArray();
    return join(array, sep, prefix);
  }

  /**
   * 以指定的分隔符来进行字符串元素连接
   * <p>
   * 例如有字符串数组array和连接符为逗号(,) <code>
   * String[] array = new String[] { "hello", "world", "bimface", "cloud","storage" };
   * </code> 那么得到的结果是: <code>
   * hello,world,bimface,cloud,storage
   * </code>
   * </p>
   *
   * @param array 需要连接的对象数组
   * @param sep 元素连接之间的分隔符
   * @param prefix 前缀字符串
   * @return 连接好的新字符串
   */
  public static String join(Object[] array, String sep, String prefix) {
    if (array == null) {
      return "";
    }

    int arraySize = array.length;

    if (arraySize == 0) {
      return "";
    }

    if (sep == null) {
      sep = "";
    }

    if (prefix == null) {
      prefix = "";
    }

    StringBuilder buf = new StringBuilder(prefix);
    for (int i = 0; i < arraySize; i++) {
      if (i > 0) {
        buf.append(sep);
      }
      buf.append(array[i] == null ? "" : array[i]);
    }
    return buf.toString();
  }

  /**
   * 以json元素的方式连接字符串中元素
   * <p>
   * 例如有字符串数组array <code>
   * String[] array = new String[] { "hello", "world", "bimface", "cloud","storage" };
   * </code> 那么得到的结果是: <code>
   * "hello","world","bimface","cloud","storage"
   * </code>
   * </p>
   *
   * @param array 需要连接的字符串数组
   * @return 以json元素方式连接好的新字符串
   */
  public static String jsonJoin(String[] array) {
    int arraySize = array.length;
    int bufSize = arraySize * (array[0].length() + 3);
    StringBuilder buf = new StringBuilder(bufSize);
    for (int i = 0; i < arraySize; i++) {
      if (i > 0) {
        buf.append(',');
      }

      buf.append('"');
      buf.append(array[i]);
      buf.append('"');
    }
    return buf.toString();
  }

  public static boolean isNullOrEmpty(String s) {
    return s == null || "".equals(s);
  }

  public static boolean inStringArray(String s, String[] array) {
    for (String x : array) {
      if (x.equals(s)) {
        return true;
      }
    }
    return false;
  }

  public static boolean containsStringArray(String s, String[] array) {
    for (String x : array) {
      if (s.contains(x)) {
        return true;
      }
    }
    return false;
  }


  public static final Charset   UTF_8    = Charset.forName("UTF-8");

  public static byte[] utf8Bytes(String data) {
    return data.getBytes(UTF_8);
  }

  public static String utf8String(byte[] data) {
    return new String(data, UTF_8);
  }


  public static String FormetFileSize(long fileS) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
    } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "K";
    } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }
    return fileSizeString;
  }

  /**
   * 通过URI获取真实文件名
   * @param context
   * @param uri
   * @return
   */
  public static String getRealFilePath( final Context context, final Uri uri ) {
    if ( null == uri ) {
      return null;
    }
    final String scheme = uri.getScheme();
    System.out.println("scheme="+scheme);
    String data = null;
    if ( scheme == null ) {
      data = uri.getPath();
    } else if ( ContentResolver.SCHEME_FILE.equals(scheme) ) {
      data = uri.getPath();
    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
      Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
      if ( null != cursor ) {
        if ( cursor.moveToFirst() ) {
          int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
          if ( index > -1 ) {
            data = cursor.getString( index );
          }
        }
        cursor.close();
      }
      if(TextUtils.isEmpty(data)){
        String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

        String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = context.getContentResolver().query(mImageUri,imageColumns,null , null,  imageOrderBy);
        if(null != imageCursor && imageCursor.moveToFirst()){

          data = imageCursor.getString(imageCursor
                  .getColumnIndex(MediaStore.Images.Media.DATA));
          imageCursor.close();

        }
      }
    }
    return data;
  }

  public static String cleanXSS(String value) {
    if(null == value){
      return "";
    }
    // You'll need to remove the spaces from the html entities below
    value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
    value = value.replaceAll("'", "&#39;");
    value = value.replaceAll("eval\\((.*)\\)", "");
    value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
    value = value.replaceAll("script", "");
    return value;

  }
}
