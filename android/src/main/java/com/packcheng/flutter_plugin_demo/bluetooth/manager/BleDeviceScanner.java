package com.packcheng.flutter_plugin_demo.bluetooth.manager;

import android.text.TextUtils;

import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.packcheng.flutter_plugin_demo.bluetooth.DDLog;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleScanListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 蓝牙扫描设备管理
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 11:11 AM
 */
public class BleDeviceScanner {
    private final static String TAG = "BleDeviceScanner";
    private final IBleScanListener mScanListener;
    private final BleScanCallback mScanCallback;
    private final LinkedHashMap<String, BleDevice> mDiscoveredDevice;

    public BleDeviceScanner(IBleScanListener listener) {
        this.mScanListener = listener;
        mDiscoveredDevice = new LinkedHashMap<>();

        mScanCallback = new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                DDLog.i(TAG, "onScanFinished, size: " + scanResultList.size());
                mDiscoveredDevice.clear();
                for (BleDevice bleDevice : scanResultList) {
                    mDiscoveredDevice.put(bleDevice.getDevice().getAddress(), bleDevice);
                }
                mScanListener.onScanFinished(scanResultList);
            }

            @Override
            public void onScanStarted(boolean success) {
                DDLog.i(TAG, "onScanStarted, SUCCESS: " + success);
                mScanListener.onScanStarted(success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                DDLog.i(TAG, "onScanning,");
                DDLog.i(TAG, "deviceName: " + bleDevice.getDevice().getAddress());
                mDiscoveredDevice.put(bleDevice.getDevice().getAddress(), bleDevice);
                mScanListener.onScanning(bleDevice);
            }
        };
    }

    /**
     * 获取设备地址
     *
     * @return 设备地址列表
     */
    public List<String> getDeviceAddressList() {
        return new ArrayList<>(mDiscoveredDevice.keySet());
    }

    /**
     * 更加设备地址获取设备
     *
     * @param bleDeviceAddress 设备地址
     * @return 设备
     */
    public BleDevice getBleDeviceByAddress(String bleDeviceAddress) {
        if (TextUtils.isEmpty(bleDeviceAddress) || mDiscoveredDevice.size() <= 0) {
            return null;
        }

        return mDiscoveredDevice.get(bleDeviceAddress);
    }

    public BleScanCallback getScanCallback() {
        return mScanCallback;
    }
}
