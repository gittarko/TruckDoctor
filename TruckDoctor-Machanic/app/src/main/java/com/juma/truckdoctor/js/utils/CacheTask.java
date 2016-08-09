package com.juma.truckdoctor.js.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/8/5 0005.
 */

public abstract class CacheTask<T> extends AsyncTask<String, Void, T> {
    private final WeakReference<Context> context;

    public CacheTask(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected T doInBackground(String[] params) {
        Serializable seri = CacheManager.readObject(context.get(), params[0]);
        if (seri == null) {
            return null;
        } else {
            return parseTask(seri);
        }
    }

    @Override
    protected void onPostExecute(T o) {
        super.onPostExecute(o);
        parseTaskCallBack(o);
    }

    /**
     * 解析数据的业务逻辑由自己实现
     * 该实现处于线程中
     * @param serializable
     * @return
     */
    public abstract T parseTask(Serializable serializable);

    /**
     * 解析数据业务完成
     * @param result
     */
    public abstract void parseTaskCallBack(T result);

}
