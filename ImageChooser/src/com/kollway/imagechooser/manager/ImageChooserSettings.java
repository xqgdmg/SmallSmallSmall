package com.kollway.imagechooser.manager;

public final class ImageChooserSettings {
	
	public static final int SHAPE_RECT = 10;
	public static final int SHAPE_CIRCLE = 20;
	
	/**
	 * 选择图片后并进行手动裁剪的模式（默认模式）
	 */
	public static final int CHOOSE_MODE_CROP = 0;
	
	/**
	 * 直接选择图片的模式<br/>
	 * <b>不建议这种方式，由于手机里的部分图片本身的大小可能会过于太大，加载可能会导致内存溢出等问题，此模式建议只用于获取所选图片的路径</b>
	 */
	@Deprecated
	public static final int CHOOSE_MODE_CHOOSE_ONLY = 1;
	
	/**
	 * 选择图片后根据设定的宽度和高度按照最佳比例缩放的模式
	 */
	public static final int CHOOSE_MODE_SCALE = 2;
	
	private int chooseMode = CHOOSE_MODE_CROP;
	private int shape = SHAPE_RECT;
	private int cropSize = -1;
	private String folderPath;
	
	private int scaleWidth = -1;
	private int scaleHeight = -1;

	public ImageChooserSettings setChooseMode(int chooseMode) {
		this.chooseMode = chooseMode;
		return this;
	}

	public ImageChooserSettings setShape(int shape) {
		this.shape = shape;
		return this;
	}

	public ImageChooserSettings setCropSize(int size) {
		this.cropSize = size;
		return this;
	}

	public int getChooseMode() {
		return chooseMode;
	}

	public int getShape() {
		return shape;
	}

	public int getCropSize() {
		return cropSize;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public ImageChooserSettings setFolderPath(String folderPath) {
		this.folderPath = folderPath;
		return this;
	}
	
	public int getScaleWidth() {
		return scaleWidth;
	}

	public ImageChooserSettings setScaleWidth(int scaleWidth) {
		this.scaleWidth = scaleWidth;
		return this;
	}

	public int getScaleHeight() {
		return scaleHeight;
	}

	public ImageChooserSettings setScaleHeight(int scaleHeight) {
		this.scaleHeight = scaleHeight;
		return this;
	}
}
