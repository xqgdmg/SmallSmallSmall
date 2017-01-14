package com.example.test.listviewscrollnoload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mLv;

    // 百度地址,这地址通常是无效的
    private String url = "http://img0.imgtn.bdimg.com/it/u=3083693965,3642567609&fm=23&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = (ListView) findViewById(R.id.lv);

        List<UserEnity> datas = new ArrayList<UserEnity>();
        for (int i=0; i<100; i++){
            UserEnity user = new UserEnity("aaa" + i,url);
            datas.add(user);
        }


        final MyAdapter adapter = new MyAdapter(this,datas);
        mLv.setAdapter(adapter);

//        adapter.notifyDataSetChanged();


        /**
         * 设置 OnScrollListener
         */
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动
                    {
                        //设置为停止滚动
                        adapter.setScrollState(false);
                        //当前屏幕中listview的子项的个数
                        int count = view.getChildCount();

                        for (int i = 0; i < count; i++) {
                            //获取到item的name
                            TextView tv_name = (TextView) view.getChildAt(i).findViewById(R.id.tv_name);
                            //获取到item的头像
                            ImageView iv_show= (ImageView) view.getChildAt(i).findViewById(R.id.iv_icon);

                            if (tv_name.getTag() != null) { //非null说明需要加载数据，自定义
                                tv_name.setText(tv_name.getTag().toString());//直接从Tag中取出我们存储的数据name并且赋值
                                tv_name.setTag(null);//设置为已加载过数据
                            }

                            if (!iv_show.getTag().equals("1")){//!="1"说明需要加载数据
                                String image_url=iv_show.getTag().toString();//直接从Tag中取出我们存储的数据image——url
                                Picasso.with(MainActivity.this).load(image_url).error(R.mipmap.error).placeholder(R.mipmap.holder).into(iv_show);//显示图片
                                iv_show.setTag("1");//设置为已加载过数据，自定义
                            }
                        }
                        break;
                    }
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动做出了抛的动作
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
                        break;
                    }

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
                        break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }
}
