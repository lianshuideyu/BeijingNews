package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.basepager.BasePager;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Log.e("TAG", "NewsPager---initData");
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

        //先获取缓存的数据
        String saveJson = CacheUtils.getString(context, ConstantUtils.NEWSCENTER_PAGER_URL);//
        if(!TextUtils.isEmpty(saveJson)){//当不是null,""
            processData(saveJson);
            Log.e("TAG","取出缓存的数据..=="+saveJson);
        }

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
                        Log.e("TAG", "联网失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "联网成功" + response);
                        //缓存数据
                        CacheUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                        //解析数据
                        processData(response);

                    }
                });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processData(String json) {
        //NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);

        //手动解析json数据
        NewsCenterBean newsCenterBean = paseJson(json);

        Log.e("TAG", "解析成功==" + newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
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

//        swichPager(0);

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

    private NewsCenterBean paseJson(String json) {
        NewsCenterBean centerBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            centerBean.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");//得到data数组
            if (data != null && data.length() > 0) {
                ArrayList<NewsCenterBean.DataBean> dataBean = new ArrayList<>();
                centerBean.setData(dataBean);

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) data.get(i);
                    if (jsonObject1 != null) {
                        NewsCenterBean.DataBean newsCenterData = new NewsCenterBean.DataBean();
                        String title = jsonObject1.optString("title");
                        newsCenterData.setTitle(title);

                        int type = jsonObject1.optInt("type");
                        newsCenterData.setType(type);

                        String url = jsonObject1.optString("url");
                        newsCenterData.setUrl(url);

                        String url1 = jsonObject1.optString("url1");
                        newsCenterData.setUrl(url1);

                        String dayurl = jsonObject1.optString("dayurl");
                        newsCenterData.setDayurl(dayurl);
                        String excurl = jsonObject1.optString("excurl");
                        newsCenterData.setExcurl(excurl);
                        String weekurl = jsonObject1.optString("weekurl");
                        newsCenterData.setWeekurl(weekurl);

                        dataBean.add(newsCenterData);

                        //children
                        JSONArray childrenData = jsonObject1.optJSONArray("children");//得到children数组
                        if (childrenData != null && childrenData.length() > 0) {
                            ArrayList<NewsCenterBean.DataBean.ChildrenBean> children = new ArrayList<>();
                            newsCenterData.setChildren(children);

                            for (int j = 0; j < childrenData.length(); j++) {
                                JSONObject childrenJson = (JSONObject) childrenData.get(i);

                                if (childrenJson != null) {
                                    NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();

                                    childrenBean.setId(childrenJson.optInt("id"));
                                    childrenBean.setTitle(childrenJson.optString("title"));
                                    childrenBean.setType(childrenJson.optInt("type"));
                                    childrenBean.setUrl(childrenJson.optString("url"));

                                    children.add(childrenBean);
                                }
                            }
                        }
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return centerBean;
    }
}
