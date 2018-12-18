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

package com.akingyin.base.net.okhttp.Interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @ Description:
 *  OkHttp 网络缓存
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/7/6 17:50
 * @ Version V1.0
 */
public class HttpCacheInterceptor  implements Interceptor {
  @Override public Response intercept(Chain chain) throws IOException {
    //Request request = chain.request();
    //if (!NetWorkHelper.isNetConnected(KissTools.getApplicationContext())) {
    //  request = request.newBuilder()
    //      .cacheControl(CacheControl.FORCE_CACHE)
    //      .build();
    //  Log.d("com.android.core", "no network");
    //}
    //
    //Response originalResponse = chain.proceed(request);
    //if (NetWorkHelper.isNetConnected(KissTools.getApplicationContext())) {
    //  //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
    //  String cacheControl = request.cacheControl().toString();
    //  return originalResponse.newBuilder()
    //      .header("Cache-Control", cacheControl)
    //      .removeHeader("Pragma")
    //      .build();
    //} else {
    //  return originalResponse.newBuilder()
    //      .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
    //      .removeHeader("Pragma")
    //      .build();
    //}
    return  null;
  }
}
