package com.hiscene.hishare;

enum MessageCode
{
    None,//未知消息
    Log,//日记
    Warning,//警告
    Error,//发生错误
    GetSystemAvailableMemory,//获取当前系统可用内存
    GetUsingMemory,//获取使用内存
    GetTotalMemory,//获取总内存
    QQLogoinCallBack,//QQ登录回调
    QQLogin,//QQ登录
    QQLogout,//QQ注销登录
    QQCheckAutorCaild,//检查QQ授权是否过期
    QQRefreshSession,//刷新QQ票据
    WxLogoinCallBack,//微信登录回调
    WxLogin,//微信登录
    WxLogout,//微信注销登录
}
