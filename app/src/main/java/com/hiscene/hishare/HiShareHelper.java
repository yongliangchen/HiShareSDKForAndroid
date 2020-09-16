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

class HiShareHelper
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

    //发送消息给Unity
    public static void SendPlatformMessageToUnity(int iMsgId,int iParam1,int iParam2,int iParam3,String strParam1,String strParam2,String strParam3)
    {
        if(m_UnityObjectName == null ||"".equals(m_UnityObjectName)) {
            Log.d(TAG,"m_UnityObjectName is null!");
        }
        if(m_Methodname == null ||"".equals(m_Methodname)) {
            Log.d(TAG,"m_Methodname is null!");
        }

        String msg = GetJsonStr(iMsgId, iParam1, iParam2, iParam3, strParam1, strParam2, strParam3);
        UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, msg);
    }

    //Unity发送消息给安卓
    public static void SendUnityMessageToPlatform(int iMsgId,int iParam1,int iParam2,int iParam3,String strParam1,String strParam2,String strParam3)
    {
        Log.d(TAG,"SendUnityMessageToPlatform: iMsgId:"+iMsgId+" iParam1:"+iParam1+" iParam2:"+iParam2+" iParam3:"+iParam3+" strParam1:"+strParam1+" strParam2:"+strParam2+" strParam3:"+strParam3);

        if (m_Activity==null) {
            Log.d(TAG,"m_Activity is null!");
        }
        if(m_UnityObjectName == null ||"".equals(m_UnityObjectName)) {
            Log.d(TAG,"m_UnityObjectName is null!");
        }

    }

    //从安卓平台获取Int类型
    public static int GetIntFromPlatform(int type)
    {
        switch (type)
        {
          //todo
        }

        return  0;
    }

    //从安卓平台获取String类型
    public static String GetStringFromPlatform(int type)
    {
        switch (type)
        {
            //todo
        }

        return  "";
    }

    //从安卓平台获取Long类型
    public static long GetLongFromPlatform(int type)
    {
        switch (type)
        {
            case 1: return GetSystemAvailableMemory(); //获取当前系统可用内存
            case 2: return GetTotalMemory(); //获取总内存
            case 3: return GetUsingMemory(); //获取使用内存
        }

        return  0;
    }

    //从安卓平台获取Long类型
    public static long GetLongFromPlatform2(int type,int iParam1,int iParam2,int iParam3,String strParam1,String strParam2,String strParam3)
    {
        switch (type)
        {
            //todo
        }

        return  0;
    }

    //通过Json对象构造字符串
    private static String GetJsonStr(int iMsgId,int iParam1,int iParam2,int iParam3,String strParam1,String strParam2,String strParam3)
    {
        try
        {
            JSONObject object=new JSONObject();
            object.put("iMsgId",iMsgId);
            object.put("iParam1",iParam1);
            object.put("iParam2",iParam2);
            object.put("iParam3",iParam3);
            object.put("strParam1",strParam1);
            object.put("strParam2",strParam2);
            object.put("strParam3",strParam3);
            return object.toString();
        }
        catch(JSONException e)
        {
            Log.d(TAG,"Json error:"+e.toString());
            return "";
        }
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
