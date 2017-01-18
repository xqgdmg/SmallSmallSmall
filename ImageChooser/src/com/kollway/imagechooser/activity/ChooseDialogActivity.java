package com.kollway.imagechooser.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kollway.imagechooser.R;
import com.kollway.imagechooser.manager.ImageChooserManager;
import com.kollway.imagechooser.manager.ImageChooserSettings;
import com.kollway.imagechooser.utils.ImageTools;

public class ChooseDialogActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView mTvPhotograph;
    private TextView mTvAlbums;
    private RelativeLayout relLayout;

    public static final int REQUEST_ALBUM = 11; // 相册
    public static final int REQUEST_CAPTURE = 22; // ic_take_photo
    public static final int REQUEST_CROP = 33; // 裁剪

    public static final int RESULT_IMAGE_COMPLETE = 88; // 结果
//	private String photoSavePath;//保存路径
//	private String photoSaveName;//图pian名
//	private String path;//图片全路径

    private int shape;//图片形状
    private int radius;//图片半径大小

    private File mFolder;
    private File mCaptureFile;
    private String mCaptureFilePath;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_choose_dialog);
        setup();
        findViews();
        registerListeners();

        String fileName = String.format("%s.jpg", System.currentTimeMillis());
        String path = new File(mFolder, fileName).getAbsolutePath();
        if (savedInstanceState != null) {
            path = savedInstanceState.getString("CaptureFilePath");
        }
        mCaptureFilePath = path;
        mCaptureFile = new File(path);
    }

    private void setup() {
        ImageChooserSettings settings = ImageChooserManager
                .getInstance()
                .loadSettings(this);
        shape = settings.getShape();
        radius = settings.getCropSize();
        String folderPath = settings.getFolderPath();
        if (TextUtils.isEmpty(folderPath)) {
            ImageChooserManager.mkdir(getApplicationContext());
            folderPath = settings.getFolderPath();
        }

        mFolder = new File(folderPath);
        if (!mFolder.exists()) {
            mFolder.mkdirs();
        }

        File nomediaFile = new File(mFolder, ".nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findViews() {
        relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        mTvPhotograph = (TextView) findViewById(R.id.tvPhotograph);//拍照
        mTvAlbums = (TextView) findViewById(R.id.tvAlbums);//相册
        tvTitle = (TextView) findViewById(R.id.tvTitle);//标题

        //设置标题
        String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);
    }

    private void registerListeners() {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tvPhotograph) {// 拍照
                    Uri imageUri = null;
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(mCaptureFile);
                    openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, REQUEST_CAPTURE);
                } else if (v.getId() == R.id.tvAlbums) {
                    Intent openAlbumIntent = new Intent(ChooseDialogActivity.this, AlbumActivity.class);
                    startActivityForResult(openAlbumIntent, REQUEST_ALBUM);
                } else if (v.getId() == R.id.relLayout){
                    finish();
                } else if (v.getId() == R.id.tvTitle){}
            }
        };
        tvTitle.setOnClickListener(listener);
        relLayout.setOnClickListener(listener);
        mTvPhotograph.setOnClickListener(listener);
        mTvAlbums.setOnClickListener(listener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCaptureFilePath != null) {
            outState.putString("CaptureFilePath", mCaptureFilePath);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * 图片选择及拍照结果,判断 requestCode，返回给 ImageChooserManager，
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != REQUEST_CROP) {
            if (requestCode == REQUEST_ALBUM && data == null) {
                return;
            }
            /*ImageChooserSettings settings = ImageChooserManager
                    .getInstance()
                    .loadSettings(this);

            // 请求标志 是 相册 获取 path 路径，不是获取缓存路径（Sring）
            String path = requestCode == REQUEST_ALBUM ?// todo 变为集合
                    data.getStringExtra("path") : mCaptureFilePath;

            int chooseMode = settings.getChooseMode();
            if (chooseMode == ImageChooserSettings.CHOOSE_MODE_CHOOSE_ONLY) {// 原图
                //直接返回所选的图片
                data = new Intent();
                data.putExtra("path", path);
                setResult(RESULT_OK, data);
                finish();
            } else if (chooseMode == ImageChooserSettings.CHOOSE_MODE_SCALE) {// 缩略图
                //仅根据比例缩放选择的图片
                int scaleWidth = settings.getScaleWidth();
                int scaleHeight = settings.getScaleHeight();
                scaleAndSaveImage(path, scaleWidth, scaleHeight);
            } else {
                // add
                ArrayList<String> path = data.getStringArrayListExtra("path");
                 //进入裁剪Activity
                Intent intent = new Intent(ChooseDialogActivity.this, ClipActivity.class);
                intent.putStringArrayListExtra("path",path);// 集合变数组
                intent.putExtra("shape", shape);
                intent.putExtra("radius", radius);
                startActivityForResult(intent, REQUEST_CROP);// 裁剪的选择
            }*/

            //add 完成，获取到的图片 不需要裁剪
            setResult(RESULT_OK, data);
            finish();
        } else {
            //完成，获取到的图片 不需要裁剪
            setResult(RESULT_OK, data);
            finish();
        }


//		if(requestCode == REQUEST_ALBUM) {	//相册
//			if (data==null) {
//				return;
//			} 
//			int chooseMode = ImageChooserManager.getInstance().settings().getChooseMode();
//			if(chooseMode == ImageChooserSettings.CHOOSE_MODE_CHOOSE_ONLY) {
//				
//			}
//			
//			String path = data.getStringExtra("path");// 图片在的路径
//			Intent intent = new Intent(ChooseDialogActivity.this, ClipActivity.class);
//			intent.putExtra("path", path);
//			intent.putExtra("shape", shape);
//			intent.putExtra("radius", radius);
//			startActivityForResult(intent, REQUEST_CROP);
//		}
//		else if(requestCode == REQUEST_CAPTURE) {	//ic_take_photo
//			String path = mCaptureFile.getAbsolutePath();
//			Intent intent = new Intent(ChooseDialogActivity.this, ClipActivity.class);
//			intent.putExtra("path", path);
//			intent.putExtra("shape", shape);
//			intent.putExtra("radius", radius);
//			startActivityForResult(intent, REQUEST_CROP);
//		}
//		else if(requestCode == REQUEST_CROP) {  //完成，获取到最终的图片
//			setResult(RESULT_OK, data);
//			finish();
//		}
    }

    private void scaleAndSaveImage(final String path, final int scaleWidth, final int scaleHeight) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在处理中，请稍候");
        mProgressDialog.show();

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Context context = ChooseDialogActivity.this;
                CompressFormat format = ImageTools.isPNG(path) ? CompressFormat.PNG : CompressFormat.JPEG;
                String folderPath = ImageChooserManager
                        .getInstance()
                        .loadSettings(context)
                        .getFolderPath();
                String fileName = String.format("%s%s", System.currentTimeMillis(), ImageTools.isPNG(path) ? ".png" : ".jpg");
                File saveFile = new File(folderPath, fileName);
                String savePath = saveFile.getAbsolutePath();
                Bitmap bitmap = ImageTools.convertToBitmap(path, scaleWidth, scaleHeight);
                boolean success = bitmap == null ? false : ImageTools.savePhotoToSDCard(bitmap, savePath, format);
                return success ? savePath : null;
            }

            protected void onPostExecute(String result) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(getApplicationContext(), "处理图片失败，请重试", Toast.LENGTH_LONG).show();
                } else {
                    Intent data = new Intent();
                    data.putExtra("path", result);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }

        }.execute();
    }

    /**
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fast_choose_in, R.anim.fast_choose_out);
    }
}
