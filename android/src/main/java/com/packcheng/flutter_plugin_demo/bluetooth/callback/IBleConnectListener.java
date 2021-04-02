package com.packcheng.flutter_plugin_demo.bluetooth.callback;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.exception.BleException;
import com.packcheng.flutter_plugin_demo.bluetooth.manager.BleDeviceManager;

/**
 * 蓝牙连接状态监听
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 2:04 PM
 */
public interface IBleConnectListener {
    void onStartConnect(BleDeviceManager bleDeviceManager);

    void onConnectFail(BleDeviceManager bleDeviceManager, BleException exception);

    void onConnectSuccess(BleDeviceManager bleDeviceManager, BluetoothGatt gatt, int status);

    void onDisConnected(BleDeviceManager bleDeviceManager, boolean isActiveDisConnected, BluetoothGatt gatt, int status);
}
