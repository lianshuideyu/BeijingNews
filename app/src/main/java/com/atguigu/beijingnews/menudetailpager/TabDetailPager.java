package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.domain.TabDetailPagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {

    public static final String READ_ID_ARRAY = "read_id_array";
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;

    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pull_refresh_list;

    ListView lv;

    private String url;
    /**
     * 顶部新闻图片的数据
     */
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    //记录当前上方viewpager的页码
    private int prePosition = 0;

    //listview新闻列表
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private MyListAdapter adapter;

    private String moreUrl;
    private boolean isLoadingMore = false;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        //Log.e("TAG","childrenBean.title" + childrenBean.getTitle());
    }

    @Override
    public View initView() {
        //创建子类的视图
        final View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);

        //得到ListView
        lv = pull_refresh_list.getRefreshableView();

        //顶部视图
        View viewTopNews = View.inflate(context,R.layout.tab_detail_topnews, null);
        viewpager = (HorizontalScrollViewPager) viewTopNews.findViewById(R.id.viewpager);
        tvTitle = (TextView) viewTopNews.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) viewTopNews.findViewById(R.id.ll_point_group);

        //把顶部的部分以添加头的方式加入ListView中
        lv.addHeaderView(viewTopNews);


        //监听页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                llPointGroup.getChildAt(position).setEnabled(true);

                //记录当前的值
                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(topnews.get(position).getTitle());


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state ==ViewPager.SCROLL_STATE_DRAGGING){
                    //消息移除
                    handler.removeCallbacksAndMessages(null);
                }else  if(state==ViewPager.SCROLL_STATE_IDLE){
                    //发消息
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new MyRunnable(),3000);
                }
            }
        });

        //设置下拉和上拉刷新的监听
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore = false;
                getDataFromNet(url);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!TextUtils.isEmpty(moreUrl)){
                    isLoadingMore = true;
                    getDataFromNet(moreUrl);
                }else{
                    Toast.makeText(context, "没有更多数据了...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //listview的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int realPostion = position - 2;
                TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(realPostion);

                //先获取缓存判断是否为点击过的
                String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
                if(!idArray.contains(newsBean.getId()+ "")) {

                    idArray += newsBean.getId() + ",";
                    //保存
                    CacheUtils.putString(context,READ_ID_ARRAY,idArray);
                    //适配器刷新
                    adapter.notifyDataSetChanged();
                }

                String url = ConstantUtils.BASE_URL + newsBean.getUrl();

                //跳转到Activity显示新闻详情内容
                Intent intent = new Intent(context,NewsDetailActivity.class);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据

        url = ConstantUtils.BASE_URL + childrenBean.getUrl();
        Log.e("TAG", "url==" + url);
        //联网请求
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {

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
                        //解析数据
                        processData(response);
                        //结束下来刷新
                        pull_refresh_list.onRefreshComplete();
                    }
                });
    }

    private void processData(String json) {
        TabDetailPagerBean bean = new Gson().fromJson(json, TabDetailPagerBean.class);

        String more = bean.getData().getMore();
        if(!TextUtils.isEmpty(more)){
            moreUrl = ConstantUtils.BASE_URL+more;
        }

        if(!isLoadingMore){

            topnews = bean.getData().getTopnews();
            //默认进来先显示第一个页面的标题
            tvTitle.setText(topnews.get(prePosition).getTitle());

            viewpager.setAdapter(new MyPagerAdapter());

            //添加viewpager灰色指示点
            //先移除之前的指示点
            llPointGroup.removeAllViews();
            for (int i = 0; i < topnews.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_selector);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));
                point.setLayoutParams(params);

                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }
                llPointGroup.addView(point);
            }

            //页面下部listview
            news = bean.getData().getNews();
            adapter = new MyListAdapter();
            lv.setAdapter(adapter);
        }else  {

            isLoadingMore = false;
            news.addAll(bean.getData().getNews());//把新的数据集合加入到原来集合中，而不是覆盖
            adapter.notifyDataSetChanged();//适配器刷新
        }

        //设置自动切换到下一个页面
        if(handler == null) {
            handler = new InternalHandler();
        }
        handler.removeCallbacksAndMessages(null);
        //重新发消息
        handler.postDelayed(new MyRunnable(),3000);

    }

    private InternalHandler handler;
    class InternalHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            //设置切换到下一个页面
            viewpager.setCurrentItem(item);

            handler.postDelayed(new MyRunnable(),3000);
        }
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews == null ? 0 : topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String imageUrl = ConstantUtils.BASE_URL + topnews.get(position).getTopimage();
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.pic_item_list_default)
                    .placeholder(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news == null ? 0 : news.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tab_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tvDesc.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());

            String imageUrl = ConstantUtils.BASE_URL+newsBean.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);
            
            //判断是否已经点击过
            String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
            if(idArray.contains(newsBean.getId() + "")) {
                //字体变灰色
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            }else{
                //黑色
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }

            return convertView;
        }


    }
    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_desc)
        TextView tvDesc;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
