/**
 * ImageGroupAdapter.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.kollway.imagechooser.adaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.kollway.imagechooser.R;
import com.kollway.imagechooser.activity.ImageListActivity;
import com.kollway.imagechooser.loader.LocalImageLoader;
import com.kollway.imagechooser.loader.LocalImageLoader.ImageCallBack;
import com.kollway.imagechooser.utils.Util;
import com.kollway.imagechooser.widget.MyImageView;

import java.util.ArrayList;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private ArrayList<String> mDataList = new ArrayList<String>();

    /**
     * 选中的图片列表
     */
    public ArrayList<String> mSelectedList = new ArrayList<String>();

    // help checkbox
    private ArrayList<String> mCheckBox = new ArrayList<String>();

    /**
     * 容器
     */
    private View mContainer = null;

    private int picNumLimit = 5;
    private int picNumCount = 0;

    public ImageListAdapter(Context context, ArrayList<String> list, View container) {
        mDataList = list;
        mContext = context;
        mSelectedList = Util.getSeletedImages(context);
        mContainer = container;


    }

    private void initListener() {
        ImageListActivity imageListActivity = (ImageListActivity)mContext;
        imageListActivity.setMyIteMClick(new ImageListActivity.MyIteMClick() {
            @Override
            public void onMyIteMClick(int position2) {


                // 选中和不选中的逻辑
                if (mCheckBox.contains(position2 + "" )){
                    mCheckBox.remove(position2 + "");
                    mSelectedList.remove(mDataList.get(Integer.parseInt(position2 + "")));
                    picNumCount --;
                }else {
                    // 大于最大图片数量返回
                    if (picNumCount >= picNumLimit){
                        Toast.makeText(mContext,"不能选中超过" + picNumLimit + "张照片",Toast.LENGTH_LONG).show();
                        return;
                    }

                    mCheckBox.add(position2 + "");
                    mSelectedList.add(mDataList.get(Integer.parseInt(position2 + "")));// mDataList.get(Integer.getInteger(position2 + "" ))
                    picNumCount ++;
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        initListener();
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, null);
            holder.mImageIv = (MyImageView)view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            holder.mSelectedCb = (CheckBox)view.findViewById(R.id.list_item_cb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        final String path = getItem(position);
        // 加载图片
        holder.mImageIv.setTag(path);
        // 加载图片
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = LocalImageLoader.getInstance().loadImage(path, holder.mImageIv.getPoint(),
                new ImageCallBack() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView)mContainer.findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });
        if (bitmap != null) {
            holder.mImageIv.setImageBitmap(bitmap);
            
        } else {
            holder.mImageIv.setImageResource(R.drawable.pic_thumb);
        }

        ImageListActivity imageListActivity = (ImageListActivity)mContext;
        imageListActivity.setMyIteMClick(new ImageListActivity.MyIteMClick() {
            @Override
            public void onMyIteMClick(int position) {

            }
        });

        // item 点击 就加入集合
        holder.mSelectedCb.setChecked(false);
//        // 该图片是否选中
        for (String cb : mCheckBox) {
            if (cb.equals(position + "") ) {
                holder.mSelectedCb.setChecked(true);
            }
        }

        return view;
    }


    static class ViewHolder {
        public MyImageView mImageIv;

        public View mClickArea;

        public CheckBox mSelectedCb;

    }

}
