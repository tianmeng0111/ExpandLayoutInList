package com.tm.expandlayoutinlist.utils;

import android.content.Context;

/*****
 * 密度像素相互转化工具类 
 * 
 *  *******/
public class DensityUtils {

    private DensityUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     * 
     * @param context
     * @param val
     * @return
     */
    public static int dp2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f); 
    }

    /**
     * sp转px
     * 
     * @param context
     * @param val
     * @return
     */
    public static int sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    }

    /**
     * sp转px
     *
     * @param context
     * @param val
     * @return
     */
    public static float sp2px2(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * px转dp
     * 
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (pxValue / scale + 0.5f); 
    }

    /**
     * px转sp
     * 
     * @param fontScale
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    }
}
