package com.kollway.imagechooser.demo;

import com.kollway.imagechooser.manager.ImageChooserManager;
import com.kollway.imagechooser.manager.ImageChooserSettings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

class MainActivity extends Activity {

    private ImageView mImageView;
    private Button mButton;

    private ImageChooserManager mImageChooserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeLayout();
        registerListeners();
        initImageChooser();
    }

    private void codeLayout() {
        LinearLayout layout = new LinearLayout(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setLayoutParams(lp);

        mImageView = new ImageView(this);
        layout.addView(mImageView);

        mButton = new Button(this);
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = 50;
        mButton.setLayoutParams(lp);
        mButton.setGravity(Gravity.CENTER);
        mButton.setText("点击选择图片");
        layout.addView(mButton);

        setContentView(layout);
    }

    private void registerListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mImageChooserManager.startChooseImage(MainActivity.this, "选择图片",100);
            }
        });
    }

    private void initImageChooser() {
        mImageChooserManager = ImageChooserManager.getInstance();
        mImageChooserManager
                .settings()
                .setChooseMode(ImageChooserSettings.CHOOSE_MODE_CROP)
                .setCropSize(320)
                .setShape(ImageChooserSettings.SHAPE_RECT);
    }

    /**
     * 调用 mImageChooserManager.startChooseImage 会给这个类返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            // 解析数据，尝试替换成了集合
            List<String> imagePath = ImageChooserManager.getResultImagePath(data);

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath.toString() + "demoMain" + imagePath.get(1));// todo 先测试结果
            mImageView.setImageBitmap(bitmap);
        }
    }

}
