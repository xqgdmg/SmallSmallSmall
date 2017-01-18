package com.example.chris.mytakephototest;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;

import me.shaohui.advancedluban.Luban;

/**
 * 开源框架 TakePhoto 的简单使用，需要 Glide
 * 支持 FragmentActivity，Fragment，Activity
 * SD卡并不需要权限。。。
 * 动态权限需要添加，不加会报错
 */
public class MainActivity extends AppCompatActivity implements
        TakePhoto.TakeResultListener,InvokeListener,View.OnClickListener{

    private Button mCamer;
    private Button mPhoto;
    private TakePhoto takePhoto;

    private int limit;
    private boolean isNeedCrop;
    private boolean isFile;
    private boolean isSelfCrop;
    private boolean isWidthXheight;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // onCreate 初始化
        getTakePhoto().onCreate(savedInstanceState);

        setUp();// 一堆的设置,设置最好在 findViewById 之前，不然回去压缩路径的时候可能会空指针
            
        initView();
        initListener();
    }

    /**
     * 获取 TakePhoto 对象
     */
    private void setUp() {
        limit = 5;// 选中的数量
        isNeedCrop = true;// 是否裁剪
        isFile = true;// 是否从文件系统选择
        isSelfCrop = false;// 是否使用自身的裁剪方法
        isWidthXheight = true;// 是否是 宽*高

        // 配置图片路径
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);

        // 配置压缩
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
    }

    private void initListener() {

        mCamer.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
    }

    private void initView() {
        mCamer = (Button) findViewById(R.id.camer);
        mPhoto = (Button) findViewById(R.id.photo);
    }

    @Override
    public void onClick(View v) {
        if (v == mCamer) {
            byCamer();
        }else if (v == mPhoto) {
            byPhoto();
        }
    }

    /**
     * 选择相册
     */
    private void byPhoto() {
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


    /**
     * 选择相机
     */
    private void byCamer() {
        if(isNeedCrop){
            takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
        }else {
            takePhoto.onPickFromCapture(imageUri);
        }
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

    private InvokeParam invokeParam;

    /**
     * 权限的处理，不写会报错的，及时清单文件夹权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 权限的处理，不写会报错的，及时清单文件夹权限
     */
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     * 成功
     */
    @Override
    public void takeSuccess(TResult result) {
        Log.e("TAG", "takeSuccess");
        showImg(result.getImages());
    }

    /**
     * 失败
     */
    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("TAG", "takeFail");
    }

    /**
     * 取消
     */
    @Override
    public void takeCancel() {
        Log.e("TAG", "takeCancel");
    }

    /**
     * 需要重写这个方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);// onActivityResult 初始化
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 需要重写这个方法
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);// onSaveInstanceState 初始化
        super.onSaveInstanceState(outState);
    }

    /**
     * 配置压缩配置
     */
    private void configCompress(TakePhoto takePhoto){

        // 设置是否需要压缩
        boolean isNeedCompress = true;

        // 不需要压缩，直接返回
        if(!isNeedCompress){
            takePhoto.onEnableCompress(null,false);
            return ;
        }

        // 最大大小不超过 多少 B eg：102400 B == 100kb
        int maxSize= 102400 * 5;

        // 配置压缩宽高
        int width= 800;
        int height= 800;

        // 是否显示 显示压缩进度条
        boolean showProgressBar = true;

        // 拍照压缩后是否保存原图,如果是相册，没有保存原图的必要？
        boolean enableRawFile = true;

        // 是否使用自带的压缩工具
        boolean isSelfCompress = true;

        CompressConfig config;

        // 压缩工具 是选择自带的还是第三方的
        // 自带
        if(isSelfCompress){
            config=new CompressConfig.Builder()
                    .setMaxSize(maxSize)
                    .setMaxPixel(width>=height? width:height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        }else {
            // 不用自带的,用 Luban
            LubanOptions option=new LubanOptions.Builder()
                    .setGear(Luban.CUSTOM_GEAR)
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config=CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 是否使用TakePhoto自带的相册进行图片选择，默认不使用，但选择多张图片会使用
     */
    private void configTakePhotoOption(TakePhoto takePhoto){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();

        boolean isUseTakePhotoGallery = true;
        boolean isNeedCorrectAngle = true;

        // 使用 TakePhoto 自带相册
        if(isUseTakePhotoGallery){
            builder.setWithOwnGallery(true);
        }

        // 纠正拍照的照片旋转角度
        if(isNeedCorrectAngle){
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto == null){
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    /**
     * 成功后 到另一个页面显示
     */
    private void showImg(ArrayList<TImage> images) {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("images",images);
        startActivity(intent);
    }
}
