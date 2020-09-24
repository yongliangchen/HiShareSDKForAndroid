package com.hiscene.hishare;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.TestLooperManager;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HiShareHelper
{
    private static MainActivity m_Activity=null;
    private static String m_UnityObjectName="";
    private static String m_Methodname="OnMessage";
    public static String TAG="HiShareHelper";

    //初始化
    private void Init(MainActivity activity,String unityObjectName)
    {
        m_Activity=activity;
        m_UnityObjectName=unityObjectName;
    }

    //平台发送消息给Unity
    public static void SendPlatformMessageToUnity(MessageCode msgCode, String... params) {
        String msg = Integer.toString(msgCode.ordinal());
        for (int i = 0; i < params.length; i++) {
            msg += "|" + params[i];
        }

        UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, msg);
    }

    //Unity发给平台的消息
    public static void SendUnityMessageToPlatform(int iMsgCode,String... params)
    {
        MessageCode msgCode = MessageCode.values()[iMsgCode];

        switch (msgCode) {

            case QQLogin://QQ登录

                break;

            case QQLogout://QQ注销登录

                break;

            case WxLogin://微信登录

                break;

            case WxLogout://微信注销登录

                break;
        }
    }

    //从android获取Int类型
    public static  int GetIntFromPlatform(MessageCode msgCode )
    {
        return 0;
    }

    //从android获取String类型
    public static  String GetStingFromPlatform(MessageCode msgCode)
    {
//        switch (msgCode)
//        {
//            case MessageCode.QQCheckAutorCaild://QQ授权是否过期
//                return String.valueOf(TencentQQ.CheckAutorCaild()).toString();
//
//            case MessageCode.QQRefreshSession://刷新QQ票据
//                return TencentQQ.RefreshSession().toString();
//
//        }

        return  "";
    }

    //从android获取Long类型
    public static long GetLongFromPlatform(MessageCode msgCode) {
        switch (msgCode) {
            case GetSystemAvailableMemory://获取当前系统可用内存
                return GetSystemAvailableMemory();

            case GetUsingMemory://获取使用内存
                return GetUsingMemory();

            case GetTotalMemory://获取总内存
                return GetTotalMemory();
        }

        return  0;
    }

    //获取当前系统可用内存
    private static long GetSystemAvailableMemory() {
        ActivityManager am = (ActivityManager) m_Activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    //获取使用内存
    private static long GetUsingMemory()
    {
        Debug.MemoryInfo memoryInfo=new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo.getTotalPss()*1024;
    }

    //获取总内存
    private static long GetTotalMemory()
    {
        long tm=0;
        try
        {
            RandomAccessFile reader=new RandomAccessFile("/proc/meminfo","r");
            String load=reader.readLine();
            reader.close();

            String[] totrm=load.split("KB");
            String[] trm =totrm[0].split("");
            tm=Long.parseLong(trm[trm.length-1])*1024;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return  tm;
    }

}
