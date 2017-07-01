package ua.com.test1.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ua.com.test1.R;

/**
 * Created by Aleksey on 30.06.2017.
 */

public class ObjectView extends SurfaceView implements SurfaceHolder.Callback, ScaleGestureDetector.OnScaleGestureListener {

    public static final String TAG_SCALE = "SCALE";

    private SurfaceHolder holder;
    private Canvas canvas;
    float lastFocusX;
    float lastFocusY;
    private Matrix drawMatrix;

    private Bitmap object;

    public ObjectView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);
        //object = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
        initMatrix();
    }

    /*[0.4016019, 0.0,       127.971085]
      [0.0,       0.4016019, -50.119144]
      [0.0,       0.0,       1.0]*/

    public void setObject(int drawableId) {
        object = BitmapFactory.decodeResource(getResources(), drawableId);
    }

    private void initMatrix() {
        drawMatrix = new Matrix();
        drawMatrix.setScale((float)0.4, (float)0.4);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        canvas = holder.lockCanvas();

        canvas.drawBitmap(object, drawMatrix, null);

        holder.unlockCanvasAndPost(canvas);
        canvas = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG_SCALE, "Scale");

        Matrix transformationMatrix = new Matrix();
        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();

        transformationMatrix.postTranslate(-focusX, -focusY);

        transformationMatrix.postScale(detector.getScaleFactor(), detector.getScaleFactor());

        float focusShiftX = focusX - lastFocusX;
        float focusShiftY = focusY - lastFocusY;
        transformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);
        drawMatrix.postConcat(transformationMatrix);
        lastFocusX = focusX;
        lastFocusY = focusY;

        canvas = holder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(object, drawMatrix, null);
        holder.unlockCanvasAndPost(canvas);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG_SCALE, "Scale begin");
        lastFocusX = detector.getFocusX();
        lastFocusY = detector.getFocusY();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG_SCALE, "Scale end");
        canvas = null;
    }

    public Matrix getDrawMatrix() {
        return drawMatrix;
    }

    public Bitmap getObject() {
        return object;
    }
}
