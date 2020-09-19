package com.hiscene.hishare;

import android.app.ActivityManager;
import android.content.Context;
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
