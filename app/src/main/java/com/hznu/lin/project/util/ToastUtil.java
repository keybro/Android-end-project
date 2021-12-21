package com.hznu.lin.project.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast myToast;
    public static void showToast(Context context, String msg, int length) {
        missToast(context);
        myToast = Toast.makeText(context, msg, length);
        myToast.show();
    }
    //利用上下文统一管理Toast生命周期，补充该方法后可在调用后直接miss
    public static void missToast(Context context) {
        if (myToast!=null){
            myToast.cancel();//仅仅为隐藏，如果不调用下面myToast=null在同一界面使用出现首次点击Toast不能正常弹出的问题
            myToast=null;
        }
    }
}
