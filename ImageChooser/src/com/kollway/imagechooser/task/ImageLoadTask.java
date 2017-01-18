/**
 * ImageLoadTask.java
 * ImageSelector
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.kollway.imagechooser.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;

import com.kollway.imagechooser.listener.OnTaskResultListener;
import com.kollway.imagechooser.log.L;
import com.kollway.imagechooser.model.ImageGroup;

import java.io.File;
import java.util.ArrayList;

/**
 * 使用contentProvider扫描图片异步任务
 * 
 * @author likebamboo
 */
public class ImageLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值对
     */
    private ArrayList<ImageGroup> mGruopList = new ArrayList<ImageGroup>();

    public ImageLoadTask(Context context) {
        super();
        mContext = context;
        result = mGruopList;
    }

    public ImageLoadTask(Context context, OnTaskResultListener listener) {
        super();
        mContext = context;
        result = mGruopList;
        setOnResultListener(listener);
    }

    /*
     * (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");

        ArrayList<String> folderPaths = new ArrayList<String>();
        
        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[] {
                    "image/jpeg", "image/png"
            }, Media.DATE_TAKEN);
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));

                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                String parentName = "";
                String parentPath = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                    parentPath = file.getParentFile().getAbsolutePath();
                } else {
                    parentName = file.getName();
                }
                
                if(!TextUtils.isEmpty(parentPath) && !folderPaths.contains(parentPath)) {
                	folderPaths.add(parentPath);
                }
                
                // 构建一个imageGroup对象
                ImageGroup item = new ImageGroup();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);
                item.setDirPath(parentPath);

                // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroup imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(path);
                } else {
                    // 否则，将该对象加入到groupList中
                    item.addImage(path);
                    mGruopList.add(item);
                }
            }
        } catch (Exception e) {
            // 输出日志
            L.e(e);
            return false;
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
        
        //查找搜索结果的文件夹中是否包含.nomedia文件，如有包含，则将此目录移出结果列表
        ArrayList<String> ignoreFolderPaths = new ArrayList<String>();
        for(String folderPath : folderPaths) {
        	if(containsNomediaFile(folderPath) && !ignoreFolderPaths.contains(folderPath)) {
        		ignoreFolderPaths.add(folderPath);
        	}
        }
        if(!ignoreFolderPaths.isEmpty()) {
        	ArrayList<ImageGroup> deleteImageGroups = new ArrayList<ImageGroup>();
        	for(ImageGroup imageGroup : mGruopList) {
        		String imageGroupFolderPath = imageGroup.getDirPath();
        		if(!TextUtils.isEmpty(imageGroupFolderPath) 
        				&& ignoreFolderPaths.contains(imageGroupFolderPath)) {
        			deleteImageGroups.add(imageGroup);
        		}
        	}
        	if(!deleteImageGroups.isEmpty()) {
        		mGruopList.removeAll(deleteImageGroups);
        	}
        }
        
        return true;
    }
    
    private boolean containsNomediaFile(String folderPath) {
    	boolean contains = false;
    	File folder = new File(folderPath);
        if(folder.isDirectory()) {
        	File[] files = folder.listFiles();
        	if(files != null) {
        		for(File file : files) {
        			if(file.getName().equals(".nomedia")) {
        				contains = true;
        				break ;
        			}
        		}
        	}
        }
        return contains;
    }
}
