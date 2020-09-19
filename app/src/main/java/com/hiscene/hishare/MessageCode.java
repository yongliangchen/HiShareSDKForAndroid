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
    OnHavePermissions,//用户已经有了权限
    OnHaveAllPermissions,//用户已经有了全部权限
    OnNoPermissions,//用户没有权限
    OnPermissionSuccess,//授权成功回调
    OnPermissionFail,//授权失败回调
}
