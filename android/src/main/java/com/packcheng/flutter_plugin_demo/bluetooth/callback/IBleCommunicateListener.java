package com.packcheng.flutter_plugin_demo.bluetooth.callback;

import com.clj.fastble.exception.BleException;
import com.packcheng.flutter_plugin_demo.bluetooth.manager.BleDeviceManager;

/**
 * 蓝牙交互状态监听
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 2:06 PM
 */
public interface IBleCommunicateListener {
    // 写相关
    void onWriteSuccess(BleDeviceManager bleDeviceManager, int current, int total, byte[] justWrite);

    void onWriteFailure(BleDeviceManager bleDeviceManager, BleException exception);

    // 读取相关
    void onNotifySuccess(BleDeviceManager bleDeviceManager);

    void onNotifyFailure(BleDeviceManager bleDeviceManager, BleException exception);

    void onCharacteristicChanged(BleDeviceManager bleDeviceManager, byte[] data);
}
