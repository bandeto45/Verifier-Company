package ai.advance.global.iqa;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ai.advance.common.utils.JsonUtils;
import ai.advance.common.utils.LogUtil;
import ai.advance.sdk.global.iqa.GlobalIQAActivity;
import ai.advance.sdk.global.iqa.lib.GlobalIQAResult;
import ai.advance.sdk.global.iqa.lib.GlobalIQASDK;
import ai.advance.sdk.global.iqa.lib.IQAExtras;
import ai.advance.sdk.global.iqa.lib.UIExtras;
import ai.advance.sdk.global.iqa.lib.enums.CardSide;
import ai.advance.sdk.global.iqa.lib.enums.CardType;
import ai.advance.sdk.global.iqa.lib.enums.ScreenOrientation;

/**
 * This class echoes a string called from JavaScript.
 */
public class GlobalIQAPlugin extends CordovaPlugin {
    private static final String LOG_TAG = "[GlobalIQAPlugin]";
    public Activity activity;
    /**
     * requestCode
     */
    private static final int REQUEST_CODE_GLOBAL_IQA = 11203;
    private static final String ACTION_INIT = "initSDK";
    private static final String ACTION_SET_LICENSE_AND_CHECK = "setLicenseAndCheck";
    private static final String ACTION_START_DETECTION = "startGlobalIQADetection";
    private static final String ACTION_GET_RESULT_DATA = "getResultData";
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
            GlobalIQASDK.init(activity.getApplication());
            return true;
        } else if (ACTION_SET_LICENSE_AND_CHECK.equals(action)) {
            setLicenseAndCheck(args, callbackContext);
            return true;
        } else if (ACTION_BIND_USER.equals(action)) {
            LogUtil.sdkLogI(LOG_TAG + "bind user:" + args);
            GlobalIQASDK.bindUser(args.getString(0));
            return true;
        } else if (ACTION_START_DETECTION.equals(action)) {
            startDetection(args, callbackContext);
            return true;
        } else if (ACTION_GET_RESULT_DATA.equals(action)) {
            callbackContext.success(getResultData());
            return true;
        } else if (ACTION_SET_LOG_ENABLE.equals(action)) {
            LogUtil.sdkLogI(LOG_TAG + "set log enable:" + args);
            GlobalIQASDK.setLogEnable(args.getBoolean(0));
            return true;
        }
        return false;
    }

    private void startDetection(JSONArray args, CallbackContext callbackContext) {
        LogUtil.sdkLogI(LOG_TAG + "startGlobalIQADetection input params:" + args);
        mStartActivityCallBack = callbackContext;
        JSONObject extrasObject = null;
        try {
            extrasObject = args.getJSONObject(0);
        } catch (JSONException e) {
            LogUtil.sdkLogE(e.getMessage());
        }
        Intent intent = new Intent(cordova.getActivity(), GlobalIQAActivity.class);
        if (extrasObject != null) {
            boolean soundPlayEnable = extrasObject.optBoolean("soundPlayEnable", true);
            String region = JsonUtils.getString(extrasObject, "region");
            String cardType = JsonUtils.getString(extrasObject, "cardType");
            String cardSide = JsonUtils.getString(extrasObject, "cardSide");
            intent.putExtra(GlobalIQAActivity.EXTRA_IQA_EXTRAS,
                    new IQAExtras.Builder(region, CardType.valueOf(cardType), CardSide.valueOf(cardSide)).setSoundPlayEnable(soundPlayEnable).build());
            UIExtras.Builder uiBuilder = new UIExtras.Builder();
            if (extrasObject.has("pageColor")) {
                uiBuilder.setPageColor(JsonUtils.getInt(extrasObject, "pageColor"));
            }
            if (extrasObject.has("primaryTextColor")) {
                uiBuilder.setPrimaryTextColor(JsonUtils.getInt(extrasObject, "primaryTextColor"));
            }
            if (extrasObject.has("frameRectColor")) {
                uiBuilder.setFrameRectColor(JsonUtils.getInt(extrasObject, "frameRectColor"));
            }
            if (extrasObject.has("titleBackgroundColor")) {
                uiBuilder.setTitleBackGroundColor(JsonUtils.getInt(extrasObject, "titleBackgroundColor"));
            }
            boolean flipCameraBtnVisible = extrasObject.optBoolean("flipCameraBtnVisible", true);
            uiBuilder.setFlipCameraBtnVisible(flipCameraBtnVisible);
            boolean lightBtnVisible = extrasObject.optBoolean("lightBtnVisible", true);
            uiBuilder.setLightBtnVisible(lightBtnVisible);
            boolean tipIconVisible = extrasObject.optBoolean("tipIconVisible", true);
            uiBuilder.setTipIconVisible(tipIconVisible);
            if (extrasObject.has("titleTextColor")) {
                uiBuilder.setTitleTextColor(JsonUtils.getInt(extrasObject, "titleTextColor"));
            }
            if (extrasObject.has("cameraWidthPercentInPortraitState")) {
                uiBuilder.setCameraWidthPercentInPortraitState((float) extrasObject.optDouble("cameraWidthPercentInPortraitState", 0.8));
            }
            if (extrasObject.has("cameraHeightPercentInLandscapeState")) {
                uiBuilder.setCameraHeightPercentInLandscapeState((float) extrasObject.optDouble("cameraHeightPercentInLandscapeState", 0.7));
            }
            if (extrasObject.has("retakeBtnTextColor")) {
                uiBuilder.setRetakeBtnTextColor(JsonUtils.getInt(extrasObject, "retakeBtnTextColor"));
            }
            if (extrasObject.has("continueBtnTextColor")) {
                uiBuilder.setContinueBtnTextColor(JsonUtils.getInt(extrasObject, "continueBtnTextColor"));
            }
            if (extrasObject.has("takePhotoTipDialogShowSeconds")) {
                int takePhotoTipDialogShowSeconds = JsonUtils.getInt(extrasObject, "takePhotoTipDialogShowSeconds");
                uiBuilder.setTakePhotoTipDialogShowSeconds(takePhotoTipDialogShowSeconds);
            }
            if (extrasObject.has("scanLimitSeconds")) {
                int scanLimitSeconds = JsonUtils.getInt(extrasObject, "scanLimitSeconds");
                uiBuilder.setScanLimitSeconds(scanLimitSeconds);
            }
            if (extrasObject.has("countdownTimerVisible")) {
                boolean countdownTimerVisible = extrasObject.optBoolean("countdownTimerVisible", true);
                uiBuilder.setCountdownTimerVisible(countdownTimerVisible);
            }
            if (extrasObject.has("screenOrientation")) {
                uiBuilder.setScreenOrientation(ScreenOrientation.valueOf(JsonUtils.getString(extrasObject, "screenOrientation")));
            }
            intent.putExtra(GlobalIQAActivity.EXTRA_UI_EXTRAS,
                    uiBuilder.build());
        }
        cordova.startActivityForResult(this, intent, REQUEST_CODE_GLOBAL_IQA);

    }

    private void setLicenseAndCheck(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String license = args.getString(0);
        String checkResult = GlobalIQASDK.setLicenseAndCheck(license);
        LogUtil.sdkLogI(LOG_TAG + "license check result:" + checkResult);
        if ("SUCCESS".equals(checkResult)) {
            callbackContext.success();
        } else {
            callbackContext.error(checkResult);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_GLOBAL_IQA) {
            try {
                mStartActivityCallBack.success(getResultData());
            } catch (JSONException e) {
                LogUtil.sdkLogE(e.getMessage());
            }
        }
    }

    /**
     * generate success json format string
     */
    private String getResultData() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", GlobalIQAResult.isSuccess());
        jsonObject.put("code", GlobalIQAResult.getErrorCode());
        jsonObject.put("image", GlobalIQAResult.getBase64Image());
        jsonObject.put("IDVID", GlobalIQAResult.getIDVID());
        jsonObject.put("errorMessage", GlobalIQAResult.getErrorMsg());
        jsonObject.put("isPay", GlobalIQAResult.isPay());
        jsonObject.put("transactionId", GlobalIQAResult.getTransactionId());
        jsonObject.put("ocrResult", GlobalIQAResult.getOCRResult());
        jsonObject.put("idForgeryResult", GlobalIQAResult.getIdForgeryResult());
        jsonObject.put("pictureType", GlobalIQAResult.getPictureType());
        LogUtil.sdkLogI(LOG_TAG + "original result:" + jsonObject);
        return jsonObject.toString();
    }

}
