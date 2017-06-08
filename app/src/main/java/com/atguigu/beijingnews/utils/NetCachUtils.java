package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/6/7.
 */

public class NetCachUtils {


    public static final int SUCESS = 1;
    public static final int FAIL = 2;
    private final Handler handler;

    public NetCachUtils(Handler handler) {
        this.handler = handler;
    }

    public void getBitmapFromNet(final String imageUrl, final int position) {
        //联网请求开启子线程
        new Thread(){
            public void run(){
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    int code = urlConnection.getResponseCode();

                    if(code == 200) {
                        //网络请求成功
                        Log.e("TAG","网络请求图片--联网成功");
                        InputStream is = urlConnection.getInputStream();
                        //把流转换成bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        //然后发送到主线程
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = SUCESS;
                        msg.arg1 = position;
                        handler.sendMessage(msg);

                        //在内存保存一份


                        //在本地保存一份

                    }

                }  catch (IOException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = FAIL;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }

            }
        }.start();


    }
}
