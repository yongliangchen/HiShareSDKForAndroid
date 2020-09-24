package com.hiscene.hishare;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.URL;

public class TencentQQ {

    private static MainActivity m_Activity = null;
    private static String m_UnityObjectName = "";
    private static Tencent m_Tencent;
    public static String TAG = "TencentQQ";

    //需要换成自己的APPID
    private static String APP_ID = "12345";

    private static BaseUiListener m_LoginCallBack = new BaseUiListener();

    //初始化
    private void Init(MainActivity activity, String unityObjectName) {
        Log.d(TAG, "TencentQQ/Init()");
        m_Activity = activity;
        m_UnityObjectName = unityObjectName;

        m_Tencent = Tencent.createInstance(APP_ID, m_Activity.getApplicationContext());
    }

    //处理低端机内存紧张导致可能无回调
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                m_Tencent.handleResultData(data, m_LoginCallBack);
            }
            m_Tencent.onActivityResultData(requestCode, resultCode, data, m_LoginCallBack);
        }
    }

    //登录
    public static void Login() {
        Log.d(TAG, "TencentQQ/Login()/登录QQ");
        m_Tencent.login(m_Activity, "all", m_LoginCallBack);
    }

    //注销
    public static void Logout() {
        Log.d(TAG, "TencentQQ/Logout()/注销QQ");
        m_Tencent.logout(m_Activity);
    }

    public  static boolean CheckAutorCaild()
    {
        return  m_Tencent.isSessionValid();
    }

    //刷新票据
    public static JSONObject RefreshSession()
    {
        JSONObject jsonObject=m_Tencent.loadSession(APP_ID);
        if (jsonObject==null)
        {
            Login();
        }
        else
        {
            m_Tencent.initSessionCache(jsonObject);
        }

        Log.d(TAG,"TencentQQ/JSONObject()/"+jsonObject.toString());

        return  SetSlefData(jsonObject);
    }

    //登录成功回调
    public static void LoginCallback(final JSONObject jsonObject) {
        Log.d(TAG, "TencentQQ/LoginCallBack:" + jsonObject.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                InitOpenidAndToken(jsonObject);
                JSONObject data=SetSlefData(jsonObject);
                HiShareHelper.SendPlatformMessageToUnity(MessageCode.QQLogoinCallBack,data.toString());
            }
        }).start();
    }

    public static String GetUinionid(String token)
    {
        String unionid="";

        try {

            URL url=new URL("https://graph.qq.com/oauth2.0/me?access_token="+token+"&unionid=1");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int code=connection.getResponseCode();
            if (code==HttpURLConnection.HTTP_OK)
            {
                InputStream is= connection.getInputStream();
                byte[] data=readStream(is);
                String json=new String(data);
                json =json.replace("(","").replace(")","").replace("callback","");
                JSONObject jsonObject=new JSONObject(json);
                unionid=jsonObject.getString("unionid");
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "TencentQQ/GetUiniodid()/获取Unionid失败！" + e);
        }

        return  unionid;

    }

    //封装成自己的数据
    private static JSONObject SetSlefData(JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        try {
            String token = data.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = data.getString(Constants.PARAM_EXPIRES_IN);
            String openId = data.getString(Constants.PARAM_OPEN_ID);
            String paytoken = data.getString("pay_token");
            String pf = data.getString("pfkey");
            String expirestime = data.getString(Constants.PARAM_EXPIRES_TIME);
            String unionid = GetUinionid(token);

            jsonObject.put("openId", openId);
            jsonObject.put("token", token);
            jsonObject.put("expires", expires);
            jsonObject.put("refreshtoken","");
            jsonObject.put("paytoken", paytoken);
            jsonObject.put("pf", pf);
            jsonObject.put("expirestime", expirestime);
            jsonObject.put("unionid", unionid);

        } catch (Exception e) {
            Log.d(TAG, "TencentQQ/SetSlefData()/Exception:" + e);
        }

        return jsonObject;
    }

    private static void InitOpenidAndToken(JSONObject jsonObject)
    {
        try {
            String token=jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires=jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId=jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(expires)&&!TextUtils.isEmpty(openId)) {

                m_Tencent.setAccessToken(token,expires);
                m_Tencent.setOpenId(openId);
            }
        }
        catch (Exception e)
        {
            Log.d(TAG,"TencentQQ/InitOpenidAndToken/Exception:"+e);
        }
    }

    private  static  byte[] readStream(InputStream inputStream) throws  Exception
    {
        ByteArrayOutputStream bout =new ByteArrayOutputStream();
        byte[] buffet=new byte[1024];
        int len=0;
        while ((len=inputStream.read(buffet))!=-1)
        {
            bout.write(buffet,0,len);
        }

        bout.close();
        inputStream.close();
        return  bout.toByteArray();
    }

}
