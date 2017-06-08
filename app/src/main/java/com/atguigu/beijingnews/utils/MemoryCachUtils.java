package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2017/6/8.
 */

public class MemoryCachUtils {

    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCachUtils() {
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    /**
     * 根据图片url获取内存的图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromMemory(String imageUrl) {

        return mMemoryCache.get(imageUrl);
    }

    /**
     * 根据url存储图片到内存中
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap2Memory(String imageUrl, Bitmap bitmap) {

        if (getBitmapFromMemory(imageUrl) == null) {
            mMemoryCache.put(imageUrl, bitmap);
        }
    }
}
