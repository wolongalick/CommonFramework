package common.utils;

import android.graphics.Color;

/**
 * Created by cxw on 2016/1/18.
 */
public class ColorUtils {
    public static int gradient(int color1, int color2, float p) {
        int r1 = (color1 & 0xff0000) >> 16;
        int g1 = (color1 & 0xff00) >> 8;
        int b1 = color1 & 0xff;
        int r2 = (color2 & 0xff0000) >> 16;
        int g2 = (color2 & 0xff00) >> 8;
        int b2 = color2 & 0xff;
        int newr = (int) (r2 * p + r1 * (1 - p));
        int newg = (int) (g2 * p + g1 * (1 - p));
        int newb = (int) (b2 * p + b1 * (1 - p));
        return Color.argb(255, newr, newg, newb);
    }
}
