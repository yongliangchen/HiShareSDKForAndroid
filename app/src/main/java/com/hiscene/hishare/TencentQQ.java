package com.hiscene.hishare;

import android.util.Log;

import com.tencent.tauth.Tencent;

class TencentQQ {

    private static MainActivity m_Activity=null;
    private static String m_UnityObjectName="";
    private static Tencent m_Tencent;
    public static String TAG="TencentQQ";

    //需要换成自己的APPID
    private static String APP_ID="12345";

    //初始化
    private void Init(MainActivity activity,String unityObjectName)
    {
        Log.d(TAG,"TencentQQ/Init()");
        m_Activity=activity;
        m_UnityObjectName=unityObjectName;

        m_Tencent=Tencent.createInstance(APP_ID,m_Activity.getApplicationContext());
    }

}
