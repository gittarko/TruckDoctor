package com.juma.truckdoctor.js.api;

import android.content.Context;

import com.google.gson.Gson;
import com.juma.truckdoctor.js.base.BaseApplication;
import com.juma.truckdoctor.js.model.User;
import com.juma.truckdoctor.js.utils.CacheTask;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;

import okhttp3.Call;

/**
 * Created by hedong on 16/8/4.
 * 与用户相关的接口调用
 */

public class ApiUser {
    private static final String TAG = ApiUser.class.getSimpleName();

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

    public static void asyncLogin(String phone, String password,
                                  final ApiResponse<User> callback) {
        String url = Api.getUrl() + "";
        OkHttpUtils.post()
                .url(url)
                .addParams("phone", phone)
                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        User user = new Gson().fromJson(response, User.class);
                        callback.onSuccess(user);
                    }
                });
    }

}
