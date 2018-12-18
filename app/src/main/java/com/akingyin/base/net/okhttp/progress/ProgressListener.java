package com.akingyin.base.net.okhttp.progress;

import com.akingyin.base.net.okhttp.progress.body.ProgressInfo;

/**
 * @author king
 * @version V1.0
 * @ Description:
 * @ Date 2018/8/25 10:50
 */
public interface ProgressListener {

  /**
   * 进度监听
   *
   * @param progressInfo 关于进度的所有信息
   */
  void onProgress(ProgressInfo progressInfo);

  /**
   * 错误监听
   *
   * @param id 进度信息的 id
   * @param e  错误
   */
  void onError(long id, Exception e);
}
