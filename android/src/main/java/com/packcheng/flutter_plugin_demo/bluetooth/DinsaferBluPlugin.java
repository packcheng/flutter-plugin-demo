package com.packcheng.flutter_plugin_demo.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.packcheng.flutter_plugin_demo.bluetooth.callback.impl.BleListener;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

/**
 * 蓝牙插件
 *
 * @author packcheng <a href="mailto:packcheng_jo@outlook.com">Contact me.</a>
 * @since 2021/4/2 3:48 PM
 */
public class DinsaferBluPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, PluginRegistry.RequestPermissionsResultListener {
    private static final String TAG = "DinsaferBluPlugin";

    private final Object initializationLock = new Object();
    private Context context;
    private MethodChannel channel;
    private EventChannel stateChannel;
    private static final String NAMESPACE = "com.liferadar";

    private FlutterPluginBinding pluginBinding;
    private ActivityPluginBinding activityBinding;
    private Application application;
    private Activity activity;

    /**
     * Plugin registration.
     */
    public static void registerWith(PluginRegistry.Registrar registrar) {
        DinsaferBluPlugin instance = new DinsaferBluPlugin();
        Activity activity = registrar.activity();
        Application application = null;
        if (registrar.context() != null) {
            application = (Application) (registrar.context().getApplicationContext());
        }
        instance.setup(registrar.messenger(), application, activity, registrar, null);
    }

    public DinsaferBluPlugin() {
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        pluginBinding = binding;
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        pluginBinding = null;

    }

    @Override
    public void onMethodCall(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {

    }


    private void setup(
            final BinaryMessenger messenger,
            final Application application,
            final Activity activity,
            final PluginRegistry.Registrar registrar,
            final ActivityPluginBinding activityBinding) {
        synchronized (initializationLock) {
            DDLog.i(TAG, "setup");
            this.activity = activity;
            this.application = application;
            this.context = application;
            channel = new MethodChannel(messenger, NAMESPACE + "/methods");
            channel.setMethodCallHandler(this);
            stateChannel = new EventChannel(messenger, NAMESPACE + "/state");
            stateChannel.setStreamHandler(new BleListener());

            if (registrar != null) {
                // V1 embedding setup for activity listeners.
                registrar.addRequestPermissionsResultListener(this);
            } else {
                // V2 embedding setup for activity listeners.
                activityBinding.addRequestPermissionsResultListener(this);
            }
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
        return false;
    }

    private void invokeMethodUIThread(final String name, final byte[] byteArray) {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        channel.invokeMethod(name, byteArray);
                    }
                });
    }
}
