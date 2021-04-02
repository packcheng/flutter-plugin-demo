package com.packcheng.flutter_plugin_demo.bluetooth.manager;

import android.bluetooth.BluetoothGatt;
import android.os.Handler;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.packcheng.flutter_plugin_demo.bluetooth.DDLog;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleCommunicateListener;
import com.packcheng.flutter_plugin_demo.bluetooth.callback.IBleConnectListener;

/**
 * 连接设备通讯管理
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/1 3:54 PM
 */
public class BleDeviceManager {
    private static final String TAG = "BleDeviceManager";

    /**
     * 连接失败标记
     * <p>
     * 重置主机时一般需要连接两次才能连接成功。
     * 所以在第一次连接失败时，需要进行连接失败的标记而不是弹窗提示连接失败，
     * 只有第二次连接失败才提示连接失败信息。
     * </p>
     */
    static boolean isFail = false;

    /**
     * 蓝牙连接状态监听
     */
    private final BleGattCallback mBleGattCallback;
    private final BleWriteCallback mBleWriteCallback;
    private final BleNotifyCallback mBleNotifyCallback;
    private final BleDevice mBleDevice;

    private final IBleConnectListener mConnectListener;
    private final IBleCommunicateListener mCommunicateListener;

    public BleDeviceManager(BleDevice bleDevice, IBleConnectListener connectListener,
                            IBleCommunicateListener communicateListener) {
        this.mBleDevice = bleDevice;
        this.mConnectListener = connectListener;
        this.mCommunicateListener = communicateListener;

        mBleGattCallback = new BleGattCallback() {
            @Override
            public void onStartConnect() {
                DDLog.d(TAG, "开始连接");
                mConnectListener.onStartConnect(BleDeviceManager.this);
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                DDLog.d(TAG, "连接失败 " + exception.toString());
                // 连接失败
                if (isFail) {
                    // 第二次连接失败，提示连接失败对话框
                    DDLog.e(TAG, "正真连接失败了！！！！！！！");
                    mConnectListener.onConnectFail(BleDeviceManager.this, exception);
                    return;
                }
                // 第一次连接失败，标记连接失败
                isFail = true;
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                DDLog.d(TAG, "连接成功");
                BleController.getInstance().addConnectDevice(BleDeviceManager.this);
                mConnectListener.onConnectSuccess(BleDeviceManager.this, gatt, status);
                // 连接成功，BleDevice即为所连接的BLE设备
                // 连接成功后，清除连接失败的标记
                isFail = false;
                BleController.getInstance().setMtu(BleDeviceManager.this);

                new Handler().postDelayed(() -> BleController.getInstance()
                        .openNotify(BleDeviceManager.this), 500);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice,
                                       BluetoothGatt gatt, int status) {
                DDLog.d(TAG, "连接中断,是否为主动端口 " + isActiveDisConnected + " ,status is " + status);
                BleController.getInstance().removeConnectDevice(BleDeviceManager.this);
                mConnectListener.onDisConnected(BleDeviceManager.this,
                        isActiveDisConnected, gatt, status);
                /**
                 * 如果是主动断开，即连接完成的时候等。就不必弹出主机蓝牙断开窗口
                 * 如果是手机蓝牙没开的情况也是
                 */
                if (isActiveDisConnected) {
                    return;
                }
                if (!BleController.getInstance().isOpenBlue()) {
                    return;
                }

                BleManager.getInstance().disconnect(bleDevice);
            }
        };

        mBleWriteCallback = new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                DDLog.d(TAG, "写入成功");
                mCommunicateListener.onWriteSuccess(BleDeviceManager.this,
                        current, total, justWrite);
            }

            @Override
            public void onWriteFailure(final BleException exception) {
                DDLog.d(TAG, "写入失败:" + exception.toString());
                mCommunicateListener.onWriteFailure(BleDeviceManager.this, exception);
            }
        };

        mBleNotifyCallback = new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                // 打开通知操作成功
                DDLog.d(TAG, "打开通知操作成功");
                mCommunicateListener.onNotifySuccess(BleDeviceManager.this);
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                // 打开通知操作失败
                DDLog.d(TAG, "打开通知操作失败, " + exception);
                mCommunicateListener.onNotifyFailure(BleDeviceManager.this, exception);
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                // 打开通知后，设备发过来的数据将在这里出现
                DDLog.d(TAG, "打开通知后，设备发过来的数据将在这里出现:");
                mCommunicateListener.onCharacteristicChanged(BleDeviceManager.this, data);
            }
        };
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public BleGattCallback getBleGattCallback() {
        return mBleGattCallback;
    }

    public BleWriteCallback getBleWriteCallback() {
        return mBleWriteCallback;
    }

    public BleNotifyCallback getBleNotifyCallback() {
        return mBleNotifyCallback;
    }
}
