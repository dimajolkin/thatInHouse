package com.example.dimaj.geo.houses;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

public class MapBitmap {
    protected String fileName;
    protected Bitmap bitmap;

    public MapBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();

            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
        }
        return b;
    }

    public void flip(Point p, int replaseColor, int color) {
        try {
            if (bitmap.getPixel(p.x, p.y) == replaseColor) {
                drawPixel(p, color);
                flip(new Point(p.x + 1, p.y), replaseColor, color);
                flip(new Point(p.x - 1, p.y), replaseColor, color);
                flip(new Point(p.x, p.y + 1), replaseColor, color);
                flip(new Point(p.x, p.y - 1), replaseColor, color);
            }
        } catch (IllegalArgumentException ex) {
            return;
        }

    }

    public int getRGB(Point point) {
        try {
            return  bitmap.getPixel(point.x, point.y);
        } catch (IllegalArgumentException ex) {
            return  0;
        } catch (Exception ex){
            return 0;
        }
    }

    public Point getPointCenter() {
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        return new Point(x, y);
    }

    public void drawPixel(Point p, int color) {
        try {
            bitmap.setPixel(p.x, p.y, color);

        } catch (IllegalStateException ex) {
            return;
        } catch (Exception ex){
            return;
        }

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

}
