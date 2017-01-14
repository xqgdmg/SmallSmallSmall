package com.example.test.listviewscrollnoload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 作者：Chris
 * 创建时间: 2017/1/14 17:43
 * 邮箱：395932265@qq.com
 * 描述:
 * TODO
 */
public class MyAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    List<UserEnity> lists;

    //定义当前listview是否在滑动状态
    private  boolean scrollState=false;

    /**
     * 重要的设置是否在滑动状态的方法
     * true 表示正在滑动， 默认为 false
     */
    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

/*******************************************分割线*****************************************************/

    public MyAdapter(Context context, List<UserEnity> lists) {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.lists=lists;
    }

    @Override
    public int getCount() {
        return lists!=null?lists.size():0;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.main_item,null,true);
            viewHolder=new ViewHolder();
            viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        UserEnity userEnity=lists.get(position);

        /**
         * 加载数据之后要 setTag ， 配合 Activity 使用
         */
        String img_url = userEnity.getIcon();
        if (!scrollState){  // 没有滚动，真实加载数据，但是 tag 是自定义的
            viewHolder.tv_name.setText(userEnity.getName());
            viewHolder.tv_name.setTag(null);// setTag...

            Picasso.with(context).load(img_url).error(R.mipmap.error).placeholder(R.mipmap.holder).into(viewHolder.iv_icon);
            viewHolder.iv_icon.setTag("1");// setTag...

        }else{ // 滚动中，设置默认值，不加载数据，但是 tag 是真实的数据
            viewHolder.tv_name.setText("加载中");
            viewHolder.tv_name.setTag(userEnity.getName());// setTag...

            viewHolder.iv_icon.setImageResource(R.mipmap.ic_launcher);
            viewHolder.iv_icon.setTag(img_url);// setTag...

        }
        return convertView;

    }

    static class ViewHolder{
        TextView tv_name;
        ImageView iv_icon;
    }

}
