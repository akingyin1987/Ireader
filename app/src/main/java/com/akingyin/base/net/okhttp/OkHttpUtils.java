/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.okhttp;

import android.text.TextUtils;
import android.util.Log;
import com.akingyin.base.net.okhttp.Interceptor.LoginStatusInterceptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zlcd on 2015/12/29.
 */
public class OkHttpUtils {

    private static OkHttpClient singleton;

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");

    /**
     * 创建文件类
     *
     * @param file
     * @return
     */
    public static RequestBody CreateFileBody(@NonNull File file) {

        RequestBody rb = RequestBody.create(MEDIA_TYPE_JPG, file);
        return rb;
    }

    /**
     * 非文件类
     *
     * @param value
     * @return
     */
    public static RequestBody CreateTextBody(@NonNull String value) {
        return RequestBody.create(MEDIA_TYPE_TEXT, value);
    }

  /**
   * 创建文件
   * @param file
   * @return
   */
    public  static  MultipartBody  CreateMultiFilepart(String key,String value,String   file){
        MultipartBody.Builder  builder = new MultipartBody.Builder();
        builder.addFormDataPart(key, value,CreateFileBody(new File(file)));
        return  builder.build();
    }

  /**
   * 创建文件并带参数
   * @param key
   * @param value
   * @param file
   * @param params
   * @return
   */
  public  static  MultipartBody  CreateMultiFilepart(String key,String value,String   file,Map<String,String>  params){
    MultipartBody.Builder  builder = new MultipartBody.Builder();
    builder.addFormDataPart(key, value,CreateFileBody(new File(file)));
    if(null != params && params.size()>0){
      for (String s : params.keySet()) {
        builder.addFormDataPart(s,params.get(s));
      }
    }
    return  builder.build();
  }

    /**
     * 创建form表单
     *
     * @param params
     * @param encodedKey
     * @param encodedValue
     * @return
     */
    public static RequestBody createRequestBody(@NonNull Map<String, String> params, String encodedKey, String encodedValue) {

        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                formEncodingBuilder.add(key, params.get(key));
            }
        }
        if (!TextUtils.isEmpty(encodedKey) && !TextUtils.isEmpty(encodedValue)) {
            formEncodingBuilder.addEncoded(encodedKey, encodedValue);
        }
        return formEncodingBuilder.build();
    }

  /**
   * 创建多文件上传
   * @param files
   * @param filepart
   * @param params
   * @return
   */
     public   static  List<MultipartBody.Part>  createMultipartBodyFileParts(@NonNull List<String>  files,@NonNull String filepart,@Nullable Map<String,String> params){
       List<MultipartBody.Part>  parts = new LinkedList<>();
       for(String  path:files){
         try {
           File   file = new File(path);
           parts.add(MultipartBody.Part.createFormData(filepart,file.getName(),CreateFileBody(file)));
         }catch (Exception e){
            e.printStackTrace();
         }
       }
       for(String  keystr : params.keySet()){
         parts.add(MultipartBody.Part.createFormData(keystr,params.get(keystr)));
       }

       return  parts;
     }

    /**
     * 创建上多文件上传数据
     * @param files 文件路径
     * @param filepart  文件属性
     * @param params  普通参数
     * @return
     */
    public static   MultipartBody   createRequestBody(@NonNull List<String>  files,@NonNull String filepart,@Nullable Map<String,String> params){
        MultipartBody.Builder  builder = new MultipartBody.Builder();
        for(String path : files){
            try {
                File   file = new File(path);
                builder.addFormDataPart(filepart,file.getName(),CreateFileBody(file));
            }catch (Exception e){

            }
        }
        if(null != params){
            for(String  key : params.keySet()){
                builder.addFormDataPart(key,params.get(key));
            }
        }

        return  builder.build();
    }


  /**
   * 上传数组类型
   * @param key
   * @param values
   * @param params
   * @return
   */
  public  static  List<MultipartBody.Part>  createMultipartBodyParts(String key,List<String>  values,Map<String,String>  params){
    List<MultipartBody.Part>   parts = new LinkedList<>();
    if(!TextUtils.isEmpty(key) && null != values && values.size()>0){
      for(String  value:values){
        parts.add(MultipartBody.Part.createFormData(key,value));
      }

    }
    for(String  keystr : params.keySet()){
      parts.add(MultipartBody.Part.createFormData(keystr,params.get(keystr)));
    }
    return  parts;
  }

  /**
   *
   * @param key
   * @param values
   * @return
   */
    public  static MultipartBody createMultipartBodyString(String key,List<String>  values,Map<String,String>  params ){
        MultipartBody.Builder  builder = new MultipartBody.Builder();
          for(String  value : values){
              builder.addFormDataPart(key,value);
          }
          for(String  keystr : params.keySet()){
             builder.addFormDataPart(keystr,params.get(keystr));
          }
        return  builder.build();
    }

    public static RequestBody createRequestBody(@NonNull Map<String, String> params) {
        return createRequestBody(params, null, null);
    }


    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Call request) throws IOException {
        return request.execute();
    }

    /**
     * 不开异步且可回调
     *
     * @param request
     * @param responseCallback
     */
    public static boolean execute(@NonNull Request request, @Nullable Callback responseCallback) {
        Call call = null;
        try {
            getInstance();
            call = singleton.newCall(request);

            Response response = execute(call);
            if (null != responseCallback) {
                responseCallback.onResponse(call, response);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (null != responseCallback) {
                responseCallback.onFailure(call, new IOException());
            }
            return false;
        }

    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        getInstance();
        singleton.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果
     *
     * @param request
     */
    public static void enqueue(Request request) {
        getInstance();
        singleton.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    /**
     * 获取请求
     *
     * @param url
     * @param method
     * @param requestBody
     * @param header
     */
    public static Request request(@NonNull String url, @NonNull HttpMethod method, @Nullable RequestBody requestBody,
                                  @Nullable Headers header) throws IOException {
        Request.Builder builder = new Request.Builder()

            .url(url);
        if(method == HttpMethod.GET){
            builder.addHeader("Connection","close");
        }
        if (null != header) {
            builder.headers(header);
        }
        switch (method) {
            case GET:
                return builder.get().build();

            case POST:
                return builder.post(requestBody).build();
            case PUT:
                return builder.put(requestBody).build();
            case DELETE:
                return builder.delete(requestBody).build();
        }
        return null;
    }

    /**
     * 同步post请求
     *
     * @param url
     * @param requestBody
     * @param callback
     * @return
     */
    public static boolean doPost(String url, RequestBody requestBody, Callback callback) {

        try {
            Request request = request(url, HttpMethod.POST, requestBody, null);
            return execute(request, callback);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean doPost(String url, Map<String, String> parmas, Callback callback) {
        RequestBody requestBody = createRequestBody(parmas);
        return doPost(url, requestBody, callback);

    }

    /**
     * 异步post请求
     *
     * @param url
     * @param requestBody
     * @param callback
     */
    public static void doAsyPost(String url, RequestBody requestBody, Callback callback) {
        try {
            Request request = request(url, HttpMethod.POST, requestBody, null);
            enqueue(request, callback);
        } catch (Exception e) {
            e.printStackTrace();
            if (null != callback) {
                callback.onFailure(null, new IOException());
            }
        }

    }


    /**
     * 同步get请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {

        try {
            Request request = request(url, HttpMethod.GET, null, null);
            execute(request, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public   static HttpLoggingInterceptor httpLoggingInterceptor ;

    static {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        if(Config.DEBUG){

          httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else{
          httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

    }

    /**
     * 获取OkHttp实例
     *
     * @return
     */
    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                 //   File cacheDir = new File(FileConfig.getHttp_CacheRootDir(), Config.RESPONSE_CACHE);

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .addInterceptor(httpLoggingInterceptor)
                      .addInterceptor(new LoginStatusInterceptor())
                       // .cookieJar(new SimpleCookieJar())
                      //  .cache(new Cache(cacheDir, Config.RESPONSE_CACHE_SIZE))
                        .connectTimeout(Config.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(Config.HTTP_CONNECT_TIMEOUT,TimeUnit.SECONDS)
                        .readTimeout(Config.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
                    singleton = builder.build();
                }
            }

        }

        return singleton;
    }

    public   static   OkHttpClient.Builder   getOkHttpClientBuilder(){
      OkHttpClient.Builder builder = new OkHttpClient.Builder()
          .addInterceptor(httpLoggingInterceptor)

          .connectTimeout(Config.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
          .writeTimeout(Config.HTTP_CONNECT_TIMEOUT,TimeUnit.SECONDS)
          .readTimeout(Config.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
         return  builder;
    }

    public  static   OkHttpClient.Builder  getOkHttpClientBuilder(Interceptor... interceptors){
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      if(null != interceptors && interceptors.length>0){
        for(Interceptor interceptor : interceptors){
          builder.addInterceptor(interceptor);
        }
      }
      builder.connectTimeout(Config.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
          .writeTimeout(Config.HTTP_CONNECT_TIMEOUT,TimeUnit.SECONDS)
          .readTimeout(Config.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);



      return  builder;
    }

  /**
   * 下载文件
   * @param okHttpClient
   * @param fileUrl
   * @param destFileDir
   * @param callBack
   */
    public   static    void  downLoadFile(OkHttpClient  okHttpClient,String fileUrl, final String destFileDir, final  FileCallBack  callBack){
      final String fileName = fileUrl;
      final File file = new File(destFileDir, fileName);
      if (file.exists()) {
        return;
      }
      final Request request = new Request.Builder().url(fileUrl).build();
      final Call call = okHttpClient.newCall(request);
      call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          if(null != callBack){
            callBack.onFail();
          }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          InputStream is = null;
          byte[] buf = new byte[2048];
          int len = 0;
          FileOutputStream fos = null;
          try {
            long total = response.body().contentLength();

            long current = 0;
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
              current += len;
              fos.write(buf, 0, len);
              Log.e("downloadFile", "current------>" + current);
              if(null != callBack){
                callBack.onProgress(total,current);
              }
            }
            fos.flush();
            if(null != callBack){
              callBack.onSuccess();
            }
          } catch (Exception e) {
           e.printStackTrace();
            if(null != callBack){
              callBack.onFail();
            }
          } finally {
            try {
              if (is != null) {
                is.close();
              }
              if (fos != null) {
                fos.close();
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      });
    }

    /**
     * 取消请求
     * @param tag
     */
    public synchronized static void cancelTag(Object tag) {
        if(null == singleton){
            return;
        }
        for (Call call : singleton.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                break;
            }
        }
        for (Call call : singleton.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                break;
            }
        }
    }

    /**
     * 取消正在进请的请求
     */
    public synchronized static  void  cancelRunningCalls(){
        if(null == singleton){
            return;
        }
        for (Call call : singleton.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有的请求
     */
    public  synchronized  static  void  cancelAllCalls(){
        if(null == singleton){
            return;
        }
        for (Call call : singleton.dispatcher().queuedCalls()) {
             call.cancel();
        }
        for (Call call : singleton.dispatcher().runningCalls()) {
              call.cancel();
        }
    }

}
