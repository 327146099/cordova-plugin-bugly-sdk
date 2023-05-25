package com.polaris.cordova.bugly;

import android.content.Context;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.tencent.bugly.crashreport.crash.h5.H5JavaScriptInterface;
import org.apache.cordova.*;
import org.json.JSONException;
import org.json.JSONObject;

public class BuglySdk extends CordovaPlugin {
    public static final String TAG = "Cordova.Plugin.Bugly";
    private String APP_ID;
    private static final String BUGLY_APP_ID = "ANDROID_APPID";
    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";

    CrashReport.a jsReportHandler;

    private volatile H5JavaScriptInterface h5JavaScriptInterface;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        APP_ID = webView.getPreferences().getString(BUGLY_APP_ID, "");
        WebView view = (WebView) webView.getView();
        jsReportHandler = new CrashReport.a() {
            @Override
            public final String a() {
                return view.getUrl();
            }

            @Override
            public final void b() {
                WebSettings var1;
                if (!(var1 = view.getSettings()).getJavaScriptEnabled()) {
                    var1.setJavaScriptEnabled(true);
                }
            }

            @Override
            public final void a(String var1) {
                webView.loadUrl(var1);
            }

            @Override
            public final void a(H5JavaScriptInterface var1, String var2) {
            }

            @Override
            public final CharSequence c() {
                return view.getContentDescription();
            }
        };
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if ("initSDK".equals(action)) {
            return this.initSDK(args, callbackContext);
        } else if ("enableJSMonitor".equals(action)) {
            return this.enableJSMonitor(args, callbackContext);
        } else if ("setDeviceID".equals(action)) {
            return this.setDeviceID(args, callbackContext);
        } else if ("setDeviceModel".equals(action)) {
            return this.setDeviceModel(args, callbackContext);
        } else if ("setTagID".equals(action)) {
            return this.setTagID(args, callbackContext);
        } else if ("setUserID".equals(action)) {
            return this.setUserID(args, callbackContext);
        } else if ("putUserData".equals(action)) {
            return this.putUserData(args, callbackContext);
        } else if ("testJavaCrash".equals(action)) {
            return this.testJavaCrash(args, callbackContext);
        } else if ("testNativeCrash".equals(action)) {
            return this.testNativeCrash(args, callbackContext);
        } else if ("testANRCrash".equals(action)) {
            return this.testANRCrash(args, callbackContext);
        } else if ("reportJSException".equals(action)) {
            return this.reportJSException(args, callbackContext);
        }

        return false;
    }


    private boolean initSDK(CordovaArgs args, CallbackContext callbackContext) {
        final JSONObject params;

        try {
            params = args.getJSONObject(0);
            UserStrategy strategy = new UserStrategy(this.cordova.getActivity().getApplicationContext());

            if (params.has("channel")) {
                strategy.setAppChannel(params.getString("channel"));
            }
            if (params.has("version")) {
                strategy.setAppVersion(params.getString("version"));
            }
            if (params.has("package")) {
                strategy.setAppPackageName(params.getString("package"));
            }
            if (params.has("delay")) {
                strategy.setAppReportDelay(params.getInt("delay"));
            }
            if (params.has("develop")) {
                CrashReport.setIsDevelopmentDevice(this.cordova.getActivity().getApplicationContext(), params.getBoolean("develop"));
            } else {
                CrashReport.setIsDevelopmentDevice(this.cordova.getActivity().getApplicationContext(), BuildConfig.DEBUG);
            }

            boolean debugModel;

            if (params.has("debug")) {
                debugModel = params.getBoolean("debug");
            } else {
                debugModel = BuildConfig.DEBUG;
            }

            Context applicationContext = this.cordova.getActivity().getApplicationContext();

            this.cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    CrashReport.initCrashReport(applicationContext, APP_ID, debugModel, strategy);
                    Log.d(TAG, "bugly sdk init success!");
                    callbackContext.success();
                }
            });

        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }

        return true;
    }

    private boolean enableJSMonitor(CordovaArgs args, CallbackContext callbackContext) {
        this.cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (h5JavaScriptInterface == null) {
                    h5JavaScriptInterface = H5JavaScriptInterface.getInstance(jsReportHandler);
                }

                callbackContext.success();
            }
        });
        return true;
    }

    private boolean reportJSException(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String message = args.getString(0);

        this.cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (message == null) {
                    return;
                }
                if (h5JavaScriptInterface != null) {
                    h5JavaScriptInterface.reportJSException(message);
                    callbackContext.success();
                } else {
                    callbackContext.error("未开启javascript异常监控");
                }
            }
        });

        return true;
    }

    private boolean setTagID(CordovaArgs args, CallbackContext callbackContext) {
        try {
            int id = args.getInt(0);
            CrashReport.setUserSceneTag(this.cordova.getActivity().getApplicationContext(), id);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        callbackContext.success();
        return true;
    }

    private boolean setUserID(CordovaArgs args, CallbackContext callbackContext) {
        try {
            String id = args.getString(0);
            CrashReport.setUserId(this.cordova.getActivity().getApplicationContext(), id);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        callbackContext.success();
        return true;
    }

    private boolean setDeviceID(CordovaArgs args, CallbackContext callbackContext) {
        try {
            String id = args.getString(0);
            CrashReport.setDeviceId(this.cordova.getActivity().getApplicationContext(), id);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        callbackContext.success();
        return true;
    }

    private boolean setDeviceModel(CordovaArgs args, CallbackContext callbackContext) {
        try {
            String deviceModel = args.getString(0);
            CrashReport.setDeviceModel(this.cordova.getActivity().getApplicationContext(), deviceModel);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        callbackContext.success();
        return true;
    }

    private boolean putUserData(CordovaArgs args, CallbackContext callbackContext) {
        try {
            String key = args.getString(0);
            String value = args.getString(1);
            CrashReport.putUserData(this.cordova.getActivity().getApplicationContext(), key, value);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        callbackContext.success();
        return true;
    }

    private boolean testJavaCrash(CordovaArgs args, CallbackContext callbackContext) {
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CrashReport.testJavaCrash();
            }
        });
        callbackContext.success();
        return true;
    }

    private boolean testNativeCrash(CordovaArgs args, CallbackContext callbackContext) {
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CrashReport.testNativeCrash();
            }
        });
        callbackContext.success();
        return true;
    }

    private boolean testANRCrash(CordovaArgs args, CallbackContext callbackContext) {

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CrashReport.testANRCrash();
            }
        });
        callbackContext.success();
        return true;
    }

    @Override
    public void onDestroy() {
        CrashReport.closeBugly();
        this.h5JavaScriptInterface = null;
    }

}
