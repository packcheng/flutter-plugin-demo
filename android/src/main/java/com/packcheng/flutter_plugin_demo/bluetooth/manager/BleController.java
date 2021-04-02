package com.packcheng.flutter_plugin_demo.bluetooth.manager;

import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.os.Build;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.packcheng.flutter_plugin_demo.bluetooth.APIKey;
import com.packcheng.flutter_plugin_demo.bluetooth.DDLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by LT on 2019/2/22.
 */
public class BleController {
    public static String TAG = "BleController";

    private BleController() {
    }

    private static class Holder {
        static BleController instance = new BleController();
    }

    public static BleController getInstance() {
        return Holder.instance;
    }

    // 已连接的设备列表
    private final LinkedHashMap<String, BleDeviceManager> connectedDeviceMap = new LinkedHashMap<>();
    private final List<BleDeviceManager> connectedDeviceList = new ArrayList<>();

    /**
     * 初始化
     */
    public void initBle(Application application) {
        BleManager.getInstance().init(application);
        BleManager.getInstance()
                .enableLog(false)
                .setReConnectCount(2, 5000)
                .setConnectOverTime(APIKey.BLE_CONNECT_TIMEOUT)
                .setOperateTimeout(APIKey.BLE_OPERATE_TIMEOUT);
    }

    /**
     * 设置扫描规则
     *
     * @param bleScanTime 扫描时间
     */
    public void setScanRuleWithUUID(long bleScanTime) {
        DDLog.i(TAG, "setScanRuleWithUUID, bleScanTime: " + bleScanTime);
        if (bleScanTime <= 0) {
            bleScanTime = APIKey.BLE_SCAN_TIMEOUT;
        }
        UUID[] uuids = new UUID[1];
        uuids[0] = UUID.fromString(APIKey.UUID_SERVICE);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(uuids)
                .setScanTimeOut(bleScanTime)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * 蓝牙是否可用
     *
     * @return true: 蓝牙已打开
     */
    public boolean isOpenBlue() {
        return BleManager.getInstance().isBlueEnable();
    }

    /**
     * 开始扫描
     */
    public void startScan(BleDeviceScanner deviceScanner) {
        DDLog.i(TAG, "startScan");
        BleManager.getInstance().scan(deviceScanner.getScanCallback());
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        DDLog.i(TAG, "stopScan");
        BleManager.getInstance().cancelScan();
    }

    /**
     * 开始连接设备
     * 在初始化的时候设置了重连次数为2，
     * 所以第二次失败才是真正的失败
     *
     * @param deviceManager 设备管理对象
     */
    public void startConnect(BleDeviceManager deviceManager) {
        DDLog.i(TAG, "startConnect");
        BleManager.getInstance().connect(deviceManager.getBleDevice(), deviceManager.getBleGattCallback());
    }

    /**
     * 设置MTU
     * 在连接成功之后调用
     */
    public void setMtu(BleDeviceManager bleDeviceManager) {
        DDLog.i(TAG, "setMtu");
        bleDeviceManager = checkBleDeviceManager(bleDeviceManager);
        if (null == bleDeviceManager) {
            DDLog.e(TAG, "招不到需要操作的设备");
            return;
        }

        BleManager.getInstance().setMtu(bleDeviceManager.getBleDevice(), 512, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                DDLog.e(TAG, "设置MTU失败:" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                DDLog.d(TAG, "设置MTU成功，并获得当前设备传输支持的MTU值:" + mtu);
            }
        });
    }

    /**
     * 打开通知，准备接收从蓝牙返回的数据
     */
    public void openNotify(BleDeviceManager bleDeviceManager) {
        DDLog.i(TAG, "openNotify");
        bleDeviceManager = checkBleDeviceManager(bleDeviceManager);
        if (null == bleDeviceManager) {
            DDLog.e(TAG, "招不到需要操作的设备");
            return;
        }

        BleManager.getInstance().notify(
                bleDeviceManager.getBleDevice(),
                APIKey.UUID_SERVICE,
                APIKey.UUID_CHRA_NOTIFY,
                bleDeviceManager.getBleNotifyCallback());
    }

    public void write(byte[] data, BleDeviceManager bleDeviceManager) {
        DDLog.d(TAG, "write");
        bleDeviceManager = checkBleDeviceManager(bleDeviceManager);
        if (null == bleDeviceManager) {
            DDLog.e(TAG, "招不到需要操作的设备");
            return;
        }
        BleManager.getInstance().write(bleDeviceManager.getBleDevice(), APIKey.UUID_SERVICE,
                APIKey.UUID_CHRA_WRITE, data, false, bleDeviceManager.getBleWriteCallback());
    }


    /**
     * 当前是否有已连接的设备
     *
     * @return true:有连接的设备
     */
    public boolean isHasDeviceConnect() {
        return BleManager.getInstance().getAllConnectedDevice().size() > 0;
    }

    /**
     * 断开连接
     */
    public void disconnectBle(BleDeviceManager bleDeviceManager) {
        DDLog.d(TAG, "disconnectBle");
        bleDeviceManager = checkBleDeviceManager(bleDeviceManager);
        if (null == bleDeviceManager) {
            DDLog.e(TAG, "招不到需要操作的设备");
            return;
        }
        BleManager.getInstance().disconnect(bleDeviceManager.getBleDevice());
        deleteAllCache();
    }

    /**
     * 检查当前需要操作的设备，如果没有设备，时候最后一次连接的设备
     *
     * @param targetBleDeviceManager 需要操作的设备
     * @return 指定的设备或最后连接成功的设备
     */
    private BleDeviceManager checkBleDeviceManager(BleDeviceManager targetBleDeviceManager) {
        if (null != targetBleDeviceManager) {
            return targetBleDeviceManager;
        }

        return getLastBleDeviceManager();
    }

    /**
     * 断开连接
     */
    public void disconnectAllBle() {
        DDLog.d(TAG, "disconnectAllBle");
        BleManager.getInstance().disconnectAllDevice();
        deleteAllCache();
    }

    public void deleteAllCache() {
        DDLog.d(TAG, "deleteAllCache");
        if (BleManager.getInstance().getAllConnectedDevice().size() > 1) {
            DDLog.d(TAG, "BleManager.getInstance().getAllConnectedDevice().size() > 1");
            for (BleDevice bleDevice : BleManager.getInstance().getAllConnectedDevice()) {
                refreshGattCache(BleManager.getInstance().getBluetoothGatt(bleDevice));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean refreshGattCache(BluetoothGatt gatt) {
        boolean result = false;
        try {
            if (gatt != null) {
                Method refresh = BluetoothGatt.class.getMethod("refresh");
                if (refresh != null) {
                    refresh.setAccessible(true);
                    result = (boolean) refresh.invoke(gatt, new Object[0]);
                }
            }
        } catch (Exception e) {
            DDLog.e(TAG, "refreshGattCache, Error!!!!!!!");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 连接，添加缓存
     *
     * @param bleDeviceManager 断开连接的设备
     */
    public synchronized void addConnectDevice(BleDeviceManager bleDeviceManager) {
        DDLog.d(TAG, "缓存连接设备");
        if (null == bleDeviceManager || null == bleDeviceManager.getBleDevice()) {
            DDLog.e(TAG, "无法缓存空的数据");
            return;
        }
        connectedDeviceMap.put(bleDeviceManager.getBleDevice().getKey(), bleDeviceManager);
        connectedDeviceList.add(bleDeviceManager);
    }

    /**
     * 断开连接，移除缓存
     *
     * @param bleDeviceManager 断开连接的设备
     */
    public synchronized void removeConnectDevice(BleDeviceManager bleDeviceManager) {
        DDLog.d(TAG, "移除断开连接设备的缓存");
        if (null == bleDeviceManager || null == bleDeviceManager.getBleDevice()) {
            DDLog.e(TAG, "无法移除空的数据");
            return;
        }
        connectedDeviceMap.remove(bleDeviceManager.getBleDevice().getKey());
        connectedDeviceList.remove(bleDeviceManager);
    }

    /**
     * 获取最后一个连接的设备
     *
     * @return 最后一个连接的设备
     */
    public synchronized BleDeviceManager getLastBleDeviceManager() {
        DDLog.d(TAG, "getLastBleDeviceManager");
        if (0 >= connectedDeviceList.size()) {
            DDLog.e(TAG, "当前没有已连接的设备");
            return null;
        }

        return connectedDeviceList.get(connectedDeviceList.size() - 1);
    }
}
