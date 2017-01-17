package com.example.chris.mytakephototest;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;

/**
 * 测试第一种使用方式，需要继承没有什么乱用
 * 如果通过继承的方式无法满足实际项目的使用,用第二种方式
 * 为了简单，这里就不测试  配置压缩 和 最后显示最终的图片了
 */
public class MainActivity2 extends TakePhotoActivity {

    private TakePhoto takePhoto;
    private Button mBtn;
    private int limit;
    private boolean isNeedCrop;
    private boolean isFile;
    private boolean isSelfCrop;
    private boolean isWidthXheight;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_2);

        initView();
        setUp();
        initListener();
    }

    /**
     * 获取 TakePhoto 对象
     */
    private void setUp() {
        takePhoto = getTakePhoto();
        limit = 5;// 选中的数量
        isNeedCrop = true;// 是否裁剪
        isFile = true;// 是否从文件系统选择
        isSelfCrop = false;// 是否使用自身的裁剪方法
        isWidthXheight = true;

        // 配置图片路径
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);

        // 配置压缩
//        configCompress(takePhoto);
//        configTakePhotoOption(takePhoto);
    }

    /**
     * 仅仅测试 从相册选择
     */
    private void initListener() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 选择数量 大于 1
                if(limit>1){
                    if(isNeedCrop){
                        takePhoto.onPickMultipleWithCrop(limit,getCropOptions());
                    }else {
                        takePhoto.onPickMultiple(limit);
                    }
                    return;
                }


                if(isFile){ // 从文件系统
                    if(isNeedCrop){
                        takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions());
                    }else {
                        takePhoto.onPickFromDocuments();
                    }
                    return;
                }else {  // 不是文件系统，就是相册，这个类没有测试 拍照
                    if(isNeedCrop){
                        takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
                    }else {
                        takePhoto.onPickFromGallery();
                    }
                }
            }
        });

    }

    private void initView() {
        mBtn = (Button) findViewById(R.id.btn);

    }
    
    // 重写三个方法
    /**
     * 获取成功
     */
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
    }

    /**
     * 获取失败
     */
    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    /**
     * 取消操作
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    /**
     * 获取裁剪的尺寸
     */
    private CropOptions getCropOptions(){

        // 不需要裁剪
        if(!isNeedCrop){
            return null;
        }

        // 裁剪的宽高
        int height= 800;
        int width= 800;

        // 是否使用自身的裁剪方法
        boolean withWonCrop = isSelfCrop;

        // CropOptions 配置
        CropOptions.Builder builder=new CropOptions.Builder();

        // 宽x高 还是 宽/高
        if(!isWidthXheight){// 宽/高
            builder.setAspectX(width).setAspectY(height);
        }else {  // 宽x高
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }
}
