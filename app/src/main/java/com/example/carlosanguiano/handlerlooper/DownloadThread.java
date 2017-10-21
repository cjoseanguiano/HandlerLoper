package com.example.carlosanguiano.handlerlooper;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Carlos Anguiano on 21/10/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public final class DownloadThread extends Thread {
    private static final String TAG = DownloadThread.class.getSimpleName();
    private Handler handler;
    private int totalQueued;
    private int totalCompleted;
    private DownloadThreadListener listener;


    public DownloadThread(DownloadThreadListener context) {
        this.listener = context;
    }

    @Override
    public void run() {
        try {
            Looper.prepare();
            Log.i(TAG, "DownloadThread entering the loop");

            handler = new Handler();
            Looper.loop();
            Log.i(TAG, "DownloadThread exiting gracefully");
        } catch (Throwable t) {
            Log.e(TAG, "DownloadThread halted due to an error", t);
        }
    }

    public synchronized void requestStop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "DownloadThread loop quitting by request");
                Looper.myLooper().quit();
            }
        });
    }

    public synchronized void enqueueDownload(final DownloadTask downloadTask) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadTask.run();
                } finally {
                    synchronized (DownloadThread.this) {
                        totalCompleted++;
                    }
                    signalUpdate();
                }

            }
        });
        totalQueued++;
        signalUpdate();
    }

    private void signalUpdate() {
        if (listener != null) {
            listener.handleDownloadThreadUpdate();
        }
    }

    public synchronized int getTotalQueued() {
        return totalQueued;
    }

    public synchronized int getTotalCompleted() {
        return totalCompleted;
    }
}
