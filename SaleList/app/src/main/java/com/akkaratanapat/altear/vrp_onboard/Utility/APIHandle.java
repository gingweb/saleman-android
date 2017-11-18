package com.akkaratanapat.altear.vrp_onboard.Utility;

import android.content.Context;
import android.util.Log;

import com.akkaratanapat.altear.daogenerator.APIFailure;
import com.akkaratanapat.altear.daogenerator.APIFailureDao;
import com.akkaratanapat.altear.daogenerator.DaoSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Altear on 10/8/2016.
 */

public class APIHandle {

    private APIHandle.ApiHandlerListener apiHandlerListener;
    private OkHttpClient okHttpClient;

    public enum APIName {
        SIGNIN,
        SIGNOUT,
        FIND_JOB,
        INTERVAL_TRACK,
        TRACK_LOCATION,
        JOB_REVIEW,
        IMAGE,
        SEND_LOG,
        SEND_NEAR
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    //private static final String BASE_URL = "https://fleet.qa.aot-hotel.transcodeglobal.com/";
    private static final String BASE_URL = "https://fleet.vrpadvance.com/";
    private static final String SIGNIN_URL = BASE_URL + "onboard/login";
    private static final String SIGNOUT_URL = BASE_URL + "onboard/logout";
    private static final String FIND_JOB_URL = BASE_URL + "onboard/findjob";
    private static final String INTERVAL_TRACKING_URL = BASE_URL + "onboard/jobtracking/track";
    private static final String TRACKING_URL = BASE_URL + "onboard/jobtracking/send";
    private static final String REVIEW_JOB_URL = BASE_URL + "onboard/review/point/save";
    private static final String SEND_IMAGE_URL = BASE_URL + "onboard/review/image/save?job_code=";
    private static final String SEND_LOG_URL = BASE_URL + "onboard/jobtracking/force";
    private static final String SEND_NEAR_URL = BASE_URL + "onboard/jobtracking/nearhotel";
    private DataPreferences dataPreferences;
    private APIFailureDao apiFailureDao;

    public APIHandle(Context context) {
        okHttpClient = new OkHttpClient();
        setGreenDAO(context);
    }

    public String getBaseURL(){
        return BASE_URL;
    }

    public void setApiHandlerListener(APIHandle.ApiHandlerListener listener) {
        this.apiHandlerListener = listener;
    }

    public void setDataPreferences(DataPreferences dataPreferences) {
        this.dataPreferences = dataPreferences;
    }

    public void requestLogin(String[] codeValue, String[] stringBody, double[] latlngValue) {
        JSONObject params = new JSONObject();
        Log.d("Login", "Login");
        try {
            params.put("dispatcher_code", codeValue[0]);
            params.put("driver_code", codeValue[1]);
            params.put("veh_code", codeValue[2]);
            params.put("sn", stringBody[0]);
            params.put("lat", latlngValue[0]);
            params.put("lng", latlngValue[1]);
            params.put("ts", stringBody[1]);
            postRequest(APIHandle.APIName.SIGNIN.toString(), SIGNIN_URL, params.toString(), true);
        } catch (JSONException e) {
            if (apiHandlerListener != null) {
                apiHandlerListener.onFailure(APIName.SIGNOUT.toString(), SIGNIN_URL, params.toString(), e);
            }
        }
    }

    public void requestLogout(String dispatcherCode, String driverCode, String vehicleCode, String imei, String timestamp, double lat, double lng) {
        JSONObject params = new JSONObject();
        try {
            params.put("dispatcher_code", dispatcherCode);
            params.put("driver_code", driverCode);
            params.put("veh_code", vehicleCode);
            params.put("sn", imei);
            params.put("lat", lat);
            params.put("lng", lng);
            params.put("ts", timestamp);
            postRequest(APIName.SIGNOUT.toString(), SIGNOUT_URL, params.toString(), true);
        } catch (JSONException e) {
            if (apiHandlerListener != null)
                apiHandlerListener.onFailure(APIName.SIGNOUT.toString(), SIGNOUT_URL, params.toString(), e);
        }
    }

    public void requestFindJob(int dispatcherID, int driverID, int vehicleID, String jobCode) {
        JSONObject params = new JSONObject();
        try {
            params.put("dispatcher_id", dispatcherID);
            params.put("driver_id", driverID);
            params.put("veh_id", vehicleID);
            params.put("job_code", jobCode);
            postRequest(APIName.FIND_JOB.toString(), FIND_JOB_URL, params.toString(), true);
        } catch (JSONException e) {
            if (apiHandlerListener != null)
                apiHandlerListener.onFailure(APIName.SIGNOUT.toString(), FIND_JOB_URL, params.toString(), e);
        }
    }

    public void requestIntervalTrackingJob(String[] StringValue, double[] doubleValue, String time) {
        JSONObject params = new JSONObject();
        try {
            params.put("veh_id", StringValue[0]);
            params.put("driver_id", StringValue[1]);
//            if (StringValue[3].equals("-999") || StringValue[4].equals("-999") || dataPreferences.getOnJob() == 0) {
//                params.put("cus_id", "");
//                params.put("job_id", "");
//                params.put("job_status", "");
//            } else
            if (dataPreferences.getOnJob() == 1) {
                params.put("cus_id", StringValue[2]);
                params.put("job_id", StringValue[3]);
                params.put("job_status", StringValue[4]);
            }
            params.put("lat", doubleValue[0]);
            params.put("lng", doubleValue[1]);
            params.put("ts", time);
            postRequest(APIName.INTERVAL_TRACK.toString(), INTERVAL_TRACKING_URL, params.toString(), false);
        } catch (JSONException e) {
            if (apiHandlerListener != null)
                apiHandlerListener.onFailure(APIName.INTERVAL_TRACK.toString(), INTERVAL_TRACKING_URL, params.toString(), e);
        }
    }

    public void requestTrackingJob(int[] idValue, String jobCode, int status) {
        JSONObject params = new JSONObject();
        try {
            params.put("dispatcher_id", idValue[0]);
            params.put("driver_id", idValue[1]);
            params.put("veh_id", idValue[2]);
            params.put("job_code", jobCode);
            params.put("job_status", status);

            postRequest(APIName.TRACK_LOCATION.toString(), TRACKING_URL, params.toString(), false);
        } catch (JSONException e) {
            if (apiHandlerListener != null)
                apiHandlerListener.onFailure(APIName.SIGNOUT.toString(), TRACKING_URL, params.toString(), e);
        }
    }

    public void requestReviewJob(int rate, String jobCode, byte[] file) {
        JSONObject params = new JSONObject();
        try {
            params.put("rate", rate);
            params.put("job_code", jobCode);
            postRequest(APIName.JOB_REVIEW.toString(), REVIEW_JOB_URL, params.toString(), true);
            postMultipart(APIName.IMAGE.toString(), SEND_IMAGE_URL + jobCode, file);
        } catch (JSONException e) {
            if (apiHandlerListener != null) {
                apiHandlerListener.onFailure(APIName.SIGNOUT.toString(), REVIEW_JOB_URL, params.toString(), e);
                apiHandlerListener.onFailure(APIName.IMAGE.toString(), SEND_IMAGE_URL + jobCode, Arrays.toString(file), e);
            }
        }
    }

    public void sendLogJob(String job_id, int[] status, String time, double[] latlng, String desc) {
        JSONObject params = new JSONObject();
        try {
            params.put("job_id", job_id);
            params.put("old_status", status[0]);
            params.put("new_status", status[1]);
            params.put("ts", time);
            params.put("lat", latlng[0]);
            params.put("lng", latlng[1]);
            params.put("desc", desc);

            postRequest(APIName.SEND_LOG.toString(), SEND_LOG_URL, params.toString(), true);

        } catch (JSONException e) {
            if (apiHandlerListener != null) {
                apiHandlerListener.onFailure(APIName.SEND_LOG.toString(), SEND_LOG_URL, params.toString(), e);

            }
        }
    }

    public void sendNearJob(String job_id) {
        JSONObject params = new JSONObject();
        try {
            params.put("job_id", job_id);
            postRequest(APIName.SEND_NEAR.toString(), SEND_NEAR_URL, params.toString(), false);
        } catch (JSONException e) {
            if (apiHandlerListener != null) {
                apiHandlerListener.onFailure(APIName.SEND_NEAR.toString(), SEND_NEAR_URL, params.toString(), e);

            }
        }
    }

    private void postRequest(final String name, final String url, final String params, final boolean showLoading) {
        //reCall();
        final RequestBody body = RequestBody.create(JSON, params);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        if (showLoading) {
            apiHandlerListener.onStartLoading();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (response.isSuccessful()) {

                    if (apiHandlerListener != null) {
                        if (showLoading) {
                            apiHandlerListener.onFinishLoading();
                        }
                        try {
                            Log.d("request" + name, "success");
                            apiHandlerListener.onSuccess(name, new JSONObject(responseBody.string()));
                        } catch (JSONException e) {
                            Log.d("request" + name, "fail" + e.toString());
                            apiHandlerListener.onFailure(name, url, params, e);
                        }
                    } else {
                        Log.d("listener", "null");
                    }
                } else {
                    if (showLoading) {
                        apiHandlerListener.onFinishLoading();
                    }
                    if (responseBody != null) {
                        apiHandlerListener.onBodyError(responseBody);
                    } else {
                        apiHandlerListener.onBodyErrorIsNull();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (showLoading) {
                    apiHandlerListener.onFinishLoading();
                }

                if (apiHandlerListener != null) {
                    apiHandlerListener.onFailure(name, url, params, e);
                }
            }
        });
    }

    private void postMultipart(final String name, final String url, final byte[] file) {

        final RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", "no-json.png", RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (response.isSuccessful()) {
                    if (apiHandlerListener != null) {
                        try {
                            Log.d("request" + name, "success");
                            apiHandlerListener.onSuccess(name, new JSONObject(responseBody.string()));
                        } catch (JSONException e) {
                            Log.d("request" + name, "fail" + e.toString());
                            apiHandlerListener.onFailure(name, url, Arrays.toString(file), e);
                        }
                    } else {
                        Log.d("listener", "null");
                    }
                } else {
                    if (responseBody != null) {
                        apiHandlerListener.onBodyError(responseBody);
                    } else {
                        apiHandlerListener.onBodyErrorIsNull();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (apiHandlerListener != null) {
                    apiHandlerListener.onFailure(name, url, Arrays.toString(file), e);
                }
            }
        });
    }

    private void setGreenDAO(Context context) {
        GreenDaoApplication application = (GreenDaoApplication) context.getApplicationContext();
        DaoSession daoSession = application.getDaoSession();
        apiFailureDao = daoSession.getAPIFailureDao();
    }

    public void addAPIFailure(String url, String name, String param) {
        APIFailure apiFailure = new APIFailure(null, name, url, param);
        apiFailureDao.insert(apiFailure);
    }

    public void reCall() {
        if (dataPreferences.getOnRecall() == 1) {
            List<APIFailure> apiFailures = apiFailureDao.loadAll();
            for (APIFailure apiFailure : apiFailures) {
                long id = apiFailure.getId();
                String name = apiFailure.getName();
                String param = apiFailure.getParam();
                String url = apiFailure.getUrl();
                postRequest(name, url, param, false);
                apiFailureDao.deleteByKey(id);
            }
            dataPreferences.edit().putOnRecall(0).apply();
        }
    }

//    private void getRequest(final APIHandle.APIName name, String url, final Dialog loadingDialog) {
//        final Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        if (loadingDialog != null)
//            loadingDialog.show();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (loadingDialog != null)
//                    loadingDialog.dismiss();
//
//                if (listener != null) {
//                    listener.onFailure(name, e);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (listener != null) {
//                    try {
//                        listener.onSuccess(name, new JSONObject(response.body().string()));
//                    } catch (JSONException e) {
//                        listener.onFailure(name, e);
//                    }
//                }
//
//                if (loadingDialog != null)
//                    loadingDialog.dismiss();
//            }
//        });
//    }

    public interface ApiHandlerListener {

        void onSuccess(String name, JSONObject json) throws JSONException;

        void onBodyError(ResponseBody responseBodyError);

        void onBodyErrorIsNull();

        void onFailure(String name, String url, String param, Exception e);

        void onStartLoading();

        void onFinishLoading();
    }
}
