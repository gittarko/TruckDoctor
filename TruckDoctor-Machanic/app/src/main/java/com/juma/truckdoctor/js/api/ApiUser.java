package com.juma.truckdoctor.js.api;

import com.google.gson.Gson;
import com.juma.truckdoctor.js.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by hedong on 16/8/4.
 * 与用户相关的接口调用
 */

public class ApiUser {
    private static final String TAG = ApiUser.class.getSimpleName();

    public static void asyncLogin(String phone, String password,
                                  final HttpResponse<User> callback) {
        String url = Api.getUrl() + "";
        OkHttpUtils.post()
                .url(url)
                .addParams("phone", phone)
                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(call, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        User user = new Gson().fromJson(response, User.class);
                        callback.onSuccess(user);
                    }
                });
    }

}
