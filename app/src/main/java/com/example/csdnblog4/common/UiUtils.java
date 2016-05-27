
package com.example.csdnblog4.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UiUtils {

    static public int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    static public int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    static public float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }
    public static Bitmap getVideoThumbnail(String path){
        Bitmap thumbnail=null;
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
            thumbnail = mediaMetadataRetriever.getFrameAtTime(1000);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }finally {
            try {
                mediaMetadataRetriever.release();

            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        return thumbnail;
    }
}
