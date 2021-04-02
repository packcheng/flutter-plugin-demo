package com.packcheng.flutter_plugin_demo.bluetooth.manager;

import android.os.Handler;

import com.packcheng.flutter_plugin_demo.bluetooth.APIKey;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LT on 2019/2/22.
 */
public class BleApi {

    public static void setWifiPassword(final String wifiPassword, final BleDeviceManager bleDeviceManager) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("CMD", "SetWifiPassword");
                    jsonObject.put("password", wifiPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BleController.getInstance().write(new byte[]{}, bleDeviceManager);

            }
        }, 200);
    }


    public static void stopBle(BleDeviceManager bleDeviceManager) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CMD", "StopBLE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BleController.getInstance().write(new byte[]{}, bleDeviceManager);
    }


    public static void getWifi(BleDeviceManager bleDeviceManager) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(APIKey.CMD, APIKey.getWifiList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BleController.getInstance().write(new byte[]{}, bleDeviceManager);
    }
}
