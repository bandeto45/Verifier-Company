package ai.advance.liveness;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import ai.advance.enums.DeviceType;
import ai.advance.liveness.lib.Detector;
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.LivenessResult;
import ai.advance.liveness.lib.Market;
import ai.advance.liveness.sdk.activity.LivenessActivity;

/**
 * This class echoes a string called from JavaScript.
 */
public class LivenessDetectionPlugin extends CordovaPlugin {
    public Activity activity;
    /**
     * requestCode
     */
    private static final int REQUEST_CODE_LIVENESS = 11202;
    private static final String ACTION_INIT = "initSDK";
    private static final String ACTION_INIT_LICENSE = "initSDKByLicense";
    private static final String ACTION_SET_LICENSE_AND_CHECK = "setLicenseAndCheck";
    private static final String ACTION_START_DETECTION = "startLivenessDetection";
    private static final String ACTION_DETECT_OCCLUSION = "isDetectOcclusion";
    private static final String ACTION_GET_RESULT_DATA = "getResultData";
    private static final String ACTION_SET_ACTION_SEQUENCE = "setActionSequence";
    private static final String ACTION_SET_RESULT_PICTURE_SIZE = "setResultPictureSize";
    private static final String ACTION_SET_ACTION_TIME_OUT_MILLS = "setActionTimeoutMills";

    private static final String ACTION_SET_LOG_ENABLE = "setLogEnable";
    private static final String ACTION_BIND_USER = "bindUser";
    private CallbackContext mStartActivityCallBack;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.activity = cordova.getActivity();
    }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (TextUtils.isEmpty(action)) {
            return false;
        }
        if (ACTION_INIT.equals(action)) {
            initSDK(args, callbackContext);
            return true;
        } else if (ACTION_INIT_LICENSE.equals(action)) {
            initSDKByLicense(args, callbackContext);
            return true;
        } else if (ACTION_SET_LICENSE_AND_CHECK.equals(action)) {
            setLicenseAndCheck(args, callbackContext);
            return true;
        } else if (ACTION_START_DETECTION.equals(action)) {
            mStartActivityCallBack = callbackContext;
            cordova.startActivityForResult(this, new Intent(cordova.getActivity(), LivenessActivity.class), REQUEST_CODE_LIVENESS);
            return true;
        } else if (ACTION_GET_RESULT_DATA.equals(action)) {
            callbackContext.success(getResultData());
            return true;
        } else if (ACTION_DETECT_OCCLUSION.equals(action)) {
            GuardianLivenessDetectionSDK.isDetectOcclusion(args.getBoolean(0));
            return true;
        } else if (ACTION_SET_RESULT_PICTURE_SIZE.equals(action)) {
            GuardianLivenessDetectionSDK.setResultPictureSize(args.getInt(0));
            return true;
        } else if (ACTION_SET_ACTION_TIME_OUT_MILLS.equals(action)) {
            GuardianLivenessDetectionSDK.setActionTimeoutMills(args.getLong(0));
            return true;
        } else if (ACTION_SET_ACTION_SEQUENCE.equals(action)) {
            setActionSequence(args, callbackContext);
            return true;
        } else if (ACTION_SET_LOG_ENABLE.equals(action)) {
            GuardianLivenessDetectionSDK.setLogEnable(args.getBoolean(0));
            return true;
        } else if (ACTION_BIND_USER.equals(action)) {
            GuardianLivenessDetectionSDK.bindUser(args.getString(0));
            return true;
        }
        return false;
    }

    private void setActionSequence(JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean shuffle = args.getBoolean(0);
        JSONArray actionArray = args.getJSONArray(1);
        Detector.DetectionType[] actionList = new Detector.DetectionType[actionArray.length()];
        ArrayList<String> availableActionNameList = new ArrayList<>();
        availableActionNameList.add("MOUTH");
        availableActionNameList.add("POS_YAW");
        availableActionNameList.add("BLINK");
        for (int i = 0; i < actionList.length; i++) {
            String actionName = actionArray.getString(i);
            if (availableActionNameList.contains(actionName)) {
                actionList[i] = Detector.DetectionType.valueOf(actionName);
            } else {
                callbackContext.error("Wrong Action type:" + actionName +
                        ", action must be in [MOUTH,POS_PAW,BLINK]");
                return;
            }
        }
        GuardianLivenessDetectionSDK.setActionSequence(shuffle, actionList);
    }

    private void setLicenseAndCheck(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String license = args.getString(0);
        String checkResult = GuardianLivenessDetectionSDK.setLicenseAndCheck(license);
        if ("SUCCESS".equals(checkResult)) {
            callbackContext.success();
        } else {
            callbackContext.error(checkResult);
        }
    }


    /**
     * init sdk
     */
    private void initSDK(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (args.length() < 3) {
            callbackContext.error("SDK init Parameter error");
        } else {
            String accessKey = (String) args.get(0);
            String secretKey = (String) args.get(1);
            String marketStr = (String) args.get(2);
            Market market;
            try {
                market = Market.valueOf(marketStr);
            } catch (Exception e) {
                callbackContext.error("Wrong market,the market must be one of :" + Arrays.asList(Market.values()));
                return;
            }
            boolean isGlobalService = false;
            try {
                isGlobalService = (boolean) args.get(3);
            } catch (Exception ignored) {

            }
            GuardianLivenessDetectionSDK.init(activity.getApplication(), accessKey, secretKey, market, isGlobalService);
            GuardianLivenessDetectionSDK.letSDKHandleCameraPermission();
            GuardianLivenessDetectionSDK.setDeviceType(Build.CPU_ABI.contains("x86") ? DeviceType.Emulator : DeviceType.RealPhone);
            callbackContext.success();
        }
    }

    /**
     * init sdk by license
     */
    private void initSDKByLicense(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (args.length() < 1) {
            callbackContext.error("SDK init Parameter error");
        } else {
            String marketStr = (String) args.get(0);
            Market market;
            try {
                market = Market.valueOf(marketStr);
            } catch (Exception e) {
                callbackContext.error("Wrong market,the market must be one of :" + Arrays.asList(Market.values()));
                return;
            }
            boolean isGlobalService = false;
            try {
                isGlobalService = (boolean) args.get(1);
            } catch (Exception ignored) {

            }
            GuardianLivenessDetectionSDK.init(activity.getApplication(), market, isGlobalService);
            GuardianLivenessDetectionSDK.letSDKHandleCameraPermission();
            GuardianLivenessDetectionSDK.setDeviceType(Build.CPU_ABI.contains("x86") ? DeviceType.Emulator : DeviceType.RealPhone);
            callbackContext.success();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_LIVENESS) {
            mStartActivityCallBack.success(getResultData());
        }
    }

    /**
     * generate success json format string
     *
     * @return
     */
    private String getResultData() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", LivenessResult.isSuccess());
        resultMap.put("code", LivenessResult.getErrorCode());
        resultMap.put("image", LivenessResult.getLivenessBase64Str());
        resultMap.put("livenessId", LivenessResult.getLivenessId());
        resultMap.put("errorMessage", LivenessResult.getErrorMsg());
        resultMap.put("isPay", LivenessResult.isPay());
        resultMap.put("transactionId", LivenessResult.getTransactionId());
        return mapToJsonStr(resultMap);
    }


    private String mapToJsonStr(HashMap<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String key : map.keySet()) {
            builder.append("\"").append(key).append("\":");
            Object value = map.get(key);
            if (value instanceof String) {
                builder.append("\"").append(value).append("\",");
            } else {
                builder.append(value).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

}
