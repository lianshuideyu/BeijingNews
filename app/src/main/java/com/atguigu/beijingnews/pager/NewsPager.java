package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.basepager.BasePager;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/2.
 */

public class NewsPager extends BasePager {

    /**
     * 左侧页面的数据集合
     */
    private List<NewsCenterBean.DataBean> datas;

    /**
     * 左侧菜单详情的页面集合
     */
    private ArrayList<MenuDetailBasePager> basePagers;

    public NewsPager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
        //把视图绑定到视图上
        Log.e("TAG","NewsPager---initData");
        //设置标题
        tvTitle.setText("新闻页面");
        ibMenu.setVisibility(View.VISIBLE);

        //创建子类视图
        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        flContent.addView(textView);

        //联网请求
        getDataFromNet();
    }

    private void getDataFromNet() {

        String url = ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {


                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","联网失败"+ e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","联网成功"+ response);
                        //解析数据
                        processData(response);

                    }
                });
    }

    /**
     * 解析数据
     * @param json
     */
    private void processData(String json) {
        NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);

        Log.e("TAG","解析成功=="+ newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();

        //将数据传到左侧菜单
        MainActivity mainActivity = (MainActivity) context;

        //实例化详情页面
        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(context));//新闻详情页面
        basePagers.add(new TopicMenuDetailPager(context));//专题详情页面
        basePagers.add(new PhotosMenuDetailPager(context));//组图详情页面
        basePagers.add(new InteractMenuDetailPager(context));//互动详情页面
        basePagers.add(new VoteMenuDetailPager(context));//投票详情页面


        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(datas);


    }

    public void swichPager(int position) {
        MenuDetailBasePager basePager = basePagers.get(position);
        View rootView = basePager.rootView;

        flContent.removeAllViews();
        flContent.addView(rootView);


        basePager.initData();
    }
}
