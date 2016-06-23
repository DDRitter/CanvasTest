package com.artactivo.canvastest;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MasterMinder";
    int boardCoordinate[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardCoordinate = getBitmapPositionInsideImageView((ImageView) findViewById(R.id.board));
        addListenerOnBoard();
    }


    public void addListenerOnBoard() {
        ImageView board = (ImageView) findViewById(R.id.board);
//        final int[] viewCoords = new int[2];
//        board.getLocationOnScreen(viewCoords);

        board.setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(getBaseContext(), "Board Touched", Toast.LENGTH_SHORT).show();
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        Log.i(TAG, "----------");
                        Log.i(TAG, "Image top left > X: " + boardCoordinate[0] + "   Y: " + boardCoordinate[1]);
                        Log.i(TAG, "Image width: " + boardCoordinate[2] + "   Height: " + boardCoordinate[3]);
                        Log.i(TAG, "CLICK position > X: " + x + "   Y: " + y);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        );
    }

    /**
     * Returns the bitmap position inside an imageView.
     *
     * @param imageView source ImageView
     * @return 0: left, 1: top, 2: width, 3: height
     */
    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null) {
            return ret;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }
}
