package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/6/8.
 */

public class LocalCachUtils {

    /**
     * 保存图片
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap2Local(String imageUrl, Bitmap bitmap) {
        try {
            //sdcard/beijingnews/ljsk;l;;llkklll;lkjj;
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";

            //文件名称
            String fileName = MD5Encoder.encode(imageUrl);
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            File file = new File(dir,fileName);

            //得到/sdcard/beijingnews/
            File parentFile = file.getParentFile();
            if(!parentFile.exists()) {
                //如果不存在则创建该目录
                parentFile.mkdirs();
            }

            if(!file.exists()) {
                //创建该文件
                file.createNewFile();
            }

            //最后保存图片
            FileOutputStream fos = new FileOutputStream(file);
            //写入数据
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url获取对应的图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(String imageUrl) {
        try {
            //sdcard/beijingnews/ljsk;l;;llkklll;lkjj;
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";

            //文件名称
            String fileName = MD5Encoder.encode(imageUrl);
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            File file = new File(dir,fileName);

            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
