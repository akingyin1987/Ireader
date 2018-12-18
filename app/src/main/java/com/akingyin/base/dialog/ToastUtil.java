package com.akingyin.base.dialog;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ToastUtil {


    public   static   void   showError(@NonNull Context  context , String message){

        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public   static   void   showSucces(@NonNull Context  context , String message){
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public   static   void   showInfo(@NonNull Context  context , String message){
        Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public   static   void   showWarning(@NonNull Context  context , String message){
        Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public   static   void   showNormal(@NonNull Context  context , String message){
        Toasty.normal(context, message, Toast.LENGTH_SHORT).show();
    }
}
