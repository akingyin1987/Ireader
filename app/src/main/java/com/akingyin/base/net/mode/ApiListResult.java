/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.mode;

import java.util.List;

/**
 * @author king
 * @version V1.0
 * @ Description:
 * 封装的通用服务器返回对象，可自行定义
 * @ Date 2017/11/22 13:41
 */
public class ApiListResult <E>  {

    private int status;

    private String msg;

    private List<E> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiListResult{" +
                "code=" + status +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
