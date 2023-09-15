package com.cjm.izeus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class PrefSaveAndRead {
    private static final String TAG = "PrefSaveAndRead";


    public static void saveSession(Context context, String sessionId){
        if(context != null){
            SharedPreferences share = context.getSharedPreferences(URLConstant.SESSION_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            editor.putString(URLConstant.SESSION_KEY,sessionId);
            editor.apply();
            Log.e(TAG,"写入完毕");
        }else{
            Log.e(TAG,"activity 为 null");
        }
    }

    public static String getSession(Context context){
        if(context != null){
            SharedPreferences share = context.getSharedPreferences(URLConstant.SESSION_NAME,MODE_PRIVATE);
            return share.getString(URLConstant.SESSION_KEY,"null");
        }else{
            Log.e(TAG,"activity 为 null");
        }
        return null;
    }


    public static void saveMsg(Context context,String name,String key,String value){
        if(context != null){
            SharedPreferences share = context.getSharedPreferences(name,MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            editor.putString(key,value);
            editor.apply();
            Log.e(TAG,"写入 " + value + "完毕");
        }else{
            Log.e(TAG,"activity 为 null");
        }
    }

    public static String getMsg(Context context,String name,String key){
        if(context != null){
            SharedPreferences share = context.getSharedPreferences(name,MODE_PRIVATE);
            return share.getString(key,"null");
        }else{
            Log.e(TAG,"context 为 null");
        }
        return null;
    }


}
