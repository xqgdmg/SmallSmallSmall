package com.kollway.imagechooser.clip;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kollway.imagechooser.manager.ImageChooserManager;
import com.kollway.imagechooser.manager.ImageChooserSettings;
import com.kollway.imagechooser.utils.DeviceUtil;

public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	private int mHorizontalPadding ;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
	}

	/**
	 * 对外公布设置边距的方法
	 * 
	 * @param mHorizontalPadding
	 */
	public void setCropSize(int size)
	{

		mClipImageView.setCropSize(size);
		Point screenSize = DeviceUtil.getDeviceSize(getContext());
		mHorizontalPadding = (screenSize.x - size) / 2;

		mHorizontalPadding = Math.max(0, mHorizontalPadding);

		// 计算padding的px
//		mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
	}
	
	
	public void setDrawShape(int shape){
		mClipImageView.setDrawShape(shape);
	}
	
	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

	public void setBitmap(Bitmap bitmap) {
		mZoomImageView.setImageBitmap(bitmap);
	}

}
