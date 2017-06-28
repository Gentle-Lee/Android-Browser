package com.example.android.android_browsesr;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadManagerActivity extends AppCompatActivity implements View.OnClickListener{

    static final String TAG = "MainActivity";
    EditText filePath,urlPath,threadNumber;
    TextView mMessageView;
    Button stratButton,pauseButton,resumeButton;
    String fileString,urlString;
    int num;
    public static ProgressBar progressBar;
    downloadTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"Creating");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadmanager_fragment);
        init();
        initVal();
        Intent intent = getIntent();
        String urlS = intent.getStringExtra("urlString");
        urlPath.setText(urlS);
    }

    void initVal(){
        System.out.println("initVal");
        urlString = urlPath.getText().toString();
        if (urlString.isEmpty())
            return;
        fileString = filePath.getText().toString();
        if (fileString.equals("")){
            fileString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/"+urlString.substring(urlString.lastIndexOf("/"));
            System.out.println("default path："+fileString);
            filePath.setText(fileString);
        }

        num = Integer.parseInt(threadNumber.getText().toString());

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                if (!NetWorkUtils.isNetworkConnected(DownLoadManagerActivity.this)){
                    Toast.makeText(DownLoadManagerActivity.this, "NetWork UnConnected", Toast.LENGTH_SHORT).show();
                    return;
                }
                initVal();
                initDownload();
                Toast.makeText(getBaseContext(),"START", Toast.LENGTH_SHORT).show();
                break;
            case R.id.resume:
                if (task== null){
                    Toast.makeText(getBaseContext(),"TASK:RESS　DOWNLOAD FIRST", Toast.LENGTH_SHORT).show();
                    return;
                }
                task.pauseFlag = false;
                Toast.makeText(getBaseContext(),"RESUME", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause:
                if (task== null){
                    Toast.makeText(getBaseContext(),"TASK:PRESS　DOWNLOAD FIRST", Toast.LENGTH_SHORT).show();
                    return;
                }
                task.pauseFlag = true;
                Toast.makeText(getBaseContext(),"PAUSE", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    void init(){
        //init EditText
        filePath =(EditText) findViewById(R.id.filePath);
        urlPath = (EditText)findViewById(R.id.urlPath);
        threadNumber = (EditText)findViewById(R.id.threadNumber);

        //init Button
        stratButton = (Button)findViewById(R.id.start);
        stratButton.setOnClickListener(this);
        resumeButton = (Button)findViewById(R.id.resume);
        resumeButton.setOnClickListener(this);
        pauseButton = (Button)findViewById(R.id.pause);
        pauseButton.setOnClickListener(this);


        //init ProgressBar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        //init TextView
        mMessageView = (TextView)findViewById(R.id.dowloadMessage);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            progressBar.setProgress(msg.getData().getInt("size"));

            float temp = (float) progressBar.getProgress()
                    / (float) progressBar.getMax();

            int progress = (int) (temp * 100);
            if (progress == 100) {
                Toast.makeText(DownLoadManagerActivity.this, "DOWNLOAD COMPELETED！", Toast.LENGTH_LONG).show();
                SQLiteOpenHelper dbHelper = new BrowserDBHelper(DownLoadManagerActivity.this,"DOWNLOADED_DB",null,2);
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("FileName",fileString);
                contentValues.put("FilePath",urlString);
                sqLiteDatabase.insertOrThrow("DOWNLOADED_DB",null,contentValues);
                sqLiteDatabase.close();
            }
            mMessageView.setText( progress + " %");

        }
    };

    private void initDownload() {
        // 设置progressBar初始化
        progressBar.setProgress(0);

        String downloadUrl = urlString;
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
        int threadNum = num;
        String filepath = fileString;
        Log.d(TAG, "download file  path:" + filepath);
        task = new downloadTask(downloadUrl, threadNum, filepath);
        task.start();
    }

    class downloadTask extends Thread {
        private String downloadUrl;
        private int threadNum;
        private String filePath;
        private int blockSize;
        boolean pauseFlag  = false;
        ThreadDownloader[] threads;
        public downloadTask(String downloadUrl, int threadNum, String filepath) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = filepath;
        }

        @Override
        public void run() {

            threads = new ThreadDownloader[threadNum];
            try {
                URL url = new URL(downloadUrl);
                Log.d(TAG, "download file http path:" + downloadUrl);
                URLConnection conn = url.openConnection();
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    Log.i(TAG, "run:UNABLE TO ACCESS FILE ");
                    Toast.makeText(getBaseContext(),"UNABLE TO ACCESS FILE", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setMax(fileSize);
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");
                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    threads[i] = new ThreadDownloader(url, file, blockSize, (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }
               try{
                   boolean isfinished = false;
                   int downloadedAllSize = 0;
                   while (!isfinished) {
                       isfinished = true;
                       downloadedAllSize = 0;
                       for (int i = 0; i < threads.length; i++) {
                           downloadedAllSize += threads[i].getDownloadLength();
                           if (!threads[i].isCompleted()) {
                               isfinished = false;
                           }
                       }
                       System.out.println("1111");
                       if (pauseFlag) {
                           while (true) {
                               for (int i = 0; i < threads.length; i++) {
                                   if (!threads[i].isCompleted()) {
                                       synchronized (threads[i]) {
                                           threads[i].sleep(2000);
                                       }
                                   }
                               }
                               if (!pauseFlag) {
                                   break;
                               }
                           }
                       }
                       Message msg = new Message();
                       msg.getData().putInt("size", downloadedAllSize);
                       mHandler.sendMessage(msg);
                       Thread.sleep(100);
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
