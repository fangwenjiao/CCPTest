/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.voice.demo.group.baseui;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hisun.phone.core.voice.util.Log4Util;
import com.voice.demo.group.utils.EmoticonUtil;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.ui.CCPHelper;

/**
 * 
 * @ClassName: EmojiGrid.java
 * @Description: TODO
 * @author Jorstin Chan 
 * @date 2013-12-27
 * @version 3.6
 */
public class EmojiGrid extends GridView implements AdapterView.OnItemClickListener{

	private Context mContext ;
	
	EmojiApapter mEmojiApapter;
	
	private int emojiMode = 20;
	
	private int perPage ;
	
	private int totalPage ;
	
	private int curPage ;
	
	private int numColumns;
	
	private int gridViewWidth;
	
	private int mItemWidthInPix;
	
	private OnEmojiItemClickListener mItemClickListener;
	/**
	 * @param context
	 * @param attrs
	 */
	public EmojiGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initEmojiLayout(context);
	}

	/**
	 * @param context
	 */
	public EmojiGrid(Context context) {
		super(context);
		mContext = context;
		initEmojiLayout(context);
	}

	void initEmojiLayout(Context context) {
		
		mEmojiApapter = new EmojiApapter(context);
		setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		setBackgroundResource(0);
		setSelector(new ColorDrawable(Color.TRANSPARENT));  
		setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		setGravity(Gravity.CENTER);  
		
		switch (emojiMode) {
		case 21:
	    	mItemWidthInPix = CCPUtil.fromDPToPix(getContext(), 43);
	    	break;
	    case 20:
	    	mItemWidthInPix = CCPUtil.fromDPToPix(getContext(), 80);
		default:
			break;
		}
		Log4Util.d(CCPHelper.DEMO_TAG, "EmojiGrid.initEmojiLayout mItemWidthInPix:" + mItemWidthInPix);
		
		setColumnWidth(mItemWidthInPix);
		setAdapter(mEmojiApapter);
		setOnItemClickListener(this);

		int left = CCPUtil.fromDPToPix(getContext(), 6);
		int top = CCPUtil.fromDPToPix(getContext(), 6);
		int right = CCPUtil.fromDPToPix(getContext(), 6);
		Log4Util.d(CCPHelper.DEMO_TAG, "EmojiGrid.initEmojiLayout paddingLeft:"
				+ left + " ,paddingRight:" + right + ",paddingTop:" + top);
		setPadding(left, top, right, 0);
	}
	
	
	public void initEmojiGrid(int mode , int perPage , int totalPage , int curPage , int numColumns , int gridViewWidth) {
		Log4Util.d(CCPHelper.DEMO_TAG, "EmojiGrid.initEmojiGrid mode:" + mode
				+ " , perPage:" + perPage + " , totalPage:" + totalPage
				+ "  ,curPage:" + curPage);
		
		this.emojiMode = mode;
		this.perPage = perPage ;
		this.totalPage = totalPage ;
		this.curPage = curPage ;
		this.numColumns = numColumns;
		this.gridViewWidth = gridViewWidth;
		if(numColumns == 7) {
			setColumnWidth(gridViewWidth / 7);
		} else {
			 setColumnWidth(gridViewWidth / 14);
		}
		
		setNumColumns(numColumns);
		mEmojiApapter.updateEmoji(getPageEmoji(curPage));
	}
	
	/**
	 * Paging loaded expression
	 * 
	 * @param page
	 * @return
	 */
	private ArrayList<CCPEmoji> getPageEmoji(int page) {
		page --  ;
		int startIndex = page * emojiMode;
		int endIndex = startIndex + emojiMode;

		ArrayList<CCPEmoji> emojiCache = EmoticonUtil.getInstace().getEmojiCache();
		if(emojiCache != null) {
			
			if (endIndex > emojiCache.size()) {
				endIndex = emojiCache.size();
			}
			ArrayList<CCPEmoji> list = new ArrayList<CCPEmoji>();
			list.addAll(emojiCache.subList(startIndex, endIndex));
			/*if (list.size() < emojiMode) {
				for (int i = list.size(); i < emojiMode; i++) {
					CCPEmoji object = new CCPEmoji();
					list.add(object);
				}
			}*/
			return list;
		}
		
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(mEmojiApapter != null) {
			
			if(mItemClickListener != null ) {
				if(position == mEmojiApapter.getCount() - 1) {
					
					mItemClickListener.onEmojiDelClick();
					return ;
				}  
				
				CCPEmoji item = (CCPEmoji) mEmojiApapter.getItem(position);
				mItemClickListener.onEmojiItemClick(item.getId() , item.getEmojiName());
			}
		}
	}
	
	public void releaseEmojiView() {
		mItemClickListener = null;
		mEmojiApapter = null;
	}
	
	@TargetApi(8)
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	 /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnEmojiItemClickListener {

       /**
        * Callback method to be invoked when an item in this EmojiGird View has
        * been clicked.
        * 
        * @param emojiid
        * @param emojiName
        */
        void onEmojiItemClick(int emojiid , String emojiName);
        
        /**
         * Callback method to be invoked when an item  of del in this EmojiGird View has
         * been clicked.
         */
        void onEmojiDelClick();
    }

    /**
     * Register a callback to be invoked when an item in this EmojiGird View has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnEmojiItemClickListener(OnEmojiItemClickListener listener) {
    	mItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this EmojiGird View has
     *         been clicked, or null id no callback has been set.
     */
    public final OnEmojiItemClickListener getOnEmojiItemClickListener() {
        return mItemClickListener;
    }
}

