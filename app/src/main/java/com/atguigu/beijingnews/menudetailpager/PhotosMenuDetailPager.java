package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.PhotosMenuDetailPagerAdapater;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private PhotosMenuDetailPagerAdapater adapater;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);

        ButterKnife.inject(this, view);

        //设置下拉刷新的监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        //设置滑动多少距离有效果
//        refreshLayout.setDistanceToTriggerSync(100);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //联网请求
        url = ConstantUtils.BASE_URL + dataBean.getUrl();

        String saveJson = CacheUtils.getString(context, url);
        if(!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet(url);

    }

    private void getDataFromNet(final String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==" + response);
                        //存储--缓存为本地文件
                        CacheUtils.putString(context,url,response);
                        processData(response);

                    }


                });
    }

    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        datas = bean.getData().getNews();

        if (datas != null && datas.size() > 0) {
            //有数据
            progressbar.setVisibility(View.GONE);
            adapater = new PhotosMenuDetailPagerAdapater(context, datas,recyclerview);
            //设置适配器
            recyclerview.setAdapter(adapater);

            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        } else {
            //没有数据
            progressbar.setVisibility(View.VISIBLE);
        }

        //隐藏刷新进度效果
        refreshLayout.setRefreshing(false);
    }

    /**
     * true:显示List效果
     * false:显示Grid
     */
    private boolean isShowList = true;

    /**
     * 设置List和Grid风格的切换和按钮的设置
     *
     * @param iv
     */
    public void swichListAndGrid(ImageButton iv) {

        if (isShowList) {
            //显示Grid效果
            recyclerview.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
            isShowList = false;
            //按钮状态-List
            iv.setImageResource(R.drawable.icon_pic_list_type);
//            adapater.notifyItemChanged(0,datas.size());
        } else {
            //显示List
            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            isShowList = true;
            //按钮状态-Grid
            iv.setImageResource(R.drawable.icon_pic_grid_type);
//            adapater.notifyItemChanged(0,datas.size());
        }
    }
}
