package com.packcheng.flutter_plugin_demo.bluetooth.callback;

import com.clj.fastble.data.BleDevice;

import java.util.List;

/**
 * 蓝牙设备扫描回调接口
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 1:59 PM
 */
public interface IBleScanListener {
    /**
     * 扫描结束
     *
     * @param scanResultList 所有扫描到的设备
     */
    void onScanFinished(List<BleDevice> scanResultList);

    /**
     * 开始扫描
     *
     * @param success 是否成功开始扫描，如果当前正在扫描，返回false
     */
    void onScanStarted(boolean success);

    /**
     * 扫描到某一设备
     *
     * @param bleDevice 扫描到的蓝牙设备
     */
    void onScanning(BleDevice bleDevice);
}
