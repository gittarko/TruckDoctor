package com.juma.truckdoctor.js.api;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by maimaiti on 2016/8/10 0010.
 *
 * app主页所有网络请求
 */

public class ApiWeb extends Api {
    public static final String TAG = ApiWeb.class.getSimpleName();

    /**
     * 获取未处理订单数
     * { url = /user/artificer/app/order/countByStatus}
     * result = {
     * "data": {
     "      total": 60,
     "      countList": [21, 10, 29]
            }
        }
     */
    public static void getUnprocessOrderNumber(final ApiResponse<Integer> callBack) {
        Log.e(TAG, "getUnprocessOrderNumber");
        String url = host + "/user/artificer/app/order/countByStatus";
        OkHttpUtils.postString()
                .url(url)
                .mediaType(MEDIA_TYPE_JSON)
                .content("{\"statusList\":[2,3,4]" +
                        "}")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(new Exception("网络连接错误"));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("code") == 0) {
                                int num = jsonObject.getJSONObject("data").getInt("total");
                                callBack.onSuccess(num);
                            }else {
                                callBack.onError(new Exception(jsonObject.getString("message")));
                            }
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
