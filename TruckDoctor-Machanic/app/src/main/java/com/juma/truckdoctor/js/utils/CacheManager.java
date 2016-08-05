package com.juma.truckdoctor.js.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by hedong on 16/8/4.
 *
 * 本地缓存管理
 */

public class CacheManager {
    /**
     * 保存对象
     * @param ser
     * @param key
     */
    public static boolean saveObject(Context context, Serializable ser,
                                     String key) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param key
     * @return
     */
    public static Serializable readObject(Context context, String key) {
        if (!isExistDataCache(context, key))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(key);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                clearCacheData(context, key);
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    public static boolean isExistDataCache(Context context, String key) {
        if (context == null)
            return false;
        boolean exist = false;
        File data = context.getFileStreamPath(key);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 删除缓存文件
     * @param context
     * @param key
     */
    public static boolean clearCacheData(Context context, String key) {
        File data = context.getFileStreamPath(key);
        return data.delete();
    }

    /**
     * 保存缓存线程
     */
    public static class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        public SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

}
