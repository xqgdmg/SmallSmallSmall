/**
 * ImageListActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-23
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.kollway.imagechooser.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.kollway.imagechooser.R;
import com.kollway.imagechooser.adaper.ImageListAdapter;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author likebamboo
 */
public class ImageListActivity extends Activity implements OnItemClickListener {

    /**
     * title
     */
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 图片列表extra
     */
    public static final String EXTRA_IMAGES_DATAS = "extra_images";

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 适配器
     */
    private ImageListAdapter mImageAdapter = null;


    // add 保存照片 path 集合
    private ArrayList<String> picPathList = new ArrayList<String>();
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        initView();
        if (getIntent().hasExtra(EXTRA_IMAGES_DATAS)) {
            mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            setAdapter(mImages);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView)findViewById(R.id.images_gv);
        btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("path", mImageAdapter.mSelectedList);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    /**
     * 构建并初始化适配器
     * 
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /**
     * todo 选择多张图片,选完到裁剪的页面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

        mMyIteMClick.onMyIteMClick(position);

    }

    /**
     * 在此处添加接口回调
     */
    MyIteMClick mMyIteMClick;
    public interface MyIteMClick{
        void onMyIteMClick(int position);
    }

    public void setMyIteMClick(MyIteMClick myIteMClick){
        this.mMyIteMClick = myIteMClick;
    }


    /**
     * 一定要释放，不然数据会空
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
