package com.example.carlosanguiano.handlerlooper;

import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DownloadThreadListener {

    private DownloadThread downloadThread;
    private Handler handler;
    private ProgressBar progressBar;
    private TextView statusText;
    private Button scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_Bar);
        statusText = findViewById(R.id.status_text);
        scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(this);

        downloadThread = new DownloadThread(this);
        downloadThread.start();

        handler = new Handler();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadThread.requestStop();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.schedule_button) {
            int totalTask = new Random().nextInt(3) + 1;

            for (int i = 0; i < totalTask; ++i) {
                downloadThread.enqueueDownload(new DownloadTask());
            }
        }
    }

    @Override
    public void handleDownloadThreadUpdate() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int total = downloadThread.getTotalQueued();
                int completed = downloadThread.getTotalCompleted();

                progressBar.setMax(total);
                progressBar.setProgress(0);
                progressBar.setProgress(completed);

                statusText.setText(String.format("Downloaded %d/%d", completed, total));

                if (completed == total) {
                    ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                }
            }
        });
    }
}
