package com.hiscene.hishare;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import java.io.StreamTokenizer;

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

    //登录成功回调
    public static void LoginCallback(JSONObject jsonObject) {
        Log.d(TAG, "TencentQQ/LoginCallBack:" + jsonObject.toString());
        InitOpenidAndToken(jsonObject);

        JSONObject data=SetSlefData(jsonObject);
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
            String unionid = "";

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

}
