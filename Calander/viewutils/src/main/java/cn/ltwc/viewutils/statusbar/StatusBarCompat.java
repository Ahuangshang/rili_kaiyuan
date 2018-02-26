package cn.ltwc.viewutils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置系统状态栏颜色
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @version 2016-06-20
 * @since 2016-06-20
 */

public class StatusBarCompat {

    static final IStatusBar IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMPL = new StatusBarMImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMPL = new StatusBarKitkatImpl();
        } else {
            IMPL = new IStatusBar() {
                @Override
                public void setStatusBarColor(Window window, int color, boolean lightStatusBar) {
//                    setFitsSystemWindows(window,false);
                }
            };

        }
    }

    public static void setStatusBarColor(Activity activity, int color) {
        boolean isLightColor = nearWhite(Color.red(color)) && nearWhite(Color.green(color)) && nearWhite(Color.blue(color));
        setStatusBarColor(activity, color, isLightColor);
    }

    private static boolean nearWhite(int singleColor) {
        return singleColor > 200;
    }

    /**
     * Set system status bar color.
     *
     * @param activity
     * @param color          status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    public static void setStatusBarColor(Activity activity, int color, boolean lightStatusBar) {
        setStatusBarColor(activity.getWindow(), color, lightStatusBar);
    }

    /**
     * Set the system status bar color
     *
     * @param window         the window
     * @param color          status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    public static void setStatusBarColor(Window window, int color, boolean lightStatusBar) {
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0) {
            return;
        }
        IMPL.setStatusBarColor(window, color, lightStatusBar);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setFitsSystemWindows(Window window, boolean fitSystemWindows) {
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            mChildView.setFitsSystemWindows(fitSystemWindows);
        }
    }
}
