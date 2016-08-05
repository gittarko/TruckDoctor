package com.juma.truckdoctor.js.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.juma.truckdoctor.js.R;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class SoundPoolManager implements SoundPool.OnLoadCompleteListener {

    private static final String TAG = SoundPoolManager.class.getSimpleName();
    private static final int[] RES_IDS = {R.raw.noti_1, R.raw.noti_2, R.raw.noti_3, R.raw.noti_4, R.raw.noti_5, R.raw.noti_6};
    private static SoundPoolManager soundPoolManager = null;
    private SoundPool soundPool;
    private Context context;

    public static SoundPoolManager getSoundPoolManager(Context context) {
        if (soundPoolManager == null) {
            synchronized ((SoundPoolManager.class)) {
                if (soundPoolManager == null) {
                    soundPoolManager = new SoundPoolManager(context);
                }
            }
        }
        return soundPoolManager;
    }

    private SoundPoolManager(Context context) {
        this.context = context.getApplicationContext();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
    }

//    private int getSoundId(Context context, int index) {
//        if (index >= 0 && index < RES_IDS.length) {
//            if (!soundMap.containsKey(index)) {
//                int soundId = soundPool.load(context, RES_IDS[index], 1);
//                if (soundId != 0) {
//                    soundMap.put(index, soundId);
//                }
//                Log.d(TAG, "getSoundId: load from soundPool!");
//                return soundId;
//            }
//            Log.d(TAG, "getSoundId: load from map!");
//            return soundMap.get(index);
//        }
//        return 0;
//    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            Log.d(TAG, "onLoadComplete load success with sampleId: " + sampleId);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int audioMaxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int audioCurrentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float audioRatio = (float) audioCurrentVolum / (float) audioMaxVolum;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioMaxVolum, 0);
            soundPool.play(sampleId, audioRatio, audioRatio, 1, 0, 1.0f);
        } else {
            Log.d(TAG, "onLoadComplete load fail with sampleId:" + sampleId);
        }
    }

    public void playNotificationSound(int index) {
        Log.d(TAG, "playNotificationSound: " + index);
        //先把系统音量调到最大
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int audioMaxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioMaxVolum, 0);
        if (index >= 0 && index < RES_IDS.length) {
            int soundId = soundPool.load(context, RES_IDS[index], 1);
            Log.d(TAG, "load soundId: " + soundId);
        }
    }
}
