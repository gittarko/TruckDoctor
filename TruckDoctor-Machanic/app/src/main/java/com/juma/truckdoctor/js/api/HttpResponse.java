package com.juma.truckdoctor.js.api;

import okhttp3.Call;

/**
 * Created by hedong on 16/8/4.
 */

public interface HttpResponse<T> {
    public void onSuccess(T response);

    public void onError(Call request, Exception e);

}
