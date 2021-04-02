package com.packcheng.flutter_plugin_demo.bluetooth.callback.impl;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleCommunicateListener;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleConnectListener;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleScanListener;
import com.packcheng.flutter_plugin_demo.bluetooth.manager.BleDeviceManager;

import java.util.List;

import io.flutter.plugin.common.EventChannel;

/**
 * 统一监听蓝牙设备各种状态，通知Flutter
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 2:16 PM
 */
public class BleListener implements EventChannel.StreamHandler
        , IBleScanListener, IBleConnectListener, IBleCommunicateListener {
    private static final String TAG = "BleListener";

    private EventChannel.EventSink mSink;

    @Override
    public void onWriteSuccess(BleDeviceManager bleDeviceManager, int current, int total, byte[] justWrite) {

    }

    @Override
    public void onWriteFailure(BleDeviceManager bleDeviceManager, BleException exception) {

    }

    @Override
    public void onNotifySuccess(BleDeviceManager bleDeviceManager) {

    }

    @Override
    public void onNotifyFailure(BleDeviceManager bleDeviceManager, BleException exception) {

    }

    @Override
    public void onCharacteristicChanged(BleDeviceManager bleDeviceManager, byte[] data) {

    }

    @Override
    public void onStartConnect(BleDeviceManager bleDeviceManager) {

    }

    @Override
    public void onConnectFail(BleDeviceManager bleDeviceManager, BleException exception) {

    }

    @Override
    public void onConnectSuccess(BleDeviceManager bleDeviceManager, BluetoothGatt gatt, int status) {

    }

    @Override
    public void onDisConnected(BleDeviceManager bleDeviceManager, boolean isActiveDisConnected, BluetoothGatt gatt, int status) {

    }

    @Override
    public void onScanFinished(List<BleDevice> scanResultList) {

    }

    @Override
    public void onScanStarted(boolean success) {

    }

    @Override
    public void onScanning(BleDevice bleDevice) {

    }

    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        mSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        mSink = null;
    }
}
