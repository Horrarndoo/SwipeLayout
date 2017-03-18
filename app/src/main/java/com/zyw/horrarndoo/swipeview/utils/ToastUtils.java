package com.zyw.horrarndoo.swipeview.utils;

import android.content.Context;
import android.widget.Toast;

import com.zyw.horrarndoo.swipeview.global.MyApplication;

/**
 * Created by Horrarndoo on 2017/3/16.
 */

public class ToastUtils {
    public static Toast mToast;

    private static void initToast(){
        try {
            if(mToast == null){
                synchronized (ToastUtils.class){
                    if(mToast == null){
                        mToast = Toast.makeText(MyApplication.getContext(), "", Toast.LENGTH_SHORT);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  static  void showToast(String msg){
        initToast();
        mToast.setText(msg);
        mToast.show();
    }

    public  static  void showToast(int resouceId){
        initToast();
        mToast.setText(MyApplication.getContext().getResources().getString(resouceId));
        mToast.show();
    }

    public  static  void showToast(Context context, String msg){
        initToast();
        mToast.setText(msg);
        mToast.show();
    }

    public  static  void showToast(Context context, int resouceId){
        initToast();
        mToast.setText(context.getResources().getString(resouceId));
        mToast.show();
    }
}
