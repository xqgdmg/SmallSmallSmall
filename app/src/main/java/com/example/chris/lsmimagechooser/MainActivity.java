package com.example.chris.lsmimagechooser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kollway.imagechooser.manager.ImageChooserManager;
import com.kollway.imagechooser.manager.ImageChooserSettings;

import java.util.ArrayList;

/**
 * 一个 mudule 文件大小 10.8 MB
 * github 上面的 TakePhoto 21 MB
 */
public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageChooserManager imageChooserManager;
    private ArrayList<String> avatarPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 ImageChooserManager
        initImageChooser();

        initView();
        initListener();
    }

    /**
     * 初始化 ImageChooserManager
     */
    private void initImageChooser() {
        imageChooserManager = ImageChooserManager.getInstance();
        imageChooserManager.settings()
                .setShape(ImageChooserSettings.SHAPE_RECT)
                .setChooseMode(ImageChooserSettings.CHOOSE_MODE_CROP);
    }

    private void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooserManager.startChooseImage(MainActivity.this, "修改头像", ImageChooserManager.REQUEST_CODE_START_CHOOSE_IMAGE);
            }
        });

    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
    }

    /**
     * 相机和相册的返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {

            if (requestCode == ImageChooserManager.REQUEST_CODE_START_CHOOSE_IMAGE) {// 100
                avatarPath = data.getStringArrayListExtra(Constants.IMAGECHOOSER_EXTRA);//  "path"

                // 显示图片，简单打印路径
                Toast.makeText(MainActivity.this,avatarPath + "", Toast.LENGTH_LONG).show();
                Log.e("TAG", "haha=" + avatarPath + "");
            }
        }
    }
}
