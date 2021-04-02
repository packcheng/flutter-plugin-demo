package com.packcheng.flutter_plugin_demo.bluetooth;

/**
 * Created by Rinfon on 16/7/8.
 */
public class APIKey {
    public static final String UUID_SERVICE = "957c0cf0-e022-4ab1-a9a5-ad6838d2abaa";
    public static final String UUID_CHRA_WRITE = "08a4a09f-f1f8-4696-a608-e06241f0a103";
    public static final String UUID_CHRA_NOTIFY = "c298e090-71a7-421e-a2c5-0f99c5489c99";
    public static final int BLE_SCAN_TIMEOUT = 500000 * 1000;
    public static final int BLE_CONNECT_TIMEOUT = 50 * 1000;
    public static final int BLE_OPERATE_TIMEOUT = 50 * 1000;
    public static final int BLE_GET_WIFI_LIST_INTERVAL = 20 * 1000;//获取wifi列表的间隔
    public static final int BLE_ADVANCE_CONNECT_TIMEOUT = 20 * 1000;
    public static final int BLE_CONFIT_NET_TIMEOUT = 120 * 1000; //配置网络时间
    public static final int BLE_BIND_TIMEOUT = 60 * 1000; //配置网络时间
    public static final int BLE_SCAN_NO_DEVICE_TIP_TIMEOUT = 180 * 1000;//扫描一段时间后，弹出提示。
    public static final boolean IS_BLE_DATA_ENCRYPT = true;


    // def 动作
    public static final String callDevice = "CallDevice";
    // 动作 end

    // def 配网必备
    public static final String getWifiList = "GetWifiList";
    public static final String setWifiName = "SetWifiName";
    public static final String setWifiPassword = "SetWifiPassword";
    public static final String startConfigureNetwork = "StartNetwork";
    // 配网必备 end

    // def 注册必备
    public static final String setAppID = "SetAppID";
    public static final String setHost = "SetHost";
    public static final String setAppKey = "SetAppKey";
    public static final String setAppSecret = "SetAppSecret";

    public static final String setFamily = "SetFamily";
    public static final String setRoom = "SetRoom";
    public static final String setDeviceName = "SetName";

    public static final String register = "Register";
    // 注册必备 end

    // def 其他
    public static final String getVersion = "GetVersion";
    // end


    public static final String CMD = "cmd";
}
