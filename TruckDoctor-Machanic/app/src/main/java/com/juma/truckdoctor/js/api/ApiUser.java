package com.juma.truckdoctor.js.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.juma.truckdoctor.js.base.BaseApplication;
import com.juma.truckdoctor.js.model.User;
import com.juma.truckdoctor.js.utils.CacheManager;
import com.juma.truckdoctor.js.utils.CacheTask;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by hedong on 16/8/4.
 * 与用户相关的接口调用
 */

public class ApiUser {
    private static final String TAG = ApiUser.class.getSimpleName();
    public static final MediaType MEDIA_TYPE_JSON =
            MediaType.parse("application/json; charset=utf-8");

    /**
     * 获取本地存储的用户信息
     * @param context
     * @param callBack
     */
    public static void getLocalUserData(Context context, final ApiResponse<User> callBack) {
        new CacheTask<User>(context) {

            @Override
            public User parseTask(Serializable serializable) {
                return (User) serializable;
            }

            @Override
            public void parseTaskCallBack(User result) {
                if(result == null) {
                    callBack.onError(new Exception("user not exist"));
                    return;
                }

                callBack.onSuccess(result);
            }
        }.execute(BaseApplication.STORAGE_USER_KEY);
    }

    /**
     * 异步执行登录: 账号密码登录
     * @param phone     手机
     * @param password  密码
     * @param callback  请求回调
     * {url = /artificer/login}
     */
    public static void asyncLoginByAccount(final String phone, final String password,
                                  final ApiResponse<User> callback) {
        String url = Api.getBaseUrl() + "/artificer/login";
        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        params.put("password", password);

        OkHttpUtils.postString()
                .url(url)
                .mediaType(MEDIA_TYPE_JSON)
                .content(new Gson().toJson(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("code") == 0) {
                                User user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                                user.setPassword(password);
                                user.setPhone(phone);
                                //保存用户信息
                                new CacheManager.SaveCacheTask(BaseApplication.getContext(), user,
                                        BaseApplication.STORAGE_USER_KEY).execute();
                                //回调用户信息
                                callback.onSuccess(user);
                            } else {
                                //账号异常
                                callback.onError(new Exception(jsonObject.getString("message")));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 异步执行登录: 手机验证码登录
     * @param phone     手机
     * @param password  密码
     * @param callback  请求回调
     * {url = /artificer/loginByCode}
     */
    public static void asyncLoginByCode(final String phone, final String password,
                                           final ApiResponse<User> callback) {
        String url = Api.getBaseUrl() + "/artificer/loginByCode";
        Map<String, String> params = new HashMap<>();
        params.put("password", password);

        OkHttpUtils.postString()
                .url(url)
                .mediaType(MEDIA_TYPE_JSON)
                .content(new Gson().toJson(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("code") == 0) {
                                User user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                                user.setPhone(phone);
                                //保存用户信息
                                new CacheManager.SaveCacheTask(BaseApplication.getContext(), user,
                                        BaseApplication.STORAGE_USER_KEY).execute();
                                //回调用户信息
                                callback.onSuccess(user);
                            } else {
                                //账号异常
                                callback.onError(new Exception(jsonObject.getString("message")));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取短信验证码
     * @param phone
     * @param callBack
     * {url = /artificer/sendCode}
     */
    public static void getVerifyCode(String phone, final ApiResponse<String> callBack) {
        String url = Api.getBaseUrl()+ "/artificer/sendCode";
        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        OkHttpUtils.postString()
                .url(url)
                .mediaType(MEDIA_TYPE_JSON)
                .content(new Gson().toJson(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, e.toString());
                        callBack.onError(new Exception("网络访问失败"));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0) {
                                callBack.onSuccess("短信已发送成功");
                            }else {
                                callBack.onSuccess(jsonObject.getString("message"));
                            }
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
