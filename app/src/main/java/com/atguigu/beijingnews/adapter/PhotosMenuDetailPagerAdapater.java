package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnews.utils.BitmapCacheUtils;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.atguigu.beijingnews.utils.NetCachUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {
    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private final Context context;
    private final RecyclerView recyclerview;

    /**
     * 做图片三级缓存
     * 1.内存缓存
     * 2.本地缓存
     * 3.网络缓存
     */
    private BitmapCacheUtils bitmapCacheUtils;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case NetCachUtils.SUCESS :
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    ImageView imageView = (ImageView) recyclerview.findViewWithTag(position);
                    if(bitmap != null && imageView != null) {

                        imageView.setImageBitmap(bitmap);
                    }

                    break;
                case NetCachUtils.FAIL :
                    Log.e("TAG","handler接收失败消息-positon==" + msg.arg1);

                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas, RecyclerView recyclerview) {
        this.datas = datas;
        this.context = context;
        this.bitmapCacheUtils = new BitmapCacheUtils(handler);
        this.recyclerview = recyclerview;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_photos, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//1.根据位置得到对应的数据
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        //2.绑定数据
        holder.tvTitle.setText(newsBean.getTitle());
        //3.设置点击事件
       String imageUrl = ConstantUtils.BASE_URL+newsBean.getLargeimage();
        /* Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);*/

        //使用自定义方式请求图片
        Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl,position);
        //图片对应的TAg就是位置
        holder.ivIcon.setTag(position);
        if(bitmap != null) {//来自内存和本地，不包括网络，因为网络请求是在子线程是异步的，不能在这里获取，只能在网络获取成功后通过handler将图片传给handler
            holder.ivIcon.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "position=" + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(ConstantUtils.BASE_URL + datas.get(getLayoutPosition()).getLargeimage()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
