package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/6/2.
 */

public class CacheUtils {
    /**
     * 保存boolean类型的数据
     * @param context
     * @param key
     * @param b
     */
    public static void putBoolean(Context context, String key, boolean b) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,b).commit();
    }

    /**
     * 得到保存的值
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }


    /**
     * 保存文本数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();

        //使用文件存储
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                //sdcard/beijingnews/files/ljsk;l;;llkklll;lkjj;
                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/files";

                //文件名称
                String fileName = MD5Encoder.encode(key);
                //sdcard/beijingnews/files/ljsk;l;;llkkljhjjsk
                File file = new File(dir,fileName);

                //得到/sdcard/beijingnews/files
                File parentFile = file.getParentFile();
                if(!parentFile.exists()) {
                    //如果不存在则创建该目录
                    parentFile.mkdirs();
                }

                if(!file.exists()) {
                    //创建该文件
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(value.getBytes());

                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 得到缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        String values = "";
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        values = sp.getString(key,"");

        //从文件中获取数据
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                //sdcard/beijingnews/files/ljsk;l;;llkklll;lkjj;
                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/files";

                //文件名称
                String fileName = MD5Encoder.encode(key);
                //sdcard/beijingnews/files/ljsk;l;;llkkljhjjsk
                File file = new File(dir,fileName);

                if(file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buttfer = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length;
                    while((length = fis.read(buttfer)) != -1) {
                        baos.write(buttfer,0,length);
                    }

                    String content = baos.toString();
                    if(!TextUtils.isEmpty(content)) {
                        values = content;
                        Log.e("TAG","从本地缓存文件中读数据...");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return values;
    }
}
