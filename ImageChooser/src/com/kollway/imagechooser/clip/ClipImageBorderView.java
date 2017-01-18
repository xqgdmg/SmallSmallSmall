package com.kollway.imagechooser.clip;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kollway.imagechooser.manager.ImageChooserSettings;

public class ClipImageBorderView extends View
{
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 2;

	private Paint mPaint;
	
	private int shape = 0;
	
//	public static int RECT = 10;
	
//	public static int CIRCLE = 20;
	
	
	private int mCropSize;
	private int mDrawRadius;

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 绘制边框
		mPaint.setColor(Color.parseColor("#FFFFFF"));
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		//方形边框
//		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
		//圆形边框
//		canvas.drawCircle( getWidth()/2, getHeight()/2, getWidth()/2-mHorizontalPadding, mPaint);

		if(shape == ImageChooserSettings.SHAPE_RECT){
			//方形边框
			int halfOfCropSize = mCropSize / 2;
			canvas.drawRect(
					getWidth()/2 - halfOfCropSize,  //左
					getHeight()/2 - halfOfCropSize, //上
					getWidth()/2 + halfOfCropSize,  //右
					getHeight()/2 + halfOfCropSize, //下
					mPaint);
		}else if(shape == ImageChooserSettings.SHAPE_CIRCLE){
			//圆形边框
			canvas.drawCircle( getWidth()/2, getHeight()/2, mCropSize / 2, mPaint);
		}
	}

	public void setCropSize(int size){
		this.mCropSize = size;
	}
	
	public void setDrawShape(int shape){
		this.shape = shape;
	}

}
