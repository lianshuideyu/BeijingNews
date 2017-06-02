package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.activity_guide)
    RelativeLayout activityGuide;

    private ArrayList<ImageView> imageViews;
    private int[] ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);

        //初始化数据
        initData();

        //设置ViewPager的适配器
        vp.setAdapter(new MyPagerAdapter());

        vp.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initData() {
        imageViews = new ArrayList<>();
        for(int i = 0; i < ids.length; i++){
            ImageView imageView = new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            //添加三个灰点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guide_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(GuideActivity.this,10),DensityUtil.dip2px(GuideActivity.this,10));
            point.setLayoutParams(params);
            
            if(i != 0) {
                params.leftMargin = DensityUtil.dip2px(GuideActivity.this,10);
            }
            //添加到线性布局
            llPointGroup.addView(point);
        }

    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {

    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        /**
         * 判断当前View和instantiateItem返回的是否是同一个页面
         * @param view
         * @param object instantiateItem方法返回的
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 相当于ListView适配器中的getView方法，创建item布局，要把当前视图添加到容器中
         * @param container ViewPager自身
         * @param position 要添加或者要创建的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //添加到容器中
            container.addView(imageView);

            return imageView;
        }

        /**
         * 销毁视图
         * @param container Viewpager
         * @param position 要销毁的位置
         * @param object 要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当滑到的时候回调
         * @param position  当前滑动的页面的下标位置
         * @param positionOffset 滑动百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当选中某个页面的时候回调
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if(position == imageViews.size() - 1) {
                btnStartMain.setVisibility(View.VISIBLE);
            }else {
                btnStartMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
