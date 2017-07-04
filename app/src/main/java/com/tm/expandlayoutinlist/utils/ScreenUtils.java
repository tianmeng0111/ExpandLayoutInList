package com.tm.expandlayoutinlist.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/****
 * 获得屏幕相关的辅助类
 * ****/
public class ScreenUtils {

	private ScreenUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	/****
	 * 修改整个界面所有控件的字体
	 * 
	 * @param viewGroup
	 * @param path
	 * @param activity
	 * @return
	 * *****/
	public static void changeFonts(ViewGroup root, String path, Activity act) {
		// path是字体路径
		Typeface tf = Typeface.createFromAsset(act.getAssets(), path);
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, path, act);
			}
		}
	}

	/***
	 * 修改整个界面所有控件的字体大小
	 * 
	 * @param root
	 * @param size
	 * @param activity
	 */
	public static void changeTextSize(ViewGroup root, int size, Activity act) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTextSize(size);
			} else if (v instanceof Button) {
				((Button) v).setTextSize(size);
			} else if (v instanceof EditText) {
				((EditText) v).setTextSize(size);
			} else if (v instanceof ViewGroup) {
				changeTextSize((ViewGroup) v, size, act);
			}
		}
	}

	/***
	 * 不改变控件位置，修改控件大小
	 * 
	 * @param view
	 * @int width
	 * @int height
	 * 
	 * **/
	public static void changeWH(View v, int W, int H) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.width = W;
		params.height = H;
		v.setLayoutParams(params);
	}

	/****
	 * 改变控件的高度
	 * 
	 * @param v
	 * @param H
	 */

	public static void changeH(View v, int H) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.height = H;
		v.setLayoutParams(params);
	}

	/****
	 * 改变控件的宽度
	 * 
	 * @param v
	 * @param W
	 */
	public static void changeW(View v, int W) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.width = W;
		v.setLayoutParams(params);

	}

}
