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

package com.akingyin.base.net.interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Okhttp 栏截器
 * @ Description:
 *
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/4/14 15:14
 * @ Version V1.0
 */
public class LoginInterceptor implements Interceptor{

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request   request =  chain.request();

    Response response = chain.proceed(request);
    if(response.isSuccessful()){

      try {
        JSONObject   jsonObject =  new JSONObject(response.body().string());

      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return response;
  }
}
