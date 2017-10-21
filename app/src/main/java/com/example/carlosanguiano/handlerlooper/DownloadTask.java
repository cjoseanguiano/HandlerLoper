package com.example.carlosanguiano.handlerlooper;

import android.util.Log;

import java.util.Random;

/**
 * Created by Carlos Anguiano on 21/10/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

class DownloadTask implements Runnable {
    private static final String TAG = DownloadTask.class.getSimpleName();
    private static final Random random = new Random();
    private int lengthSec;

    public DownloadTask() {
        lengthSec = random.nextInt(3) + 1;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(lengthSec * 1000);
        } catch (Throwable throwable) {
            Log.i(TAG, "run: " + throwable);
        }
    }
}
