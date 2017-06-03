package com.atguigu.beijingnews.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.basefragment.BaseFragment;
import com.atguigu.beijingnews.domain.NewsCenterBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    private List<NewsCenterBean.DataBean> datas;

    private LeftMenuAdapter adapter;
    private int prePosition = 0;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0,40,0,0);

        //设置listview的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //记录位置
                prePosition = position;
                //刷新适配器，会重新调用适配器中的getview()方法
                adapter.notifyDataSetChanged();

                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<->开
            }
        });

        return listView;
    }

    @Override
    public void initData() {
        super.initData();

    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;
        for(int i = 0; i < datas.size(); i++) {
            Log.e("TAG","LeftMenuFragment=="+datas.get(i).getTitle());

        }

        //设置适配器
        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public NewsCenterBean.DataBean getItem(int position) {

            return datas.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);

            if(prePosition==position){
                //高亮
                textView.setEnabled(true);
            }else{
                //默认
                textView.setEnabled(false);
            }

            //根据位置得到数据
            NewsCenterBean.DataBean dataBean = datas.get(position);
            textView.setText(dataBean.getTitle());
            Log.e("TAG","getView");
            return textView;
        }
    }
}
