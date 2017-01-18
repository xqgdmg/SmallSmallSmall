package com.kollway.imagechooser.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.kollway.imagechooser.R;
import com.kollway.imagechooser.clip.ClipImageLayout;
import com.kollway.imagechooser.manager.ImageChooserManager;
import com.kollway.imagechooser.manager.ImageChooserSettings;
import com.kollway.imagechooser.utils.ImageTools;

public class ClipActivity extends BaseActivity {
	private ClipImageLayout mClipImageLayout;
	private ArrayList<String> path;
	private ProgressDialog loadingDialog;

	private CompressFormat mFormat;

	private String restoredPath;

	private String mFolderPath;

	private int shape;

	private int cropSize;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimage);
		//这步必须要加
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");


        ImageChooserSettings settings = ImageChooserManager
        		.getInstance()
        		.loadSettings(this);
        mFolderPath = settings.getFolderPath();
        cropSize = settings.getCropSize();
        shape = settings.getShape();

        path = getIntent().getStringArrayListExtra("path");// 变为集合

//        Toast.makeText(ClipActivity.this, path, Toast.LENGTH_SHORT).show();
		if(TextUtils.isEmpty(path.get(0))||!(new File(path.get(0)).exists())){ // TODO: 2017/1/18
			Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
			return;
		}
		
		Bitmap bitmap=ImageTools.convertToBitmap(path.get(0), cropSize, cropSize);// TODO: 2017/1/18
		if(bitmap==null){
			Toast.makeText(this, "图片加载失败",Toast.LENGTH_SHORT).show();
			return;
		}
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		mClipImageLayout.setCropSize(cropSize);
		mClipImageLayout.setDrawShape(shape);

		mClipImageLayout.setBitmap(bitmap);
		((Button)findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingDialog.show();

				new AsyncTask<Void, Void, Bitmap>() {

					@Override
					protected Bitmap doInBackground(Void... params) {
						Bitmap bitmap = mClipImageLayout.clip();
						return bitmap;
					}

					protected void onPostExecute(Bitmap bitmap) {
						if(loadingDialog != null) {
							loadingDialog.dismiss();
						}
						if(bitmap != null) {
							String fileName = String.format("%d.%s", System.currentTimeMillis(), ImageTools.isPNG(path.get(0)) ? "png" : "jpg");// todo
							restoredPath = new File(mFolderPath, fileName).getAbsolutePath();
							mFormat = ImageTools.isPNG(path.get(0)) ? CompressFormat.PNG : CompressFormat.JPEG;// TODO: 2017/1/18
							ImageTools.savePhotoToSDCard(bitmap,restoredPath,mFormat);
							Intent intent = new Intent();
							intent.putExtra("path",restoredPath);
							setResult(RESULT_OK, intent);
							finish();
						}
					};

				}.execute();
			}
		});
	}

}
