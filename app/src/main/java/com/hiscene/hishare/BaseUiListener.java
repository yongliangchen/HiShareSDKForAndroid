package com.hiscene.hishare;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class BaseUiListener implements IUiListener
{

    @Override
    public void onComplete(Object o) {

        if (o == null) return;

        JSONObject jsonObject = (JSONObject) o;
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        Log.d(TencentQQ.TAG,"登入QQ成功！");
        TencentQQ.LoginCallback(jsonObject);
    }

    @Override
    public void onError(UiError uiError) {

        Log.d(TencentQQ.TAG,"登入QQ失败！");
    }

    @Override
    public void onCancel() {

        Log.d(TencentQQ.TAG,"取消QQ登录！");
    }
}
